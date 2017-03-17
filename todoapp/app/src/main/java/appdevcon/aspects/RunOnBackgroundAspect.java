package appdevcon.aspects;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Xavier F. Gouchet
 */
@Aspect
public class RunOnBackgroundAspect {


    private final Handler handler;

    public RunOnBackgroundAspect() {
        HandlerThread handlerThread = new HandlerThread("@RunOnWorkerThread");
        handlerThread.start();

        synchronized (handlerThread) {
            while (handlerThread.getLooper() == null) {
                try {
                    handlerThread.wait();
                } catch (InterruptedException e) {
                    // The handler thread is prepared
                }
            }
        }

        handler = new Handler(handlerThread.getLooper());
    }

    @Pointcut("execution(@appdevcon.annotations.RunOnBackground void *(..)) && if()")
    public static boolean executeAnnotatedMethod() {
        return Looper.myLooper() == Looper.getMainLooper();
    }


    @Around("executeAnnotatedMethod()")
    public Object proceedOnBackgroundThread(final ProceedingJoinPoint pjp) {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    pjp.proceed();
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }
        };

        handler.post(r);
        return null;
    }
}
