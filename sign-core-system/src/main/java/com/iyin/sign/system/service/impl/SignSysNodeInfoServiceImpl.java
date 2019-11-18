package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.entity.SignSysEnterpriseInfo;
import com.iyin.sign.system.entity.SignSysNodeInfo;
import com.iyin.sign.system.entity.SignSysUserInfo;
import com.iyin.sign.system.mapper.SignSysEnterpriseInfoMapper;
import com.iyin.sign.system.mapper.SignSysNodeInfoMapper;
import com.iyin.sign.system.mapper.SignSysUserInfoMapper;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.SignSysNodeInfoService;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.vo.req.SaveNodeReqVO;
import com.iyin.sign.system.vo.req.UpdateNodeReqVO;
import com.iyin.sign.system.vo.resp.NodeInfoRespVO;
import com.iyin.sign.system.vo.resp.NodeRootInfoRespVO;
import com.iyin.sign.system.vo.resp.NodeTreeRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName: SignSysNodeInfoServiceImpl
 * @Description: 组织结构service
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 18:39
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 18:39
 * @Version: 0.0.1
 */
@Service
@Slf4j
public class SignSysNodeInfoServiceImpl extends ServiceImpl<SignSysNodeInfoMapper,SignSysNodeInfo> implements SignSysNodeInfoService {

    @Autowired
    SignSysNodeInfoMapper signSysNodeInfoMapper;

    @Autowired
    SignSysUserInfoMapper signSysUserInfoMapper;

    @Autowired
    SignSysEnterpriseInfoMapper signSysEnterpriseInfoMapper;

    /**
     * 指定根节点下的所有子节点
     */
    private List<SignSysNodeInfo> allNodeOfCurrentNodeList=null;

    private List<SignSysNodeInfo> removeNodeInfoList =new ArrayList<SignSysNodeInfo>();

    private static final String PARENT_NODE_ID = "0";

    @Override
    public IyinResult<String> addNode(SaveNodeReqVO saveNodeReqVO) {
        String parentId = saveNodeReqVO.getParentNodeId();
        String nodeName = saveNodeReqVO.getNodeName();
        //判定节点名称不能重复
        QueryWrapper<SignSysNodeInfo> queryWrapper = new QueryWrapper<SignSysNodeInfo>()
                .eq("node_name",nodeName);

        List<SignSysNodeInfo> dbSignNodes = signSysNodeInfoMapper.selectList(queryWrapper);

        for(SignSysNodeInfo dbSignNode:dbSignNodes){
            if(dbSignNode!=null){
                //同一个单位下不能重复,不同单位可以重复
                String rootNodeId1 = getRootNodeId(dbSignNode.getId());
                String rootNodeId2 = getRootNodeId(parentId);
                if(rootNodeId1.equals(rootNodeId2)){
                    throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1031);
                }
            }
        }

        queryWrapper = new QueryWrapper<SignSysNodeInfo>()
                .eq("id",parentId);
        SignSysNodeInfo parentdbSignNode = signSysNodeInfoMapper.selectOne(queryWrapper);
        if(parentdbSignNode==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1032);
        }

        //判定父级节点层级，如果达到5级则不能添加
        Integer level =  getNodeLevel(parentId,0);
        if(level>=5){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1034);
        }
        SignSysNodeInfo signSysNodeInfo = new SignSysNodeInfo();
        String nodeId = SnowflakeIdWorker.getIdToStr();
        signSysNodeInfo.setId(nodeId);
        signSysNodeInfo.setParentNodeId(parentId);
        signSysNodeInfo.setNodeName(nodeName);
        signSysNodeInfo.setGmtCreate(new Date());
        signSysNodeInfo.setIsDeleted(0);
        int count = signSysNodeInfoMapper.insert(signSysNodeInfo);
        if(count==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1035);
        }
        return  IyinResult.getIyinResult(nodeId);
    }

    @Override
    public IyinResult<Boolean> updateNode(UpdateNodeReqVO updateNodeReqVO) {

        String nodeId = updateNodeReqVO.getNodeId();
        String nodeName = updateNodeReqVO.getNodeName();

        SignSysNodeInfo dbSignNode = signSysNodeInfoMapper.selectById(nodeId);
        if(dbSignNode==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1033);
        }

        //判定节点名称不能重复
