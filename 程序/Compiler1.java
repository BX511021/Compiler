import java.io.*;

public class Compiler1 {
    public static void main(String[] args) throws IOException {
        File in = new File("程序/testfile.txt");
        File out = new File("程序/output.txt");

        FileInputStream input = new FileInputStream(in);
        FileOutputStream output = new FileOutputStream(out);

        InputStreamReader isr = new InputStreamReader(input,"UTF-8");
        OutputStreamWriter osr = new OutputStreamWriter(output,"UTF-8");

        BufferedReader br = new BufferedReader(isr);
        BufferedWriter bw = new BufferedWriter(osr);

        FileWriter fw =new FileWriter(out);
        String inline = null;
        fw.write("");
        fw.flush();

        while((inline=br.readLine())!=null){
            if(inline.length()!=0){
                int len=inline.length();
                idenfy(inline,fw);
            }

        }
        return;

    }

    public static void idenfy(String args,FileWriter fw) throws IOException {
        //空行
        if (args.equals("")|args.equals(" ")) {
            return;
        }
        char singleChar[] = args.toCharArray();
        for(int i=0;i<singleChar.length;i++){
            char ch=singleChar[i];
            StringBuilder outstr=new StringBuilder();
            //处理注释

            //首字符是字母
            if(isAlpha(ch)){
                do{
                    outstr.append(ch);
                    i++;
                    if(i>=singleChar.length)break;
                    ch=singleChar[i];
                }while((isAlpha(ch)||isDigit(ch)||ch=='_')&&ch!='\0');//一直读连续字母数字下划线
                //判断关键字
                if(matchKeyword(outstr.toString(),fw)){

                }
                //不是关键字，输出标识符
                else {
                    String idenout="IDENFR "+outstr.toString()+"\n";
                    fw.write(idenout);
                    fw.flush();
                    //fw.close();
                }
                --i;
            }
            //首字符是数字
            else if(isDigit(ch)){
                do{
                    outstr.append(ch);
                    i++;
                    if(i>=singleChar.length)break;
                    ch=singleChar[i];
                }while(isDigit(ch));
                String intout = "INTCON "+outstr.toString()+"\n";
                fw.write(intout);
                fw.flush();
                //fw.close();
                --i;
            }
            //strcon
            else if(ch=='"'){
                do{
                    outstr.append(ch);
                    i++;
                    if(i>=singleChar.length)break;
                    ch=singleChar[i];
                }while(ch!='"'||(singleChar[i-1]=='\\'&&ch=='"'));
                //后边引号的ch没加入outstr中，在此加入
                outstr.append(ch);
                String strconout = "STRCON "+outstr.toString()+"\n";
                fw.write(strconout);
                fw.flush();
                //fw.close();
            }
            //识别||,&&,==,>=,>,<=,<,=
            else if(ch=='|'){
                if(i+1< singleChar.length&&singleChar[i+1]=='|'){
                    String orout="OR "+"||\n";
                    i++;
                    fw.write(orout);
                    fw.flush();
                    //fw.close();
                }
            }
            else if(ch=='&'){
                if(i+1< singleChar.length&&singleChar[i+1]=='&'){
                    String andout="AND "+"&&\n";
                    i++;
                    fw.write(andout);
                    fw.flush();
                    //fw.close();
                }
            }
            else if(ch=='>'){
                if(i+1< singleChar.length&&singleChar[i+1]=='='){
                    String gqout="GEQ "+">=\n";
                    i++;
                    fw.write(gqout);
                    fw.flush();
                    //fw.close();
                }
                else {
                    String gtout="GRE "+">\n";
                    fw.write(gtout);
                    fw.flush();
                    //fw.close();
                }
            }
            else if(ch=='<'){
                if(i+1< singleChar.length&&singleChar[i+1]=='='){
                    String lqout="LEQ "+"<=\n";
                    i++;
                    fw.write(lqout);
                    fw.flush();
                    //fw.close();
                }
                else {
                    String lsout="LSS "+"<\n";
                    fw.write(lsout);
                    fw.flush();
                    //fw.close();
                }
            }
            else if(ch=='='){
                if(i+1< singleChar.length&&singleChar[i+1]=='='){
                    String eqout="EQL "+"==\n";
                    i++;
                    fw.write(eqout);
                    fw.flush();
                    //fw.close();
                }
                else {
                    String asout="ASSIGN "+"=\n";
                    fw.write(asout);
                    fw.flush();
                    //fw.close();
                }
            }
            else if(ch=='!'){
                if(i+1< singleChar.length&&singleChar[i+1]=='=')
                {

                    String neout="NEQ "+"!=\n";
                    i++;
                    fw.write(neout);
                    fw.flush();
                    //fw.close();
                    
                }
                else {
                    String noout="NOT "+"!\n";
                    fw.write(noout);
                    fw.flush();
                    //fw.close();
                }
            }
            //空格，继续
            else if(ch==' '){
                continue;
            }
            //普通字符识别
            else if(ordinarymatch(ch,fw));

        }



    }

