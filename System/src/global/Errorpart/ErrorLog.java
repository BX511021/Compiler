package global.Errorpart;

import java.util.ArrayList;
import java.util.Comparator;

public class ErrorLog {
    static private ArrayList<ErrorMessage> Errors=new ArrayList<>();

    public ErrorLog(ArrayList<ErrorMessage> errors) {
        Errors = errors;
    }

    static class ErrorMessage {
        Integer line;
        String message;

        ErrorMessage(Integer a, String message) {
            this.line = a;
            this.message = message;

        }
    }

    public static void ErrorLogadd(String string) {
        Integer line = Integer.valueOf(string.split(" ")[0]);
        Errors.add(new ErrorMessage(line, string));
    }

    public static void DumpError() {
        Errors.sort(new Comparator<ErrorMessage>() {
            @Override
            public int compare(ErrorMessage o1, ErrorMessage o2) {
                if (o1.line >= o2.line) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        ErrorMessage former = null;
        for (ErrorMessage message : Errors) {
            if (former != null) {
                if (former.line == message.line) {
                    continue;
                }
            }
            try {
                System.out.println(message.message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            former = message;
        }
    }


}
