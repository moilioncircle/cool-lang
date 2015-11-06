package com.leon.cool.lang.parser;

import com.leon.cool.lang.ast.*;
import com.leon.cool.lang.factory.TreeFactory;
import com.leon.cool.lang.support.ClassTable;
import com.leon.cool.lang.support.Utils;
import com.leon.cool.lang.tokenizer.CoolScanner;
import com.leon.cool.lang.tokenizer.Filter;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.tokenizer.TokenKind;
import com.leon.cool.lang.util.Constant;
import com.leon.cool.lang.util.Pos;
import com.leon.cool.lang.util.Stack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.leon.cool.lang.tokenizer.TokenKind.*;

/**
 * Copyright leon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author leon on 15-10-8
 */
public class CoolParser {
    private final CoolScanner scanner;
    private final TreeFactory f;
    public final List<String> errMsgs = new ArrayList<>();

    public CoolParser(CoolScanner scanner, TreeFactory f) {
        this.scanner = scanner;
        this.f = f;
        scanner.nextToken();
        token = scanner.token();
    }

    /*
    program ::= [[class; ]] +
     */
    public Program parseProgram() {
        List<ClassDef> classDefs = new ArrayList<>();
        if (token.kind == EOF) {
            syntaxError(token.kind, token.startPos);
        } else {
            Pos startPos = token.startPos;
            classDefs.addAll(new ClassTable().installBasicClasses());
            while (true) {
                if (token.kind == EOF) {
                    accept(EOF);
                    return f.at(startPos, classDefs.get(classDefs.size() - 1).endPos).program(classDefs);
                }
                try {
                    ClassDef classDef = parseClass();
                    classDefs.add(classDef);
                    accept(SEMI);
                } catch (RuntimeException e) {
                    reportSyntaxError(e.getMessage());
                    if (!errRecovery(true, false, false, EOF)) {
                        break;
                    }
                }
            }
        }
        return null;
    }

    /*
    class ::= class TYPE [inherits TYPE] { [[feature; ]] ∗ }
     */
    private ClassDef parseClass() {
        accept(CLASS);
        Pos startPos = scanner.prevToken().startPos;
        if (token.kind == TYPE) {
            Token type = token;
            nextToken();
            Optional<Token> inheritsType = Optional.of(new Token(Constant.OBJECT, TYPE));
            List<Feature> features = new ArrayList<>();
            if (token.kind == INHERITS) {
                accept(INHERITS);
                if (token.kind == TYPE) {
                    inheritsType = Optional.of(token);
                    nextToken();
                } else {
                    syntaxError(token.kind, token.startPos, TYPE);
                }
            }
            if (token.kind == LBRACE) {
                accept(LBRACE);
                if (token.kind == RBRACE) {
                    accept(RBRACE);
                } else {
                    while (true) {
                        if (token.kind == RBRACE) {
                            accept(RBRACE);
                            return f.at(startPos, scanner.prevToken().endPos).classDef(type, inheritsType, features);
                        }
                        try {
                            Feature feature = parseFeature();
                            features.add(feature);
                            accept(SEMI);
                        } catch (RuntimeException e) {
                            reportSyntaxError(e.getMessage());
                            if (!errRecovery(false, false, true, ID, RBRACE)) {
                                break;
                            }
                        }
                    }

                }
                return f.at(startPos, scanner.prevToken().endPos).classDef(type, inheritsType, features);
            }

        } else {
            syntaxError(token.kind, token.startPos, TYPE);
        }
        return null;
    }

