package com.example.composetutorial

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.compose.runtime.Composable

class SensorEvent(private val context: Context) : Activity(), SensorEventListener {

    private val obj = CreateNotification(context)
    private lateinit var sensorManager: SensorManager
    private var mTemp: Sensor? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mTemp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

    override fun onSensorChanged(event: SensorEvent) {
        val celcius = event.values[0]
            if (celcius >= 32) {

            }
        }

    override fun onResume() {
        super.onResume()
        mTemp?.also {
            temp -> sensorManager.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onStart() {
        super.onStart()
    }
    }
