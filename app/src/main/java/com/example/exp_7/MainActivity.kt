package com.example.exp_7

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnGetInfo: Button
    private lateinit var txtInfo: TextView

    private val REQUEST_PHONE_STATE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGetInfo = findViewById(R.id.btnGetInfo)
        txtInfo = findViewById(R.id.txtInfo)

        btnGetInfo.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), REQUEST_PHONE_STATE)
            } else {
                displayTelephonyInfo()
            }
        }
    }

    private fun displayTelephonyInfo() {
        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val builder = StringBuilder()

        builder.append("Network Operator Name: ${telephonyManager.networkOperatorName}\n")
        builder.append("SIM Operator Name: ${telephonyManager.simOperatorName}\n")
        builder.append("SIM Country ISO: ${telephonyManager.simCountryIso}\n")

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.append("IMEI: ${telephonyManager.imei}\n")
            } else {
                builder.append("Device ID: ${telephonyManager.deviceId}\n")
            }
            builder.append("Phone Number: ${telephonyManager.line1Number}\n")
        }

        txtInfo.text = builder.toString()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PHONE_STATE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayTelephonyInfo()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
