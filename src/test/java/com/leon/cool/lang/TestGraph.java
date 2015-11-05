package com.leon.cool.lang;

import com.leon.cool.lang.util.FileUtils;
import net.jcip.annotations.NotThreadSafe;
import org.testng.annotations.Test;

/**
 * Created by leon on 15-11-1.
 */
@NotThreadSafe
public class TestGraph {
    @Test
    public void test() {
        String str = FileUtils.readJarFile("graph.cl");
        Main.run(str);
    }
}