//        QueryWrapper<SignSysNodeInfo> queryWrapper = new QueryWrapper<SignSysNodeInfo>()
//                .eq("node_name",nodeName);
//
//        SignSysNodeInfo  dbSignNodeByName = signSysNodeInfoMapper.selectOne(queryWrapper);
//        if(dbSignNodeByName!=null){
//            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1031);
//        }

        dbSignNode.setNodeName(nodeName);
        dbSignNode.setGmtModified(new Date());
        int count = signSysNodeInfoMapper.updateById(dbSignNode);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1036);
        }
        return IyinResult.getIyinResult(true);
    }

    @Override
    @Transactional
    public IyinResult<Boolean> delNode(String nodeId) {

        SignSysNodeInfo nodeInfo = signSysNodeInfoMapper.selectById(nodeId);
        if(nodeInfo ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1033);
        }
        String parentNodeId = nodeInfo.getParentNodeId();

        //顶级节点不能删除
        if("0".equals(parentNodeId)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1038);
        }


        //根节点ID
        String rootNodeId = getRootNodeId(nodeId);
        //删除子节点逻辑：先删除子节点 删除后子节点会一并删除，同时成员归属跟节点
        facadeDelSonNode(nodeId,rootNodeId);




        //删除该节点,同时该节点成员归属跟节点
        QueryWrapper<SignSysUserInfo> userQueryWrapper = new QueryWrapper<SignSysUserInfo>()
                .eq("node_id",nodeId);
        List<SignSysUserInfo> signSysUserInfos = signSysUserInfoMapper.selectList(userQueryWrapper);
        if(signSysUserInfos!=null && !signSysUserInfos.isEmpty()){
            for(SignSysUserInfo userInfo:signSysUserInfos){
                userInfo.setNodeId(rootNodeId);
                userInfo.setGmtModified(new Date());
                signSysUserInfoMapper.updateById(userInfo);
            }
        }
        int count = signSysNodeInfoMapper.deleteById(nodeId);

        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1037);
        }
        return IyinResult.getIyinResult(true);
    }

    /**
     * 根据nodeId获取节点的层级数
     * @Author:      luwenxiong
     * @CreateDate:  2019/7/16
     * @UpdateUser:
     * @UpdateDate:  2019/7/16
     * @Version:     0.0.1
     * @return
     * @throws
     */
    @Override
    public Integer getNodeLevel(String nodeId,Integer level){
        QueryWrapper<SignSysNodeInfo> queryWrapper = new QueryWrapper<SignSysNodeInfo>()
                .eq("id",nodeId);
        SignSysNodeInfo dbSignNode = signSysNodeInfoMapper.selectOne(queryWrapper);
        if(dbSignNode==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1033);
        }
        level+=1;
        String parentId = dbSignNode.getParentNodeId();
        if("0".equals(parentId)){
            return level;
        }
        return   getNodeLevel(parentId,level);
    }

    /**
     * 根据节点ID，获取根节点ID
     * @return
     */
    @Override
    public String getRootNodeId(String nodeId){
        QueryWrapper<SignSysNodeInfo> queryWrapper = new QueryWrapper<SignSysNodeInfo>()
                .eq("id",nodeId);
        SignSysNodeInfo dbSignNode = signSysNodeInfoMapper.selectOne(queryWrapper);
        if(dbSignNode ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1033);
        }
        String parentId = dbSignNode.getParentNodeId();
        if("0".equals(parentId)){
            return dbSignNode.getId();
        }
        return   getRootNodeId(parentId);
    }

