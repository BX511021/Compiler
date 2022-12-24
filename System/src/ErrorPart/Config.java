package ErrorPart;

import convert.regpool.Register;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Config是全局常量类，遵循单例模式
 */
public class Config {

    public static long ProgramInitTime = System.currentTimeMillis();
    private static Integer tmpNameSed = 0;


    public static Integer getTmpNameSed() {
        return tmpNameSed++;
    }

    public static ArrayList<Register> getGlbReg() {
        Register[] regs = new Register[]{
                Register.$s0,
                Register.$s1,
                Register.$s2,
                Register.$s3,
                Register.$s4,
                Register.$s5,
                Register.$s6,
                Register.$s7,
                Register.$a1,
                Register.$a2,
                Register.$a3,
                Register.$v1,
                Register.$gp,
                Register.$t8,
                Register.$t9,
                Register.$t5,
                Register.$t6,
                Register.$t7
        };
        ArrayList ret = new ArrayList();
        Collections.addAll(ret, regs);
        return ret;
    }

    public static ArrayList<Register> getLocReg() {
        Register[] regs = new Register[]{
                Register.$t0,
                Register.$t1,
                Register.$t2,
                Register.$t3,
                Register.$t4
        };
        ArrayList ret = new ArrayList();
        Collections.addAll(ret, regs);
        return ret;
    }

    public static Integer PowerOfTwo(int n) {
        if (n > 0 && (n & (n - 1)) == 0) {
            return (int) (Math.log(n) / Math.log(2));
        }
        return null;
    }
}
