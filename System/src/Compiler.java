import Node.Node;
import Node.SYMBOL;
import Token.Token;
import Token.TokenS;
import global.Errorpart.ErrorLog;

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
        File error = new File("System/error.txt");

        FileInputStream input = new FileInputStream(in);
        FileOutputStream output = new FileOutputStream(out);
        FileOutputStream errorput = new FileOutputStream(error);

        InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);
        OutputStreamWriter osr = new OutputStreamWriter(output, StandardCharsets.UTF_8);
        OutputStreamWriter esr = new OutputStreamWriter(errorput, StandardCharsets.UTF_8);

        BufferedReader br = new BufferedReader(isr);
        BufferedWriter bw = new BufferedWriter(osr);
        BufferedWriter be = new BufferedWriter(esr);

        FileWriter fw = new FileWriter(out);
        String inline = null;
        fw.write("");
        fw.flush();

        FileWriter ew = new FileWriter(error);
        String inline2 = null;
        ew.write("");
        ew.flush();

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
//            fw.write(toekn.getSymbol()+" "+toekn.getValue()+"\n");
//            fw.flush();
//            System.out.println(toekn.getSymbol()+" " +toekn.getValue()+" "+toekn.getLine());
//
//        }

        GrammarParser grammarParser=new GrammarParser(tokenArrayList);
        Node seed = grammarParser.seed();
        String outputFile=grammarParser.grammar();
        System.out.println(outputFile);
        fw.write(outputFile);
        fw.flush();
        Visitor visitor=new Visitor(seed);
        visitor.visit();
        ErrorLog.DumpError(ew);

        return;

    }



}



