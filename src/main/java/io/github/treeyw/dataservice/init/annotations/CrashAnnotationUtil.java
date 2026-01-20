package io.github.treeyw.dataservice.init.annotations;

import io.github.treeyw.crud.config.datasource.model.DataSourceDB;
import io.github.treeyw.crud.config.datasource.model.FieldComment;
import io.github.treeyw.dataservice.init.BlockAttribute;
import io.github.treeyw.dataservice.model.common.ConsoleLog;
import io.github.treeyw.dataservice.model.common.ConsoleLogBO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Field;
import java.util.*;

import static io.github.treeyw.crud.config.sys.SysConfig.sysPackagePath;
import static io.github.treeyw.crud.util.CheckObjUtil.ckIsNotEmpty;
import static io.github.treeyw.crud.util.FileUtil.writeTxt;
import static io.github.treeyw.dataservice.init.BlockAttribute.*;
import static io.github.treeyw.dataservice.model.common.ConsoleLogBO.console2BO;


public class CrashAnnotationUtil {
    public static void main(String[] args) {
        entity2md();
    }

    public static void init(String path) {
        List<ConsoleLogBO>[] list = crashReflections(path);
        //获取了所有类
        List<ConsoleLogBO> logClassList = list[0];
        //获取了所有方法
        List<ConsoleLogBO> logMethodList = list[1];
        Map<Class, ConsoleLogBO> classMap = new HashMap<>();
        //tags-contrller
        //类循环 当前只要controller
        for (int i = 0; i < logClassList.size(); i++) {
            ConsoleLogBO bo = logClassList.get(i);
            if (bo.getRequestMethod() == null) continue;
            classMap.put(bo.getClazz(), bo);
            //当前controller的名字
            String name = bo.getClazz().getSimpleName();
            if (ckIsNotEmpty(bo.getName())) name = bo.getName();
            //当前controller的标签
            String tag = name;
            if (bo.getTags() != null && bo.getTags()[0] != null) tag = bo.getTags()[0];
            //通过标签创建当前的map
            DOC_TAGS_MAP.putIfAbsent(tag, new LinkedHashMap<>());
            //通过名字创建list
            DOC_TAGS_MAP.get(tag).put(bo.getName(), new ArrayList<>());
        }
        ConsoleLogBO consoleLogBO = new ConsoleLogBO();
        consoleLogBO.setName("默认接口");
        consoleLogBO.setTags(new String[]{"默认接口"});
        consoleLogBO.setClazz(ConsoleLog.class);
        DOC_TAGS_MAP.putIfAbsent(consoleLogBO.getTags()[0], new LinkedHashMap<>());
        DOC_TAGS_MAP.get(consoleLogBO.getName()).put(consoleLogBO.getName(), new ArrayList<>());
        //方法循环写入之前的map
        for (ConsoleLogBO bo : logMethodList) {
            //只要controller
            if (bo.getRequestUrl() == null) continue;
            //当前的Controller
            ConsoleLogBO cb = classMap.get(bo.getClazz());
            if (cb == null) cb = consoleLogBO;
            //当前controller的名字
            String name = cb.getClazz().getSimpleName();
            if (ckIsNotEmpty(cb.getName())) name = cb.getName();
            //当前controller的标签
            String tag = name;
            if (cb.getTags() != null && cb.getTags()[0] != null) tag = cb.getTags()[0];
            DOC_TAGS_MAP.get(tag).get(name).add(bo);
            DOC_URL_MAP.put(bo.getRequestUrl(), bo);
        }
    }


    public static List<ConsoleLogBO>[] crashReflections(String packPath) {
        List<ConsoleLogBO> logClassList = new ArrayList<>();
        List<ConsoleLogBO> logMethodList = new ArrayList<>();
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackages(packPath) // 指定扫描包
                        // 指定多中扫描工具
                        .setScanners(new MethodParameterScanner(),
                                new TypeAnnotationsScanner(),
                                new SubTypesScanner(),
                                new MethodAnnotationsScanner(),
                                new FieldAnnotationsScanner(),
                                new TypeElementsScanner()
                        )
        );