    /*
    feature ::= ID( [ formal [[, formal]] ∗ ] ) : TYPE { expr }
            |ID : TYPE [ <- expr ]
     */
    private Feature parseFeature() {
        if (token.kind == ID) {
            Token id = token;
            nextToken();
            switch (token.kind) {
                case LPAREN:
                    nextToken();
                    List<Formal> formals = new ArrayList<>();
                    if (token.kind == RPAREN) {
                        accept(RPAREN);
                    } else {
                        do {
                            Formal formal = parseFormal();
                            formals.add(formal);
                        } while (isToken(COMMA));
                        accept(RPAREN);
                    }
                    accept(COL);
                    if (token.kind == TYPE) {
                        Token returnType = token;
                        nextToken();
                        accept(LBRACE);
                        Expression expr = parseExpr();
                        accept(RBRACE);
                        return f.at(id.startPos, scanner.prevToken().endPos).methodDef(id, formals, returnType, expr);
                    } else {
                        syntaxError(token.kind, token.startPos, TYPE);
                    }
                case COL:
                    accept(COL);
                    if (token.kind == TYPE) {
                        Token type = token;
                        Optional<Expression> exprOpt = Optional.empty();
                        nextToken();
                        if (token.kind == LTSUB) {
                            accept(LTSUB);
                            exprOpt = Optional.of(parseExpr());
                        }
                        return f.at(id.startPos, exprOpt.isPresent() ? exprOpt.get().endPos : type.endPos).attrDef(id, type, exprOpt);
                    } else {
                        syntaxError(token.kind, token.startPos, TYPE);
                    }
                default:
                    syntaxError(token.kind, token.startPos, COL, ID);
            }
        } else {
            syntaxError(token.kind, token.startPos, ID);
        }
        return null;
    }

    /*
    formal ::= ID : TYPE
     */
    private Formal parseFormal() {
        if (token.kind == ID) {
            Token id = token;
            nextToken();
            accept(COL);
            if (token.kind == TYPE) {
                Formal formal = f.at(id.startPos, token.endPos).formal(id, token);
                nextToken();
                return formal;
            }
        } else {
            syntaxError(token.kind, token.startPos, ID);
        }
        return null;
    }

    /*
    expr ::= ID <- expr
        | ID( [ expr [[, expr]] ∗ ] )
        | ID
        | if expr then expr else expr fi
        | while expr loop expr pool
        | { [[expr; ]] + }
        | let ID : TYPE [ <- expr ] [[, ID : TYPE [ <- expr ]]] ∗ in expr
        | case expr of [[ID : TYPE => expr; ]] + esac
        | new TYPE
        | isvoid expr
        | ~expr
        | not expr
        | (expr)
        | integer
        | string
        | true
        | false

        -- left recursive
        | expr[@TYPE].ID( [ expr [[, expr]] ∗ ] )
        | expr + expr
        | expr − expr
        | expr ∗ expr
        | expr / expr
        | expr < expr
        | expr <= expr
        | expr = expr
     */

    /*
    expr = false... exprRest

    exprRest = + - / * expr exprRest
     */
    public Expression parseExpr() {
        newSuffixExprList();
        newOpStack();
        newErrStartToken();
        Expression expr = parseExpr(0);
        suffixExprListSupply.remove(suffixExprListSupply.size() - 1);
        opStackSupply.remove(opStackSupply.size() - 1);
        errStartTokenSupply.pop();
        return expr;
    }

    /*
    Same as SymbolTable
     */
    private final List<List<Object>> suffixExprListSupply = new ArrayList<>();
    private final List<Stack<TokenKind>> opStackSupply = new ArrayList<>();
    private final Stack<Token> errStartTokenSupply = new Stack<>();
    private Token errEndToken;

    private void newErrStartToken() {
        errStartTokenSupply.push(token);
    }

    private List<Object> newSuffixExprList() {
        suffixExprListSupply.add(new ArrayList<>());
        return suffixExprListSupply.get(suffixExprListSupply.size() - 1);
    }

    private Stack<TokenKind> newOpStack() {
        opStackSupply.add(new Stack<>());
        return opStackSupply.get(opStackSupply.size() - 1);
    }

    private Expression add(Expression expr) {
        suffixExprListSupply.get(suffixExprListSupply.size() - 1).add(expr);
        return expr;
    }

