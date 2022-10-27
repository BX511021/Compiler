package symboltable.symbols;


import global.Errorpart.ErrorType;
import symboltable.BasicType;
import symboltable.Block;
import symboltable.SymbolItemType;

public class VarSymbolItem extends SymbolItem {

    public Boolean isConst;
    public Object constValue;

    public VarSymbolItem(Boolean isConst, BasicType basicType, String name, Block block) {
        super(basicType, SymbolItemType.VAR, name, block);
        this.isConst = isConst;
        this.constValue = null;
    }

    public Object getConstValue() {
        if (!isConst) {
            ErrorType.warning(" <" + this.name + ">try to get value from a non-const var! (const[] element is also non-const!)");
        }
        return constValue;
    }

    public void setConstValue(Integer constValue) {
        if (this.constValue != null) {
            ErrorType.warning(" <" + this.name + "> try to set value for a var that already has a const value");
        }
        if (!isConst) {
            ErrorType.warning(" <" + this.name + ">try to set value for a non-const var! (const[] element is also non-const!)");
        }
        this.constValue = constValue;
    }

    @Override
    public String type() {
        if (!isConst) {
            return "int";
        } else {
            return "const int";
        }
    }

    @Override
    public String toString() {
        return type() + "\t" + name;
    }
}
