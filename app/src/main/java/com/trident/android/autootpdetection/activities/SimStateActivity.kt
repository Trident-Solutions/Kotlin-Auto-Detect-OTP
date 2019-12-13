package com.trident.android.autootpdetection.activities


import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.trident.android.autootpdetection.R
import kotlinx.android.synthetic.main.activity_connection_lost.*

/**
 * @author Trident_Surya Devi
 */
class SimStateActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_connection_lost)

        txt_try_again.setOnClickListener(View.OnClickListener {
            Toast.makeText(this,R.string.connection_lost,Toast.LENGTH_SHORT).show()
            finish()
        })
    }
}