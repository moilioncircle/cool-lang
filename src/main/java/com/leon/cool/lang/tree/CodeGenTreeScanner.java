package com.leon.cool.lang.tree;

import com.leon.cool.lang.Constant;
import com.leon.cool.lang.ast.*;
import com.leon.cool.lang.support.CgenSupport;
import com.leon.cool.lang.support.ScannerSupport;
import com.leon.cool.lang.support.TypeSupport;
import com.leon.cool.lang.support.declaration.AttrDeclaration;
import com.leon.cool.lang.support.infrastructure.ConstantPool;
import com.leon.cool.lang.util.Stack;

import java.io.PrintStream;
import java.util.Collections;
import java.util.Map;

import static com.leon.cool.lang.Constant.*;

/**
 * Created by leon on 4/4/16.
 */
public class CodeGenTreeScanner extends TreeScanner {

    private final PrintStream str;

    public CodeGenTreeScanner(ScannerSupport scannerSupport, String fileName) {
        super(scannerSupport);
        str = System.err;
        codeConstants();
        codeClassNameTab();
        codeClassObjTab();
        codeDispTab();
        codeProtObj();
        codeGlobalText();
    }

    @Override
    public void applyAssign(Assign assign) {

    }

    @Override
    public void applyBlocks(Blocks blocks) {

    }

    @Override
    public void applyNewDef(NewDef newDef) {

    }

    @Override
    public void applyIsVoid(IsVoid isVoid) {

    }

    @Override
    public void applyPlus(Plus plus) {

    }

    @Override
    public void applySub(Sub sub) {

    }

    @Override
    public void applyMul(Mul mul) {

    }

    @Override
    public void applyDivide(Divide divide) {

    }

    @Override
    public void applyNeg(Neg neg) {

    }

    @Override
    public void applyLt(Lt lt) {

    }

    @Override
    public void applyLtEq(LtEq ltEq) {

    }

    @Override
    public void applyComp(Comp comp) {

    }

    @Override
    public void applyNot(Not not) {

    }

    @Override
    public void applyIdConst(IdConst idConst) {

    }

    @Override
    public void applyParen(Paren paren) {

    }

    @Override
    public void applyNoExpression(NoExpression expr) {

    }

    @Override
    public void applyDispatch(Dispatch dispatch) {

    }

    @Override
    public void applyStaticDispatchBody(StaticDispatchBody staticDispatchBody) {

    }

    @Override
    public void applyStaticDispatch(StaticDispatch staticDispatch) {

    }

    @Override
    public void applyCond(Cond cond) {

    }

    @Override
    public void applyLoop(Loop loop) {

    }

    @Override
    public void applyLet(Let let) {

    }

    @Override
    public void applyCaseDef(CaseDef caseDef) {

    }

    @Override
    public void applyBranch(Branch branch) {

    }

    @Override
    public void applyLetAttrDef(LetAttrDef letAttrDef) {

    }

    private void codeConstants() {
        ConstantPool.getInstance().genConstantPool(str);
    }

