package com.iyin.sign.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iyin.sign.system.common.interfaces.CacheConstants;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.entity.SignSysMenu;
import com.iyin.sign.system.entity.SignSysRoleMenu;
import com.iyin.sign.system.mapper.ISignSysMenuMapper;
import com.iyin.sign.system.mapper.SignSysRoleMenuMapper;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.MenuVO;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.ISignSysMenuService;
import com.iyin.sign.system.service.SignSysRoleMenuService;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName: SignSysMenuServiceImpl
 * @Description: 节点 服务实现类
 * @Author: yml
 * @CreateDate: 2019/7/17
 * @UpdateUser: yml
 * @UpdateDate: 2019/7/17
 * @Version: 1.0.0
 */
@Slf4j
@Service
public class SignSysMenuServiceImpl extends ServiceImpl<ISignSysMenuMapper, SignSysMenu> implements ISignSysMenuService {

    @Autowired
    SignSysRoleMenuMapper signSysRoleMenuMapper;
    /**
     * 级联删除菜单
     *
     * @param id
     * @return : java.lang.Boolean
     * @Author: yml
     * @CreateDate: 2019/7/17
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/17
     * @Version: 0.0.1
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
    public IyinResult<Boolean> removeMenuById(String id) {
        // 查询父节点为当前节点的节点
        List<SignSysMenu> menuList = this.list(Wrappers.<SignSysMenu>query()
                .lambda().eq(SignSysMenu::getParentId, id));
        if (CollUtil.isNotEmpty(menuList)) {
            throw new BusinessException(ErrorCode.REQUEST_70001);
        }

        //菜单被被分配到角色时不让删除
        QueryWrapper<SignSysRoleMenu> queryWrapper = new QueryWrapper<SignSysRoleMenu>()
                .eq("menu_id",id);
        List<SignSysRoleMenu> roleMenus = signSysRoleMenuMapper.selectList(queryWrapper);
        if(roleMenus!=null && !roleMenus.isEmpty()){
            throw new BusinessException(ErrorCode.REQUEST_70002);
        }
        //删除当前菜单及其子菜单
        return IyinResult.success(this.removeById(id));
    }

    /**
     * 更新菜单
     *
     * @param sysMenu
     * @return : java.lang.Boolean
     * @Author: yml
     * @CreateDate: 2019/7/19
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/19
     * @Version: 0.0.1
     */
    @Override
    @CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
    public Boolean updateMenuById(SignSysMenu sysMenu) {
        return this.updateById(sysMenu);
    }

    /**
     * 菜单列表
     *
     * @return : java.util.List<com.iyin.sign.system.model.MenuVO>
     * @Author: yml
     * @CreateDate: 2019/7/22
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/22
     * @Version: 0.0.1
     */
    @Override
    public List<MenuVO> findMenu() {
        List<SignSysMenu> signSysMenus = baseMapper.selectList(new QueryWrapper<SignSysMenu>().orderByAsc("sort"));
        List<MenuVO> menuvos = Lists.newArrayList();
        for (SignSysMenu signSysMenu : signSysMenus) {
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(signSysMenu, menuVO);
            menuvos.add(menuVO);
        }
        return menuvos;
    }

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
    @Override
    public IyinResult<Boolean> saveMenAndButton(SignSysMenu sysMenu) {
        /**
         * 菜单层级最多两级，二级菜单下不能继续添加菜单，但是可以添加按钮
         */
        String menuId = SnowflakeIdWorker.getIdToStr();
        String type = sysMenu.getType();
        String parentId = sysMenu.getParentId();
        if("0".equals(type)){
            if(!"-1".equals(parentId)){
                //添加的是菜单
                SignSysMenu parentMenu = this.getById(parentId);
                if(parentMenu == null){
                    throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
                }
                if(!"-1".equals(parentMenu.getParentId())){
                    throw new BusinessException(ErrorCode.REQUEST_70003);
                }
            }
        }else {
            //如果添加的是按钮，需检验前端传递的按钮ID不能为空
            String buttonId = sysMenu.getMenuId();
            if(StringUtils.isEmpty(buttonId)){
                throw new BusinessException(ErrorCode.REQUEST_70004);
            }
            //判断是否是重复添加
            SignSysMenu dbutton =this.getById(buttonId);
            if(dbutton!=null){
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1059);
            }
            menuId = buttonId;
        }
        sysMenu.setMenuId(menuId);
        return IyinResult.getIyinResult(this.save(sysMenu)) ;
    }
}




