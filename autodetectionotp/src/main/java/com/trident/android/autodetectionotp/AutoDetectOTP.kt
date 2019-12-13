package com.trident.android.autodetectionotp

import android.content.*
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
/**
 * @author Trident_Surya Devi
 */
class AutoDetectOTP(context: Context) {
    private var smsCallback: SmsCallback? = null
    private var googleApiClient: GoogleApiClient? = null
    private val context: Context
    private var chargerReceiver: BroadcastReceiver? = null
    private val appCompatActivity: AppCompatActivity = context as AppCompatActivity
    private var intentFilter: IntentFilter? = null

    init {
        this.context = appCompatActivity.applicationContext
    }

    fun requestPhoneNoHint() {
        googleApiClient = GoogleApiClient.Builder(context)
            .enableAutoManage(
                appCompatActivity
            ) { }
            .addApi(Auth.CREDENTIALS_API)
            .build()
        val hintRequest = HintRequest.Builder()
            .setHintPickerConfig(
                CredentialPickerConfig.Builder()
                    .setShowCancelButton(true)
                    .build()
            )
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest)
        try {
            appCompatActivity.startIntentSenderForResult(
                intent.intentSender,
                RC_HINT,
                null,
                0,
                0,
                0
            )
        } catch (e: IntentSender.SendIntentException) {
            Log.e("PHONE_HINT", "Could not start hint picker Intent", e)
        }

    }

    fun requestPhoneNoHint(callback: Callback) {
        googleApiClient = GoogleApiClient.Builder(context)
            .enableAutoManage(
                appCompatActivity
            ) { }
            .addApi(Auth.CREDENTIALS_API)
            .build()
        googleApiClient = GoogleApiClient.Builder(context)
            .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                override fun onConnected(@Nullable bundle: Bundle?) {
                    callback.connectionSuccess(bundle)
                }

                override fun onConnectionSuspended(i: Int) {
                    callback.connectionSuspend(i)
                }
            })
            .enableAutoManage(
                appCompatActivity
            ) { connectionResult -> callback.connectionFailed(connectionResult) }
            .addApi(Auth.CREDENTIALS_API)
            .build()
        val hintRequest = HintRequest.Builder()
            .setHintPickerConfig(
                CredentialPickerConfig.Builder()
                    .setShowCancelButton(true)
                    .build()
            )
            .setPhoneNumberIdentifierSupported(true)
            .build()


        val intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest)
        try {
            appCompatActivity.startIntentSenderForResult(
                intent.intentSender,
                RC_HINT,
                null,
                0,
                0,
                0
            )
        } catch (e: IntentSender.SendIntentException) {
            Log.e("PHONE_HINT", "Could not start hint picker Intent", e)
        }

    }

    fun startSmsRetriver(smsCallback: SmsCallback) {
        registerReceiver()
        this.smsCallback = smsCallback
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        val client = SmsRetriever.getClient(context)

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        val task = client.startSmsRetriever()
        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener { aVoid ->
            Log.e("SMS RECEIVER", "success")
            if(aVoid!=null){
                smsCallback.connectionSuccess(aVoid)
            }
        }

        task.addOnFailureListener { smsCallback.connectionFailed() }

    }

    fun getPhoneNo(data: Intent): String {
        val cred = data.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
        return cred.id

    }

    private fun registerReceiver() {
        //        filter to receive SMS
        intentFilter = IntentFilter()
        intentFilter!!.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)

        //        receiver to receive and to get otp from SMS
        chargerReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                    val extras = intent.extras
                    val status = extras!!.get(SmsRetriever.EXTRA_STATUS) as Status?
                    when (status!!.statusCode) {
                        CommonStatusCodes.SUCCESS -> {
                            // Get SMS message contents
                            val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String?
                            // Extract one-time code from the message and complete verification
                            // by sending the code back to your server for SMS authenticity.
                            smsCallback!!.smsCallback(message.toString())
                            stopSmsReciever()
                        }
                        CommonStatusCodes.TIMEOUT ->
                            // Waiting for SMS timed out (5 minutes)
                            smsCallback!!.connectionFailed()
                    }
                }
            }
        }
        appCompatActivity.getApplication().registerReceiver(chargerReceiver, intentFilter)
    }

    fun stopSmsReciever() {
        try {
            appCompatActivity.getApplicationContext().unregisterReceiver(chargerReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

    }

    interface Callback {
        fun connectionFailed(connectionResult: ConnectionResult)
        fun connectionSuspend(i: Int)
        fun connectionSuccess(bundle: Bundle?)
    }

    interface SmsCallback {
        fun connectionFailed()
        fun connectionSuccess(aVoid: Void)
        fun smsCallback(sms: String)
    }

    companion object {
        val RC_HINT = 1000
        fun getHashCode(context: Context): String {
            val appSignature = AppSignatureHelper(context)
            Log.e(" getAppSignatures ", "" + appSignature.appSignatures)
            return appSignature.appSignatures.get(0)

        }
    }


}
