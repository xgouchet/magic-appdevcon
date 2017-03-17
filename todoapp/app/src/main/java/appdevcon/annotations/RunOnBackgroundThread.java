package appdevcon.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Xavier F. Gouchet
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface RunOnBackgroundThread {
}
