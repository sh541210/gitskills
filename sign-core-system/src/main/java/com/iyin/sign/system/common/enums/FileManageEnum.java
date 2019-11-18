package com.iyin.sign.system.common.enums;

/**
 * @ClassName: SignStatusEnum.java
 * @Description: 状态
 * @Author: yml
 * @CreateDate: 2019/8/7
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
public enum FileManageEnum {
    /**
     * 合同文件状态
     */
    DRAFT("01", "草稿"),
    REVOKED("02", "已撤销"),
    REFUSED("03", "已拒签"),
    SIGNING("04", "签署中"),
    COMPLETE("05", "签署完成"),
    EXPIRED("06", "已过期"),

    /**
     * 签署状态
     */
    TODO("01", "待我签署"),
    SOMEONE_TODO("02", "待他人签"),
    PASS("03", "签署通过"),
    NOT_PASSED("04", "签署不通过"),

    SIGN("01", "签署"),
    CARBON_COPY("02", "抄送"),

    FILE_MANAGE("01", "文件管理"),
    CONTRACT_MANAGE("02", "合同管理"),

    CONTRACT_FILE("01", "合同文件"),
    CONTRACT_ATTACHMENT("02", "合同附件"),

    SELF_SIGN("01", "需自签"),
    OTHERS_SIGN("02", "仅他人签"),

    /**
     * 签署类型
     */
    UNORDERED_SIGN("01", "无序签"),
    ORDERED_SIGN("02", "顺序签署"),
    INDIVIDUALLY_SIGN("03", "每人单独签署"),

    /**
     * 签署域类型
     */
    FIELD_SEAL("01", "印章"),
    FIELD_AUTOGRAPH("02", "签名"),
    FIELD_DATE("03", "日期"),

    /**
     * 操作记录 0查看 1签署 2打印 3下载 4拒签 5用印申请
     */
    LOG_VIEW("0", "查看"),
    LOG_SIGN("1", "签署"),
    LOG_PRINT("2", "打印"),
    LOG_DOWNLOAD("3", "下载"),
    LOG_REFUSE("4", "拒签"),
    LOG_USE_SEAL("5", "用印申请"),

    ;

    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    FileManageEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
