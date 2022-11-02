package symbolstruct.entries;


import datatype.Datatype;
import narray.NArray;

public class ConstArrayEntry extends ArrayEntry implements ConstFeature {
    public NArray value;

    public ConstArrayEntry(String name, Datatype type, NArray value) {
        super(name, type);
        this.value = value;
        this.size = 1;
    }

    @Override
    public Object getValue() {
        return this.value;
    }
}
