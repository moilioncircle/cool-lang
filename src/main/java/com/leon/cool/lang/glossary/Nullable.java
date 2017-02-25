package com.leon.cool.lang.glossary;

import java.lang.annotation.*;

/**
 * Created by Baoyi Chen on 2017/2/25.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface Nullable {
}
