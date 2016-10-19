package com.snaptechnology.bgonzalez.bookmyroomandroid.utils;

import android.os.SystemClock;

/**
 * Utilities to events android
 */

public class EventHandlerUtilities {

    private static long mLastClickTime;

    public static void preventDoubleClick(){
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }
}
