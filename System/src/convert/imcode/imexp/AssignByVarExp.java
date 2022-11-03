package convert.imcode.imexp;


import convert.regpool.RegPool;
import convert.imcode.imitem.IMItem;
import convert.imcode.imitem.VarItem;

import symbolstruct.CodeText;
import symbolstruct.LineContainer;
import symbolstruct.entries.ConstValueEntry;
import symbolstruct.entries.Entry;

public class AssignByVarExp extends IMExp {
    protected AssignByVarExp(IMItem toAssign, IMItem value) {
        assert toAssign instanceof VarItem;
        assert value instanceof VarItem;
        this.item1 = toAssign;
        this.item2 = value;
    }

    @Override
    public void toCode(RegPool pool) {
        LineContainer c = new LineContainer();
        Entry toAssign = ((VarItem) this.item1).entry;
        Entry from = ((VarItem) this.item2).entry;

        if (from instanceof ConstValueEntry) {
            c.addLine(String.format("li %s, %d", pool.findNoLoad(toAssign), ((ConstValueEntry) from).getValue()));
        } else {
            String fromReg = pool.find(from);
            c.addLine(String.format("add %s, %s, $0", pool.findNoLoad(toAssign), fromReg));
        }

        CodeText.textNLine(c.dump());
    }

    @Override
    public String toString() {
        String a = ((VarItem) this.item1).entry.name;

        String b = ((VarItem) this.item2).entry.name;

        return a + " = " + b;
    }
}
