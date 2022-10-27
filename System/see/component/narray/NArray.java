package see.component.narray;

import see.component.datatype.ArrayType;
import see.component.datatype.Datatype;
import global.Error;
import see.symbolstruct.entries.VarEntry;

import java.util.ArrayList;

public class NArray {
    private ArrayList<NArrayItem> values;
    private ArrayType datatype;

    public NArray(Datatype datatype, ArrayList<NArrayItem> values) {
        this.datatype = (ArrayType) datatype;
        assert values.size() == this.datatype.flat();
        this.values = values;
    }

    public ArrayList<NArrayItem> getValues() {
        return values;
    }

    public ArrayType getDatatype() {
        return datatype;
    }
}
