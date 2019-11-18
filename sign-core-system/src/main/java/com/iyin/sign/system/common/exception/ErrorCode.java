package com.iyin.sign.system.common.exception;

import com.iyin.sign.system.model.code.ResponseCodeInterface;

/**
 * @ClassName: ErrorCode.java
 * @Description: 自定义的异常枚举
 * @Author: yml
 * @CreateDate: 2019/6/22
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/22
 * @Version: 1.0.0
 */
public enum ErrorCode implements ResponseCodeInterface {

  // 1.系统级别
  SYS_BUSY("-1", "系统繁忙，请稍候再试"),
  SYS_OK("0", "请求成功"),
  // SYS_200("200","请求成功"),
  SYS_204("204", "请求成功，无数据返回"),
  SYS_300("300", "请求重定向"),
  SYS_400("400", "请求参数异常"),
  SYS_500("500", "服务器异常"),
  SYS_WRONG_FORMAT("600", "格式错误"),
  SYS_RATE_LIMIT("999", "接口请求超过限制，请稍后重试"),

  REQUEST_10014("10014", "该应用已经停用"),
  REQUEST_10015("10015", "该应用已删除"),
  REQUEST_10004("10004", "创建token失败"),
  REQUEST_10006("10006", "用户信息获取失败"),
  REQUEST_10007("10007", "未查询到相关用户信息"),
  REQUEST_10008("10008", "同步用户信息失败"),
  REQUEST_10009("10009", "该印章信息不存在"),
  REQUEST_10010("10010", "用户信息不存在"),
  REQUEST_10011("10011", "白鹤token获取失败"),
  REQUEST_10012("10012", "白鹤数据获取失败"),

  REQUEST_20000("20000", "您的打印次数已用完，若仍需打印请联系管理员"),
  REQUEST_20001("20001", "没有打印权限"),
  REQUEST_20002("20002", "打印失败"),

  // 2.前端请求出错类型
  REQUEST_40001("40001", "请求参数异常，请检查后重试"),
  REQUEST_40002("40002", "请求的数字证书无效"),
  REQUEST_40003("40003", "验证签名失败！"),
  REQUEST_40004("40004", "电子印章不存在！"),
  REQUEST_40005("40005", "验证电子印章失败!"),
  REQUEST_40006("40006", "电子印章已过期！"),
  REQUEST_40007("40007", "请求方式不支持！"),

  REQUEST_40100("40100", "应用名称已存在"),
  REQUEST_40101("40101", "签章页数无效"),

  REQUEST_40201("40201", "根证书不存在！"),
  REQUEST_40202("40202", "创建用户证书失败！"),
  REQUEST_40203("40203", "请求参数异常，企业不存在！"),
  REQUEST_40204("40204", "此印章已开通云签章，请刷新后重试！"),
  REQUEST_40205("40205", "上传的文件超过最大值10 MB！"),
  REQUEST_40206("40206", "上传的文件超过最大值1 MB！"),
  REQUEST_40248("40248", "章模大小暂不支持大于2M"),
  REQUEST_40249("40249", "仅支持PDF文件签章"),
  REQUEST_40250("40250", "章模格式仅支持bmp、jpg、jpeg"),
  REQUEST_40251("40251", "签章文件名称长度暂不支持超过100字符"),
  REQUEST_40252("40252", "印章名称长度暂不支持超过100字符"),
  REQUEST_40253("40253", "签章文件暂不支持大于10M"),
  REQUEST_40254("40254", "章模编码无效"),
  REQUEST_40255("40255", "章模关联的证书不存在"),

  REQUEST_40301("40301", "token error"),
  REQUEST_40302("40302", "用户名或密码错误"),
  REQUEST_40303("40303", "未开通对应服务"),
  REQUEST_40304("40304", "token已过期，请重新获取"),
  REQUEST_40305("40305", "未经授权的客户端"),
  REQUEST_40306("40306", "缺少请求参数"),
  REQUEST_40307("40307", "请求token无效"),
  REQUEST_40308("40308", "服务已暂停"),

  REQUEST_40309("40309", "此文件找不到对应的签章信息"),
  REQUEST_40310("40310", "此文件对应的签章信息无效"),
  REQUEST_40311("40311", "申请单据id无效"),
  REQUEST_40312("40312", "文件个数超出限制"),

