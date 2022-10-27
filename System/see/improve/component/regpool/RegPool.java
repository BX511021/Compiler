package see.improve.component.regpool;

import mips.register.Register;
import see.symbolstruct.CodeText;
import see.symbolstruct.Frame;
import see.symbolstruct.FuncRegion;
import see.symbolstruct.Region;
import see.symbolstruct.entries.ConstValueEntry;
import see.symbolstruct.entries.Entry;

import java.util.HashMap;

public class RegPool {
    public LocRegPool locPool;
    public GlbRegPool glbPool;
    private FuncRegion region;

    public RegPool(Region region, HashMap<Entry, Register> colorMap) {
        this.glbPool = new GlbRegPool(colorMap, region);
        this.locPool = new LocRegPool(region);
        if (region instanceof FuncRegion) {
            this.region = (FuncRegion) region;
        }
    }

    public String find(Entry entry) {
        Register ret = this.glbPool.find(entry);
        if (ret == null) {
            ret = this.locPool.find(entry);
        }
        return ret.toString();
    }

    public String findNoLoad(Entry entry) {
        Register ret = this.glbPool.findNoLoad(entry);
        if (ret == null) {
            ret = this.locPool.findNoLoad(entry);
        }
        return ret.toString();
    }

    public String allocTmpReg() {
        return "$fp";
    }

    public void freeTmpReg(String register) {
        ;
    }

    public Frame getFrame() {
        return this.region.frame;
    }
}
