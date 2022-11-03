package symboltable;

import java.util.ArrayList;

public class Symbolpart {
    public   static  ArrayList<String> Sybmbols =new ArrayList<>();

    public static void SymbolLog(String str) {
        try {
//            this.SymbolLogWriter.write(str + "\n");
//            this.SymbolLogWriter.flush();
            Sybmbols.add(str+"\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