  // 快捷签功能
  REQUEST_40401("40401", "文件ID参数无效"),
  REQUEST_40402("40402", "印章状态参数无效"),
  REQUEST_40403("40403", "签章方式参数无效"),
  REQUEST_40404("40404", "缺失证件类型参数"),
  REQUEST_40405("40405", "证件类型参数无效"),
  REQUEST_40406("40406", "缺失印章状态参数"),
  REQUEST_40407("40407", "缺失印章Base64字符串参数"),
  REQUEST_40408("40408", "缺失用户姓名参数"),
  REQUEST_40409("40409", "证件编码无效"),
  REQUEST_40410("40410", "缺失签章方式参数"),
  REQUEST_40411("40411", "缺失签章关键字参数"),
  REQUEST_40412("40412", "缺失关键字索引号参数"),
  REQUEST_40413("40413", "关键字索引号参数无效"),
  REQUEST_40414("40414", "缺失文件名称参数"),
  REQUEST_40415("40415", "缺失起始签章页数参数"),
  REQUEST_40416("40416", "缺失结束签章页数参数"),
  REQUEST_40417("40417", "缺失签章X坐标轴参数"),
  REQUEST_40418("40418", "缺失签章Y坐标轴参数"),
  REQUEST_40419("40419", "缺失签章页数参数"),
  REQUEST_40420("40420", "缺失签章方向参数"),
  REQUEST_40421("40421", "缺失文件Id参数"),
  REQUEST_40422("40422", "缺失证件编码参数"),
  REQUEST_40423("40423", "缺失企业名称参数"),
  REQUEST_40424("40424", "无效签章关键字"),
  REQUEST_40425("40425", "无效企业营业编号"),
  REQUEST_40426("40426", "缺失用户邮箱参数"),
  REQUEST_40427("40427", "缺失签章对象参数"),
  REQUEST_40428("40428", "邮箱参数格式错误"),
  REQUEST_40429("40429", "缺失企业邮箱参数"),
  REQUEST_40430("40430", "缺失企业名称参数"),
  REQUEST_40431("40431", "调用第三方生成证书失败"),
  REQUEST_40432("40432", "缺少关键字X轴偏移量"),
  REQUEST_40433("40433", "缺少关键字Y轴偏移量"),
  // 平台签代码信息
  REQUEST_40434("40434", "缺失token参数"),
  REQUEST_40435("40435", "缺失签章文件Base64字符串参数"),
  REQUEST_40436("40436", "缺失印章编码参数"),
  REQUEST_40437("40437", "缺失印章名称参数"),
  REQUEST_40438("40438", "缺少签章日志类型参数"),
  REQUEST_40439("40439", "缺失签名类型参数"),
  REQUEST_40440("40440", "缺失签名字体类型参数"),
  REQUEST_40441("40441", "缺失签名字体大小参数"),
  REQUEST_40442("40442", "无效签名类型参数"),
  REQUEST_40443("40443", "缺失签名字体颜色参数"),
  REQUEST_40444("40444", "无效字体颜色参数"),
  REQUEST_40445("40445", "无效签章方向参数"),
  REQUEST_40446("40446", "缺失章模编码参数"),
  REQUEST_40447("40447", "无效印章编码"),
  REQUEST_40448("40448", "缺失html内容参数"),
  REQUEST_40449("40449", "文件仅支持pdf/html/doc/docx/xls/xlsx/zip格式"),
  REQUEST_40450("40450", "缺失文件类型参数"),
  REQUEST_40451("40451", "无效文件类型"),
  REQUEST_40452("40452", "缺失业务请求ID"),
  REQUEST_40453("40453", "缺失签章文件ID"),
  REQUEST_40454("40454", "缺失印章编码"),
  REQUEST_40455("40455", "缺失签章模板ID"),
  REQUEST_40456("40456", "无效签章模板ID"),
  REQUEST_40457("40457", "原始密码不能为空"),
  REQUEST_40458("40458", "新密码不能为空"),
  REQUEST_40459("40459", "确认密码不能为空"),
  REQUEST_40460("40460", "新密码和确认密码不一致"),
  REQUEST_40461("40461", "密码长度必须为8到20位字符"),
  REQUEST_40462("40462", "原始密码错误"),
  REQUEST_40463("40463", "无效的印章编码"),
  REQUEST_40464("40464", "同一组织下印章名称不能相同"),
  REQUEST_40465("40465", "印章类型已存在，请重试"),
  REQUEST_40466("40466", "当前类型下存在印章，无法删除"),
  REQUEST_40467("40467", "当前机构下存在数据(次级组织)，无法删除"),
  REQUEST_40468("40468", "当前机构下存在数据(印章)，无法删除"),
  REQUEST_40469("40469", "无效组织ID"),
  REQUEST_40470("40470", "文件为空"),
  REQUEST_40471("40471", "当前组织名称已存在，无法保存"),
  REQUEST_40472("40472", "文件签章失败"),
  REQUEST_40473("40473", "没有指定所在组织"),
  REQUEST_40474("40474", "顶级组织不能删除"),
  REQUEST_40475("40475", "模板名称不能重复"),
  REQUEST_40476("40476", "角色名称不能重复"),
  REQUEST_40477("40477", "当前角色下存在对应账户，无法删除"),
  REQUEST_40478("40478", "当前类型下存在模板，无法删除"),
  REQUEST_40479("40479", "模型类型已存在，请重试"),
  REQUEST_40480("40480", "请选择一种签章方式"),
  REQUEST_40481("40481", "模板不存在"),
  REQUEST_40482("40482", "章模不匹配"),
  REQUEST_40483("40483", "模板类型不匹配"),
  REQUEST_40484("40484", "模板数据错误"),
  // 3.服务器内部业务逻辑出错类型
  SERVER_50001("50001", "服务器异常"),
  SERVER_50002("50005", "请求基础服务失败！"),
  SERVER_50003("50006", "数码印章信息不完整！"),
  SERVER_50100("50100", "应用创建失败"),
  SERVER_50101("50101", "应用修改失败"),
  SERVER_50102("50102", "应用暂停失败"),
  SERVER_50103("50103", "应用启动失败"),
  SERVER_50104("50104", "应用删除失败"),
  SERVER_50105("50105", "应用恢复失败"),
  SERVER_50106("50106", "应用彻底删除失败"),
  SERVER_50107("50107", "应用不存在，请刷新当前页"),
  SERVER_50108("50108", "Word文档转PDF失败！"),

