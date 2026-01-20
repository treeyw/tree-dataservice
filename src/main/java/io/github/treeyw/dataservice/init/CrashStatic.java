package io.github.treeyw.dataservice.init;

import com.alibaba.fastjson.JSONObject;
import io.github.treeyw.crud.config.init.CrashStaticCrud;
import io.github.treeyw.crud.constant.ParameAttribute;
import io.github.treeyw.dataservice.init.annotations.CrashAnnotationUtil;
import io.github.treeyw.dataservice.model.common.ConsoleLog;
import jakarta.annotation.PostConstruct;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description //启动服务静态资源配置
 * @Author 岳浩
 * @Date 2019/10/31 16:13
 **/
@Component
@Transactional
public class CrashStatic {
    final static Logger log = LoggerFactory.getLogger(CrashStatic.class);


    @PostConstruct
    public void init() throws Exception {

        //装载项目名
        ParameAttribute.PROJECTNAME = CrashStaticCrud.SYS_CONSOLE_YML.getProperty("server.servlet.context-path");

        JSONObject.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

        reflectionStatic();
        CrashAnnotationUtil.init("");
    }

    public void reflectionStatic() {

        Map controllerTextMap = new HashMap();
        Map controllerUrlMap = new HashMap();
        Reflections controllerRe = new Reflections(
                new ConfigurationBuilder()
                        .addScanners(new SubTypesScanner()) // 添加子类扫描工具
                        .addScanners(new FieldAnnotationsScanner()) // 添加 属性注解扫描工具
                        .addScanners(new MethodAnnotationsScanner()) // 添加 方法注解扫描工具
                        .addScanners(new MethodParameterScanner()) // 添加方法参数扫描工具
        );
        // 反射出带有指定注解的所有类
        controllerRe.getTypesAnnotatedWith(RequestMapping.class).forEach(clazz -> {
                    String url = clazz.getAnnotation(RequestMapping.class).value()[0];
                    String name = clazz.getName();
                    String text = url;
                    if (clazz.getAnnotation(ConsoleLog.class) != null) {
                        text = clazz.getAnnotation(ConsoleLog.class).name();
                    }
                    controllerTextMap.put(name, text);
                    controllerUrlMap.put(name, url);
                }
        );
        // 反射出带有指定注解的所有方法
        controllerRe.getMethodsAnnotatedWith(RequestMapping.class).forEach(method -> {
                    String name = method.getDeclaringClass().getName();
                    String url = method.getAnnotation(RequestMapping.class).value()[0];
                    String text = url;
                    if (method.getAnnotation(ConsoleLog.class) != null) {
                        ConsoleLog consoleLog = method.getAnnotation(ConsoleLog.class);
                        text = consoleLog.name();
                    }
                    BlockAttribute.LOG_URL_AND_METHOD_TEXT_MAP.put(ParameAttribute.PROJECTNAME + controllerUrlMap.get(name) + url, text);
                    BlockAttribute.LOG_URL_AND_CLAZZ_TEXT_MAP.put(ParameAttribute.PROJECTNAME + controllerUrlMap.get(name) + url, controllerTextMap.get(name) + "");
                }
        );
    }


}