    private TokenKind add(TokenKind op) {
        suffixExprListSupply.get(suffixExprListSupply.size() - 1).add(op);
        return op;
    }

    private Token add(Token tok) {
        suffixExprListSupply.get(suffixExprListSupply.size() - 1).add(tok);
        return tok;
    }

    private void pushOp(TokenKind o1) {
        Stack<TokenKind> opStack = opStackSupply.get(opStackSupply.size() - 1);

        switch (o1) {
            case MONKEYS_AT:
            case DOT:
            case LTSUB:
            case NOT:
            case ISVOID:
            case TILDE:
            case PLUS:
            case SUB:
            case STAR:
            case SLASH:
            case LT:
            case LTEQ:
            case EQ:
                if (opStack.isEmpty()) {
                    opStack.push(o1);
                } else {
                    while (!opStack.isEmpty() && ((o1.assoc != Assoc.RIGHT && o1.prec <= opStack.top().prec) || (o1.assoc == Assoc.RIGHT && o1.prec < opStack.top().prec))) {
                        add(opStack.pop());
                    }
                    opStack.push(o1);
                }
                break;
            default:
                while (!opStack.isEmpty()) {
                    add(opStack.pop());
                }
        }

    }

    private Expression parseExpr(int prec) {
        Expression returnExpr = null;
        Pos startPos = token.startPos;
        switch (token.kind) {
            case INTEGER:
                Expression expr = f.at(startPos, token.endPos).intConst(token);
                nextToken();
                add(expr);
                returnExpr = parseExprRest(expr);
                break;
            case STRING:
                expr = f.at(startPos, token.endPos).stringConst(token);
                nextToken();
                add(expr);
                returnExpr = parseExprRest(expr);
                break;
            case TRUE:
                expr = f.at(startPos, token.endPos).boolConst(true);
                nextToken();
                add(expr);
                returnExpr = parseExprRest(expr);
                break;
            case FALSE:
                expr = f.at(startPos, token.endPos).boolConst(false);
                nextToken();
                add(expr);
                returnExpr = parseExprRest(expr);
                break;
            case LPAREN:
                accept(LPAREN);
                expr = f.paren(parseExpr());
                accept(RPAREN);
                add(expr);
                returnExpr = parseExprRest(expr);
                break;
            case NOT:
                accept(NOT);
                pushOp(NOT);
                returnExpr = parseExpr(0);
                break;
            case TILDE:
                accept(TILDE);
                pushOp(TILDE);
                returnExpr = parseExpr(0);
                break;
            case ISVOID:
                accept(ISVOID);
                pushOp(ISVOID);
                returnExpr = parseExpr(0);
                break;
            case NEW:
                accept(NEW);
                if (token.kind == TYPE) {
                    expr = f.at(startPos, token.endPos).newDef(token);
                    nextToken();
                    add(expr);
                    returnExpr = parseExprRest(expr);
                } else {
                    syntaxError(token.kind, token.startPos, TYPE);
                }
                break;
            case CASE:
                accept(CASE);
                expr = parseExpr();
                accept(OF);
                if (peekToken(0, ESAC)) {
                    syntaxError(token.kind, token.startPos);
                } else {
                    List<Branch> branches = new ArrayList<>();
                    do {
                        if (token.kind == ESAC) {
                            accept(ESAC);
                            Expression caseExpr = f.at(startPos, scanner.prevToken().endPos).caseDef(expr, branches);
                            add(caseExpr);
                            returnExpr = parseExprRest(caseExpr);
                            break;
                        }
                        if (token.kind == ID) {
                            Token id = token;
                            nextToken();
                            accept(COL);
                            if (token.kind == TYPE) {
                                Token type = token;
                                nextToken();
                                accept(EQGT);
                                Expression branchExpr = parseExpr();
                                Branch branch = f.at(id.startPos, branchExpr.endPos).branch(id, type, branchExpr);
                                branches.add(branch);
                            } else {
                                syntaxError(token.kind, token.startPos, TYPE);
                            }
                        } else {
                            syntaxError(token.kind, token.startPos, ID);
                        }
                    } while (isToken(SEMI));
                }
                break;
            case LET:
                accept(LET);
                if (token.kind == IN) {
                    syntaxError(token.kind, token.startPos);
                } else {
                    List<LetAttrDef> attrDefs = new ArrayList<>();
                    do {
                        try {
                            if (token.kind == ID) {
                                Token id = token;
                                nextToken();
                                accept(COL);
                                if (token.kind == TYPE) {
                                    Token type = token;
                                    Optional<Expression> exprOpt = Optional.empty();
                                    nextToken();
                                    if (token.kind == LTSUB) {
                                        accept(LTSUB);
                                        exprOpt = Optional.of(parseExpr());
                                    }
                                    attrDefs.add(f.at(id.startPos, exprOpt.isPresent() ? exprOpt.get().endPos : type.endPos).letAttrDef(id, type, exprOpt));
                                } else {
                                    syntaxError(token.kind, token.startPos, TYPE);
                                }
                            } else {
                                syntaxError(token.kind, token.startPos, ID);
                            }
                        } catch (RuntimeException e) {
                            reportSyntaxError(e.getMessage());
                            errRecovery(false, true, false, IN);
                        }
                    } while (isToken(COMMA));
                    accept(IN);
                    expr = parseExpr();
                    Expression letExpr = f.at(startPos, expr.endPos).let(attrDefs, expr);
                    returnExpr = letExpr;
                }
                break;
            case LBRACE:
                accept(LBRACE);
                List<Expression> exprs = new ArrayList<>();
                if (peekToken(0, RBRACE)) {
                    syntaxError(token.kind, token.startPos);
                } else {
                    while (true) {
                        if (token.kind == RBRACE) {
                            accept(RBRACE);
                            Expression blocksExpr = f.at(startPos, scanner.prevToken().endPos).blocks(exprs);
                            add(blocksExpr);
                            returnExpr = parseExprRest(blocksExpr);
                            break;
                        }
                        try {
                            expr = parseExpr();
                            exprs.add(expr);
                            accept(SEMI);
                        } catch (RuntimeException e) {
                            reportSyntaxError(e.getMessage());
                            if (!errRecovery(false, false, true, RBRACE)) {
                                break;
                            }
                        }

                    }
                }
                break;
            case WHILE:
                accept(WHILE);
                expr = parseExpr();
                accept(LOOP);
                Expression loopExpr = parseExpr();
                accept(POOL);
                Expression whileExpr = f.at(startPos, scanner.prevToken().endPos).loop(expr, loopExpr);
                add(whileExpr);
                returnExpr = parseExprRest(whileExpr);
                break;
            case IF:
                accept(IF);
                expr = parseExpr();
                accept(THEN);
                Expression thenExpr = parseExpr();
                accept(ELSE);
                Expression elseExpr = parseExpr();
                accept(FI);
                Expression ifExpr = f.at(startPos, scanner.prevToken().endPos).cond(expr, thenExpr, elseExpr);
                add(ifExpr);
                returnExpr = parseExprRest(ifExpr);
                break;
            case ID:
                Token id = token;
                if (peekToken(0, LPAREN)) {
                    nextToken();
                    accept(LPAREN);
                    List<Expression> params = new ArrayList<>();
                    if (token.kind != RPAREN) {
                        do {
                            Expression param = parseExpr();
                            params.add(param);
                        } while (isToken(COMMA));
                    }
                    accept(RPAREN);
                    Expression dispatchExpr = f.at(id.startPos, scanner.prevToken().endPos).dispatch(id, params);
                    add(dispatchExpr);
                    returnExpr = parseExprRest(dispatchExpr);
                } else {
                    expr = f.at(id.startPos, id.endPos).idConst(id);
                    nextToken();
                    add(expr);
                    returnExpr = parseExprRest(expr);
                }
                break;
            default:
                syntaxError(token.kind, token.startPos, INTEGER, ID, TRUE, FALSE, STRING, LPAREN, NOT, ISVOID, NEW, TILDE, LBRACE, IF, WHILE, LET, CASE);
        }
        assert returnExpr != null;
        return returnExpr;
    }

