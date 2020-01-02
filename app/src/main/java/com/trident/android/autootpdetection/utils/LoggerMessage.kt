package com.trident.android.autootpdetection.utils

import android.util.Log

/**
 * @author surya devi
 */
object LoggerMessage {

    /**
     * Method for showing the debug log messages
     *
     * @param TAG
     * @param message
     */
    fun LogDebugMsg(TAG: String, message: String?) {
        if (message != null)
            Log.d(TAG, message)
    }

    /**
     * Method for showing the all log messages
     *
     * @param TAG
     * @param message
     */
    fun LogVerboseMsg(TAG: String, message: String?) {
        if (message != null)
            Log.v(TAG, message)
    }

    /**
     * Method for showing the info log messages
     *
     * @param TAG
     * @param message
     */
    fun LogInfoMsg(TAG: String, message: String?) {
        if (message != null)
            Log.i(TAG, message)
    }

    /**
     * Method for showing the Log Error Messages
     *
     * @param TAG
     * @param message
     */
    fun LogErrorMsg(TAG: String, message: String?) {

        if (message != null)
            Log.e(TAG, message)
    }

    /**
     * Method for printing the exception message
     *
     * @param TAG
     */
    fun LogException(TAG: String, e: Exception?) {
        if (e != null)
            Log.e(TAG, Log.getStackTraceString(e))


    }
}
