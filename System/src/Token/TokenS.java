package Token;

import Node.SYMBOL;

import java.util.HashMap;
import java.util.Map;

public  class TokenS {
    public Map<String, String> dick = new HashMap<>();

    public TokenS() {
        this.dick.put("ident", "IDENFR");
        this.dick.put("IntConst", "INTCON");
        this.dick.put("FormatString", "STRCON");
        this.dick.put("main", "MAINTK");
        this.dick.put("const", "CONSTTK");
        this.dick.put("int", "INTTK");
        this.dick.put("break", "BREAKTK");
        this.dick.put("continue", "CONTINUETK");
        this.dick.put("if", "IFTK");
        this.dick.put("else", "ELSETK");
        this.dick.put("!", "NOT");
        this.dick.put("&&", "AND");
        this.dick.put("||", "OR");
        this.dick.put("while", "WHILETK");
        this.dick.put("getint", "GETINTTK");
        this.dick.put("printf", "PRINTFTK");
        this.dick.put("return", "RETURNTK");
        this.dick.put("+", "PLUS");
        this.dick.put("-", "MINU");
        this.dick.put("void", "VOIDTK");
        this.dick.put("*", "MULT");
        this.dick.put("/", "DIV");
        this.dick.put("%", "MOD");
        this.dick.put("<", "LSS");
        this.dick.put("<=", "LEQ");
        this.dick.put(">", "GRE");
        this.dick.put(">=", "GEQ");
        this.dick.put("==", "EQL");
        this.dick.put("!=", "NEQ");
        this.dick.put("=", "ASSIGN");
        this.dick.put(";", "SEMICN");
        this.dick.put(",", "COMMA");
        this.dick.put("(", "LPARENT");
        this.dick.put(")", "RPARENT");
        this.dick.put("[", "LBRACK");
        this.dick.put("]", "RBRACK");
        this.dick.put("{", "LBRACE");
        this.dick.put("}", "RBRACE");

    }

    public String getToken(String keyName) {
        String ID = this.dick.get(keyName);
        return ID;
    }

}