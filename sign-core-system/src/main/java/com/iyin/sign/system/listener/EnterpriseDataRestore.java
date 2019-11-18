package com.iyin.sign.system.listener;

import cn.hutool.crypto.asymmetric.Sign;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iyin.sign.system.entity.SignSysAdminUser;
import com.iyin.sign.system.entity.SignSysEnterpriseInfo;
import com.iyin.sign.system.entity.SignSysUserInfo;
import com.iyin.sign.system.mapper.SignSysAdminUserMapper;
import com.iyin.sign.system.mapper.SignSysEnterpriseInfoMapper;
import com.iyin.sign.system.mapper.SignSysUserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: EnterpriseDataRestore
 * @Description: 项目启动时修复单位node_id数据
 * @Author: luwenxiong
 * @CreateDate: 2019/7/24 10:13
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/24 10:13
 * @Version: 0.0.1
 */
@Component
@Slf4j
public class EnterpriseDataRestore implements CommandLineRunner {

    @Autowired
    SignSysEnterpriseInfoMapper signSysEnterpriseInfoMapper;

    @Autowired
    SignSysUserInfoMapper signSysUserInfoMapper;

    @Autowired
    SignSysAdminUserMapper signSysAdminUserMapper;


    @Override
    public void run(String... strings) throws Exception {


        /**
         * 1、迭代二在单位表中增加了 node_id字段，启动时补全数据
         */
//        QueryWrapper<SignSysEnterpriseInfo> enterpriseInfoQueryWrapper = new QueryWrapper<SignSysEnterpriseInfo>()
//                .isNull("node_id");
//        List<SignSysEnterpriseInfo> signSysEnterpriseInfos = signSysEnterpriseInfoMapper.selectList(enterpriseInfoQueryWrapper);
//        int count=0;
//        for(SignSysEnterpriseInfo enterpriseInfo:signSysEnterpriseInfos){
//            QueryWrapper<SignSysUserInfo> userInfoQueryWrapper = new QueryWrapper<SignSysUserInfo>()
//                    .eq("enterprise_or_personal_id",enterpriseInfo.getId())
//                    .eq("first_create","01");
//            SignSysUserInfo signSysUserInfo =  signSysUserInfoMapper.selectOne(userInfoQueryWrapper);
//
//            if(signSysUserInfo!=null){
//                enterpriseInfo.setNodeId(signSysUserInfo.getNodeId());
//                enterpriseInfo.setGmtModified(new Date());
//                signSysEnterpriseInfoMapper.updateById(enterpriseInfo);
//                count++;
//            }
//        }
//        log.info("启动时修复单位数据节点ID成功数:{}条",count);

        /**
         * 迭代三：账号分离，将创建单位时生产的账号作为后台单位管理员账号。修复迭代一中将创建单位时生产的账户放到了sign_sys_user_info表中了，转移到sign_sys_admin_user表
         */
        QueryWrapper<SignSysUserInfo> queryWrapper = new QueryWrapper<SignSysUserInfo>()
                .eq("first_create","01");
        List<SignSysUserInfo>  userInfos = signSysUserInfoMapper.selectList(queryWrapper);
        int success_count=0;
        int fail_count=0;
        StringBuffer failMsg =new StringBuffer("失败信息:");
        if(userInfos!=null && !userInfos.isEmpty()){
            for(SignSysUserInfo userInfo: userInfos){
                SignSysAdminUser adminUser = new SignSysAdminUser();
                adminUser.setId(userInfo.getId());
                adminUser.setEnterpriseId(userInfo.getEnterpriseOrPersonalId());
                adminUser.setUserName(userInfo.getUserName());
                adminUser.setUserNickName(userInfo.getUserNickName());
                adminUser.setPassword(userInfo.getPassword());
                adminUser.setPowerLevel("01");
                adminUser.setIsDeleted(0);
                adminUser.setCreateDate(new Date());
                adminUser.setUpdateDate(new Date());
                adminUser.setCreateUser("数据迁移");
                adminUser.setUpdateUser("数据修复");
                adminUser.setRemark("数据修复");
                try {
                    signSysAdminUserMapper.insert(adminUser);
                    signSysUserInfoMapper.deleteById(userInfo.getId());
                    success_count++;
                } catch (Exception e) {
                    e.printStackTrace();
                    failMsg.append(userInfo.toString()).append("\r\n");
                    fail_count++;
                }
            }
            log.info("启动时迁移单位管理员数据成功:{}条",success_count);
            log.info("启动时迁移单位管理员数据失败:{}条,详情:{}",fail_count,failMsg.toString());
        }




    }
}
