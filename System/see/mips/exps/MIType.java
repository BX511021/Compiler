package see.mips.exps;

import see.mips.register.Register;

public abstract class MIType extends MIPSExp{
    private Register rs;
    private Register rt;

    private Integer immediate;

    public abstract String toString();
}
