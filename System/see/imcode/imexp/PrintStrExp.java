package see.imcode.imexp;

import see.component.LineContainer;
import see.improve.component.regpool.RegPool;
import see.symbolstruct.CodeText;
import see.symbolstruct.FuncRegion;

import see.imcode.imitem.IMItem;
import see.imcode.imitem.StrItem;

public class PrintStrExp extends IMExp {
    protected PrintStrExp(IMItem strItem) {
        assert strItem instanceof StrItem;
        this.item2 = strItem;
    }

    @Override
    public void toCode(RegPool pool) {
        LineContainer c = new LineContainer();
        String marker = ((StrItem) this.item2).strValue;
        c.addLine(String.format("la $a0, %s", marker));
        c.addLine(String.format("li $v0, 4"));
        c.addLine(String.format("syscall"));
        CodeText.textNLine(c.dump());
        return;
    }

    @Override
    public String toString() {
        String top = ((StrItem) this.item2).strValue;
        return "print \"" + top + "\"";
    }
}
