package convert.imcode.imexp;


import convert.regpool.RegPool;
import convert.imcode.imitem.FuncItem;
import convert.imcode.imitem.IMItem;

import symbolstruct.CodeText;
import symbolstruct.LineContainer;
import symbolstruct.entries.Entry;

/**
 * 调用函数的四元式
 */
public class CallExp extends IMExp {
    protected CallExp(IMItem item1) {
        assert item1 instanceof FuncItem;
        this.item1 = item1;
    }

    @Override
    public void toCode(RegPool pool) {
        Entry func = ((FuncItem) this.item1).entry;
        LineContainer c = new LineContainer();
        c.addLine(String.format("jal %s", func.name));
        c.addLine(String.format("nop", func.name));
        CodeText.textNLine(c.dump());
        return;
    }

    @Override
    public String toString() {
        String item = ((FuncItem) this.item1).entry.name;
        return "call " + item + "()";
    }
}
