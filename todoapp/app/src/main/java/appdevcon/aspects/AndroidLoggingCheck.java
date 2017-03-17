package appdevcon.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareError;

/**
 * @author Xavier F. Gouchet
 */
@Aspect
public class AndroidLoggingCheck {




    @DeclareError("get (* java.lang.System.out)")
    public static final String OUT_STREAM = "You should use Android Log.?()";

    @DeclareError("get (* java.lang.System.err)")
    public static final String ERR_STREAM = "You should use Android Log.e()";

    @DeclareError("call (* java.lang.Throwable+.printStackTrace(..))")
    public static final String  PRINT_STACK_TRACE = "Do not do that ! ";
}