    //关键字识别
    public static boolean matchKeyword(String args,FileWriter fw) throws IOException {
        if(args.equals("int")){
            String outstr="INTTK int\n";
            fw.write(outstr);
            fw.flush();

            return true;
        }
        else if(args.equals("main")){
            String outstr="MAINTK main\n";
            fw.write(outstr);
            fw.flush();

            return true;
        }
        else if(args.equals("const")){
            String outstr="CONSTTK const\n";
            fw.write(outstr);
            fw.flush();
            //fw.close();
            return true;
        }
        else if(args.equals("break")){
            String outstr="BREAKTK break\n";
            fw.write(outstr);
            fw.flush();
            //fw.close();
            return true;
        }
        else if(args.equals("continue")){
            String outstr="CONTINUETK continue\n";
            fw.write(outstr);
            fw.flush();
            //fw.close();
            return true;
        }
        else if(args.equals("if")){
            String outstr="IFTK if\n";
            fw.write(outstr);
            fw.flush();
            //fw.close();
            return true;
        }
        else if(args.equals("else")){
            String outstr="ELSETK else\n";
            fw.write(outstr);
            fw.flush();
            //fw.close();
            return true;
        }
        else if(args.equals("while")){
            String outstr="WHILETK while\n";
            fw.write(outstr);
            fw.flush();
            //fw.close();
            return true;
        }
        else if(args.equals("getint")){
            String outstr="GETINTTK getint\n";
            fw.write(outstr);
            fw.flush();
            //fw.close();
            return true;
        }
        else if(args.equals("printf")){
            String outstr="PRINTFTK printf\n";
            fw.write(outstr);
            fw.flush();
            //fw.close();
            return true;
        }
        else if(args.equals("return")){
            String outstr="RETURNTK return\n";
            fw.write(outstr);
            fw.flush();
            //fw.close();
            return true;
        }
        else if(args.equals("void")){
            String outstr="VOIDTK void\n";
            fw.write(outstr);
            fw.flush();
            //fw.close();
            return true;
        }
        return false;
    }

    public static boolean ordinarymatch(char ch,FileWriter fw) throws IOException {
        String out = new String();
        if(ch=='+') {
            out="PLUS +\n";
            fw.write(out);
            fw.flush();
            //fw.close();
            return true;
        } else if (ch=='-') {
            out="MINU -\n";
            fw.write(out);
            fw.flush();
            //fw.close();
            return true;
        } else if (ch=='*') {
            out="MULT *\n";
            fw.write(out);
            fw.flush();
            //fw.close();
            return true;
        } else if (ch=='/') {
            out="DIV /\n";
            fw.write(out);
            fw.flush();
            //fw.close();
            return true;
        } else if (ch=='%') {
            out="MOD %\n";
            fw.write(out);
            fw.flush();
            //fw.close();
            return true;
        } else if (ch==';') {
            out="SEMICN ;\n";
            fw.write(out);
            fw.flush();
            //fw.close();
            return true;
        } else if (ch==',') {
            out="COMMA ,\n";
            fw.write(out);
            fw.flush();
            //fw.close();
            return true;
        } else if (ch=='[') {
            out="LBRACK [\n";
            fw.write(out);
            fw.flush();
            //fw.close();
            return true;
        } else if (ch==']') {
            out="RBRACK ]\n";
            fw.write(out);
            fw.flush();
            //fw.close();
            return true;
        } else if (ch=='(') {
            out="LPARENT (\n";
            fw.write(out);
            fw.flush();
            //fw.close();
            return true;
        } else if (ch==')') {
            out="RPARENT )\n";
            fw.write(out);
            fw.flush();
            //fw.close();
            return true;
        } else if (ch=='{') {
            out="LBRACE {\n";
            fw.write(out);
            fw.flush();
            //fw.close();
            return true;
        } else if (ch=='}') {
            out="RBRACE }\n";
            fw.write(out);
            fw.flush();
            //fw.close();
            return true;
        }
        return false;
    }

    public static boolean isAlpha(char ch){
        if(Character.isLetter(ch))
            return true;
        return false;
    }
    public static boolean isDigit(char ch){
        if(Character.isDigit(ch))
            return true;
        return false;
    }
}
