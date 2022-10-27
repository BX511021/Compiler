package see.imcode.imexp;

import see.improve.component.regpool.RegPool;

import see.imcode.imitem.IMItem;
import see.symbolstruct.FuncRegion;
import see.symbolstruct.Scope;

public abstract class IMExp {
    public IMItem item1;
    public IMItem item2;
    public IMItem item3;

    public Scope in;

    public abstract void toCode(RegPool pool);

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " " + item1 +
                ", " + item2 +
                ", " + item3;
    }

    public void setIn(Scope in) {
        this.in = in;
    }
}