  SERVER_50201("50201", "用户证书不合法，不能保存！"),
  SERVER_50202("50202", "内部文件服务器网络异常！"),
  SERVER_50203("50203", "暂不支持该证书方式"),

  SERVER_50300("50300", "用户证书不合法！"),
  SERVER_50301("50301", "用户证书安装失败！"),
  SERVER_50302("50302", "用户证书失效！"),
  SERVER_50303("50303", "用户证书验证非法！"),
  SERVER_50304("50304", "系统试用已到期，请联系安印科技申请继续试用或升级为正式版本！"),
  SERVER_50305("50305", "系统使用已到期，请联系安印科技续费！"),
  SERVER_50306("50306", "调用失败，系统试用已到期"),

  SERVER_50400("50400", "文档转换异常"),
  SERVER_50401("50401", "文件IO异常"),
  SERVER_50402("50402", "文件资源不存在"),
  SERVER_50403("50403", "PNG文件处理异常"),
  SERVER_50404("50404", "图片转换异常"),
  SERVER_50405("50405", "invalid document type"),

  // 4.登录
  LOGIN_60001("60001", "未找到该用户"),
  LOGIN_60002("60002", "该用户已禁用"),
  LOGIN_60003("60003", "该用户未分配权限"),
  LOGIN_60004("60004", "未分配此权限"),
  LOGIN_60005("60005", "请登录"),

  LOGIN_60006("60006", "当前账户名称不唯一，请重试"),

  LOGIN_61000("61000", "未找到"),
  CERTIFICATION_PASSWORD_ERROR("7000", "证书密码错误"),

  CERT_FILE_NAME_ERROR("2006", "证书文件名称不能为空"),
  PDF_FILE_NAME_ERROR("2006", "待签章文件名称不能为空"),
  SEAL_FILE_NAME_ERROR("2006", "章模文件名称不能为空"),
  SEAL_FILE_TYPE_NAME_ERROR("2006", "章模文件无效"),

