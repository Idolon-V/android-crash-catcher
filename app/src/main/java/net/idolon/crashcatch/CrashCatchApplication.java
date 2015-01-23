package net.idolon.crashcatch;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

public class CrashCatchApplication extends Application {

    private static final String LOG_TAG = "CrashCatch";

    @Override
    public void onCreate() {
        super.onCreate();
        startCatcher();
    }

    private void startCatcher() {
        UncaughtExceptionHandler systemUncaughtHandler = Thread.getDefaultUncaughtExceptionHandler();
        // the following handler is used to catch exceptions thrown in background threads
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtHandler(new Handler()));

        while (true) {
            try {
                Log.v(LOG_TAG, "Starting crash catch Looper");
                Looper.loop();
                Thread.setDefaultUncaughtExceptionHandler(systemUncaughtHandler);
                throw new RuntimeException("Main thread loop unexpectedly exited");
            } catch (BackgroundException e) {
                Log.e(LOG_TAG, "Caught the exception in the background thread " + e.threadName + ", TID: " + e.tid, e.getCause());
                showCrashDisplayActivity(e.getCause());
            } catch (Throwable e) {
                Log.e(LOG_TAG, "Caught the exception in the UI thread, e:", e);
                showCrashDisplayActivity(e);
            }
        }
    }

    void showCrashDisplayActivity(Throwable e) {
        Intent i = new Intent(this, CrashDisplayActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("e", e);
        startActivity(i);
    }


    /**
     * This handler catches exceptions in the background threads and propagates them to the UI thread
     */
    static class UncaughtHandler implements UncaughtExceptionHandler {

        private final Handler mHandler;

        UncaughtHandler(Handler handler) {
            mHandler = handler;
        }

        public void uncaughtException(Thread thread, final Throwable e) {
            Log.v(LOG_TAG, "Caught the exception in the background " + thread + " propagating it to the UI thread, e:", e);
            final int tid = Process.myTid();
            final String threadName = thread.getName();
            mHandler.post(new Runnable() {
                public void run() {
                    throw new BackgroundException(e, tid, threadName);
                }
            });
        }
    }

    /**
     * Wrapper class for exceptions caught in the background
     */
    static class BackgroundException extends RuntimeException {

        final int tid;
        final String threadName;

        /**
         * @param e original exception
         * @param tid id of the thread where exception occurred
         * @param threadName name of the thread where exception occurred
         */
        BackgroundException(Throwable e, int tid, String threadName) {
            super(e);
            this.tid = tid;
            this.threadName = threadName;
        }
    }

}
