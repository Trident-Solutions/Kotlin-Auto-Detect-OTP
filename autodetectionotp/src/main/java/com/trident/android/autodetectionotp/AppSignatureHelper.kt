package com.trident.android.autodetectionotp

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import android.util.Log

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.ArrayList
import java.util.Arrays

/**
 * @author Trident_Surya Devi
 */
class AppSignatureHelper(context: Context) : ContextWrapper(context) {

    /**
     * Get all the app signatures for the current package
     */
    // Get all package signatures for the current package
    // For each signature create a compatible hash
    public val appSignatures: ArrayList<String>
        @SuppressLint("PackageManagerGetSignatures")
        get() {
            val appCodes = ArrayList<String>()

            try {
                val packageName = packageName
                val packageManager = packageManager
                val signatures = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                ).signatures
                for (signature in signatures) {
                    val hash = hash(packageName, signature.toCharsString())
                    if (hash != null) {
                        appCodes.add(String.format("%s", hash))
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e(TAG, "Unable to find package to obtain hash.", e)
            }

            return appCodes
        }

    companion object {
        val TAG = AppSignatureHelper::class.java.simpleName

        private val HASH_TYPE = "SHA-256"
        val NUM_HASHED_BYTES = 9
        val NUM_BASE64_CHAR = 11

        @SuppressLint("ObsoleteSdkInt")
        private fun hash(packageName: String, signature: String): String? {
            val appInfo = "$packageName $signature"
            try {
                val messageDigest = MessageDigest.getInstance(HASH_TYPE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
                } else {
                    messageDigest.update(appInfo.toByteArray(Charset.forName("UTF-8")))
                }
                var hashSignature = messageDigest.digest()

                // truncated into NUM_HASHED_BYTES
                hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)
                // encode into Base64
                var base64Hash =
                    Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
                base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)

                Log.e(TAG, String.format("pkg: %s -- hash: %s", packageName, base64Hash))
                return base64Hash
            } catch (e: NoSuchAlgorithmException) {
                Log.e(TAG, "hash:NoSuchAlgorithm", e)
            }

            return null
        }
    }
}
