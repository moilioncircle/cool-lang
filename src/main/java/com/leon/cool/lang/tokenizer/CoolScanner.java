package com.leon.cool.lang.tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 15-10-8.
 */
public class CoolScanner {

    private Token token;

    private Token prevToken;

    private List<Token> savedTokens = new ArrayList<>();

    private CoolTokenizer tokenizer;

    public CoolScanner(CoolTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public Token token() {
        return token(0);
    }

    public Token token(int lookahead) {
        if (lookahead == 0) {
            return token;
        } else {
            ensureLookahead(lookahead);
            return savedTokens.get(lookahead - 1);
        }
    }

    //where
    private void ensureLookahead(int lookahead) {
        for (int i = savedTokens.size(); i < lookahead; i++) {
            savedTokens.add(tokenizer.readToken());
        }
    }

    public Token prevToken() {
        return prevToken;
    }

    public void nextToken() {
        prevToken = token;
        if (!savedTokens.isEmpty()) {
            token = savedTokens.remove(0);
        } else {
            token = tokenizer.readToken();
        }
    }
}
