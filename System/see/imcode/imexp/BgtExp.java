package see.imcode.imexp;

import see.component.LineContainer;
import see.imcode.imitem.VarItem;
import see.improve.component.regpool.RegPool;
import see.symbolstruct.CodeText;

import see.imcode.imitem.IMItem;
import see.symbolstruct.entries.ConstValueEntry;
import see.symbolstruct.entries.Entry;

public class BgtExp extends IMExp {
    protected BgtExp(IMItem item1, IMItem item2, IMItem item3) {
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
            Integer aValue = (Integer) ((ConstValueEntry) a).getValue();
            Integer bValue = (Integer) ((ConstValueEntry) b).getValue();
            Integer boolValue = (aValue > bValue) ? 1 : 0;
            ret.addLine(String.format("li %s, %d", pool.findNoLoad(toAssign), boolValue));
        } else if (a instanceof ConstValueEntry) {
            String bReg = pool.find(b);
            Integer aValue = (Integer) ((ConstValueEntry) a).getValue();
//            ret.addLine(String.format("li %s 0", regStr));
//            ret.addLine(String.format("bge %s, %d, %s", bReg, aValue, tmpLabel));
//            ret.addLine(String.format("li %s, 1", regStr));
//            ret.addLine(tmpLabel + ":");
//            ret.addLine(String.format("add %s, %s, $0", pool.findNoLoad(toAssign), regStr));
            String tmpReg = pool.allocTmpReg();
            ret.addLine(String.format("li %s, %d", tmpReg, aValue));
            ret.addLine(String.format("slt %s, %s, %s", pool.findNoLoad(toAssign), bReg, tmpReg));
        } else if (b instanceof ConstValueEntry) {
            String aReg = pool.find(a);
            Integer bValue = (Integer) ((ConstValueEntry) b).getValue();
//            ret.addLine(String.format("li %s 0", regStr));
//            ret.addLine(String.format("ble %s, %d, %s", aReg, bValue, tmpLabel));
//            ret.addLine(String.format("li %s, 1", regStr));
//            ret.addLine(tmpLabel + ":");
//            ret.addLine(String.format("add %s, %s, $0", pool.findNoLoad(toAssign), regStr));
            ret.addLine(String.format("sgt %s, %s, %d", pool.findNoLoad(toAssign), aReg, bValue));
        } else {
            String aReg = pool.find(a);
            String bReg = pool.find(b);
//            ret.addLine(String.format("li %s 0", regStr));
//            ret.addLine(String.format("ble %s, %s, %s", aReg, bReg, tmpLabel));
//            ret.addLine(String.format("li %s, 1", regStr));
//            ret.addLine(tmpLabel + ":");
//            ret.addLine(String.format("add %s, %s, $0", pool.findNoLoad(toAssign), regStr));
            ret.addLine(String.format("sgt %s, %s, %s", pool.findNoLoad(toAssign), aReg,bReg));
        }

        CodeText.textNLine(ret.dump());
    }
}
