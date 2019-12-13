package com.trident.android.autootpdetection.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.trident.android.autodetectionotp.AutoDetectOTP
import com.trident.android.autootpdetection.R
import kotlinx.android.synthetic.main.otp_verification.*
import java.util.regex.Pattern



/**
 *
 * @author Trident_Surya Devi
 */
class OtpActivity : AppCompatActivity() {
    lateinit var autoDetectOTP: AutoDetectOTP
    private var enteredOtp: String? = null
    private var receivedOtp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.otp_verification)
        autoDetectOTP = AutoDetectOTP(this)

        getOtpMessage()

    }

    /**
     * Method is used to set the received otp sms into textview
     * @param receivedOtp
     * @param enteredOtp
     */
    private fun OtpActivity.getOtpMessage() {
        autoDetectOTP.startSmsRetriver(object : AutoDetectOTP.SmsCallback {
            override fun connectionFailed() {
//                Toast.makeText(this@OtpActivity, "Failed", Toast.LENGTH_SHORT).show()
            }

            override fun connectionSuccess(aVoid: Void) {
                Toast.makeText(this@OtpActivity, "Success", Toast.LENGTH_SHORT).show()
            }

            override fun smsCallback(sms: String) {
                if (sms.contains(":") && sms.contains(".")) {
                    receivedOtp =
                        sms.substring(sms.indexOf(":") + 1, sms.indexOf(".")).trim { it <= ' ' }

                    val pattern = Pattern.compile("(\\d{4})")
                    val matcher = pattern.matcher(receivedOtp)
                    if (matcher.find()) {
                        enteredOtp = matcher.group(0)
                        val output = enteredOtp
                        txt_otp_1.text = output!![0].toString()
                        txt_otp_2.text = output[1].toString()
                        txt_otp_3.text = output[2].toString()
                        txt_otp_4.text = output[3].toString()

                        Handler().postDelayed(Runnable {
                           intent= Intent(this@OtpActivity,MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }, 2000)

                    }
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        autoDetectOTP.stopSmsReciever()
    }
}


