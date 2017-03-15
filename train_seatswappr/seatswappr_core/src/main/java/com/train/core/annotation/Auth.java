package com.train.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by sungang on 2016/8/31.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {

    public boolean verifyLogin() default false; //验证登录

    public boolean verifyAuth() default false; //验证企业认证

    boolean verifyMoney() default false; //验证用户金额
}
