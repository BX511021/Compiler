package ErrorPart;

public class ErrorType {
    public static String formatStringError(int line) {
        return (line + " a");
    }

    public static void warning(String message) {
        System.out.println("[Warning]: " + message);throw new RuntimeException("[Warning]: " + message);
    }

    public static String duplicatedDefinitionError(int line) {
        return (line + " b");
    }

    public static String undefinedError(int line) {
        return (line + " c");
    }

    public static String parameterAmountError(int line) {
        return (line + " d");
    }

    public static String parameterTypeError(int line) {
        return (line + " e");
    }

    public static String voidFuncButReturnError(int line) {
        return (line + " f");
    }

    public static String intFuncButNoReturnError(int line) {
        return (line + " g");
    }

    public static String modifiedConstError(int line) {
        return (line + " h");
    }

    public static String semiconMissingError(int line) {
        return (line + " i");
    }

    public static String leftParentMissingError(int line) {
        return (line + " j");
    }

    public static String leftBraketMissingError(int line) {
        return (line + " k");
    }

    public static String printfExpAmountError(int line) {
        return (line + " l");
    }

    public static String breakContinueError(int line) {
        return (line + " m");
    }
}
