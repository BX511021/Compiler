import Node.*;
import global.Errorpart.ErrorLog;
import symboltable.*;
import symboltable.symbols.SymbolItem;

import java.util.ArrayList;

public class Visitor {
    private SymbolTable symbolTable;
    private Node root;

    public Visitor(Node root) {
        this.symbolTable = new SymbolTable();
        this.root = root;
    }
    private void FindError(String message) {
        ErrorLog.ErrorLogadd(message);
    }
//    检测是否为分支节点，并且检测符号
    private Boolean ckBrN(Node n, GUNIT type) {
        return (n instanceof BranchNode &&
                ((BranchNode) n).getGUnit().equals(type));
    }
//    检测叶节点，并且检测对应的符号
    private Boolean ckLfN(Node n, SYMBOL type) {
        return (n instanceof LeafNode &&
                ((LeafNode) n).getType().equals(type));
    }
//    获取特定子节点
    private Node export(ArrayList<Node> children, int i) {
        try {
            return children.get(i);
        } catch (IndexOutOfBoundsException e) {
            return new LeafNode(SYMBOL.UNKNOWN, null, false);
        }
    }
//    启动函数
    public void visit() {
        CompUnit(root);
        symbolTable.toLog();
    }

    private void CompUnit(Node syn){
        ArrayList<Node> cs = syn.getChildren();
        symbolTable.addOneBlock(BlockType.GLOBAL_BLOCK);
        int i = 0;
        while (ckBrN(export(cs, i), GUNIT.Decl)) {
            Decl(export(cs, i++));
        }

        while (ckBrN(export(cs, i), GUNIT.FuncDef)) {
            FuncDef(export(cs, i++));
        }

        MainFuncDef(export(cs, i));
        symbolTable.exitPresentBlock();
    }

    private void Decl(Node syn){

    }
    private void ConstDecl(Node syn){

    }
    private BasicType BType(Node syn){
        return BasicType.INT;
    }
    private void ConstDef(Node syn, BasicType type){

    }

    private Object ConstInitVal(Node syn){
        return null;
    }

    private void VarDecl(Node syn){

    }

    private void VarDef(Node syn, BasicType type){

    }

    private void InitVal(Node syn){

    }
    private void FuncDef(Node syn){

    }
    private void MainFuncDef(Node syn){}

    private BasicType FuncType(Node syn){return null;}

    private ArrayList<SymbolItem> FuncFParams(Node syn){return null;}

    private SymbolItem FuncFParam(Node syn){return null;}

    private void Block(Node syn){}

    private void BlockItem(Node syn){}

    private void Stmt(Node syn){}

    private SymbolItem Exp(Node syn){return null;}

    private SymbolItem Cond(Node syn){return null;}

    private SymbolItem LVal(Node syn){return null;}

    private SymbolItem PrimaryExp(Node syn){return null;}

    private SymbolItem Number(Node syn){return null;}

    private SymbolItem UnaryExp(Node syn){return null;}

    private void UnaryOp(Node syn){return;}

    private ArrayList<SymbolItem> FuncRParams(Node syn){return null;}

    private SymbolItem MulExp(Node syn){return null;}

    private SymbolItem AddExp(Node syn){return null;}

    private SymbolItem RelExp(Node syn){return null;}

    private SymbolItem EqExp(Node syn){return null;}

    private SymbolItem LAndExp(Node syn){return null;}

    private SymbolItem LOrExp(Node syn){return null;}

    private Integer ConstExp(Node syn){return null;}

    private Integer calNode(Node syn){return null;}











}