    private Expression parseExprRest(Expression left) {
        switch (token.kind) {
            case PLUS:
                accept(PLUS);
                pushOp(PLUS);
                left = parseExpr(0);
                return left;
            case SUB:
                accept(SUB);
                pushOp(SUB);
                left = parseExpr(0);
                return left;
            case STAR:
                accept(STAR);
                pushOp(STAR);
                left = parseExpr(0);
                return left;
            case SLASH:
                accept(SLASH);
                pushOp(SLASH);
                left = parseExpr(0);
                return left;
            case LT:
                accept(LT);
                pushOp(LT);
                left = parseExpr(0);
                return parseExprRest(left);
            case LTEQ:
                accept(LTEQ);
                pushOp(LTEQ);
                left = parseExpr(0);
                return left;
            case EQ:
                accept(EQ);
                pushOp(EQ);
                left = parseExpr(0);
                return left;
            case LTSUB:
                accept(LTSUB);
                pushOp(LTSUB);
                left = parseExpr(0);
                return left;
            case MONKEYS_AT:
            case DOT:
                if (token.kind == MONKEYS_AT) {
                    accept(MONKEYS_AT);
                    pushOp(MONKEYS_AT);
                    if (token.kind == TYPE) {
                        add(token);
                        nextToken();
                    }
                }
                if (token.kind == DOT) {
                    accept(DOT);
                    if (token.kind == ID) {
                        Token id = token;
                        nextToken();
                        accept(LPAREN);
                        List<Expression> params = new ArrayList<>();
                        if (token.kind != RPAREN) {
                            do {
                                Expression param = parseExpr();
                                params.add(param);
                            } while (isToken(COMMA));
                        }
                        accept(RPAREN);
                        pushOp(DOT);
                        left = f.at(id.startPos, scanner.prevToken().endPos).staticDispatchBody(id, params);
                        add(left);
                        return parseExprRest(left);
                    } else {
                        syntaxError(token.kind, token.startPos, TokenKind.ID);
                    }
                }
            case TILDE:
            case NOT:
            case ISVOID:
            default:
                pushOp(token.kind);
                List<Object> suffixExpr = suffixExprListSupply.get(opStackSupply.size() - 1);
                errEndToken = scanner.prevToken();
                return expr(suffixExpr);
        }
    }

