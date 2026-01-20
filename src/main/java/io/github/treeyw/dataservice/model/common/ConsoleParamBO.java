package io.github.treeyw.dataservice.model.common;


import lombok.Data;

@Data
public class ConsoleParamBO {

    private String name;
    private String explain;
    private Class type;
    private String typeStr;
    private String example;
    //是否必填
    Boolean required;
    //header query path body form
    String paramType;


}
