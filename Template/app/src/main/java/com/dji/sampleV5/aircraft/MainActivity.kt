package com.dji.sampleV5.aircraft

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dji.v5.manager.SDKManager
import dji.v5.manager.aircraft.flightcontrol.FlightControlManager
import dji.v5.common.callback.CommonCallbacks
import dji.v5.common.error.IDJIError

class MainActivity : AppCompatActivity() {
    private lateinit var statusText: TextView
    private lateinit var connectButton: Button
    private lateinit var takeoffButton: Button
    private lateinit var landButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        initUI()

        // Set up button listeners
        setupButtonListeners()

        // Update status
        updateStatus()
    }

    private fun initUI() {
        statusText = findViewById(R.id.statusText)
        connectButton = findViewById(R.id.connectButton)
        takeoffButton = findViewById(R.id.takeoffButton)
        landButton = findViewById(R.id.landButton)
    }

    private fun setupButtonListeners() {
        connectButton.setOnClickListener { checkConnection() }
        takeoffButton.setOnClickListener { takeoff() }
        landButton.setOnClickListener { land() }
    }

    private fun checkConnection() {
        val isConnected = SDKManager.getInstance().getProduct() != null
        val status = if (isConnected) "Connected to Drone" else "Not Connected"
        statusText.text = status
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show()

        // Enable/disable buttons based on connection
        takeoffButton.isEnabled = isConnected
        landButton.isEnabled = isConnected
    }

    private fun takeoff() {
        val flightControlManager = FlightControlManager.getInstance()
        flightControlManager.startTakeoff(object : CommonCallbacks.CompletionCallback {
            override fun onSuccess() {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Takeoff successful", Toast.LENGTH_SHORT).show()
                    updateStatus()
                }
            }

            override fun onFailure(error: IDJIError) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Takeoff failed: ${error.description()}", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun land() {
        val flightControlManager = FlightControlManager.getInstance()
        flightControlManager.startLanding(object : CommonCallbacks.CompletionCallback {
            override fun onSuccess() {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Landing successful", Toast.LENGTH_SHORT).show()
                    updateStatus()
                }
            }

            override fun onFailure(error: IDJIError) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Landing failed: ${error.description()}", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun updateStatus() {
        runOnUiThread {
            val product = SDKManager.getInstance().getProduct()
            val isConnected = product != null

            val statusMessage = when {
                isConnected -> "Connected - ${product?.productType?.name ?: "Unknown"}"
                else -> "Disconnected"
            }

            statusText.text = statusMessage

            // Update button states
            takeoffButton.isEnabled = isConnected
            landButton.isEnabled = isConnected
        }
    }
}