package com.leon.cool.lang;

import com.leon.cool.lang.util.FileUtils;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.junit.Assert.assertEquals;

/**
 * Created by leon on 15-11-1.
 */
@NotThreadSafe
public class TestBookList {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @org.junit.Test
    public void test() {
        String str = FileUtils.readJarFile("book_list.cl");
        Main.run(str);
        assertEquals("title:      The Top 100 CD_ROMs\n" +
                "author:     Ulanoff\n" +
                "periodical:  PC Magazine\n" +
                "- dynamic type was Article -\n" +
                "title:      Compilers, Principles, Techniques, and Tools\n" +
                "author:     Aho, Sethi, and Ullman\n" +
                "- dynamic type was Book -\n", systemOutRule.getLog());
    }
}
