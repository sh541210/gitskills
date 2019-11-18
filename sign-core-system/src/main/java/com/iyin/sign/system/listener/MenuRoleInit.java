package com.iyin.sign.system.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.iyin.sign.system.entity.SignSysMenu;
import com.iyin.sign.system.entity.SignSysRole;
import com.iyin.sign.system.entity.SignSysRoleMenu;
import com.iyin.sign.system.entity.SignSysUserRole;
import com.iyin.sign.system.mapper.ISignSysMenuMapper;
import com.iyin.sign.system.mapper.SignSysRoleMapper;
import com.iyin.sign.system.mapper.SignSysRoleMenuMapper;
import com.iyin.sign.system.mapper.SignSysUserRoleMapper;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName: MenuRoleInit
 * @Description: 系统菜单、角色、用户角色初始化
 * @Author: luwenxiong
 * @CreateDate: 2019/8/26 10:30
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/26 10:30
 * @Version: 0.0.1
 */
@Slf4j
@Component
public class MenuRoleInit implements CommandLineRunner {

    @Autowired
    SignSysRoleMapper signSysRoleMapper;
    @Autowired
    ISignSysMenuMapper signSysMenuMapper;
    @Autowired
    SignSysUserRoleMapper signSysUserRoleMapper;
    @Autowired
    SignSysRoleMenuMapper signSysRoleMenuMapper;

    @Override
    public void run(String... strings) throws Exception {

        /**
         * 初始化前台菜单数据
         */

        SignSysMenu signSysMenu = signSysMenuMapper.selectById("10000");
        if(signSysMenu == null){

            QueryWrapper queryWrapper = new QueryWrapper();
            signSysMenuMapper.delete(queryWrapper);

            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("10000");
            signSysMenu.setName("合同管理");
            signSysMenu.setParentId("-1");
            signSysMenu.setPath("/contract/manager123");
            signSysMenu.setIcon("iconfont icon-wendangzhongxin1");
            signSysMenu.setSort(5);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);

            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("100001");
            signSysMenu.setName("全部合同");
            signSysMenu.setParentId("10000");
            signSysMenu.setPath("/contractMgt/index");
            signSysMenu.setIcon("iconfont icon-wendangzhongxin1");
            signSysMenu.setSort(2);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);

//            signSysMenu = new SignSysMenu();
//            signSysMenu.setMenuId("100002");
//            signSysMenu.setName("合同模板");
//            signSysMenu.setParentId("10000");
//            signSysMenu.setPath("/contractMgt/contractModule");
//            signSysMenu.setIcon("iconfont icon-batch-import");
//            signSysMenu.setSort(1);
//            signSysMenu.setKeepAlive("0");
//            signSysMenu.setType("0");
//            signSysMenu.setCreateTime(LocalDateTime.now());
//            signSysMenu.setCreateUser("系统初始化");
//            signSysMenuMapper.insert(signSysMenu);

