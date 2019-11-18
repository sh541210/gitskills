package com.iyin.sign.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.iyin.sign.system.entity.SignSysMenu;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.MenuTree;
import com.iyin.sign.system.model.MenuVO;
import com.iyin.sign.system.service.ISignSysMenuService;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.util.TreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName: SignSysMenuController
 * @Description: 节点管理模块
 * @Author: yml
 * @CreateDate: 2019/7/17
 * @UpdateUser: yml
 * @UpdateDate: 2019/7/17
 * @Version: 1.0.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/menu")
@Api(value = "menu", tags = "v1.1.0_节点管理模块")
public class SignSysMenuController {

    private static final String MENU = "0";

    private final ISignSysMenuService sysMenuService;

    /**
     * 返回当前用户的树形菜单集合
     *
     * @return当前用户的树形菜单
     */
    @GetMapping
    @ApiOperation("v1.1.0_当前用户的树形菜单")
    public IyinResult<List<MenuTree>> getUserMenu() {
        // 获取符合条件的菜单
        Set<MenuVO> all = new HashSet<>(sysMenuService.findMenu());
        List<MenuTree> menuTreeList = all.stream()
                .filter(menuVo -> MENU.equals(menuVo.getType()))
                .map(MenuTree::new)
                .sorted(Comparator.comparingInt(MenuTree::getSort))
                .collect(Collectors.toList());
        return new IyinResult<>(TreeUtil.build(menuTreeList, "-1"));
    }

    /**
     * 返回树形节点集合
     *
     * @Author: yml
     * @CreateDate: 2019/7/17
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/17
     * @Version: 0.0.1
     * @return : com.iyin.sign.system.model.IyinResult<java.util.List<com.iyin.sign.system.model.MenuTree>>
     */
    @GetMapping(value = "/tree")
    @ApiOperation("v.1.1.0_返回树形节点集合")
    public IyinResult<List<MenuTree>> getTree() {
        return new IyinResult<>(TreeUtil.buildTree(sysMenuService
                .list(Wrappers.<SignSysMenu>lambdaQuery()
                        .orderByAsc(SignSysMenu::getSort)), "-1"));
    }

    /**
     * 通过ID查询菜单的详细信息
     *
     * @Author: yml
     * @CreateDate: 2019/7/17
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/17
     * @Version: 0.0.1
     * @param id
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.entity.SignSysMenu>
     */
    @GetMapping("/{id}")
    @ApiOperation("v.1.1.0_通过ID查询菜单的详细信息")
    public IyinResult<SignSysMenu> getById(@PathVariable String id) {
        return new IyinResult<>(sysMenuService.getById(id));
    }

    /**
     * 新增菜单
     *
     * @Author: yml
     * @CreateDate: 2019/7/17
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/17
     * @Version: 0.0.1
     * @param sysMenu
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    @PostMapping
    @ApiOperation("v.1.1.0_新增菜单")
    public IyinResult<Boolean> save(@Valid @RequestBody SignSysMenu sysMenu) {
        return sysMenuService.saveMenAndButton(sysMenu);
    }

    /**
     * 删除菜单
     *
     * @Author: yml
     * @CreateDate: 2019/7/17
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/17
     * @Version: 0.0.1
     * @param id
     * @return : com.iyin.sign.system.model.IyinResult
     */
    @DeleteMapping("/{id}")
    @ApiOperation("v.1.1.0_删除菜单")
    public IyinResult<Boolean> removeById(@PathVariable String id) {
        return sysMenuService.removeMenuById(id);
    }

    /**
     * 更新菜单
     *
     * @Author: yml
     * @CreateDate: 2019/7/17
     * @UpdateUser: yml
     * @UpdateDate: 2019/7/17
     * @Version: 0.0.1
     * @param sysMenu
     * @return : com.iyin.sign.system.model.IyinResult
     */
    @PutMapping
    @ApiOperation("v.1.1.0_更新菜单")
    public IyinResult update(@Valid @RequestBody SignSysMenu sysMenu) {
        return new IyinResult<>(sysMenuService.updateMenuById(sysMenu));
    }
}
