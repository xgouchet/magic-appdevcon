package appdevcon.aspects;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Xavier F. Gouchet
 */
@Aspect
public class LoggingAspect {

    @Pointcut("execution(* com.example.android.architecture.blueprints..*(..) )")
    public static void executeAnyMethod(){

    }


    @Before("executeAnyMethod()")
    public void logBeforeMethod(JoinPoint jp){
        Log.v("Aop", jp.getSignature().toShortString());
    }
}
