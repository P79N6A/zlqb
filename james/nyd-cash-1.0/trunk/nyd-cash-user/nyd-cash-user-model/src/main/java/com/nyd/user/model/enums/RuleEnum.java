package com.nyd.user.model.enums;

public enum RuleEnum {

    Rule1("001","撞库规则1:30天活跃且资料完整"),
    Rule2("002","撞库规则2:60天活跃且资料完整"),
    Rule3("003","撞库规则3:90天活跃且资料完整"),
    Rule4("004","撞库规则4:撞全库"),
    Rule5("005","撞库规则5:零撞库");

    private String code;
    private String name;

    RuleEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public static RuleEnum toEnum(String code) {
        for (RuleEnum type : RuleEnum.values()) {
            if(type.getCode().equals(code)){
                return type;
            }

        }
        return null;
    }
}
