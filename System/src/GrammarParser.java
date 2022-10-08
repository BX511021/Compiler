import Node.SYMBOL;
import Token.*;
import Node.*;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class GrammarParser {
    private final List<Token> tokenList;
    private Integer pToken;
    public StringBuilder stringBuilder;

    public GrammarParser(List<Token> tokenList) {
        this.tokenList = tokenList;
        this.pToken = -1;
        this.stringBuilder= new StringBuilder("");

    }

    private Token peek() {
        return tokenList.get(pToken + 1);
    }

    private Token peek(Integer offset) {
        if (pToken + offset >= tokenList.size()) {

            return new Token(SYMBOL.UNKNOWN, "unknown", Integer.MAX_VALUE);
        }
        return tokenList.get(pToken + offset);
    }

    private void next() {
        this.pToken++;
    }

    private Token getCurToken() {
        if (this.pToken == -1)
            return new Token(SYMBOL.UNKNOWN, "STARTVALUE", -1);
        try {
            return this.tokenList.get(this.pToken);
        } catch (IndexOutOfBoundsException e) {
            return new Token(SYMBOL.UNKNOWN, "ENDVALUE", Integer.MAX_VALUE);
        }

    }

    private Token preToken() {
        if (this.pToken == -1)
            return new Token(SYMBOL.UNKNOWN, "STARTVALUE", -1);
        try {
            return this.tokenList.get(this.pToken - 1);
        } catch (IndexOutOfBoundsException e) {
            return new Token(SYMBOL.UNKNOWN, "ENDVALUE", Integer.MAX_VALUE);
        }
    }

    /* 非终结符检查函数 */
    private Node checkMultSymbol(SYMBOL... symbols) {
//        System.out.println("Checking: " + peek().getValue() + " " + peek().getLine() + " with " + symbols);
        int cnt = 0;
        for (SYMBOL symbol : symbols) {
            if (peek().getSymbol().equals(symbol)) {
                cnt += 1;
            }
        }
        if (cnt == 0) {
            //  当检查失败，返回的Node内存储的是上一个符号的位置
            return new LeafNode(symbols[0], preToken(), false);
        } else {
            next();
//            System.out.println("check pass");
            return new LeafNode(getCurToken().getSymbol(), getCurToken(), true);
        }

    }
//    用于测试token的存在，并敲定为终结符号，设置为叶节点
    private Node checkSymbol(SYMBOL symbol) {
        if (peek().getSymbol().equals(symbol)) {
            next();
            return new LeafNode(symbol, getCurToken(), true);
        } else {
            //  当检查失败，返回的Node内存储的是上一个符号的位置
            return new LeafNode(symbol, preToken(), false);
        }

    }


    private Node pureMulOp() {
        SYMBOL[] bolster = {SYMBOL.DIV, SYMBOL.MULT, SYMBOL.MOD};
        return checkMultSymbol(bolster);
    }

    private Node pureCompareOp() {
        SYMBOL[] bolster = {SYMBOL.LEQ, SYMBOL.GEQ, SYMBOL.LSS, SYMBOL.GRE};
        return checkMultSymbol(bolster);
    }

    private Node pureEquOp() {
        SYMBOL[] bolster = {SYMBOL.EQL, SYMBOL.NEQ};
        return checkMultSymbol(bolster);
    }

    private Node pureAddOp() {
        SYMBOL[] bolster = {SYMBOL.PLUS, SYMBOL.MINU};
        return checkMultSymbol(bolster);
    }
//入口函数
    private void postorderTraversal(Node root) {
        if (root instanceof BranchNode) {
            ArrayList<Node> children = root.getChildren();
            for (Node node : children) {
                postorderTraversal(node);
            }
            GUNIT type = ((BranchNode) root).getGUnit();
            if (!type.equals(GUNIT.BlockItem) && !type.equals(GUNIT.BType) && !type.equals(GUNIT.Decl)) {
//                System.out.println("<" + type + ">");
                this.stringBuilder.append("<").append(type).append(">\n");
            }
        } else if (root instanceof LeafNode) {
//            System.out.println(((LeafNode) root).getType().toString() + " " + ((LeafNode) root).getToken().getValue());
            this.stringBuilder.append(((LeafNode) root).getType().toString()).append(" ").append(((LeafNode) root).getToken().getValue()).append("\n");

        }
    }

    public String parse() {
        Node root = CompUnit(0);
        postorderTraversal(root);
        return stringBuilder.toString();
    }
//    编译单元解析函数
    private Node CompUnit(int level) {
        BranchNode ret =new BranchNode(GUNIT.CompUnit);
        ArrayList<Node> children = new ArrayList<>();

        while (!peek(3).getSymbol().equals(SYMBOL.LPARENT)){
            Node temp = Decl(level+1);
            children.add(temp);
        }

        while (!peek(2).getSymbol().equals(SYMBOL.MAINTK)){
            Node temp = FuncDef(level+1);
            children.add(temp);
        }
        Node childNode = MainFuncDef(level+1);
        children.add(childNode);

        ret.setCorrect(childNode.isCorrect());
        children.forEach(ret::addChild);

        return ret;
    }

    private Node Decl(int level) {
        BranchNode ret = new BranchNode(GUNIT.Decl);

        Node childNode;
        if (peek().getSymbol().equals(SYMBOL.CONSTTK)){
            childNode=ConstDecl(level);
        }else {
            childNode=VarDecl(level);
        }

        ret.addChild(childNode);
        ret.setCorrect(childNode.isCorrect());

        return ret;
    }

    private Node ConstDecl(int level) {
        BranchNode ret =new BranchNode(GUNIT.ConstDecl);
        ArrayList<Node> children = new ArrayList<>();

        Node child1 = checkSymbol(SYMBOL.CONSTTK);
        children.add(child1);

        Node child2 = BType(level);
        children.add(child2);

        Node child3 = ConstDef(level);
        children.add(child3);

        while (peek().getSymbol().equals(SYMBOL.COMMA)) {
            Node temp1 = checkSymbol(SYMBOL.COMMA);
            Node temp2 = ConstDef(level);
            children.add(temp1);
            children.add(temp2);  // tmp1 和 tmp2 是相互依存的关系，因此必须一起存在
        }

        Node child4 = checkSymbol(SYMBOL.SEMICN);
        children.add(child4);

        ret.setCorrect(child1.isCorrect()&&child2.isCorrect()&&child3.isCorrect()&&child4.isCorrect());
        children.forEach(ret::addChild);
        return ret;
    }

    private Node BType(int level) {
        BranchNode ret =new BranchNode(GUNIT.BType);
        ArrayList<Node> children = new ArrayList<>();

        Node child1=checkSymbol(SYMBOL.INTTK);
        children.add(child1);

        ret.setCorrect(child1.isCorrect());
        children.forEach(ret::addChild);

        return ret;
    }

    private Node ConstDef(int level) {
        BranchNode ret =new BranchNode(GUNIT.ConstDef);
        ArrayList<Node> children = new ArrayList<>();

        Node child1=checkSymbol(SYMBOL.IDENFR);
        children.add(child1);

        while (peek().getSymbol().equals(SYMBOL.LBRACK)) {
            Node tmp1 = checkSymbol(SYMBOL.LBRACK);
            Node tmp2 = ConstExp(level);
            Node tmp3 = checkSymbol(SYMBOL.RBRACK);
            children.add(tmp1);
            children.add(tmp2);
            children.add(tmp3);
        }

        Node child2 = checkSymbol(SYMBOL.ASSIGN);
        children.add(child2);

        Node child3 = ConstInitVal(level);
        children.add(child3);

        ret.setCorrect(child1.isCorrect()&&child2.isCorrect()&&child3.isCorrect());
        children.forEach(ret::addChild);

        return ret;
    }

    private Node ConstInitVal(int level) {
        BranchNode ret =new BranchNode(GUNIT.ConstInitVal);
        ArrayList<Node> children = new ArrayList<>();

        Node child1;
        if (peek().getSymbol().equals(SYMBOL.LBRACE)) {
            child1 = checkSymbol(SYMBOL.LBRACE);
            children.add(child1);

            if (!peek().getSymbol().equals(SYMBOL.RBRACE)) {
                Node tmp1 = ConstInitVal(level);
                children.add(tmp1);
                while (peek().getSymbol().equals(SYMBOL.COMMA)) {
                    Node tmp2 = checkSymbol(SYMBOL.COMMA);
                    Node tmp3 = ConstInitVal(level);
                    children.add(tmp2);
                    children.add(tmp3);
                }

            }
            Node child2 = checkSymbol(SYMBOL.RBRACE);
            children.add(child2);

            ret.setCorrect(child1.isCorrect() && child2.isCorrect());
            children.forEach(ret::addChild);
        } else {
            child1 = ConstExp(level);
            ret.setCorrect(child1.isCorrect());
            ret.addChild(child1);
        }

        return ret;
    }

    private Node VarDecl(int level) {
        BranchNode ret =new BranchNode(GUNIT.VarDecl);
        ArrayList<Node> children = new ArrayList<>();

        Node child1 = BType(level);
        children.add(child1);

        Node child2 = VarDef(level);
        children.add(child2);

        while (peek().getSymbol().equals(SYMBOL.COMMA)) {
            Node tmp1 = checkSymbol(SYMBOL.COMMA);
            Node tmp2 = VarDef(level);
            children.add(tmp1);
            children.add(tmp2);
        }

        Node child3 = checkSymbol(SYMBOL.SEMICN);
        children.add(child3);

        ret.setCorrect(child1.isCorrect()&&child2.isCorrect()&&child3.isCorrect());
        children.forEach(ret::addChild);
        return ret;

    }

    private Node VarDef(int level) {

        BranchNode ret = new BranchNode(GUNIT.VarDef);
        ArrayList<Node> children = new ArrayList<>();

        Node child1 = checkSymbol(SYMBOL.IDENFR);
        children.add(child1);

        while (peek().getSymbol().equals(SYMBOL.LBRACK)) {
            Node temp1 = checkSymbol(SYMBOL.LBRACK);
            Node temp2 = ConstExp(level);
            Node temp3 = checkSymbol(SYMBOL.RBRACK);

            children.add(temp1);
            children.add(temp2);
            children.add(temp3);
        }

        if (peek().getSymbol().equals(SYMBOL.ASSIGN)) {
            Node temp1 = checkSymbol(SYMBOL.ASSIGN);
            Node temp2 = InitVal(level);
            children.add(temp1);
            children.add(temp2);
        }

        ret.setCorrect(child1.isCorrect());
        children.forEach(ret::addChild);
        return ret;


    }

    private Node InitVal(int level) {
        BranchNode ret = new BranchNode(GUNIT.InitVal);
        ArrayList<Node> children = new ArrayList<>();

        if (peek().getSymbol().equals(SYMBOL.LBRACE)) {
            Node child1 = checkSymbol(SYMBOL.LBRACE);
            children.add(child1);

            if (!peek().getSymbol().equals(SYMBOL.RBRACE)) {
                Node tmp1 = InitVal(level);
                children.add(tmp1);
                while (peek().getSymbol().equals(SYMBOL.COMMA)) {
                    Node tmp2 = checkSymbol(SYMBOL.COMMA);
                    Node tmp3 = InitVal(level);
                    children.add(tmp2);
                    children.add(tmp3);
                }
            }

            Node child2 = checkSymbol(SYMBOL.RBRACE);
            children.add(child2);

            ret.setCorrect(child1.isCorrect() && child2.isCorrect());
        } else {
            Node child1 = Exp(level);
            children.add(child1);

            ret.setCorrect(child1.isCorrect());
        }

        children.forEach(ret::addChild);
        return ret;

    }

    private Node FuncDef(int level) {
        BranchNode ret = new BranchNode(GUNIT.FuncDef);
        ArrayList<Node> children = new ArrayList<>();

        Node child1 = FuncType(level);
        children.add(child1);

        Node child2 = checkSymbol(SYMBOL.IDENFR);
        children.add(child2);

        Node child3 = checkSymbol(SYMBOL.LPARENT);
        children.add(child3);

        if (isNextFuncFParams()){
            Node child4 = FuncFParams(level);
            children.add(child4);
        }

        Node child5 = checkSymbol(SYMBOL.RPARENT);
        children.add(child5);

        Node child6 = Block(level+1);
        children.add(child6);

        ret.setCorrect(child1.isCorrect()&&child2.isCorrect()&&child3.isCorrect()&&child5.isCorrect()&&child6.isCorrect());

        children.forEach(ret::addChild);
        return  ret;

    }

    private Node MainFuncDef(int level) {
        BranchNode ret = new BranchNode(GUNIT.MainFuncDef);
        ArrayList<Node> children = new ArrayList<>();

        Node child1 =checkSymbol(SYMBOL.INTTK);
        children.add(child1);

        Node child2 =checkSymbol(SYMBOL.MAINTK);
        children.add(child2);

        Node child3 =checkSymbol(SYMBOL.LPARENT);
        children.add(child3);

        Node child4 =checkSymbol(SYMBOL.RPARENT);
        children.add(child4);

        Node child5 =Block(level+1);
        children.add(child5);

        ret.setCorrect(child1.isCorrect()&&child2.isCorrect()&&child3.isCorrect()&&child5.isCorrect());
        children.forEach(ret::addChild);
        return ret;
    }

    private Node FuncType(int level) {
        BranchNode ret = new BranchNode(GUNIT.FuncType);
        ArrayList<Node> children = new ArrayList<>();

        SYMBOL[] bolster ={SYMBOL.VOIDTK,SYMBOL.INTTK};

        Node child1 = checkMultSymbol(bolster);
        children.add(child1);

        ret.setCorrect(child1.isCorrect());
        children.forEach(ret::addChild);
        return ret;
    }

    private Node FuncFParams(int level) {

        BranchNode ret = new BranchNode(GUNIT.FuncFParams);
        ArrayList<Node> children = new ArrayList<>();

        Node child1 = FuncFParam(level);
        children.add(child1);

        while (peek().getSymbol().equals(SYMBOL.COMMA)) {
            Node tmp1 = checkSymbol(SYMBOL.COMMA);
            Node tmp2 = FuncFParam(level);
            children.add(tmp1);
            children.add(tmp2);
        }

        ret.setCorrect(child1.isCorrect());

        children.forEach(ret::addChild);
        return ret;

    }

    private Node FuncFParam(int level) {
        BranchNode ret = new BranchNode(GUNIT.FuncFParam);
        ArrayList<Node> children = new ArrayList<>();

        Node child1 = BType(level);
        children.add(child1);

        Node child2 = checkSymbol(SYMBOL.IDENFR);
        children.add(child1);

        if (peek().getSymbol().equals(SYMBOL.LBRACK)) {
            Node tmp1 = checkSymbol(SYMBOL.LBRACK);
            Node tmp2 = checkSymbol(SYMBOL.RBRACK);

            children.add(tmp1);
            children.add(tmp2);

            while (peek().getSymbol().equals(SYMBOL.LBRACK)) {
                Node tmp3 = checkSymbol(SYMBOL.LBRACK);
                Node tmp4 = ConstExp(level);
                Node tmp5 = checkSymbol(SYMBOL.RBRACK);

                children.add(tmp3);
                children.add(tmp4);
                children.add(tmp5);
            }
        }

        ret.setCorrect(child1.isCorrect() && child2.isCorrect());

        children.forEach(ret::addChild);
        return ret;

    }

    private Node Block(int level) {
        BranchNode ret = new BranchNode(GUNIT.Block);
        ArrayList<Node> children = new ArrayList<>();

        Node child1 =checkSymbol(SYMBOL.LBRACE);
        children.add(child1);

        while (!peek().getSymbol().equals(SYMBOL.RBRACE)) {
            Node tmp1 = BlockItem(level + 1);
            children.add(tmp1);
        }

        Node child2 =checkSymbol(SYMBOL.RBRACE);
        children.add(child2);

        ret.setCorrect(child1.isCorrect()&&child2.isCorrect());
        children.forEach(ret::addChild);
        return ret;

    }

    private Node BlockItem(int level) {
        BranchNode ret = new BranchNode(GUNIT.BlockItem);
        ArrayList<Node> children = new ArrayList<>();

        Node child1;
        if (peek().getSymbol().equals(SYMBOL.CONSTTK) || peek().getSymbol().equals(SYMBOL.INTTK)){
            child1=Decl(level);
        }else {
            child1=Stmt(level);
        }
        children.add(child1);

        ret.setCorrect(child1.isCorrect());
        children.forEach(ret::addChild);

        return ret;
    }

    private Node Stmt(int level) {
        BranchNode ret = new BranchNode(GUNIT.Stmt);
        ArrayList<Node> children = new ArrayList<>();

        if (peek().getSymbol().equals(SYMBOL.IFTK)) {

            Node child1 = checkSymbol(SYMBOL.IFTK);
            children.add(child1);

            Node child2 = checkSymbol(SYMBOL.LPARENT);
            children.add(child2);

            Node child3 = Cond(level);
            children.add(child3);

            Node child4 = checkSymbol(SYMBOL.RPARENT);
            children.add(child4);

            Node child5 = Stmt(level);
            children.add(child5);

            if (peek().getSymbol().equals(SYMBOL.ELSETK)) {
                Node tmp1 = checkSymbol(SYMBOL.ELSETK);
                Node tmp2 = Stmt(level);
                children.add(tmp1);
                children.add(tmp2);
            }

            ret.setCorrect(child1.isCorrect() && child2.isCorrect() && child3.isCorrect() && child4.isCorrect() && child5.isCorrect());

        } else if (peek().getSymbol().equals(SYMBOL.WHILETK)) {
            Node child1 = checkSymbol(SYMBOL.WHILETK);
            children.add(child1);

            Node child2 = checkSymbol(SYMBOL.LPARENT);
            children.add(child2);

            Node child3 = Cond(level);
            children.add(child3);

            Node child4 = checkSymbol(SYMBOL.RPARENT);
            children.add(child4);

            Node child5 = Stmt(level);
            children.add(child5);

            ret.setCorrect(child1.isCorrect() && child2.isCorrect() && child3.isCorrect() && child4.isCorrect() && child5.isCorrect());

        } else if (peek().getSymbol().equals(SYMBOL.BREAKTK)) {

            Node child1 = checkSymbol(SYMBOL.BREAKTK);
            children.add(child1);

            Node child2 = checkSymbol(SYMBOL.SEMICN);
            children.add(child2);

            ret.setCorrect(child1.isCorrect() && child2.isCorrect());
        } else if (peek().getSymbol().equals(SYMBOL.CONTINUETK)) {

            Node child1 = checkSymbol(SYMBOL.CONTINUETK);
            children.add(child1);

            Node child2 = checkSymbol(SYMBOL.SEMICN);
            children.add(child2);

            ret.setCorrect(child1.isCorrect() && child2.isCorrect());
        } else if (peek().getSymbol().equals(SYMBOL.RETURNTK)) {
            Node child1 = checkSymbol(SYMBOL.RETURNTK);
            children.add(child1);

            if (!peek().getSymbol().equals(SYMBOL.SEMICN)) {
                Node tmp1 = Exp(level);
                children.add(tmp1);
            }

            Node child2 = checkSymbol(SYMBOL.SEMICN);
            children.add(child2);

            ret.setCorrect(child1.isCorrect() && child2.isCorrect());

        } else if (peek().getSymbol().equals(SYMBOL.PRINTFTK)) {
            Node child1 = checkSymbol(SYMBOL.PRINTFTK);
            children.add(child1);
            Node child2 = checkSymbol(SYMBOL.LPARENT);
            children.add(child2);
            Node child3 = checkSymbol(SYMBOL.STRCON);
            children.add(child3);

            while (peek().getSymbol().equals(SYMBOL.COMMA)) {
                Node tmp1 = checkSymbol(SYMBOL.COMMA);
                Node tmp2 = Exp(level);
                children.add(tmp1);
                children.add(tmp2);
            }

            Node child4 = checkSymbol(SYMBOL.RPARENT);
            children.add(child4);

            Node child5 = checkSymbol(SYMBOL.SEMICN);
            children.add(child5);

            ret.setCorrect(child1.isCorrect() && child2.isCorrect() && child3.isCorrect() && child4.isCorrect() && child5.isCorrect());
        } else if (peek().getSymbol().equals(SYMBOL.LBRACE)) {
            Node child1 = Block(level + 1);
            children.add(child1);

            ret.setCorrect(child1.isCorrect());
        } else if (isNextLValAndAssign()) {
            Node child1 = LVal(level);
            children.add(child1);
            Node child2 = checkSymbol(SYMBOL.ASSIGN);
            children.add(child2);

            if (peek().getSymbol().equals(SYMBOL.GETINTTK)) {
                Node child3 = checkSymbol(SYMBOL.GETINTTK);
                children.add(child3);
                Node child4 = checkSymbol(SYMBOL.LPARENT);
                children.add(child4);

                Node child5 = checkSymbol(SYMBOL.RPARENT);
                children.add(child5);

                Node child6 = checkSymbol(SYMBOL.SEMICN);
                children.add(child6);

                ret.setCorrect(child1.isCorrect() && child2.isCorrect() && child3.isCorrect()
                        && child4.isCorrect() && child5.isCorrect() && child6.isCorrect());
            } else {
                Node child3 = Exp(level);
                children.add(child3);
                Node child4 = checkSymbol(SYMBOL.SEMICN);
                children.add(child4);

                ret.setCorrect(child1.isCorrect() && child2.isCorrect() && child3.isCorrect() && child4.isCorrect());
            }


        } else {
            if (isNextExpAndSemicon()) {
                Node tmp1 = Exp(level);
                children.add(tmp1);

                Node child1 = checkSymbol(SYMBOL.SEMICN);
                children.add(child1);

                ret.setCorrect(child1.isCorrect());
            } else {
                Node child1 = checkSymbol(SYMBOL.SEMICN);
                children.add(child1);

                ret.setCorrect(child1.isCorrect());
            }
        }

        children.forEach(ret::addChild);
        return ret;

    }

    private Node Exp(int level) {
        BranchNode ret = new BranchNode(GUNIT.Exp);
        ArrayList<Node> children = new ArrayList<>();

        Node child1 = AddExp(level);
        children.add(child1);

        ret.setCorrect(child1.isCorrect());
        children.forEach(ret::addChild);

        return ret;
    }

    private Node Cond(int level) {

        BranchNode ret = new BranchNode(GUNIT.Cond);
        ArrayList<Node> children = new ArrayList<>();

        Node child1 = LOrExp(level);
        children.add(child1);

        ret.setCorrect(child1.isCorrect());
        children.forEach(ret::addChild);

        return ret;
    }

    private Node LVal(int level) {
        BranchNode ret = new BranchNode(GUNIT.LVal);
        ArrayList<Node> children = new ArrayList<>();

        Node child1 = checkSymbol(SYMBOL.IDENFR);
        children.add(child1);

        while (peek().getSymbol().equals(SYMBOL.LBRACK)) {
            Node tmp1 = checkSymbol(SYMBOL.LBRACK);
            Node tmp2 = Exp(level);
            Node tmp3 = checkSymbol(SYMBOL.RBRACK);

            children.add(tmp1);
            children.add(tmp2);
            children.add(tmp3);
        }

        ret.setCorrect(child1.isCorrect());

        children.forEach(ret::addChild);

        return ret;
    }

    private Node PrimaryExp(int level) {
        BranchNode ret = new BranchNode(GUNIT.PrimaryExp);
        ArrayList<Node> children = new ArrayList<>();

        if (peek().getSymbol().equals(SYMBOL.INTCON)) {
            Node child1 = Number(level);
            children.add(child1);

            ret.setCorrect(child1.isCorrect());
        } else if (peek().getSymbol().equals(SYMBOL.LPARENT)) {
            Node child1 = checkSymbol(SYMBOL.LPARENT);
            children.add(child1);

            Node child2 = Exp(level);
            children.add(child2);

            Node child3 = checkSymbol(SYMBOL.RPARENT);
            children.add(child3);

            ret.setCorrect(child1.isCorrect() && child2.isCorrect() && child3.isCorrect());
        } else {
            Node child1 = LVal(level);
            children.add(child1);

            ret.setCorrect(child1.isCorrect());
        }

        children.forEach(ret::addChild);
        return ret;

    }

    private Node Number(int level) {
        BranchNode ret = new BranchNode(GUNIT.Number);
        ArrayList<Node> children = new ArrayList<>();

        Node child1 = checkSymbol(SYMBOL.INTCON);
        children.add(child1);

        ret.setCorrect(child1.isCorrect());
        children.forEach(ret::addChild);

        return ret;
    }

    private Node UnaryExp(int level) {
        BranchNode ret = new BranchNode(GUNIT.UnaryExp);
        ArrayList<Node> children = new ArrayList<>();

        if (peek().getSymbol().equals(SYMBOL.PLUS) ||
                peek().getSymbol().equals(SYMBOL.MINU) ||
                peek().getSymbol().equals(SYMBOL.NOT))
        {
            Node child1 = UnaryOp(level);
            children.add(child1);

            Node child2 = UnaryExp(level);
            children.add(child2);

            ret.setCorrect(child1.isCorrect() && child2.isCorrect());
        } else if (peek().getSymbol().equals(SYMBOL.IDENFR) && peek(2).getSymbol().equals(SYMBOL.LPARENT)) {
            Node child1 = checkSymbol(SYMBOL.IDENFR);
            children.add(child1);
            Node child2 = checkSymbol(SYMBOL.LPARENT);
            children.add(child2);
            if (isNextFuncRParams()) {
                Node tmp1 = FuncRParams(level);
                children.add(tmp1);
            }

            Node child3 = checkSymbol(SYMBOL.RPARENT);
            children.add(child3);

            ret.setCorrect(child1.isCorrect() && child2.isCorrect() && child3.isCorrect());
        }
        else
        {
            Node child1 = PrimaryExp(level);
            children.add(child1);

            ret.setCorrect(child1.isCorrect());
        }

        children.forEach(ret::addChild);
        return ret;

    }

    private Node UnaryOp(int level) {
        BranchNode ret = new BranchNode(GUNIT.UnaryOp);
        ArrayList<Node> children = new ArrayList<>();

        SYMBOL[] bolster = {SYMBOL.PLUS, SYMBOL.MINU, SYMBOL.NOT};

        Node child1 = checkMultSymbol(bolster);
        children.add(child1);

        ret.setCorrect(child1.isCorrect());
        children.forEach(ret::addChild);

        return ret;
    }

    private Node FuncRParams(int level) {
        BranchNode ret = new BranchNode(GUNIT.FuncRParams);
        ArrayList<Node> children = new ArrayList<>();

        Node child1 = Exp(level);
        children.add(child1);

        while (peek().getSymbol().equals(SYMBOL.COMMA)) {
            Node tmp1 = checkSymbol(SYMBOL.COMMA);
            Node tmp2 = Exp(level);
            children.add(tmp1);
            children.add(tmp2);
        }

        ret.setCorrect(child1.isCorrect());

        children.forEach(ret::addChild);
        return ret;

    }


    private Node MulExp(int level) {
        BranchNode ret = new BranchNode(GUNIT.MulExp);

        Node child1 = UnaryExp(level);
        ret.addChild(child1);
        ret.setCorrect(child1.isCorrect());

        while (peek().getSymbol().equals(SYMBOL.MULT) ||
                peek().getSymbol().equals(SYMBOL.DIV) ||
                peek().getSymbol().equals(SYMBOL.MOD)) {

            BranchNode tmpBranch = new BranchNode(GUNIT.MulExp);
            tmpBranch.addChild(ret);
            tmpBranch.setCorrect(ret.isCorrect());
            ret = tmpBranch;

            Node tmp1 = pureMulOp();
            Node tmp2 = UnaryExp(level);

            ret.addChild(tmp1);
            ret.addChild(tmp2);
        }

        return ret;
    }

    private Node AddExp(int level) {
        BranchNode ret = new BranchNode(GUNIT.AddExp);
        ArrayList<Node> children = new ArrayList<>();

        Node child1 = MulExp(level);
        children.add(child1);
        ret.setCorrect(child1.isCorrect());
        children.forEach(ret::addChild);

        while (peek().getSymbol().equals(SYMBOL.PLUS) ||
                peek().getSymbol().equals(SYMBOL.MINU)) {

            BranchNode tmpBranch = new BranchNode(GUNIT.AddExp);
            tmpBranch.addChild(ret);
            tmpBranch.setCorrect(ret.isCorrect());
            ret = tmpBranch;

            Node tmp1 = pureAddOp();
            Node tmp2 = MulExp(level);

            ret.addChild(tmp1);
            ret.addChild(tmp2);
        }

        return ret;
    }


    private Node RelExp(int level) {
        BranchNode ret = new BranchNode(GUNIT.RelExp);

        Node child1 = AddExp(level);
        ret.addChild(child1);
        ret.setCorrect(child1.isCorrect());

        while (peek().getSymbol().equals(SYMBOL.LEQ) || peek().getSymbol().equals(SYMBOL.LSS) || peek().getSymbol().equals(SYMBOL.GEQ) ||
                peek().getSymbol().equals(SYMBOL.GRE)) {
            BranchNode tmpBranch = new BranchNode(GUNIT.RelExp);
            tmpBranch.addChild(ret);
            tmpBranch.setCorrect(ret.isCorrect());
            ret = tmpBranch;

            Node tmp1 = pureCompareOp();
            Node tmp2 = AddExp(level);

            ret.addChild(tmp1);
            ret.addChild(tmp2);
        }

        return ret;

    }


    private Node EqExp(int level) {
        BranchNode ret = new BranchNode(GUNIT.EqExp);

        Node child1 = RelExp(level);
        ret.addChild(child1);
        ret.setCorrect(child1.isCorrect());

        while (peek().getSymbol().equals(SYMBOL.EQL) ||
                peek().getSymbol().equals(SYMBOL.NEQ)) {
            BranchNode tmpBranch = new BranchNode(GUNIT.EqExp);
            tmpBranch.addChild(ret);
            tmpBranch.setCorrect(ret.isCorrect());
            ret = tmpBranch;

            Node tmp1 = pureEquOp();
            Node tmp2 = RelExp(level);

            ret.addChild(tmp1);
            ret.addChild(tmp2);
        }

        return ret;

    }


    private Node LAndExp(int level) {

        BranchNode ret = new BranchNode(GUNIT.LAndExp);

        Node child1 = EqExp(level);
        ret.addChild(child1);
        ret.setCorrect(child1.isCorrect());

        while (peek().getSymbol().equals(SYMBOL.AND)) {
            BranchNode tmpBranch = new BranchNode(GUNIT.LAndExp);
            tmpBranch.addChild(ret);
            tmpBranch.setCorrect(ret.isCorrect());
            ret = tmpBranch;

            Node tmp1 = checkSymbol(SYMBOL.AND);
            Node tmp2 = EqExp(level);

            ret.addChild(tmp1);
            ret.addChild(tmp2);
        }

        return ret;

    }


    private Node LOrExp(int level) {
        BranchNode ret = new BranchNode(GUNIT.LOrExp);

        Node child1 = LAndExp(level);
        ret.addChild(child1);
        ret.setCorrect(child1.isCorrect());

        while (peek().getSymbol().equals(SYMBOL.OR)) {
            BranchNode tmpBranch = new BranchNode(GUNIT.LOrExp);
            tmpBranch.addChild(ret);
            tmpBranch.setCorrect(ret.isCorrect());
            ret = tmpBranch;

            Node tmp1 = checkSymbol(SYMBOL.OR);
            Node tmp2 = LAndExp(level);

            ret.addChild(tmp1);
            ret.addChild(tmp2);
        }

        return ret;

    }

    private Node ConstExp(int level) {
        BranchNode ret = new BranchNode(GUNIT.ConstExp);
        ArrayList<Node> children = new ArrayList<>();

        Node child1=AddExp(level);
        children.add(child1);

        ret.setCorrect(child1.isCorrect());
        children.forEach(ret::addChild);

        return ret;
    }
//    四个用于检测是否存在的函数
    private Boolean isNextExpAndSemicon() {
        int pTokenMark = pToken;
        Node trial = Exp(-1);
        if (pToken == pTokenMark) {
            // 如果根本没检查到Exp，就返回错误
            return false;
        }
        Node semicn = checkSymbol(SYMBOL.SEMICN);
        pToken = pTokenMark;
        //  如果查到了Exp，但是Exp由于缺失中括号呈现false，此时分号一定正确，因此返回正确
        //  如果查到Exp但是缺失分号，此时Exp一定正确，因此返回正确
        return trial.isCorrect() || semicn.isCorrect();

    }

    private Boolean isNextLValAndAssign() {
        int pTokenMark = pToken;
        Node lval = LVal(-1);
        Node assign = checkSymbol(SYMBOL.ASSIGN);
        pToken = pTokenMark;
        //  如果跨过LVal检测到=号，返回正确
        //  LVal的正确性或存在性（目前不可能不存在）不影响正确性
        return assign.isCorrect();

    }

    private Boolean isNextFuncRParams() {
        int pTokenMark = pToken;
        Node funcRParams = FuncRParams(-1);
        pToken = pTokenMark;
        return funcRParams.isCorrect();
    }

    private Boolean isNextFuncFParams() {
        int pTokenMark = pToken;
        Node funcFParams = FuncFParams(-1);
        if (pToken == pTokenMark) {
            //  没有检测到，则返回错误
            return false;
        }
        pToken = pTokenMark;

        return true;

    }





}
