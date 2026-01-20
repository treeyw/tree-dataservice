package io.github.treeyw.dataservice.model.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsoleParam {

    String name() default "";

    String explain() default "";

    Class type() default String.class;

    String example() default "";

    //是否必填
    boolean required() default false;
    //header query path body form
    String paramType() default "query";



}
