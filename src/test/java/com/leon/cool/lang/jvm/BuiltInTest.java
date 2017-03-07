package com.leon.cool.lang.jvm;

import com.leon.cool.lang.Bootstrap;
import com.leon.cool.lang.util.FileUtil;

/**
 * Created by Baoyi Chen on 2017/3/7.
 */
public class BuiltInTest {
    public static void main(String[] args) {
        String str = FileUtil.readJarFile("jvm/self_test.cl");
        Bootstrap.run(str);
    }
}
