package com.iyin.sign.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SignSysNodeInfo;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.req.SaveNodeReqVO;
import com.iyin.sign.system.vo.req.UpdateNodeReqVO;
import com.iyin.sign.system.vo.resp.NodeTreeRespVO;

import java.util.List;

/**
 * @ClassName: SignSysNodeInfoService
 * @Description: 签章系统节点服务
 * @Author: wdf
 * @CreateDate: 2019/8/7
 * @UpdateUser: wdf
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
public interface SignSysNodeInfoService extends IService<SignSysNodeInfo> {

    /**
     * 增加节点
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<String> addNode(SaveNodeReqVO saveNodeReqVO);

    /**
     * 更新节点
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<Boolean> updateNode(UpdateNodeReqVO updateNodeReqVO);

    /**
     * 删除节点
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<Boolean> delNode(String nodeId);

    /**
     * 获取节点层级
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/22 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/22 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    Integer getNodeLevel(String nodeId,Integer level);

    String getRootNodeId(String nodeId);

    IyinResult<NodeTreeRespVO> getNodeTree(String enterpriseId);

    /**
     * 根据userId查询当前用户所在节点及其所有子节点的ID集合
     * @param userId
     * @return
     */
    List<String> getNodeAndSonNodeIdByUserId(String userId);

}
