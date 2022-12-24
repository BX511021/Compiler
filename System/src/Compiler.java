import Node.Node;
import Token.Token;
import Token.TokenS;
import ErrorPart.ErrorLog;
import symbolstruct.CodeText;

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
        String path = ".";

        File in = new File(String.format("%s/testfile.txt",path));
        File out = new File(String.format("%s/output.txt",path));
        File error = new File(String.format("%s/error.txt",path));
        File MIPS = new File(String.format("%s/mips.txt",path));

        FileInputStream input = new FileInputStream(in);
        FileOutputStream output = new FileOutputStream(out);
        FileOutputStream errorput = new FileOutputStream(error);
        FileOutputStream mipsput = new FileOutputStream(MIPS);

        InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);
        OutputStreamWriter osr = new OutputStreamWriter(output, StandardCharsets.UTF_8);
        OutputStreamWriter esr = new OutputStreamWriter(errorput, StandardCharsets.UTF_8);
        OutputStreamWriter Misp_sr = new OutputStreamWriter(mipsput, StandardCharsets.UTF_8);

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

        FileWriter mips_w = new FileWriter(MIPS);
        String inline3 = null;
        mips_w.write("");
        mips_w.flush();

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

//        Errorparser errorparser = new Errorparser(seed);
//        errorparser.visit();


        Converter converter = new Converter(seed);
        converter.visit();
        mips_w.write(".data\n");
        mips_w.write(CodeText.dumpData());
        mips_w.write(".text\n");
        mips_w.write(CodeText.dumpText());
        mips_w.flush();



        ErrorLog.DumpError(ew);

        return;

    }



}



