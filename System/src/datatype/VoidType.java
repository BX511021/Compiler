package datatype;

import ErrorPart.ErrorType;

public class VoidType implements Datatype {

    @Override
    public String toString() {
        return "void";
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public Integer spaceSize() {
        ErrorType.warning("you are querying an void type's spaceSize!");
        return 0;
    }
}
