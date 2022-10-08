import Node.SYMBOL;
import Token.Token;
import Token.TokenS;

import java.io.IOException;
import java.util.ArrayList;

public class Lexparser {
    public ArrayList<Token> TokenList=new ArrayList<>();
    public static boolean isAlpha(char ch) {
        if (Character.isLetter(ch) || ch == '_')
            return true;
        return false;
    }

    public static boolean isDigit(char ch) {
        if (Character.isDigit(ch))
            return true;
        return false;
    }

    public void LexParser(String args, TokenS tokens, Integer lineNumber) throws IOException {
        if (args.equals(" ") || args.equals("")) {
            return;
        }
        char[] chars = args.toCharArray();
        for (int i = 0; i < chars.length; i++) {

            StringBuilder stringBuilder = new StringBuilder();
            //跳过空格
//           针对多行注释
            if (Compiler.vomit) {
                while (Compiler.vomit) {
                    if (i != chars.length - 1 && chars[i] == '*' && chars[i + 1] == '/') {
                        i++;
                        Compiler.vomit = false;
//                       System.out.print("*/\n");
                        continue;
                    }
                    if (i == chars.length - 1) break;
//                   System.out.print(chars[i]);
                    i++;
                }
            } else {
                if (chars[i] == ' ') {
                    continue;
                }
//           分析注释，同时将除号进行处理
                else if (chars[i] == '/') {
//                   处理多行注释
                    if (i != chars.length - 1 && chars[i + 1] == '*') {
                        Compiler.vomit = true;
//                       System.out.print("IDENFR ");
                        while (i != chars.length - 1 && (chars[i] != '*' || chars[i + 1] != '/')) {
//                           System.out.print(chars[i]);
                            i++;
                            if (i != chars.length - 1 && chars[i] == '*' && chars[i + 1] == '/') {
                                Compiler.vomit = false;
//                               System.out.print("*/\n");
                                i++;
                                break;
                            }
                        }
                    }
//                   处理单行注释
                    else if (i != chars.length - 1 && chars[i + 1] == '/') {
                        while (i < chars.length) {
                            stringBuilder.append(chars[i]);
                            i++;
                        }
//                       System.out.println("IDENFR "+stringBuilder.toString());
                    }
//                   处理单个除号
                    else {
//                       System.out.println("DIV /");
                        Token curtoken = new Token(SYMBOL.DIV,"/",lineNumber);
                        this.TokenList.add(curtoken);
                        String ideout = "DIV /\n";

                    }

                }
//           分析单行注释


//           利用token表格直接进行符号查询
                else if (tokens.getToken(String.valueOf(chars[i])) != null) {
                    stringBuilder.append(chars[i]);
                    StringBuilder stringBuilder1 = new StringBuilder();
                    stringBuilder1.append(chars[i]);
//               查询连携双符号同时出现的情况
                    if (i != chars.length - 1 && tokens.getToken(stringBuilder1.append(chars[i + 1]).toString()) != null) {
                        i++;
                        stringBuilder.append(chars[i]);
//                       System.out.println(token.getToken(stringBuilder.toString()) + " " + stringBuilder.toString());
                        String ideout = tokens.getToken(stringBuilder.toString()) + " " + stringBuilder.toString() + "\n";

                        Token curtoken = new Token(SYMBOL.valueOf(tokens.getToken(stringBuilder.toString())),stringBuilder.toString(),lineNumber);
                        this.TokenList.add(curtoken);
                    } else {
//                       System.out.println(token.getToken(stringBuilder.toString()) + " " + stringBuilder.toString());
                        String ideout = tokens.getToken(stringBuilder.toString()) + " " + stringBuilder.toString() + "\n";

                        Token curtoken = new Token(SYMBOL.valueOf(tokens.getToken(stringBuilder.toString())),stringBuilder.toString(),lineNumber);
                        this.TokenList.add(curtoken);

                    }
                }

//          补充&&和||
                else if (chars[i] == '|') {
                    if (i != chars.length - 1 && chars[i + 1] == '|') {
                        i++;
//                       System.out.println(token.getToken("||") + " ||" );
                        String ideout = tokens.getToken("||") + " ||" + "\n";

                        Token curtoken = new Token(SYMBOL.valueOf(tokens.getToken("||")),"||",lineNumber);
                        this.TokenList.add(curtoken);
                        //fw.close();
                    }
                } else if (chars[i] == '&') {
                    if (i != chars.length - 1 && chars[i + 1] == '&') {
                        i++;
//                       System.out.println(token.getToken("&&") + " &&" );
                        String ideout = tokens.getToken("&&") + " &&" + "\n";

                        Token curtoken = new Token(SYMBOL.valueOf(tokens.getToken("&&")),"&&",lineNumber);
                        this.TokenList.add(curtoken);
                    }
                }
                //识别标识符或者变量名称
                else if (isAlpha(chars[i])) {
                    stringBuilder.append(chars[i]);
//               创建识别用的单词串
                    while (i != chars.length - 1 && (isAlpha(chars[i + 1]) || isDigit(chars[i + 1]) || chars[i + 1] == '_')) {
                        i++;
                        stringBuilder.append(chars[i]);
                    }

                    if (tokens.getToken(stringBuilder.toString()) == null) {
//                       System.out.println("IDENFR "+stringBuilder.toString());
                        String ideout = "IDENFR " + stringBuilder.toString() + "\n";

                        Token curtoken = new Token(SYMBOL.IDENFR,stringBuilder.toString(),lineNumber);
                        this.TokenList.add(curtoken);
                    } else {
//                       System.out.println(token.getToken(stringBuilder.toString()) + " " + stringBuilder.toString());
                        String ideout = tokens.getToken(stringBuilder.toString()) + " " + stringBuilder.toString() + "\n";

                        Token curtoken = new Token(SYMBOL.valueOf(tokens.getToken(stringBuilder.toString())),stringBuilder.toString(),lineNumber);
                        this.TokenList.add(curtoken);
                    }
                }
//            识别数字符号
                else if (isDigit(chars[i])) {
                    stringBuilder.append(chars[i]);
//               创建识别用的单词串
                    while (i != chars.length - 1 && (isDigit(chars[i + 1]))) {
                        i++;
                        stringBuilder.append(chars[i]);
                    }

//                   System.out.println("INTCON" + " " + stringBuilder.toString());
                    String ideout = "INTCON" + " " + stringBuilder.toString() + "\n";

                    Token curtoken = new Token(SYMBOL.INTCON,stringBuilder.toString(),lineNumber);
                    this.TokenList.add(curtoken);
                }
//           识别双引号以及内部内容
                else if (chars[i] == '"') {
                    stringBuilder.append(chars[i]);
                    while (i != chars.length - 1 && (chars[i + 1] != '"') || (chars[i] == '\\' && chars[i + 1] == '"')) {
                        i++;
                        stringBuilder.append(chars[i]);
                    }
                    i++;
                    stringBuilder.append(chars[i]);
//                   System.out.println("STRCON" + " " + stringBuilder.toString());
                    String ideout = "STRCON" + " " + stringBuilder.toString() + "\n";
                    Token curtoken = new Token(SYMBOL.STRCON,stringBuilder.toString(),lineNumber);
                    this.TokenList.add(curtoken);
                }

            }

        }
    }
}