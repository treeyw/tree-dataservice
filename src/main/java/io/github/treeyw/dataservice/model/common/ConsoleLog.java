package io.github.treeyw.dataservice.model.common;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ConsoleLog {
    String name();

    //标签
    String[] tags() default "";

    String notes() default "";

    //自定义参数
    ConsoleParam[] enterParameters() default {@ConsoleParam};

    //参数实体类，类里的字段需要有@FieldComment的注解
    Class enterParamModel() default String.class;

    String outParameters() default "";

    boolean queryBOFlag() default true;
}
