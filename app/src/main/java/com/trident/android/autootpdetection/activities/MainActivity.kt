package com.trident.android.autootpdetection.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.trident.android.autootpdetection.R
import kotlinx.android.synthetic.main.activity_main.*
import android.telephony.TelephonyManager
import android.content.Context
import android.view.WindowManager

/**
 * @author Trident_Surya Devi
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)


        if (isSimAvailable()) {
            txt_getStarted.setOnClickListener(View.OnClickListener {
                intent = Intent(this, VerifyPhoneNumberActivity::class.java)
                startActivity(intent)
            })
        } else {
            txt_getStarted.setOnClickListener(View.OnClickListener {
                intent = Intent(this, SimStateActivity::class.java)
                startActivity(intent)
            })
        }
    }

    /**
     * Method is used to identify the sim state in phone
     */
    private fun isSimAvailable(): Boolean {
        var isAvailable = false
        val telMgr = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simState = telMgr.simState
        when (simState) {
            TelephonyManager.SIM_STATE_ABSENT //SimState = “No Sim Found!”;
            -> {
            }
            TelephonyManager.SIM_STATE_NETWORK_LOCKED //SimState = “Network Locked!”;
            -> {
            }
            TelephonyManager.SIM_STATE_PIN_REQUIRED //SimState = “PIN Required to access SIM!”;
            -> {
            }
            TelephonyManager.SIM_STATE_PUK_REQUIRED //SimState = “PUK Required to access SIM!”; // Personal Unblocking Code
            -> {
            }
            TelephonyManager.SIM_STATE_READY -> isAvailable = true
            TelephonyManager.SIM_STATE_UNKNOWN //SimState = “Unknown SIM State!”;
            -> {
            }
        }
        return isAvailable
    }

}