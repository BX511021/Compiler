import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Compiler {

    public static class Token{
        public Map<String, String> dick =new HashMap<>();

        public Token() {
            this.dick.put("ident","IDENFR");
            this.dick.put("IntConst","INTCON");
            this.dick.put("FormatString","STRCON");
            this.dick.put("main","MAINTK");
            this.dick.put("const","CONSTTK");
            this.dick.put("int","INTTK");
            this.dick.put("break","BREAKTK");
            this.dick.put("continue","CONTINUETK");
            this.dick.put("if","IFTK");
            this.dick.put("else","ELSETK");
            this.dick.put("!","NOT");
            this.dick.put("&&","AND");
            this.dick.put("||","OR");
            this.dick.put("while","WHILETK");
            this.dick.put("getint","GETINTTK");
            this.dick.put("printf","PRINTFTK");
            this.dick.put("return","RETURNTK");
            this.dick.put("+","PLUS");
            this.dick.put("-","MINU");
            this.dick.put("void","VOIDTK");
            this.dick.put("*","MULT");
            this.dick.put("/","DIV");
            this.dick.put("%","MOD");
            this.dick.put("<","LSS");
            this.dick.put("<=","LEQ");
            this.dick.put(">","GRE");
            this.dick.put(">=","GEQ");
            this.dick.put("==","EQL");
            this.dick.put("!=","NEQ");
            this.dick.put("=","ASSIGN");
            this.dick.put(";","SEMICN");
            this.dick.put(",","COMMA");
            this.dick.put("(","LPARENT");
            this.dick.put(")","RPARENT");
            this.dick.put("[","LBRACK");
            this.dick.put("]","RBRACK");
            this.dick.put("{","LBRACE");
            this.dick.put("}","RBRACE");

        }
        public String getToken(String keyName){
            String ID=this.dick.get(keyName);
            return ID;
        }

    }
    public static boolean vomit = false;

    public  static void  main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        Token token=new Token();

        File in = new File("./testfile.txt");
        File out = new File("./output.txt");

        FileInputStream input = new FileInputStream(in);
        FileOutputStream output = new FileOutputStream(out);

        InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);
        OutputStreamWriter osr = new OutputStreamWriter(output, StandardCharsets.UTF_8);

        BufferedReader br = new BufferedReader(isr);
        BufferedWriter bw = new BufferedWriter(osr);

        FileWriter fw =new FileWriter(out);
        String inline = null;
        fw.write("");
        fw.flush();

        while((inline=br.readLine())!=null){
            if(inline.length()!=0){
                int len=inline.length();
                Words(inline,fw,token);
            }

        }
        return;

    }

    public static boolean isAlpha(char ch){
        if(Character.isLetter(ch)||ch=='_')
            return true;
        return false;
    }
    public static boolean isDigit(char ch){
        if(Character.isDigit(ch))
            return true;
        return false;
    }

    public static void Words(String args,FileWriter fw,Token token) throws IOException {
       if (args.equals(" ")||args.equals("")){
           return;
       }
       char[] chars=args.toCharArray();
       for (int i=0;i<chars.length;i++){

           StringBuilder stringBuilder=new StringBuilder();
           //跳过空格
//           针对多行注释
           if (vomit){
               while (vomit){
                   if (i!=chars.length-1&&chars[i]=='*'&&chars[i+1]=='/'){
                       i++;
                       vomit=false;
//                       System.out.print("*/\n");
                       continue;
                   }
                   if (i==chars.length-1)break;
//                   System.out.print(chars[i]);
                   i++;
               }
           }
           else  {
               if (chars[i]==' '){
                   continue;
               }
//           分析注释，同时将除号进行处理
               else if(chars[i]=='/'){
//                   处理多行注释
                   if(i!=chars.length-1&&chars[i+1]=='*'){
                       vomit=true;
//                       System.out.print("IDENFR ");
                       while (i!=chars.length-1&&(chars[i]!='*'||chars[i+1]!='/')){
//                           System.out.print(chars[i]);
                           i++;
                           if (i!=chars.length-1&&chars[i]=='*'&&chars[i+1]=='/'){
                               vomit=false;
//                               System.out.print("*/\n");
                               i++;
                               break;
                           }
                       }
                   }
//                   处理单行注释
                   else if(i!=chars.length-1&&chars[i+1]=='/'){
                       while (i<chars.length){
                           stringBuilder.append(chars[i]);
                           i++;
                       }
//                       System.out.println("IDENFR "+stringBuilder.toString());
                   }
//                   处理单个除号
                   else {
//                       System.out.println("DIV /");
                       String ideout="DIV /\n";
                       fw.write(ideout);
                       fw.flush();
                   }

               }
//           分析单行注释



//           利用token表格直接进行符号查询
               else if (token.getToken(String.valueOf(chars[i]))!=null)
               {
                   stringBuilder.append(chars[i]);
                   StringBuilder stringBuilder1=new StringBuilder();
                   stringBuilder1.append(chars[i]);
//               查询连携双符号同时出现的情况
                   if (i!=chars.length-1&&token.getToken(stringBuilder1.append(chars[i+1]).toString())!=null){
                       i++;
                       stringBuilder.append(chars[i]);
//                       System.out.println(token.getToken(stringBuilder.toString()) + " " + stringBuilder.toString());
                       String ideout=token.getToken(stringBuilder.toString()) + " " + stringBuilder.toString()+"\n";
                       fw.write(ideout);
                       fw.flush();
                   }else {
//                       System.out.println(token.getToken(stringBuilder.toString()) + " " + stringBuilder.toString());
                       String ideout=token.getToken(stringBuilder.toString()) + " " + stringBuilder.toString()+"\n";
                       fw.write(ideout);
                       fw.flush();

                   }
               }

//          补充&&和||
               else if(chars[i]=='|'){
                   if(i!=chars.length-1&&chars[i+1]=='|'){
                       i++;
//                       System.out.println(token.getToken("||") + " ||" );
                       String ideout=token.getToken("||") + " ||" +"\n";
                       fw.write(ideout);
                       fw.flush();
                       //fw.close();
                   }
               }
               else if(chars[i]=='&'){
                   if(i!=chars.length-1&&chars[i+1]=='&'){
                       i++;
//                       System.out.println(token.getToken("&&") + " &&" );
                       String ideout=token.getToken("&&") + " &&" +"\n";
                       fw.write(ideout);
                       fw.flush();
                   }
               }
               //识别标识符或者变量名称
               else if (isAlpha(chars[i])){
                   stringBuilder.append(chars[i]);
//               创建识别用的单词串
                   while (i!=chars.length-1&&(isAlpha(chars[i+1])||isDigit(chars[i+1])||chars[i+1]=='_')){
                       i++;
                       stringBuilder.append(chars[i]);
                   }

                   if (token.getToken(stringBuilder.toString())==null){
//                       System.out.println("IDENFR "+stringBuilder.toString());
                       String ideout="IDENFR "+stringBuilder.toString()+"\n";
                       fw.write(ideout);
                       fw.flush();
                   }else {
//                       System.out.println(token.getToken(stringBuilder.toString()) + " " + stringBuilder.toString());
                       String ideout=token.getToken(stringBuilder.toString()) + " " + stringBuilder.toString()+"\n";
                       fw.write(ideout);
                       fw.flush();
                   }
               }
//            识别数字符号
               else if  (isDigit(chars[i])){
                   stringBuilder.append(chars[i]);
//               创建识别用的单词串
                   while (i!=chars.length-1&&(isDigit(chars[i+1]))){
                       i++;
                       stringBuilder.append(chars[i]);
                   }

//                   System.out.println("INTCON" + " " + stringBuilder.toString());
                   String ideout="INTCON" + " " + stringBuilder.toString()+"\n";
                   fw.write(ideout);
                   fw.flush();
               }
//           识别双引号以及内部内容
               else if (chars[i]=='"'){
                   stringBuilder.append(chars[i]);
                   while (i!=chars.length-1&&(chars[i+1]!='"')||(chars[i]=='\\'&&chars[i+1]=='"')){
                       i++;
                       stringBuilder.append(chars[i]);
                   }
                   i++;
                   stringBuilder.append(chars[i]);
//                   System.out.println("STRCON" + " " + stringBuilder.toString());
                   String ideout="STRCON" + " " + stringBuilder.toString()+"\n";
                   fw.write(ideout);
                   fw.flush();
               }

           }

       }
    }

}



