package appdevcon.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import appdevcon.annotations.Retry;

/**
 * @author Xavier F. Gouchet
 */
@Aspect
public class RetryAspect {


    @Pointcut("execution(@appdevcon.annotations.Retry * *(..))")
    public void executeAnnotatedMethod() {
    }


    @Around("executeAnnotatedMethod() && @annotation(r) ")
    public Object retryExecution(ProceedingJoinPoint pjp, Retry r) throws Throwable {

        int retriesLeft = r.retryCount();
        long delayBeforeRetryMs = r.retryDelayMs();
        Class<? extends Throwable> allowedClass = r.exception();

        Throwable lastThrowable = new RuntimeException();

        while (retriesLeft > 0) {
            try {
                Object result = pjp.proceed();
                return result;
            } catch (Throwable e) {
                if (!allowedClass.isAssignableFrom(e.getClass())) {
                    throw e;
                }
                lastThrowable = e;
                retriesLeft--;
                if ((retriesLeft > 0) && (delayBeforeRetryMs > 0)) {
                    try {
                        Thread.sleep(delayBeforeRetryMs);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }

        throw lastThrowable;
    }

}