//    @Override
//    public IyinResult<NodeTreeRespVO> getNodeTree(String enterpriseId) {
//        SignSysEnterpriseInfo signSysEnterpriseInfo = signSysEnterpriseInfoMapper.selectById(enterpriseId);
//        if(signSysEnterpriseInfo ==null){
//            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
//        }
//        String rootNodeId =  signSysEnterpriseInfo.getNodeId();
//
//        //父节点
//        SignSysNodeInfo parentNode = signSysNodeInfoMapper.selectById(rootNodeId);
//        if(parentNode == null){
//            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1032);
//        }
//        NodeTreeRespVO nodeTreeRespVO = new NodeTreeRespVO();
//        NodeInfoRespVO rootNode = new NodeInfoRespVO();
//        BeanUtils.copyProperties(parentNode,rootNode);
//        nodeTreeRespVO.setRootNode(rootNode);
//
//        //递归获取子节点
//
//        return null;
//    }

    @Override
    public IyinResult<NodeTreeRespVO> getNodeTree(String enterpriseId){

        SignSysEnterpriseInfo signSysEnterpriseInfo = signSysEnterpriseInfoMapper.selectById(enterpriseId);
        if(signSysEnterpriseInfo ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        String rootNodeId =  signSysEnterpriseInfo.getNodeId();
        SignSysNodeInfo rootNode = signSysNodeInfoMapper.selectById(rootNodeId);

        QueryWrapper<SignSysNodeInfo> queryWrapper = new QueryWrapper<SignSysNodeInfo>()
                .eq("is_deleted",0);
        List<SignSysNodeInfo> allNodeInfoList = signSysNodeInfoMapper.selectList(queryWrapper);

        //只显示制定的当前根节点下的子节点
        allNodeOfCurrentNodeList =new ArrayList<SignSysNodeInfo>();
        for(SignSysNodeInfo son:allNodeInfoList){
            String rootId =null;
            try {
                rootId = getRootNodeId(son.getId());
            }catch (BusinessException e){
                log.info(e.getDetailMessage());
            }

           if(rootNodeId.equals(rootId) && ! PARENT_NODE_ID.equals(son.getParentNodeId())){
               allNodeOfCurrentNodeList.add(son);
           }
        }
        List<NodeInfoRespVO>  sonNodeList = new ArrayList<NodeInfoRespVO>();
        for(SignSysNodeInfo  nodeInfo:allNodeOfCurrentNodeList){
            if(!removeNodeInfoList.contains(nodeInfo)){
                NodeInfoRespVO node = facadeTree(nodeInfo);
                sonNodeList.add(node);
                removeNodeInfoList.add(nodeInfo);
            }
        }
        removeNodeInfoList.clear();
        NodeTreeRespVO nodeTreeRespVO =new NodeTreeRespVO();

        NodeRootInfoRespVO rootNodeResp =new NodeRootInfoRespVO();
        BeanUtils.copyProperties(rootNode,rootNodeResp);
        nodeTreeRespVO.setRootNode(rootNodeResp);
        nodeTreeRespVO.setSonNodeList(sonNodeList);
        return IyinResult.getIyinResult(nodeTreeRespVO);
    }

    @Override
    public List<String> getNodeAndSonNodeIdByUserId(String userId) {
        SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(userId);
        if (signSysUserInfo == null) {
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        String nodeId = signSysUserInfo.getNodeId();
        List<String> nodeIds = new ArrayList<>();
        nodeIds = facadeNodeAndSonNodeId(nodeId, nodeIds);
        nodeIds.add(nodeId);
        return nodeIds;
    }

    private List<String> facadeNodeAndSonNodeId(String nodeId, List<String> nodeIds) {
        QueryWrapper<SignSysNodeInfo> queryWrapper = new QueryWrapper<SignSysNodeInfo>()
                .eq("parent_node_id", nodeId);
        List<SignSysNodeInfo> signSysNodeInfos = signSysNodeInfoMapper.selectList(queryWrapper);
        if (signSysNodeInfos != null && !signSysNodeInfos.isEmpty()) {
            for (SignSysNodeInfo signSysNodeInfo : signSysNodeInfos) {
                nodeIds.add(signSysNodeInfo.getId());
                facadeNodeAndSonNodeId(signSysNodeInfo.getId(), nodeIds);

            }
        }
        return nodeIds;
    }

    private NodeInfoRespVO facadeTree(SignSysNodeInfo nodeInfo) {
        NodeInfoRespVO respVO = new NodeInfoRespVO();
        List<NodeInfoRespVO> sonNodes =new ArrayList<NodeInfoRespVO>();
        for(SignSysNodeInfo sysNodeInfo:allNodeOfCurrentNodeList){
            if(nodeInfo.getId().equals(sysNodeInfo.getParentNodeId())){
                NodeInfoRespVO node = facadeTree(sysNodeInfo);
                sonNodes.add(node);
                respVO.setSonNodes(sonNodes);
                removeNodeInfoList.add(sysNodeInfo);
            }
        }
        BeanUtils.copyProperties(nodeInfo,respVO);
        return respVO;
    }


    /**
     * 给定一个节点ID，递归删除下面的所有节点
     * @param nodeId
     * @param rootNodeId
     */
    public void facadeDelSonNode(String nodeId, String rootNodeId){
        QueryWrapper<SignSysNodeInfo> queryWrapper = new QueryWrapper<SignSysNodeInfo>()
                .eq("parent_node_id",nodeId);
        List<SignSysNodeInfo> sonNodeInfos = signSysNodeInfoMapper.selectList(queryWrapper);
        for(SignSysNodeInfo sonNode:sonNodeInfos){
            signSysNodeInfoMapper.deleteById(sonNode.getId());

            //同时把节点下的成员， 放到跟节点
            QueryWrapper<SignSysUserInfo> userQueryWrapper = new QueryWrapper<SignSysUserInfo>()
                    .eq("node_id",sonNode.getId());
            List<SignSysUserInfo> signSysUserInfos = signSysUserInfoMapper.selectList(userQueryWrapper);
            if(signSysUserInfos!=null && !signSysUserInfos.isEmpty()){
                for(SignSysUserInfo userInfo:signSysUserInfos){
                    userInfo.setNodeId(rootNodeId);
                    userInfo.setGmtModified(new Date());
                    signSysUserInfoMapper.updateById(userInfo);
                }
            }

            facadeDelSonNode(sonNode.getId(), rootNodeId);
        }
    }
}
