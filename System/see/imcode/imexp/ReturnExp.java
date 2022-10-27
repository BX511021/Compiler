package see.imcode.imexp;

import see.component.LineContainer;
import see.improve.component.regpool.RegPool;
import see.symbolstruct.CodeText;
import see.symbolstruct.FuncRegion;

import see.imcode.imitem.IMItem;
import see.imcode.imitem.VarItem;
import see.symbolstruct.entries.ConstValueEntry;
import see.symbolstruct.entries.Entry;

public class ReturnExp extends IMExp {
    protected ReturnExp(IMItem var) {
        assert var instanceof VarItem || var == null;
        this.item2 = var;
    }

    @Override
    public void toCode(RegPool pool) {
        LineContainer c = new LineContainer();

        if (item2 != null) {
            Entry toa = ((VarItem) this.item2).entry;
            if (toa instanceof ConstValueEntry) {
                c.addLine(String.format("addi $v0, $0, %d", ((ConstValueEntry) toa).getValue()));
            } else {
                c.addLine(String.format("add $v0, $0, %s", pool.find(toa)));
            }
        }

        c.addLine("# ret and restore the frame");
        c.addLine(String.format("lw $ra, %d($sp)", pool.getFrame().offsetRA));
        c.addLine(String.format("addiu $sp, $sp, %d", pool.getFrame().size));
        c.addLine(String.format("jr $ra"));
        // 恢复函数栈帧并返回指定地址

        CodeText.textNLine(c.dump());
        return;
    }

    @Override
    public String toString() {
        if (item2 != null) {
            String ret = ((VarItem) this.item2).entry.name;
            return "return " + ret;
        } else {
            return "return;";
        }
    }
}
