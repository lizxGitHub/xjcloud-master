package gov.pbc.xjcloud.provider.contract.json;


import gov.pbc.xjcloud.provider.contract.enumutils.ApiCodeEnum;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author com.mhout.lizx
 * @version 1.0.0
 */
public class AjaxJson {

    private boolean success = true;
    private String code;
    private String msg;
    private ArrayList<Object> data;

    public AjaxJson() {
        this.code = String.valueOf(ApiCodeEnum.SUCCESS.getCode());
        this.msg = ApiCodeEnum.SUCCESS.getMsg();
        this.data = new ArrayList();
    }

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data = data;
    }

    public void put(Collection value) {
        this.data.addAll(value);
    }

    public void remove(String key) {
        this.data.remove(key);
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

}
