package appdevcon.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareWarning;

/**
 * @author Xavier F. Gouchet
 */
@Aspect
public class ExceptionHandlingCheck {

    @DeclareWarning("handler (java.lang.Error+)")
    public static final String ERROR_HANDLING =
            "Errors represent unrecoverable errors. They should not be caught.";

    @DeclareWarning("handler (java.lang.Throwable || java.lang.Exception || java.lang.RuntimeException)")
    public static final String GENERIC_HANDLING = "You should catch a specific exception.";

}