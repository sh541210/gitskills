package com.iyin.sign.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SignSysMenu;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.MenuVO;

import java.util.List;

/**
 * @ClassName: ISignSysMenuService
 * @Description: 节点服务类
 * @Author: yml
 * @CreateDate: 2019/7/17
 * @UpdateUser: yml
 * @UpdateDate: 2019/7/17
 * @Version: 1.0.0
 */
public interface ISignSysMenuService extends IService<SignSysMenu> {
    /**
     * 级联删除菜单
     *
     * @Author: yml
     * @CreateDate: 2019/7/17
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/17
     * @Version: 0.0.1
     * @param id
     * @return : java.lang.Boolean
     */
    IyinResult<Boolean> removeMenuById(String id);

    /**
     * 更新菜单
     *
     * @Author: yml
     * @CreateDate: 2019/7/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/19
     * @Version: 0.0.1
     * @param sysMenu
     * @return : java.lang.Boolean
     */
    Boolean updateMenuById(SignSysMenu sysMenu);

    /**
     * 菜单列表
     *
     * @Author: yml
     * @CreateDate: 2019/7/22
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/22
     * @Version: 0.0.1

     * @return : java.util.List<com.iyin.sign.system.model.MenuVO>
     */
    List<MenuVO> findMenu();

    /**
     * 新增菜单和按钮
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/23 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/23 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<Boolean> saveMenAndButton(SignSysMenu sysMenu);
}