            log.info("######初始化合同中心菜单成功######");
        }

        signSysMenu = signSysMenuMapper.selectById("20000");
        if(signSysMenu ==null){
            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("20000");
            signSysMenu.setName("文件管理");
            signSysMenu.setParentId("-1");
            signSysMenu.setPath("");
            signSysMenu.setIcon("iconfont icon-yingyongguanli1");
            signSysMenu.setSort(4);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);

            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("200001");
            signSysMenu.setName("全部文件");
            signSysMenu.setParentId("20000");
            signSysMenu.setPath("/fileCenter/fileManage");
            signSysMenu.setIcon("iconfont icon-wendangzhongxin1");
            signSysMenu.setSort(2);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);

            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("200002");
            signSysMenu.setName("文件模板");
            signSysMenu.setParentId("20000");
            signSysMenu.setPath("/fileCenter/fileTmpList");
            signSysMenu.setIcon("iconfont icon-yingyongguanli1");
            signSysMenu.setSort(1);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);

            log.info("######初始化文件管理菜单成功######");
        }

        signSysMenu = signSysMenuMapper.selectById("30000");
        if(signSysMenu == null){
            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("30000");
            signSysMenu.setName("签章验证");
            signSysMenu.setParentId("-1");
            signSysMenu.setPath("");
            signSysMenu.setIcon("iconfont icon-gongjubao1");
            signSysMenu.setSort(3);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);

            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("300001");
            signSysMenu.setName("电子文件验证");
            signSysMenu.setParentId("30000");
            signSysMenu.setPath("/signVertify/elecFileVertify");
            signSysMenu.setIcon("iconfont icon-wendangzhongxin1");
            signSysMenu.setSort(2);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);

            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("300002");
            signSysMenu.setName("验证码验证");
            signSysMenu.setParentId("30000");
            signSysMenu.setPath("/signVertify/codeVertify");
            signSysMenu.setIcon("iconfont icon-yingyongguanli1");
            signSysMenu.setSort(1);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);
            log.info("######初始化签章验证菜单成功######");
        }

        signSysMenu = signSysMenuMapper.selectById("40000");
        if(signSysMenu ==null){
            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("40000");
            signSysMenu.setName("统计报表");
            signSysMenu.setParentId("-1");
            signSysMenu.setPath("");
            signSysMenu.setIcon("iconfont icon-gongjubao1");
            signSysMenu.setSort(2);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);

            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("400001");
            signSysMenu.setName("签章日志");
            signSysMenu.setParentId("40000");
            signSysMenu.setPath("/statistical/signLog");
            signSysMenu.setIcon("iconfont icon-wendangzhongxin1");
            signSysMenu.setSort(1);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);

            log.info("######初始化统计报表菜单成功######");
        }


        signSysMenu = signSysMenuMapper.selectById("50000");
        if(signSysMenu ==null){
            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("50000");
            signSysMenu.setName("账号管理");
            signSysMenu.setParentId("-1");
            signSysMenu.setPath("");
            signSysMenu.setIcon("iconfont icon-gongjubao1");
            signSysMenu.setSort(1);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);

            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("500001");
            signSysMenu.setName("我的账号");
            signSysMenu.setParentId("50000");
            signSysMenu.setPath("/backManage/myAccount");
            signSysMenu.setIcon("iconfont icon-wendangzhongxin1");
            signSysMenu.setSort(1);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);

            log.info("######初始化账号管理菜单成功######");
        }

        signSysMenu = signSysMenuMapper.selectById("60000");
        if(signSysMenu ==null){
            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("60000");
            signSysMenu.setName("开放平台");
            signSysMenu.setParentId("-1");
            signSysMenu.setPath("");
            signSysMenu.setIcon("iconfont icon-jiaoseguanli1");
            signSysMenu.setSort(1);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);

            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("600001");
            signSysMenu.setName("应用管理");
            signSysMenu.setParentId("60000");
            signSysMenu.setPath("/electronContract/myContract");
            signSysMenu.setIcon("iconfont icon-gongjubao1");
            signSysMenu.setSort(1);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);

            signSysMenu = new SignSysMenu();
            signSysMenu.setMenuId("600002");
            signSysMenu.setName("API文档");
            signSysMenu.setParentId("60000");
            signSysMenu.setPath("/electronContract/apiDocument");
            signSysMenu.setIcon("iconfont icon-gongjubao1");
            signSysMenu.setSort(1);
            signSysMenu.setKeepAlive("0");
            signSysMenu.setType("0");
            signSysMenu.setCreateTime(LocalDateTime.now());
            signSysMenu.setCreateUser("系统初始化");
            signSysMenuMapper.insert(signSysMenu);

            log.info("######初始化账号管理菜单成功######");
        }


        /**
         * 初始化角色菜单权限数据
         */
        QueryWrapper<SignSysRoleMenu> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SignSysRoleMenu::getRoleId,"10000")
                .eq(SignSysRoleMenu::getMenuId,"10000");
        SignSysRoleMenu roleMenu =  signSysRoleMenuMapper.selectOne(queryWrapper);
        if(roleMenu ==null){
            signSysRoleMenuMapper.delete(queryWrapper);
            String[] menuIds = new String[]{"10000","100001","100002","20000","200001","200002","30000","300001","300002","40000","400001","50000","500001","60000","600001","600002"};
            for(int i=0;i<menuIds.length;i++){
                roleMenu = new SignSysRoleMenu();
                roleMenu.setId(SnowflakeIdWorker.getIdToStr());
                roleMenu.setRoleId("10000");
                roleMenu.setMenuId(menuIds[i]);
                roleMenu.setGmtCreate(new Date());
                roleMenu.setIsDeleted(0);
                signSysRoleMenuMapper.insert(roleMenu);
            }
        }

        /**
         * 初始化角色数据
         */
        SignSysRole initRole =  signSysRoleMapper.selectById("10000");
        if(initRole ==null){
            QueryWrapper queryWrapper1 = new QueryWrapper();
            signSysRoleMapper.delete(queryWrapper1);

            initRole = new SignSysRole();
            initRole.setId("10000");
            initRole.setRoleName("基础角色");
            initRole.setGmtCreate(new Date());
            initRole.setIsDeleted(0);
            initRole.setRoleDesc("系统初始化角色");
            initRole.setRoleFlag("init");
            int count = signSysRoleMapper.insert(initRole);
            if(count ==0){
                log.error("系统初始化角色信息失败");
            }
            //用户默认给与基础角色
            SignSysUserRole signSysUserRole = new SignSysUserRole();
            signSysUserRole.setRoleId("10000");
            UpdateWrapper<SignSysUserRole> updateWrapper = new UpdateWrapper<SignSysUserRole>().eq("is_deleted",0);
            signSysUserRoleMapper.update(signSysUserRole,updateWrapper);
        }




    }
}