    /*
    Shunting yard algorithm
     */
    private Expression expr(List<Object> suffixExpr) {
        Pos startPos = errStartTokenSupply.top().startPos;
        Pos endPos = errEndToken.endPos;
        Stack<Object> stack = new Stack<>();
        boolean nonAssoc = false;
        for (int i = 0; i < suffixExpr.size(); i++) {
            if (suffixExpr.get(i) instanceof TokenKind) {
                TokenKind op = (TokenKind) suffixExpr.get(i);
                switch (op) {
                    case MONKEYS_AT:
                        Utils.error("parser.error.unexpected.at", Utils.errorPos(errStartTokenSupply.top().startPos, errEndToken.endPos));
                    case DOT:
                        if (nextIsAt(i, suffixExpr)) {
                            StaticDispatchBody dispatch = this.getGenericElement(stack);
                            Token type = this.getGenericElement(stack);
                            Expression expr = this.getGenericElement(stack);
                            stack.push(f.at(startPos, endPos).staticDispatch(expr, Optional.of(type), dispatch));
                            i++;
                        } else {
                            StaticDispatchBody dispatch = this.getGenericElement(stack);
                            Expression expr = this.getGenericElement(stack);
                            // self.doSomething() equals to doSomething(). this is totally for simple tail-recursive optimization.
                            if (expr instanceof IdConst && Utils.isSelf(((IdConst) expr).tok)) {
                                stack.push(f.at(startPos, endPos).dispatch(dispatch.id, dispatch.params));
                            } else {
                                stack.push(f.at(startPos, endPos).staticDispatch(expr, Optional.empty(), dispatch));
                            }
                        }
                        break;
                    case LTSUB:
                        Expression expr = this.getGenericElement(stack);
                        IdConst id = this.getGenericElement(stack);
                        stack.push(f.at(startPos, endPos).assign(id, expr));
                        break;
                    case NOT:
                        expr = this.getGenericElement(stack);
                        stack.push(f.at(startPos, endPos).not(expr));
                        break;
                    case ISVOID:
                        expr = this.getGenericElement(stack);
                        stack.push(f.at(startPos, endPos).isVoid(expr));
                        break;
                    case TILDE:
                        expr = this.getGenericElement(stack);
                        stack.push(f.at(startPos, endPos).neg(expr));
                        break;
                    case PLUS:
                        Expression right = this.getGenericElement(stack);
                        Expression left = this.getGenericElement(stack);
                        stack.push(f.at(startPos, endPos).plus(left, right));
                        break;
                    case SUB:
                        right = this.getGenericElement(stack);
                        left = this.getGenericElement(stack);
                        stack.push(f.at(startPos, endPos).sub(left, right));
                        break;
                    case STAR:
                        right = this.getGenericElement(stack);
                        left = this.getGenericElement(stack);
                        stack.push(f.at(startPos, endPos).mul(left, right));
                        break;
                    case SLASH:
                        right = this.getGenericElement(stack);
                        left = this.getGenericElement(stack);
                        stack.push(f.at(startPos, endPos).divide(left, right));
                        break;
                    case LT:
                        if (nonAssoc) {
                            Utils.error("parser.error.non.assoc", Utils.errorPos(errStartTokenSupply.top().startPos, errEndToken.endPos));
                        } else {
                            right = this.getGenericElement(stack);
                            left = this.getGenericElement(stack);
                            stack.push(f.at(startPos, endPos).lt(left, right));
                            nonAssoc = true;
                        }
                        break;
                    case LTEQ:
                        if (nonAssoc) {
                            Utils.error("parser.error.non.assoc", Utils.errorPos(errStartTokenSupply.top().startPos, errEndToken.endPos));
                        } else {
                            right = this.getGenericElement(stack);
                            left = this.getGenericElement(stack);
                            stack.push(f.at(startPos, endPos).ltEq(left, right));
                            nonAssoc = true;
                        }
                        break;
                    case EQ:
                        if (nonAssoc) {
                            Utils.error("parser.error.non.assoc", Utils.errorPos(errStartTokenSupply.top().startPos, errEndToken.endPos));
                        } else {
                            right = this.getGenericElement(stack);
                            left = this.getGenericElement(stack);
                            stack.push(f.at(startPos, endPos).comp(left, right));
                            nonAssoc = true;
                        }
                        break;
                    default:
                        Utils.error("parser.error.expr", Utils.errorPos(errStartTokenSupply.top().startPos, errEndToken.endPos));
                }
            } else {
                stack.push(suffixExpr.get(i));
            }
        }
        assert stack.size() == 1;
        return (Expression) stack.pop();
    }

