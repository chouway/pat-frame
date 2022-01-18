package com.pat.starter.common.anno;

import java.lang.annotation.*;

/**
 * PatAnnotation
 *
 * @author chouway
 * @date 2021.12.31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PatAnnotation {

    String message() default "Check entity msg";

    String key() default "#id";

}
