package see.symboltable.symbols;

import see.symboltable.BasicType;
import see.symboltable.Block;
import see.symboltable.SymbolItemType;

public class StrSymbolItem extends SymbolItem {

    private String content;

    public StrSymbolItem(String name, Block block, String content) {
        super(BasicType.STR, SymbolItemType.STR, name, block);
        this.content = content;
    }
}
