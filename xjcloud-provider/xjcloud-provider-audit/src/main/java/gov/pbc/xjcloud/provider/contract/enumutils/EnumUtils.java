package gov.pbc.xjcloud.provider.contract.enumutils;

import gov.pbc.xjcloud.provider.contract.entity.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EnumUtils {

    private static Logger log= LoggerFactory.getLogger(EnumUtils.class);
    /***
     *  取枚举的key 和 value 返回list
     * @param enumT 枚举类
     * @param methodNames 取枚举类的方法
     * @param <T>
     * @return
     */
    public static <T> List<State> getEnumToList(Class<T> enumT, String... methodNames) {

        List<State> enumList = new ArrayList<>(); //最终返回的list  enumEntity是对枚举类的封装实体
        if (!enumT.isEnum()) {
            return enumList;
        }
        T enums[] = enumT.getEnumConstants();  //得么枚举类里所有的枚举类
        if (enums.length == 0) {  //如果没有枚举键和值   结束
            return enumList;
        }
        int count = methodNames.length;
        String codeMethod = "getCode";  //默认的取 key的方法
        String nameMethod = "getName";  //默认的取 value 的方法
        if (count >= 1 && !methodNames[0].equals("")) { //如果方法的长度是大于等于1的,并且不为空
            codeMethod = methodNames[0];
        }
        if (count == 2 && !methodNames[1].equals("")) { //如果方法的长度是等于2的,并且不为空
            nameMethod = methodNames[1];
        }
        try {
            for (int i = 0; i < enums.length; i++) {
                State State = new State();
                T object = enums[i];     //得到枚举类里每条值
                Object resultKey = getMethodValue(codeMethod, object); //获取key值
                if (resultKey.equals("")) {
                    continue;
                }
                Object resultValue = getMethodValue(nameMethod, object); //获取value值
                if (resultValue.equals("")) {
                    resultValue = object;
                }
                //MessageUtils.getMessage为读取国际化的.
                State.setCode(resultKey.toString());  //把key存到实体类
                State.setName(resultValue.toString()); //把value存到实体类
                enumList.add(State);   //存到list
            }
        } catch (Exception e) {
            e.getStackTrace();
            log.error("枚举类转成List异常", e);
        }
        return enumList;
    }

    private static <T> Object getMethodValue(String methodName, T obj, Object... args) {
        Object resut = "";
        try {
            /********************************* start *****************************************/
            /**获取方法数组，这里只要公有的方法*/
            Method[] methods = obj.getClass().getMethods();
            if (methods.length <= 0) {
                return resut;
            }
            Method method = null;
            for (int i = 0, len = methods.length; i < len; i++) {
                /**忽略大小写取方法*/
                if (methods[i].getName().equalsIgnoreCase(methodName)) {
                    /**如果存在，则取出正确的方法名称*/
                    methodName = methods[i].getName();
                    method = methods[i];
                    break;
                }
            }
            /*************************** end ***********************************************/
            if (method == null) {
                return resut;
            }
            /**方法执行*/
            resut = method.invoke(obj, args);
            if (resut == null) {
                resut = "";
            }
            /**返回结果*/
            return resut;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resut;
    }

}
