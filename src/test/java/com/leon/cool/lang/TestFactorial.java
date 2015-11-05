package com.leon.cool.lang;

import com.leon.cool.lang.util.FileUtils;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by leon on 15-11-1.
 */
@NotThreadSafe
public class TestFactorial {
    @Rule
    public final TextFromStandardInputStream systemInMock = TextFromStandardInputStream.emptyStandardInputStream();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void test() {
        systemInMock.provideLines("10");
        String str = FileUtils.readJarFile("factorial.cl");
        Main.run(str);
        assertEquals("Enter an integer greater-than or equal-to 0: The factorial of 10 is 3628800\n", systemOutRule.getLog());
    }
}
