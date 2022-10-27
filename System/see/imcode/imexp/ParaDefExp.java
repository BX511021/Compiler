package see.imcode.imexp;

import see.imcode.imitem.IMItem;
import see.imcode.imitem.VarItem;
import see.improve.component.regpool.RegPool;
import see.symbolstruct.FuncRegion;

public class ParaDefExp extends IMExp {
    public ParaDefExp(IMItem var) {
        assert var instanceof VarItem;
        this.item1 = var;
    }

    @Override
    public void toCode(RegPool pool) {
        return;
    }

    @Override
    public String toString() {
        return "import " + ((VarItem) this.item1).entry.name;
    }
}
