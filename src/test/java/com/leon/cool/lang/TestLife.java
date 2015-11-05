package com.leon.cool.lang;

import com.leon.cool.lang.util.FileUtils;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.testng.annotations.Test;

/**
 * Created by leon on 15-11-1.
 */
public class TestLife {

    @Rule
    public final TextFromStandardInputStream systemInMock = TextFromStandardInputStream.emptyStandardInputStream();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void test() throws InterruptedException {
        String str = FileUtils.readJarFile("life.cl");
        Main.run(str);
    }
}
