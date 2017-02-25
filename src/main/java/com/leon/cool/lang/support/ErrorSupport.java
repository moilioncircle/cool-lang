package com.leon.cool.lang.support;

import com.leon.cool.lang.ast.TreeNode;
import com.leon.cool.lang.support.infrastructure.Pos;
import com.leon.cool.lang.tokenizer.Token;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * Created by Baoyi Chen on 2017/2/24.
 */
public class ErrorSupport {

    private static final Properties messages = new Properties();

    static {
        try (InputStream stream = TreeSupport.class.getClassLoader().getResourceAsStream("messages.properties")) {
            messages.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void error(String key, Object... params) {
        throw new RuntimeException(errorMsg(key, params));
    }

    public static String errorMsg(String key, Object... params) {
        return MessageFormat.format(messages.getProperty(key), params);
    }

    public static String errorPos(TreeNode node) {
        return errorPos(node.starPos, node.endPos);
    }

    public static String errorPos(Token token) {
        return errorPos(token.startPos, token.endPos);
    }

    public static String errorPos(Pos startPos, Pos endPos) {
        return " at " + startPos + " to " + endPos;
    }

}
