package symboltable;

import global.Errorpart.ErrorLog;

import java.util.ArrayList;

public class Symbolpart {
    public   static  ArrayList<String> Sybmbols;

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