    private <T> T getGenericElement(Stack<Object> stack) {
        try {
            return (T) stack.pop();
        } catch (ClassCastException e) {
            Utils.error("parser.error.expr", Utils.errorPos(errStartTokenSupply.top().startPos, errEndToken.endPos));
        }
        return null;
    }

    private boolean errRecovery(boolean stopAtClass, boolean stopAtComma, boolean stopAtSemi, TokenKind... tks) {
        while (true) {
            if (Arrays.asList(tks).stream().filter(e -> token.kind == e).findFirst().isPresent()) {
                return true;
            }
            switch (token.kind) {
                case CLASS:
                    if (stopAtClass) {
                        return true;
                    }
                    break;
                case SEMI:
                    if (stopAtSemi) {
                        nextToken();
                        return true;
                    }
                    break;
                case COMMA:
                    if (stopAtComma) {
                        return true;
                    }
                    break;
                case EOF:
                    return false;
            }
            nextToken();
        }
    }

    private boolean nextIsAt(int i, List<Object> suffixExpr) {
        if ((i + 1) < suffixExpr.size() && suffixExpr.get(i + 1) instanceof TokenKind) {
            TokenKind op = (TokenKind) suffixExpr.get(i + 1);
            if (op == MONKEYS_AT) {
                return true;
            }
        }
        return false;
    }

