package imcode.imexp;



import Node.Label;
import component.regpool.RegPool;
import imcode.imitem.IMItem;
import imcode.imitem.LabelItem;

import symbolstruct.CodeText;
import symbolstruct.LineContainer;

/**
 * 跳转语句
 */
public class JumpExp extends IMExp {
    protected JumpExp(IMItem item1) {
        assert item1 instanceof LabelItem;
        this.item1 = item1;
    }

    @Override
    public void toCode(RegPool pool) {
        LineContainer ret = new LineContainer();
        Label label = ((LabelItem) this.item1).labelName;

        ret.addLine("j " + label.labelName);

        CodeText.textNLine(ret.dump());
    }

    @Override
    public String toString() {
        Label label = ((LabelItem) this.item1).labelName;
        return "jump " + label.labelName;
    }
}
