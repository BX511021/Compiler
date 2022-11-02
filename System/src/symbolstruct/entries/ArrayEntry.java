package symbolstruct.entries;

import datatype.Datatype;

public class ArrayEntry extends AbsVarEntry {

    public ArrayEntry(String name, Datatype datatype) {
        super(name, datatype);
        this.size = 1;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + this.datatype + " " + this.name;
    }
}
