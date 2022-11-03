package convert.imcode.imexp;

import convert.regpool.RegPool;
import convert.imcode.imitem.IMItem;
import convert.imcode.imitem.VarItem;


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
