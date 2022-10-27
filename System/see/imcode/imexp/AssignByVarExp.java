package see.imcode.imexp;

import see.component.LineContainer;
import see.imcode.imitem.IntItem;
import see.improve.component.regpool.RegPool;
import see.symbolstruct.CodeText;
import see.symbolstruct.FuncRegion;

import see.imcode.imitem.IMItem;
import see.imcode.imitem.VarItem;
import see.symbolstruct.entries.ConstValueEntry;
import see.symbolstruct.entries.Entry;

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
