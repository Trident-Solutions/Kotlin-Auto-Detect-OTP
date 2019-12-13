package com.trident.android.autootpdetection.activities
/**
 * This class is used to verify the phone number. If you have two sim it will show a number in dialog box.
 * From that you can pick any one phone number.
 * @param SMSHashString : OKpl0nwrEpF
 */
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.trident.android.autodetectionotp.AppSignatureHelper
import com.trident.android.autodetectionotp.AutoDetectOTP
import com.trident.android.autootpdetection.R
import com.trident.android.autootpdetection.utils.LoggerMessage
import kotlinx.android.synthetic.main.verify_phone_number.*



/**
 * @author Trident_Surya Devi
 */

class VerifyPhoneNumberActivity : AppCompatActivity() {
    internal var autoDetectOTP: AutoDetectOTP? = null
    internal var edtPhoneNumber: AppCompatEditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.verify_phone_number)

        edtPhoneNumber = findViewById(R.id.phone_number_edt)
        autoDetectOTP = AutoDetectOTP(this)
        autoDetectOTP!!.requestPhoneNoHint()
        val appSignatureHelper = AppSignatureHelper(applicationContext)
        appSignatureHelper.appSignatures

        LoggerMessage.LogDebugMsg("HASH STRING", "HASH STRING: " + appSignatureHelper.appSignatures)
        // OKpl0nwrEpF

        txt_next.setOnClickListener(View.OnClickListener {
            if (phone_number_edt.length() == 0) {
                Toast.makeText(this, "Kindly enter your mobile number", Toast.LENGTH_SHORT).show()
            } else if (phone_number_edt.length() != 10) {
                Toast.makeText(this, "Kindly enter valid mobile number", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@VerifyPhoneNumberActivity, OtpActivity::class.java)
                startActivity(intent)
            }

        })
        phone_number_edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (phone_number_edt.length() == 1) {
                    if (phone_number_edt.text.toString().startsWith("6") ||
                        phone_number_edt.text.toString().startsWith("7") ||
                        phone_number_edt.text.toString().startsWith("8") ||
                        phone_number_edt.text.toString().startsWith("9")
                    ) {
                    } else {
                        phone_number_edt.setText("")
                        phone_number_edt.error = "Enter valid mobile number"
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val word = getString(R.string.selected_mobile_number)
                txt_selected_mobile_num.text = "$word ${"+91 "+s.toString()}"
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AutoDetectOTP.RC_HINT) {
            if (resultCode == RESULT_OK) {
                autoDetectOTP!!.getPhoneNo(data!!)
                val new_number = autoDetectOTP!!.getPhoneNo(data).substring(3)
                phone_number_edt.setText(new_number)
            } else {
            }
        }
    }
}
