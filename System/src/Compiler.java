import Node.SYMBOL;
import Token.Token;
import Token.TokenS;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Compiler {
    public static boolean vomit = false;

    public static void main(String[] args) throws IOException {
        ArrayList<Token> tokenArrayList;
        Scanner scan = new Scanner(System.in);
        TokenS tokens = new TokenS();

        File in = new File("System/testfile.txt");
        File out = new File("System/output.txt");

        FileInputStream input = new FileInputStream(in);
        FileOutputStream output = new FileOutputStream(out);

        InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);
        OutputStreamWriter osr = new OutputStreamWriter(output, StandardCharsets.UTF_8);

        BufferedReader br = new BufferedReader(isr);
        BufferedWriter bw = new BufferedWriter(osr);

        FileWriter fw = new FileWriter(out);
        String inline = null;
        fw.write("");
        fw.flush();

    /*
        利用词法分析解析原本的程序，记录token以及行数，储存在一个arrayList里面

    */
        int number=0;
        Lexparser lexparser=new Lexparser();
        while ((inline = br.readLine()) != null) {
            number++;
            if (inline.length() != 0) {
                int len = inline.length();

                lexparser.LexParser(inline, tokens,number);
            }

        }

        tokenArrayList=lexparser.TokenList;

//        for (Token toekn:tokenArrayList) {
////            fw.write(toekn.getSymbol()+" "+toekn.getValue()+"\n");
////            fw.flush();
//            System.out.println(toekn.getSymbol()+" " +toekn.getValue()+" "+toekn.getLine());
//
//        }

        GrammarParser grammarParser=new GrammarParser(tokenArrayList);
        String outputFile=grammarParser.parse();
        fw.write(outputFile);
        fw.flush();

        return;

    }



}