  DATE_PARSE_ERROR("3003", "时间转换异常"),

  VERIFY_FILE_BASE_64_ERRORE("3004", "验签文件base64数据异常"),

  GET_FILE_BASE_64_ERRORE("3004", "根据日志ID获取签章文件base64异常"),

  DATA_PIC_COVERT_EXCEPTION("3407", "图片转换异常"),
  DATA_FILE_COVERT_EXCEPTION("3406", "文档转换异常"),
  DATA_IO_EXCEPTION("3001", "文件IO异常"),
  DATA_NOT_FOUND("3404", "文件资源不存在"),

  GET_FILE_BASE_64_BY_ID("3005", "根据日志ID未找到签章信息"),
  CREATE_FILE_FAIL("1001", "create file fail"),
  CONTRACT_INFORMATION_ILLEGAL_EXCEPTION("10008", "签约证件信息不合法！"),
  REQUEST_20004("20004", "签章起始页大于结束页"),

  /*菜单*/
  REQUEST_70001("70001", "菜单含有下级不能删除"),
  REQUEST_70002("70002", "菜单关联了角色不能删除"),
  REQUEST_70003("70003", "二级菜单下不能添加子菜单"),
  REQUEST_70004("70004", "添加按钮时ID不能为空"),

  /*文件管理*/
  REQUEST_80001("80001", "文件上传失败"),
  REQUEST_80002("80002", "文件删除失败"),
  REQUEST_80003("80003", "保存失败"),
  REQUEST_80004("80004", "无签署权限"),
  REQUEST_80005("80005", "无撤销权限"),
  REQUEST_80006("80006", "无重新发起权限"),
  REQUEST_80007("80007", "无打印权限"),
  REQUEST_80008("80008", "打印次数不足"),
  REQUEST_80009("80009", "合同文件已被删除或撤销"),

  /*通知*/
  /**
   * 验证码错误
   */
  SECURITY_CODE_ERROR("2005", "请输入正确的验证码","security code error"),
  /**
   * 手机号码有误
   */
  PHONE_ERROR("2008", "手机号码有误，请重新输入","phone error"),
  /**
   * 验证码使用类型错误
   */
  SECURITY_CODE_USE_TYPE_ERROR("2006", "验证码使用类型错误","security code use type error"),
  /**
   * 邮箱地址格式有误
   */
  EMAIL_ERROR("2010", "邮箱地址格式有误，请重新输入","email error"),
  /**
   * 密码格式有误
   */
  PWD_STYLE_ERROR("2014", "密码长度有误，请重新输入","pwd style error"),
  /**
   * 密码长度有误
   */
  PWD_LENGTH_ERROR("2013", "密码长度有误，请重新输入","pwd length error"),
  /**
   * 当天验证码获取次数达到上限
   */
  CODE_TIME_MAX_ERROR("2009", "当天验证码获取次数达到上限，请第二天再重新获取","code time max error"),
  /**
   * 验证码失效
   */
  SECURITY_CODE_INVALID("2007", "验证码失效，请重新获取","security code invalid"),
  /**
   * 短信发送失败
   */
  SMS_SEND_ERROR("1021", "短信发送失败","sms send error"),
  /**
   * 邮件发送失败
   */
  MAIL_SEND_ERROR("1022", "邮件发送失败","mail send error"),

  ;

  private int code;

  private String msg;

  private String msgEn;

  ErrorCode(String code, String msg) {
    this.code = Integer.parseInt(code);
    this.msg = msg;
  }

  ErrorCode(String code, String msg, String msgEn) {
    this.code = Integer.parseInt(code);
    this.msg = msg;
    this.msgEn = msgEn;
  }

  /** 根据代码获取枚举名称 */
  public static String getMsgByCode(int code) {
    for (ErrorCode errorCode : ErrorCode.values()) {
      if (errorCode.getCode() == code) {
        return errorCode.getMsg();
      }
    }
    return null;
  }

  /** 根据名称获取枚举对象 */
  public static ErrorCode getErrorCodeEnumByMsg(String msg) {
    for (ErrorCode errorCode : ErrorCode.values()) {
      if (errorCode.getMsg().equals(msg)) {
        return errorCode;
      }
    }
    return null;
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
    return msgEn;
  }
}
