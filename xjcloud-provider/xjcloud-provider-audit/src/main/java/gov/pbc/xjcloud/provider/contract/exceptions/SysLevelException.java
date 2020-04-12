package gov.pbc.xjcloud.provider.contract.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

public class SysLevelException extends RuntimeException {
    private static final long serialVersionUID = -3085055009384088487L;

    public SysLevelException(String msg, Throwable th) {
        super(msg, th);
    }

    public SysLevelException(String msg) {
        super(msg);
    }

    public SysLevelException(Throwable th) {
        super(th);
    }

    public void printStackTrace(PrintStream s) {
        if (super.getCause() != null) {
            s.print(this.getClass().getName() + " Caused by: ");
            super.getCause().printStackTrace(s);
        } else {
            super.printStackTrace(s);
        }

    }

    public void printStackTrace(PrintWriter s) {
        if (super.getCause() != null) {
            s.print(this.getClass().getName() + " Caused by: ");
            super.getCause().printStackTrace(s);
        } else {
            super.printStackTrace(s);
        }

    }
}
