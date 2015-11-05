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
public class TestCell {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @org.junit.Test
    public void test() {
        String str = FileUtils.readJarFile("cell.cl");
        Main.run(str);
        assertEquals("         X         \n" +
                "........XXX........\n" +
                ".......X...X.......\n" +
                "......XXX.XXX......\n" +
                ".....X.......X.....\n" +
                "....XXX.....XXX....\n" +
                "...X...X...X...X...\n" +
                "..XXX.XXX.XXX.XXX..\n" +
                ".X...............X.\n" +
                "XXX.............XXX\n" +
                "...X...........X...\n" +
                "..XXX.........XXX..\n" +
                ".X...X.......X...X.\n" +
                "XXX.XXX.....XXX.XXX\n" +
                ".......X...X.......\n" +
                "......XXX.XXX......\n" +
                ".....X.......X.....\n" +
                "....XXX.....XXX....\n" +
                "...X...X...X...X...\n" +
                "..XXX.XXX.XXX.XXX..\n" +
                ".X...............X.\n", systemOutRule.getLog());
    }
}
