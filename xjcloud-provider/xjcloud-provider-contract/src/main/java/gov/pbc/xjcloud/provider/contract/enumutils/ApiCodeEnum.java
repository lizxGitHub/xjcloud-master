package gov.pbc.xjcloud.provider.contract.enumutils;

/**
 * @author com.mhout.lizx
 */
public enum ApiCodeEnum {

    SUCCESS(10000, "成功", "成功", "SUCCESS"),
    LOGINFAIL(10001, "登录失败", "登录失败，用户名或密码错误", "login fail"),
    ISPUNKNOWERROR(20000, "服务不可用", "服务暂不可用(业务系统不可用)", "isp.unknow-error"),
    AOPUNKNOWERROR(20000, "服务不可用", "服务暂不可用(网关自身的未知错误)", "aop.unknow-error"),
    AOPINVALIDAUTHTOKEN(20001, "授权权限不足", "无效的访问令牌", "aop.invalid-auth-token"),
    AOPAUTHTOKENTIMEOUT(20001, "授权权限不足", "访问令牌已过期", "aop.auth-token-time-out"),
    AOPINVALIDAPPAUTHTOKEN(20001, "授权权限不足", "无效的应用授权令牌", "aop.invalid-app-auth-token"),
    AOPINVALIDAPPAUTHTOKENNOAPI(20001, "授权权限不足", "商户未授权当前接口", "aop.invalid-app-auth-token-no-api"),
    AOPAPPAUTHTOKENTIMEOUT(20001, "授权权限不足", "应用授权令牌已过期", "aop.app-auth-token-time-out"),
    AOPNOPRODUCTREGBYPARTNER(20001, "授权权限不足", "商户未签约任何产品", "aop.no-product-reg-by-partner"),
    ISVMISSINGPARAMETER(40001, "缺少参数", "缺少参数", "isv.missing-parameter"),
    ISVINVALIDPARAMETER(40002, "无效的参数", "无效的参数", "isv.invalid-parameter"),
    LOGICMOBILEWRONG(50000, "逻辑错误", "旧手机号错误，无法修改", "old mobile is wrong!"),
    LOGICPASSWORDWRIONG(50000, "逻辑错误", "密码错误，无法修改", "password is wrong!"),
    LOGICSENDCODEWRONG(50000, "逻辑错误", "邮箱无效，无法发送邮件", "email is wrong!"),
    LOGICVERIFICATIONWRONG(50000, "逻辑错误", "验证码无效", "verification is wrong!");

    private int code;
    private String msg;
    private String sub_msg;
    private String sub_code;

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getSub_msg() {
        return this.sub_msg;
    }

    public String getSub_code() {
        return this.sub_code;
    }

    private ApiCodeEnum(int code, String msg, String sub_msg, String sub_code) {
        this.code = code;
        this.msg = msg;
        this.sub_msg = sub_msg;
        this.sub_code = sub_code;
    }
}
