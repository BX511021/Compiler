package see.imcode.imexp;

import see.component.LineContainer;
import see.improve.component.regpool.RegPool;
import see.symbolstruct.CodeText;
import see.symbolstruct.FuncRegion;

import see.imcode.imitem.FuncItem;
import see.imcode.imitem.IMItem;
import see.imcode.imitem.LabelItem;
import see.symbolstruct.entries.Entry;

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
