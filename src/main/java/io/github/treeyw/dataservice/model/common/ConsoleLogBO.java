package io.github.treeyw.dataservice.model.common;

import io.github.treeyw.crud.config.datasource.model.FieldComment;
import io.github.treeyw.crud.config.init.CrashStaticCrud;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.treeyw.crud.constant.ClassUtil.class2FieldComment;
import static io.github.treeyw.crud.constant.ClassUtil.class2FieldType;
import static io.github.treeyw.crud.util.CheckObjUtil.ckIsNotEmpty;


@Data
public class ConsoleLogBO {
    //包
    private String packagePath;
    //所属类
    private Class clazz;
    //是方法还是类 0类1方法
    private Integer classOrMethod;

    private String requestMethod;
    private String requestUrl;
    private boolean queryBOFlag;

    public String getRequestUrl() {
        if (ckIsNotEmpty(requestUrl)) return requestUrl.split("\\{")[0];
        return requestUrl;
    }

    //方法名称
    private String name;
    private String[] tags;
    //英文方法名
    private String nameEn;

    private String notes;
    //自定义参数
    private List<ConsoleParamBO> enterParameters;
    //参数实体类，类里的字段需要有@FieldComment的注解
    private Class enterParamModel;
    private String outParameters;

    //RequestMapping.class
    //PostMapping.class
    //GetMapping.class
    //PutMapping.class
    //DeleteMapping.class
    public static void console2BO(Method clazz, ConsoleLogBO bo) {
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mp = clazz.getAnnotation(RequestMapping.class);
            bo.setRequestMethod(RequestMapping.class.getSimpleName());
            String mnameStr = "";
            if (clazz.getDeclaringClass().isAnnotationPresent(RequestMapping.class)) {
                mnameStr = clazz.getDeclaringClass().getAnnotation(RequestMapping.class).value()[0];
            }
            mappingValue2Console(mp.value(), mnameStr, bo);
        }
        if (clazz.isAnnotationPresent(PostMapping.class)) {
            PostMapping mp = clazz.getAnnotation(PostMapping.class);
            bo.setRequestMethod(PostMapping.class.getSimpleName());
            String mnameStr = "";
            if (clazz.getDeclaringClass().isAnnotationPresent(RequestMapping.class)) {
                mnameStr = clazz.getDeclaringClass().getAnnotation(RequestMapping.class).value()[0];
            }
            mappingValue2Console(mp.value(), mnameStr, bo);
        }
        if (clazz.isAnnotationPresent(GetMapping.class)) {
            GetMapping mp = clazz.getAnnotation(GetMapping.class);
            bo.setRequestMethod(GetMapping.class.getSimpleName());
            String mnameStr = "";
            if (clazz.getDeclaringClass().isAnnotationPresent(RequestMapping.class)) {
                mnameStr = clazz.getDeclaringClass().getAnnotation(RequestMapping.class).value()[0];
            }
            mappingValue2Console(mp.value(), mnameStr, bo);
        }
        if (clazz.isAnnotationPresent(PutMapping.class)) {
            PutMapping mp = clazz.getAnnotation(PutMapping.class);
            bo.setRequestMethod(PutMapping.class.getSimpleName());
            String mnameStr = "";
            if (clazz.getDeclaringClass().isAnnotationPresent(RequestMapping.class)) {
                mnameStr = clazz.getDeclaringClass().getAnnotation(RequestMapping.class).value()[0];
            }
            mappingValue2Console(mp.value(), mnameStr, bo);
        }
        if (clazz.isAnnotationPresent(DeleteMapping.class)) {
            DeleteMapping mp = clazz.getAnnotation(DeleteMapping.class);
            bo.setRequestMethod(DeleteMapping.class.getSimpleName());
            String mnameStr = "";
            if (clazz.getDeclaringClass().isAnnotationPresent(RequestMapping.class)) {
                mnameStr = clazz.getDeclaringClass().getAnnotation(RequestMapping.class).value()[0];
            }
            mappingValue2Console(mp.value(), mnameStr, bo);
        }
        if (clazz.isAnnotationPresent(ConsoleLog.class)) {
            console2BO(clazz.getAnnotation(ConsoleLog.class), bo);
        }
    }

    private static void mappingValue2Console(String[] value, String mnameStr, ConsoleLogBO bo) {
        boolean haveXg = false;
        if (mnameStr.length() > 1 && !mnameStr.contains("/")) {
            mnameStr = "/" + mnameStr + "/";
            haveXg = true;
        }
        if (mnameStr.length() > 1 && mnameStr.lastIndexOf("/") == mnameStr.length() - 1)
            haveXg = true;

        StringBuilder sb = new StringBuilder();
        for (String s : value) {
            //如果已经有斜杠了，此处的s变量开头去掉一个斜杠
            if (haveXg && s.indexOf("/") == 0)
                s = s.substring(1);
            sb.append(mnameStr + s + ",");
        }
        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);
        //获取系统路径
        String sysPath = "";
        try {
            sysPath = CrashStaticCrud.SYS_CONSOLE_YML.getProperty("server.servlet.context-path");
        } catch (Exception e) {
        }
        String sbUrl = sb.toString();
        boolean sysPathLast = sysPath.lastIndexOf("/") == sysPath.length() - 1;
        boolean sbUrlIndex = sbUrl.indexOf("/") == 0;
        //系统最后是斜杠，url第一没斜杠
        if (sysPathLast && !sbUrlIndex) {
            bo.setRequestUrl(sysPath + sbUrl);
        }
        //系统最后是斜杠，url第一也是斜杠
        else if (sysPathLast && sbUrlIndex) {
            bo.setRequestUrl(sysPath + sbUrl.substring(1));
        }
        //系统最后不是斜杠，url第一也不是斜杠
        else if (sysPathLast && sbUrlIndex) {
            bo.setRequestUrl(sysPath + "/" + sbUrl);
        }
        //系统最后不是斜杠，url第一是斜杠
        else {
            bo.setRequestUrl(sysPath + sbUrl);
        }


    }

    public static void main(String[] args) {
        String ctm = "/wc";
        System.out.println(ctm.indexOf("/"));
        System.out.println(ctm.contains("/"));
        System.out.println(ctm.substring(1));
    }

    public static void console2BO(Class<?> clazz, ConsoleLogBO bo) {
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mp = clazz.getAnnotation(RequestMapping.class);
            bo.setRequestMethod(RequestMapping.class.getSimpleName());
            mappingValue2Console(mp.value(), "", bo);
        }
        if (clazz.isAnnotationPresent(PostMapping.class)) {
            PostMapping mp = clazz.getAnnotation(PostMapping.class);
            bo.setRequestMethod(PostMapping.class.getSimpleName());
            mappingValue2Console(mp.value(), "", bo);
        }
        if (clazz.isAnnotationPresent(GetMapping.class)) {
            GetMapping mp = clazz.getAnnotation(GetMapping.class);
            bo.setRequestMethod(GetMapping.class.getSimpleName());
            mappingValue2Console(mp.value(), "", bo);
        }
        if (clazz.isAnnotationPresent(PutMapping.class)) {
            PutMapping mp = clazz.getAnnotation(PutMapping.class);
            bo.setRequestMethod(PutMapping.class.getSimpleName());
            mappingValue2Console(mp.value(), "", bo);
        }
        if (clazz.isAnnotationPresent(DeleteMapping.class)) {
            DeleteMapping mp = clazz.getAnnotation(DeleteMapping.class);
            bo.setRequestMethod(DeleteMapping.class.getSimpleName());
            mappingValue2Console(mp.value(), "", bo);
        }
        if (clazz.isAnnotationPresent(ConsoleLog.class)) {
            console2BO(clazz.getAnnotation(ConsoleLog.class), bo);
        }
    }

    public static void console2BO(ConsoleLog consoleLog, ConsoleLogBO bo) {
        bo.setName(consoleLog.name());
        bo.setTags(consoleLog.tags());
        bo.setEnterParamModel(consoleLog.enterParamModel());
        bo.setEnterParameters(consoleParam2BOList(consoleLog.enterParameters()));
        bo.setNotes(consoleLog.notes());
        bo.setOutParameters(consoleLog.outParameters());
        bo.setQueryBOFlag(consoleLog.queryBOFlag());
        //循环model的参数
        if (bo.getEnterParamModel() != null) {
            Map<String, Class> typeMap = class2FieldType(bo.enterParamModel);
            Map<String, FieldComment> commentMap = class2FieldComment(bo.enterParamModel);
            commentMap.forEach((field, fieldComment) -> {
                if (bo.getEnterParameters() == null) bo.setEnterParameters(new ArrayList<>());
                ConsoleParamBO cbp = new ConsoleParamBO();
                cbp.setParamType("query");
                cbp.setExample("");
                cbp.setName(field);
                cbp.setExplain(field);
                cbp.setRequired(false);
                cbp.setType(typeMap.get(field));
                if (fieldComment != null) {
                    cbp.setExplain(fieldComment.value());
                    cbp.setRequired(fieldComment.required());
                    if (fieldComment.param()) {
                        bo.getEnterParameters().add(cbp);
                    }
                }
            });
        }

    }

    public static void console2BO(ConsoleParam param, ConsoleParamBO bo) {
        bo.setExample(param.example());
        bo.setExplain(param.explain());
        bo.setName(param.name());
        bo.setType(param.type());
        bo.setParamType(param.paramType());
        bo.setRequired(param.required());
    }

    public static List<ConsoleParamBO> consoleParam2BOList(ConsoleParam[] params) {
        if (params != null && params.length > 0) {
            List<ConsoleParamBO> list = new ArrayList<>();
            for (ConsoleParam param : params) {
                ConsoleParamBO cb = new ConsoleParamBO();
                console2BO(param, cb);
                if (!ckIsNotEmpty(cb.getName())) continue;
                list.add(cb);
            }
            return list;
        }
        return null;
    }


}
