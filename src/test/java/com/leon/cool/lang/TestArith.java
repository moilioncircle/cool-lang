package com.leon.cool.lang;

import com.leon.cool.lang.util.FileUtils;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by leon on 15-11-1.
 */
@NotThreadSafe
public class TestArith {
    @Rule
    public final TextFromStandardInputStream systemInMock = TextFromStandardInputStream.emptyStandardInputStream();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @org.junit.Test
    public void test() {
        systemInMock.provideText("q");
        String str = FileUtils.readJarFile("arith.cl");
        Main.run(str);
        assertEquals("number 0 is even!\n" +
                "Class type is now A\n" +
                "\n" +
                "\tTo add a number to 0 ...enter a:\n" +
                "\tTo negate 0 ...enter b:\n" +
                "\tTo find the difference between 0 and another number...enter c:\n" +
                "\tTo find the factorial of 0 ...enter d:\n" +
                "\tTo square 0 ...enter e:\n" +
                "\tTo cube 0 ...enter f:\n" +
                "\tTo find out if 0 is a multiple of 3...enter g:\n" +
                "\tTo divide 0 by 8...enter h:\n" +
                "\tTo get a new number...enter j:\n" +
                "\tTo quit...enter q:\n" +
                "\n", systemOutRule.getLog());
    }
}
