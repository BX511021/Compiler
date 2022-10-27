package see.imcode.imexp;

import see.component.LineContainer;
import global.Config;
import see.imcode.imitem.IntItem;
import see.improve.component.regpool.RegPool;
import see.symbolstruct.CodeText;
import see.symbolstruct.FuncRegion;

import see.component.Label;
import see.imcode.imitem.IMItem;
import see.imcode.imitem.LabelItem;
import see.imcode.imitem.VarItem;
import see.symbolstruct.entries.ConstValueEntry;
import see.symbolstruct.entries.Entry;

public class ConJumpExp extends IMExp {
    protected ConJumpExp(IMItem label, IMItem conVar) {
        assert label instanceof LabelItem;
        assert conVar instanceof VarItem;
        this.item1 = label;
        this.item2 = conVar;
    }

    @Override
    public void toCode(RegPool pool) {
        Label label = ((LabelItem) this.item1).labelName;

        Entry a = ((VarItem) this.item2).entry;
        if (a instanceof ConstValueEntry) {
            Integer value = (Integer) ((ConstValueEntry) a).getValue();
            if (value == 1) {
                CodeText.textNLine(String.format("j %s", label.labelName));
            }
        } else {
            CodeText.textNLine(String.format("bne $0, %s, %s", pool.find(a), label.labelName));
        }
    }

    @Override
    public String toString() {
        Label label = ((LabelItem) this.item1).labelName;
        String con = ((VarItem) this.item2).entry.name;
        return "conjump " + label.labelName + " by " + con;
    }
}
