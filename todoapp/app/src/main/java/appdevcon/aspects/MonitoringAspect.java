package appdevcon.aspects;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Xavier F. Gouchet
 */
@Aspect
public class MonitoringAspect {


    @Pointcut("execution(@appdevcon.annotations.Monitor * * (..) )")
    public static void executeAnnotatedMethod() {
    }


    @Around("executeAnnotatedMethod()")
    public Object monitorExecution(ProceedingJoinPoint pjp) throws Throwable {

        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long end = System.currentTimeMillis();

        Log.i("AOP", pjp.getSignature().toShortString() +
                " took " + (end - start) + "ms");

        return result;
    }

}
