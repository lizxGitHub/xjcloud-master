package gov.pbc.xjcloud.provider.contract.config;

/**
 * 令牌上下文
 *
 * @author PaungMiao
 * @date 2020年8月4日09:18:35
 */
public class AuthorizationContextHolder {

    private static final ThreadLocal<String> AUTHORIZATION = new ThreadLocal<>();

    public static void setAuthorization(String authorization) {
        AUTHORIZATION.set(authorization);
    }

    public static void clear() {
        AUTHORIZATION.remove();
    }

    public static String getAuthorization() {
        return AUTHORIZATION.get();
    }

}
