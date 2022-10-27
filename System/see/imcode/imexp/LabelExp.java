package see.imcode.imexp;

import see.component.LineContainer;
import see.imcode.imitem.VarItem;
import see.improve.component.regpool.RegPool;
import see.symbolstruct.CodeText;
import see.symbolstruct.FuncRegion;

import see.component.Label;
import see.imcode.imitem.IMItem;
import see.imcode.imitem.LabelItem;
import see.symbolstruct.entries.Entry;

public class LabelExp extends IMExp {
    protected LabelExp(IMItem item1) {
        assert item1 instanceof LabelItem;
        this.item1 = item1;
    }

    @Override
    public void toCode(RegPool pool) {
        LineContainer ret = new LineContainer();
        Label label = ((LabelItem) this.item1).labelName;

        ret.addLine(label.labelName + ":");

        CodeText.textNLine(ret.dump());
    }

    @Override
    public String toString() {
        Label label = ((LabelItem) this.item1).labelName;
        return label.labelName + ":";
    }
}
