package com.leon.cool.lang.tokenizer;

import com.leon.cool.lang.parser.Assoc;
import com.leon.cool.lang.support.Dumpable;

/**
 * Created by leon on 15-10-7.
 */
public enum TokenKind implements Filter<TokenKind>, Dumpable {
    //
    ID(TokenTag.NAME), TYPE(TokenTag.TYPE), STRING(TokenTag.STR), INTEGER(TokenTag.NUM), COMMENT(TokenTag.COMMENT),
    //KEY WORDS
    CLASS("class"), ELSE("else"), FALSE("false"), FI("fi"), IF("if"), IN("in"), INHERITS("inherits"), ISVOID("isvoid", 70), LET("let"), LOOP("loop"),
    POOL("pool"), THEN("then"), WHILE("while"), CASE("case"), ESAC("esac"), NEW("new"), OF("of"), NOT("not", 30), TRUE("true"),
    //OPERATORS
    TILDE("~", 80), PLUS("+", 50), SUB("-", 50), STAR("*", 60), SLASH("/", 60), LTEQ("<=", 40, Assoc.NONE), LT("<", 40, Assoc.NONE),
    //OTHERS
    DOT(".", 100), MONKEYS_AT("@", 90), SEMI(";"), LBRACE("{"), RBRACE("}"), LPAREN("("), RPAREN(")"), EQGT("=>"), COL(":"), EQ("=", 40, Assoc.NONE), LTSUB("<-", 20, Assoc.RIGHT), COMMA(","),
    //ERROR,EOF
    ERROR(), EOF();
    public final String name;
    public final TokenTag tag;
    public int prec = 10;
    public Assoc assoc = Assoc.LEFT;

    TokenKind(String name) {
        this(name, TokenTag.DEFAULT, 10, Assoc.LEFT);
    }

    TokenKind(String name, int prec, Assoc assoc) {
        this(name, TokenTag.DEFAULT, prec, assoc);
    }

    TokenKind(String name, int prec) {
        this(name, TokenTag.DEFAULT, prec, Assoc.LEFT);
    }

    TokenKind() {
        this(null, TokenTag.DEFAULT, 10, Assoc.LEFT);
    }

    TokenKind(TokenTag tag) {
        this(null, tag, 10, Assoc.LEFT);
    }

    TokenKind(String name, TokenTag tag, int prec, Assoc assoc) {
        this.name = name;
        this.tag = tag;
        this.prec = prec;
        this.assoc = assoc;
    }

    @Override
    public boolean accepts(TokenKind that) {
        return this == that;
    }

    @Override
    public void dump() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        switch (tag) {
            case NAME:
                return "ID";
            case TYPE:
                return "Type";
            case STR:
                return "String";
            case NUM:
                return "Int";
            case COMMENT:
                return "Comment";
            case DEFAULT:
                return "'" + name + "'";
            default:
                return "";
        }
    }
}
