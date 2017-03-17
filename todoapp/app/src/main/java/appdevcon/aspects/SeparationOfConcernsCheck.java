package appdevcon.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareError;
import org.aspectj.lang.annotation.DeclareWarning;

/**
 * @author Xavier F. Gouchet
 */
@Aspect
public class SeparationOfConcernsCheck {

    @DeclareWarning(" get(public !final * *) || set (public !final * *)")
    public static final String PUBLIC_NON_FINAL =
            "Don't use non final public fields !";
}
