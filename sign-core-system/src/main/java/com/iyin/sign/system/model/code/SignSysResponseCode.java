package com.iyin.sign.system.model.code;
/**
 * @ClassName: SignSysResponseCode
 * @Description: 签章系统响应编码
 * @Author: luwenxiong
 * @CreateDate: 2019/6/22 16:23
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/22 16:23
 * @Version: 0.0.1
 */
public enum SignSysResponseCode implements ResponseCodeInterface{

    /**
     * 签章系统响应编码
     */
    SIGN_SYS_ERRORE_1000(1000,"请求参数有误"),

    SIGN_SYS_ERRORE_1001(1001,"管理员用户名不存在"),

    SIGN_SYS_ERRORE_1002(1002,"管理员密码错误"),

    SIGN_SYS_ERRORE_1003(1003,"查询不到单位信息"),

    SIGN_SYS_ERRORE_1004(1004,"查询不到用户信息"),

    SIGN_SYS_ERRORE_1005(1005,"用户名或密码错误请重试"),

    SIGN_SYS_ERRORE_1006(1006,"您的账户已被禁用请联系管理员"),

    SIGN_SYS_ERRORE_1007(1007,"当前账户密码错误次数已达上限请明天再试"),

    SIGN_SYS_ERRORE_1008(1008,"请求头中无token或token信息"),

    SIGN_SYS_ERRORE_1009(1009, "授权信息已过期，请您重新登录"),

    SIGN_SYS_ERRORE_1010(1010, "授权信息已被重置"),

    SIGN_SYS_ERRORE_1011(1011, "无效的授权信息"),


    SIGN_SYS_ERRORE_1012(1012, "保存印章信息失败"),

    SIGN_SYS_ERRORE_1013(1013, "编辑印章信息失败"),

    SIGN_SYS_ERRORE_1014(1014, "删除印章失败"),

    SIGN_SYS_ERRORE_1015(1015, "设置用户印章权限失败"),

    SIGN_SYS_ERRORE_1016(1016, "密码长度不能小于6位"),

    SIGN_SYS_ERRORE_1017(1017, "旧密码错误请重新输入"),

    SIGN_SYS_ERRORE_1018(1018, "密码更新失败"),

    SIGN_SYS_ERRORE_1019(1019, "单位名称已被注册请更换"),

    SIGN_SYS_ERRORE_1020(1020, "企业信用代码已被注册请更换"),

    SIGN_SYS_ERRORE_1021(1021, "密码长度为6~16字符之内"),

    SIGN_SYS_ERRORE_1022(1022, "密码包含非法字符"),

    SIGN_SYS_ERRORE_1023(1023, "手机号不合法"),

    SIGN_SYS_ERRORE_1024(1024, "邮箱不合法"),

    SIGN_SYS_ERRORE_1025(1025, "账户名已存在"),

    SIGN_SYS_ERRORE_1026(1026, "企业数据入库失败"),

    SIGN_SYS_ERRORE_1027(1027, "组织架构信息入库失败"),

    SIGN_SYS_ERRORE_1028(1028, "用户信息入库失败"),

    SIGN_SYS_ERRORE_1029(1029, "更新单位信息失败"),

    SIGN_SYS_ERRORE_1030(1030,"未关联证书"),

    SIGN_SYS_ERRORE_1031(1031,"该组织已存在，请确认后重新输入"),

    SIGN_SYS_ERRORE_1032(1032,"父节点ID不存在"),

    SIGN_SYS_ERRORE_1033(1033,"节点ID不存在"),

    SIGN_SYS_ERRORE_1034(1034,"已达到最大层级添加失败"),

    SIGN_SYS_ERRORE_1035(1035,"添加节点插入数据库失败"),

    SIGN_SYS_ERRORE_1036(1036,"更新节点操作数据库失败"),

    SIGN_SYS_ERRORE_1037(1037,"删除节点操作数据库失败"),

    SIGN_SYS_ERRORE_1038(1038,"根节点不能删除"),

    SIGN_SYS_ERRORE_1039(1039,"不支持的文件格式"),

    SIGN_SYS_ERRORE_1040(1040,"设置禁用/启用用户失败"),

    SIGN_SYS_ERRORE_1041(1041,"用户角色信息入库失败"),

    SIGN_SYS_ERRORE_1042(1042,"用户数据权限入库失败"),

    SIGN_SYS_ERRORE_1043(1043,"角色名重复"),

    SIGN_SYS_ERRORE_1044(1044,"数据库插入角色数据失败"),

    SIGN_SYS_ERRORE_1045(1045,"编辑角色操作数据库失败"),

    SIGN_SYS_ERRORE_1046(1046,"设置禁用/启用单位失败"),

    SIGN_SYS_ERRORE_1047(1047,"该UKEY信息已存在请勿重复导入"),

    SIGN_SYS_ERRORE_1048(1048,"导入UKEY印章保存数据库失败"),

    SIGN_SYS_ERRORE_1049(1049,"编辑UKEY印章失败"),

    SIGN_SYS_ERRORE_1050(1050,"配置菜单权限失败"),

    SIGN_SYS_ERRORE_1051(1051,"导入ukey印章时保存证书信息失败"),

    SIGN_SYS_ERRORE_1052(1052,"获取验证码失败"),

    SIGN_SYS_ERRORE_1053(1053,"验证码不存在请重新获取验证码"),

    SIGN_SYS_ERRORE_1054(1054,"验证码错误"),

    SIGN_SYS_ERRORE_1055(1055,"导入的表格数据不能为空"),

    SIGN_SYS_ERRORE_1056(1056,"当前角色下存在对应账户无法删除"),

    SIGN_SYS_ERRORE_1057(1057,"查询的资源文件无访问权限"),

    SIGN_SYS_ERRORE_1058(1058,"章模图片类型不能为空"),

    SIGN_SYS_ERRORE_1059(1059,"按钮已存在请勿重复添加"),

    SIGN_SYS_ERRORE_1060(1060,"系统初始化的角色不能删除"),

    SIGN_SYS_ERRORE_1061(1061,"单位已被禁用请联系管理员"),

    SIGN_SYS_ERRORE_1062(1062,"当日短信发生次数已达上限"),

    SIGN_SYS_ERRORE_1063(1063,"印章权限已分配无法删除"),

    SIGN_SYS_ERRORE_1064(1064,"该用户不存在"),





    ;


    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误消息
     */
    private final String msg;

    /**
     * 错误消息（英文）
     */
    private final String enMsg;

    SignSysResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.enMsg = "";
    }

    SignSysResponseCode(int code, String msg, String enMsg) {
        this.code = code;
        this.msg = msg;
        this.enMsg = enMsg;
    }



    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String getEnMsg() {
        return enMsg;
    }
}
