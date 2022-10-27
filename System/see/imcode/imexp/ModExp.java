package see.imcode.imexp;

import see.improve.component.regpool.RegPool;

import see.component.LineContainer;
import see.imcode.imitem.IMItem;
import see.imcode.imitem.IntItem;
import see.imcode.imitem.VarItem;
import see.symbolstruct.CodeText;
import see.symbolstruct.entries.ConstValueEntry;
import see.symbolstruct.entries.Entry;

public class ModExp extends IMExp {
    protected ModExp(IMItem item1, IMItem item2, IMItem item3) {
        assert item1 instanceof VarItem;
        assert item2 instanceof VarItem;
        assert item3 instanceof VarItem;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
    }

    @Override
    public void toCode(RegPool pool) {
        LineContainer ret = new LineContainer();

        Entry toAssign = ((VarItem) this.item1).entry;
        Entry a = ((VarItem) this.item2).entry;
        Entry b = ((VarItem) this.item3).entry;

        if (a instanceof ConstValueEntry && b instanceof ConstValueEntry) {
            ret.addLine(String.format("li %s, %d", pool.findNoLoad(toAssign),
                    ((Integer) ((ConstValueEntry) a).getValue()) % ((Integer) ((ConstValueEntry) b).getValue())));
        } else if (a instanceof ConstValueEntry) {
            String regStr = pool.allocTmpReg();
            ret.addLine(String.format("li %s %d", regStr, ((ConstValueEntry) a).getValue()));
            ret.addLine(String.format("div %s, %s", regStr, pool.find(b)));
            ret.addLine(String.format("mfhi %s", pool.findNoLoad(toAssign)));
        } else if (b instanceof ConstValueEntry) {
            String regStr = pool.allocTmpReg();
            ret.addLine(String.format("li %s %d", regStr, ((ConstValueEntry) b).getValue()));
            ret.addLine(String.format("div %s, %s", pool.find(a), regStr));
            ret.addLine(String.format("mfhi %s", pool.findNoLoad(toAssign)));
        } else {
            ret.addLine(String.format("div %s, %s", pool.find(a), pool.find(b)));
            ret.addLine(String.format("mfhi %s", pool.findNoLoad(toAssign)));
        }

        CodeText.textNLine(ret.dump());
    }
    @Override
    public String toString() {
        String a = null;
        if (this.item2 instanceof IntItem) {
            a = String.valueOf(((IntItem) this.item2).intValue);
        } else if (this.item2 instanceof VarItem) {
            a = ((VarItem) this.item2).entry.name;
        }

        String b = null;
        if (this.item3 instanceof IntItem) {
            b = String.valueOf(((IntItem) this.item3).intValue);
        } else if (this.item3 instanceof VarItem) {
            b = ((VarItem) this.item3).entry.name;
        }
        String it = ((VarItem) this.item1).entry.name;
        return it + " = " + a + " % " + b;
    }
}
