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
public class TestPalindrome {
    @Rule
    public final TextFromStandardInputStream systemInMock = TextFromStandardInputStream.emptyStandardInputStream();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void test() {
        systemInMock.provideText("aabaa");
        String str = FileUtils.readJarFile("palindrome.cl");
        Main.run(str);
        assertEquals("enter a string\n" +
                "that was a palindrome\n",systemOutRule.getLog());
    }
}
