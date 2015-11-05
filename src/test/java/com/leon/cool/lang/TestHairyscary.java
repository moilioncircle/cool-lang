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
public class TestHairyscary {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void test() {
        String str = FileUtils.readJarFile("hairyscary.cl");
        Main.run(str);
        assertEquals("17141611714163171416511714161171416317141653117141611714163171416511714161171416317141653171416117141631714165171416", systemOutRule.getLog());
    }

}