    private Token token;

    private Token token() {
        return token;
    }

    private void nextToken() {
        scanner.nextToken();
        token = scanner.token();
    }

    private boolean peekToken(Filter<TokenKind> tk) {
        return peekToken(0, tk);
    }

    private boolean peekToken(int lookAhead, Filter<TokenKind> tk) {
        return tk.accepts(scanner.token(lookAhead + 1).kind);
    }

    private boolean peekToken(Filter<TokenKind> tk1, Filter<TokenKind> tk2) {
        return peekToken(0, tk1, tk2);
    }

    private boolean peekToken(int lookAhead, Filter<TokenKind> tk1, Filter<TokenKind> tk2) {
        return tk1.accepts(scanner.token(lookAhead + 1).kind) &&
                tk2.accepts(scanner.token(lookAhead + 2).kind);
    }

    private boolean peekToken(Filter<TokenKind> tk1, Filter<TokenKind> tk2, Filter<TokenKind> tk3) {
        return peekToken(0, tk1, tk2, tk3);
    }

    private boolean peekToken(int lookAhead, Filter<TokenKind> tk1, Filter<TokenKind> tk2, Filter<TokenKind> tk3) {
        return tk1.accepts(scanner.token(lookAhead + 1).kind) &&
                tk2.accepts(scanner.token(lookAhead + 2).kind) &&
                tk3.accepts(scanner.token(lookAhead + 3).kind);
    }

    private boolean peekToken(Filter<TokenKind>... kinds) {
        return peekToken(0, kinds);
    }

    private boolean peekToken(int lookAhead, Filter<TokenKind>... kinds) {
        for (; lookAhead < kinds.length; lookAhead++) {
            if (!kinds[lookAhead].accepts(scanner.token(lookAhead + 1).kind)) {
                return false;
            }
        }
        return true;
    }

    private void accept(TokenKind tk) {
        if (token.kind == tk) {
            nextToken();
        } else {
            reportSyntaxError(token.kind, token.startPos, tk);
        }
    }

    private boolean isToken(TokenKind tk) {
        if (token.kind == tk) {
            nextToken();
            return true;
        } else {
            return false;
        }
    }

    private void syntaxError(TokenKind actual, Pos pos, TokenKind... tks) {
        Utils.error("parser.error.expected", Utils.mkString(Arrays.asList(tks), ","), actual.toString(), pos.toString());
    }

    private void reportSyntaxError(TokenKind actual, Pos pos, TokenKind... tks) {
        reportSyntaxError(Utils.errorMsg("parser.error.expected", Utils.mkString(Arrays.asList(tks), ","), actual.toString(), pos.toString()));
    }

    private void reportSyntaxError(String message) {
        errMsgs.add(message);
    }

    private void syntaxError(TokenKind actual, Pos pos) {
        Utils.error("parser.error.unexpected", actual.toString(), pos.toString());
    }

}
