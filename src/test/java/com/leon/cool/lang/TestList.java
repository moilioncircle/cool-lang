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
public class TestList {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void test() {
        String str = FileUtils.readJarFile("list.cl");
        Main.run(str);
        assertEquals("5 4 3 2 1 \n" +
                "4 3 2 1 \n" +
                "3 2 1 \n" +
                "2 1 \n" +
                "1 \n",systemOutRule.getLog());
    }
}
