package gov.pbc.xjcloud.provider.contract.enumutils;

public enum StateEnum {

    NO_PRESENTATION("1001", "未呈批"),
    AWAITING_AUDIT("1002", "待审核")

    ;

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    StateEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code找到对应的String的值
     * @param code
     * @return
     */
    public static String valuesOf(String code){
        for (StateEnum state:values()){
            if (state.getCode() == code){
                return state.getName();
            }
        }
        return "null";
    }

}