    private void codeGlobalText() {
        str.println(CgenSupport.GLOBAL + CgenSupport.HEAP_START);
        str.print(CgenSupport.HEAP_START + CgenSupport.LABEL);
        str.println(CgenSupport.WORD + 0);
        str.println("\t.text");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitInitRef(MAIN_CLASS, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitInitRef(INT, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitInitRef(STRING, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitInitRef(BOOL, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitMethodRef(MAIN_CLASS, MAIN_METHOD, str);
        str.println("");
    }

    private void codeClassNameTab() {
        str.print(CgenSupport.CLASSNAMETAB + CgenSupport.LABEL);
        scannerSupport.classGraph.keySet().forEach(e -> {
            str.print(CgenSupport.WORD); // tag
            ConstantPool.getInstance().addString(e).codeRef(str);
            str.println();
        });
    }

    private void codeClassObjTab() {
        str.print(CgenSupport.CLASSOBJTAB + CgenSupport.LABEL);
        scannerSupport.classGraph.keySet().forEach(e -> {
            str.print(CgenSupport.WORD);
            CgenSupport.emitProtObjRef(e, str);
            str.println();
            str.print(CgenSupport.WORD);
            CgenSupport.emitInitRef(e, str);
            str.println();
        });
    }

    private void codeDispTab() {
        scannerSupport.methodGraph.entrySet().forEach(e -> {
            String className = e.getKey();
            CgenSupport.emitDispTableRef(className, str);
            str.print(CgenSupport.LABEL);

            e.getValue().forEach(m -> {
                str.print(CgenSupport.WORD);
                CgenSupport.emitMethodRef(m.belongs, m.methodName, str);
                str.println();
            });
        });
    }

    private void codeProtObj() {
        // 0 Object
        // 1 IO
        // 2 Int
        // 3 Bool
        // 4 String
        // 5+ self define class tag
        int index = Constant.SELF_DEFINE_TAG;
        for (String className : scannerSupport.classGraph.keySet()) {
            switch (className) {
                case Constant.INT:
                    str.println(CgenSupport.WORD + "-1");
                    CgenSupport.emitProtObjRef(className, str);
                    str.print(CgenSupport.LABEL);
                    str.println(CgenSupport.WORD + Constant.INT_TAG); //tag
                    str.println(CgenSupport.WORD + (CgenSupport.DEFAULT_OBJFIELDS + CgenSupport.INT_SLOTS));
                    str.println(CgenSupport.WORD + className + CgenSupport.DISPTAB_SUFFIX);
                    str.println(CgenSupport.WORD + 0);
                    break;
                case Constant.BOOL:
                    str.println(CgenSupport.WORD + "-1");
                    CgenSupport.emitProtObjRef(className, str);
                    str.print(CgenSupport.LABEL);
                    str.println(CgenSupport.WORD + Constant.BOOL_TAG); //tag
                    str.println(CgenSupport.WORD + (CgenSupport.DEFAULT_OBJFIELDS + CgenSupport.BOOL_SLOTS));
                    str.println(CgenSupport.WORD + className + CgenSupport.DISPTAB_SUFFIX);
                    str.println(CgenSupport.WORD + 0);
                    break;
                case Constant.STRING:
                    str.println(CgenSupport.WORD + "-1");
                    CgenSupport.emitProtObjRef(className, str);
                    str.print(CgenSupport.LABEL);
                    str.println(CgenSupport.WORD + Constant.STRING_TAG); //tag
                    str.println(CgenSupport.WORD + (CgenSupport.DEFAULT_OBJFIELDS + CgenSupport.STRING_SLOTS + ("".length() + 4) / 4));
                    str.println(CgenSupport.WORD + className + CgenSupport.DISPTAB_SUFFIX);
                    //.word	int_const0
                    IntConst intConst = ConstantPool.getInstance().addInt(0);
                    str.print(CgenSupport.WORD);
                    intConst.codeRef(str);
                    str.println();
                    str.println(CgenSupport.WORD + 0);
                    break;
                case Constant.OBJECT:
                    str.println(CgenSupport.WORD + "-1");
                    CgenSupport.emitProtObjRef(className, str);
                    str.print(CgenSupport.LABEL);
                    str.println(CgenSupport.WORD + Constant.OBJECT_TAG); //tag
                    str.println(CgenSupport.WORD + CgenSupport.DEFAULT_OBJFIELDS);
                    str.println(CgenSupport.WORD + className + CgenSupport.DISPTAB_SUFFIX);
                    break;
                case Constant.IO:
                    str.println(CgenSupport.WORD + "-1");
                    CgenSupport.emitProtObjRef(className, str);
                    str.print(CgenSupport.LABEL);
                    str.println(CgenSupport.WORD + Constant.IO_TAG); //tag
                    str.println(CgenSupport.WORD + CgenSupport.DEFAULT_OBJFIELDS);
                    str.println(CgenSupport.WORD + className + CgenSupport.DISPTAB_SUFFIX);
                    break;
                default:
                    Stack<String> inheritsLinks = new Stack<>();
                    String temp = className;
                    int attrSize = 0;
                    while (temp != null) {
                        attrSize += scannerSupport.attrGraph.get(temp).values().size();
                        inheritsLinks.push(temp);
                        temp = scannerSupport.classGraph.get(temp);
                    }

                    str.println(CgenSupport.WORD + "-1");
                    CgenSupport.emitProtObjRef(className, str);
                    str.print(CgenSupport.LABEL);
                    str.println(CgenSupport.WORD + index++); //tag
                    str.println(CgenSupport.WORD + (CgenSupport.DEFAULT_OBJFIELDS + attrSize));
                    str.println(CgenSupport.WORD + className + CgenSupport.DISPTAB_SUFFIX);

                    /**
                     * 遍历继承树，找到父类中的属性。
                     * 如果父类中的属性是String,Bool,Int类型，则对属性赋默认值.
                     * 如果不是上述类型，则赋值void
                     * String  = ""
                     * Bool = false
                     * Int = 0
                     * Object = void
                     */
                    while (!inheritsLinks.isEmpty()) {
                        String parentClassName = inheritsLinks.pop();
                        Map<String, AttrDeclaration> attrs = scannerSupport.attrGraph.getOrDefault(parentClassName, Collections.EMPTY_MAP);
                        for (Map.Entry<String, AttrDeclaration> attr : attrs.entrySet()) {
                            if (TypeSupport.isStringType(attr.getValue().type)) {
                                StringConst stringConst = ConstantPool.getInstance().addString("");
                                str.print(CgenSupport.WORD);
                                stringConst.codeRef(str);
                                str.println();
                            } else if (TypeSupport.isBoolType(attr.getValue().type)) {
                                BoolConst boolConst = ConstantPool.getInstance().addBool(false);
                                str.print(CgenSupport.WORD);
                                boolConst.codeRef(str);
                                str.println();
                            } else if (TypeSupport.isIntType(attr.getValue().type)) {
                                intConst = ConstantPool.getInstance().addInt(0);
                                str.print(CgenSupport.WORD);
                                intConst.codeRef(str);
                                str.println();
                            } else {
                                str.println(CgenSupport.WORD + 0);
                            }
                        }
                    }
                    break;
            }
        }
    }
}
