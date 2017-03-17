package appdevcon.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Xavier F. Gouchet
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Retry {

    int retryCount() default 3;

    long retryDelayMs() default 500;

    Class<? extends Throwable> exception() default Throwable.class;
}
