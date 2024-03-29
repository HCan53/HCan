package com.hcan53.android.defense;


import com.hcan53.android.defense.core.hook.HookH;
import com.hcan53.android.defense.core.hook.HookInstrumentation;
import com.hcan53.android.defense.core.hook.HookThread;
import com.hcan53.android.defense.core.hook.IHook;
import com.hcan53.android.defense.handler.ExceptionDispatcher;
import com.hcan53.android.defense.handler.IExceptionHandler;

public final class DefenseCrash {

    private static ExceptionDispatcher mExceptionDispatcher;

    private static boolean installed = false;

    private static boolean initialized = false;

    private static IHook hookThread;

    private static HookH hookH;

    private static IHook hookInstrumentation;

    private DefenseCrash() {
    }

    /**
     * initialize the defense lib
     */
    public static void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        mExceptionDispatcher = new ExceptionDispatcher();
        hookThread = new HookThread(mExceptionDispatcher);
        hookH = new HookH(mExceptionDispatcher);
        hookInstrumentation = new HookInstrumentation(mExceptionDispatcher);
    }

    /**
     * install the defense fire wall
     *
     * @param handler
     */
    public static void install(IExceptionHandler handler) {
        if (!initialized) {
            throw new IllegalStateException("need call DefenseCrash.initialize() first");
        }
        if (installed) {
            return;
        }
        installed = true;
        mExceptionDispatcher.setIExceptionHandler(handler);
        hookInstrumentation.hook();
        hookThread.hook();
        hookH.hook();
        hookH.setHookedInstrumentation(hookInstrumentation.isHooked());
    }

    /**
     * uninstall the defense fire wall
     */
    public static void unInstall() {
        if (!initialized) {
            throw new IllegalStateException("need call DefenseCrash.initialize() first");
        }
        if (!installed) {
            return;
        }
        installed = false;
        hookInstrumentation.unHook();
        hookThread.unHook();
        hookH.unHook();
        hookH.setHookedInstrumentation(hookInstrumentation.isHooked());
    }


}
