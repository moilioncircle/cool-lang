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
public class TestSortList {

    @Rule
    public final TextFromStandardInputStream systemInMock = TextFromStandardInputStream.emptyStandardInputStream();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void test() {
        systemInMock.provideText("5");
        String str = FileUtils.readJarFile("sort_list.cl");
        Main.run(str);
        assertEquals("How many numbers to sort?0\n" +
                "1\n" +
                "2\n" +
                "3\n" +
                "4\n",systemOutRule.getLog());
    }
}
