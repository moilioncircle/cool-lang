package com.leon.cool.lang;

import com.leon.cool.lang.util.FileUtils;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.junit.Assert.assertEquals;

/**
 * Created by leon on 15-11-1.
 */
@NotThreadSafe
public class TestCool {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void test() {
        String str = FileUtils.readJarFile("cool.cl");
        Main.run(str);
        assertEquals("cool\n", systemOutRule.getLog());
    }

}