        //类
        reflections.getTypesAnnotatedWith(ConsoleLog.class, true).forEach(n -> {

            ConsoleLogBO consoleLogBO = new ConsoleLogBO();
            //独立属性
            consoleLogBO.setClazz(n);
            consoleLogBO.setClassOrMethod(0);
            //注解属性
            console2BO(n, consoleLogBO);
            logClassList.add(consoleLogBO);
        });

        //方法
        reflections.getMethodsAnnotatedWith(ConsoleLog.class).forEach(n -> {
            ConsoleLogBO consoleLogBO = new ConsoleLogBO();
            //独立属性
            consoleLogBO.setClazz(n.getDeclaringClass());
            consoleLogBO.setClassOrMethod(1);
            consoleLogBO.setNameEn(n.getName());
            //注解属性
            console2BO(n, consoleLogBO);
            logMethodList.add(consoleLogBO);
        });
        return new List[]{logClassList, logMethodList};
    }

    /**
     * 功能描述:
     *
     * @author 岳浩
     * @date jpa增加字段注解
     */
    public static void jpaAddComment() {
        crashAnnotation();
    }

    public static void crashAnnotation() {
        //TODO: 后续再行实现
        if (1 == 1) return;
        //对该路径扫描实体类
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .forPackages(sysPackagePath) // 指定扫描包
                // 指定多中扫描工具
                .setScanners(new MethodParameterScanner(),
                        new TypeAnnotationsScanner(),
                        new SubTypesScanner(),
                        new MethodAnnotationsScanner(),
                        new FieldAnnotationsScanner(),
                        new TypeElementsScanner()
                );

        Reflections reflections = new Reflections(configurationBuilder);
        reflections.getTypesAnnotatedWith(Entity.class, true).forEach(n -> {
            //通过@DataSourceDB追加jpa认可的表明注解
            if (n.isAnnotationPresent(DataSourceDB.class)) {
                DataSourceDB db = n.getAnnotation(DataSourceDB.class);
                if (db.tableName() != null) {
                    Map<String, Object> element = new HashMap();
                    element.put("comment", db.tableName());
                    //如果有表名
                    if (n.isAnnotationPresent(Table.class)) {
                        element.put("appliesTo", n.getAnnotation(Table.class).name());
                    }

                    //如果有分组，单独处理分组
                    //AnnotationsUtil.classAddAnnotation(n, org.hibernate.annotations.Table.class.getName(), element);
                }
            }
//            //判断其字段有没有FieldComment
//            for (Field field : n.getFields()) {
//                if (field.isAnnotationPresent(FieldComment.class)) {
//                    FieldComment fieldComment = field.getAnnotation(FieldComment.class);
//
//                    Map<String, Object> element = new HashMap();
////                    element.put("value", consoleLog.name());
////                    AnnotationsUtil.classAddAnnotation(n, Api.class.getName(), element);
//
//                }
//            }
        });
    }

    public static void entity2md() {
        StringBuilder sb = new StringBuilder();
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        // 指定多中扫描工具
                        .setScanners(new MethodParameterScanner(),
                                new TypeAnnotationsScanner(),
                                new SubTypesScanner(),
                                new MethodAnnotationsScanner(),
                                new FieldAnnotationsScanner(),
                                new TypeElementsScanner()
                        )
        );
        //类
        reflections.getTypesAnnotatedWith(Entity.class, true).forEach(n -> {
            sb.append("### 实体类：" + n.getSimpleName() + "\n");
            String bt = "\n|字段名|类型|说明\n" +
                    "|:----|:---|:-----|\n";
            sb.append(bt);
            for (Field field : n.getDeclaredFields()) {
                FieldComment fieldComment = null;
                if (field.isAnnotationPresent(FieldComment.class))
                    fieldComment = field.getAnnotation(FieldComment.class);
                if (fieldComment != null)
                    sb.append("|" + field.getName() + "|" + field.getType().getSimpleName() + "|" + fieldComment.value() + "|\n");
                else
                    sb.append("|" + field.getName() + "|" + field.getType().getSimpleName() + "| |\n");
            }
        });
        writeTxt("/home/allClass.md", sb.toString());
    }

}
