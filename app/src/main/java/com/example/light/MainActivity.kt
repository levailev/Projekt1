package com.example.light

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.mikhaellopez.circularprogressbar.CircularProgressBar



class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var brightness: Sensor? = null
    private lateinit var text: TextView
    private lateinit var pb: CircularProgressBar
    private lateinit var layout: RelativeLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        text = findViewById(R.id.tv_text)
        pb = findViewById(R.id.circularProgressBar)
        layout = findViewById(R.id.MyLayout)

        setUpSensor()
    }

    private fun setUpSensor() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    private fun brightness(brightness: Float): String {

        when(brightness.toInt()){
            0 -> layout.setBackgroundColor(Color.BLACK)
            in 1..10 -> layout.setBackgroundColor(Color.DKGRAY)
            in 11..50 -> layout.setBackgroundColor(Color.GRAY)
            in 51..5000 -> layout.setBackgroundColor(Color.LTGRAY)
            in 5001..25000 -> layout.setBackgroundColor(Color.TRANSPARENT)
            else -> layout.setBackgroundColor(Color.WHITE)
        }

        return when (brightness.toInt()) {
            0 -> "Teljes sötétség"
            in 1..10 -> "Sötét"
            in 11..50 -> "Szürkület"
            in 51..5000 -> "Normál"
            in 5001..25000 -> "Világos"
            else -> "Nagyon világos"
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val light1 = event.values[0]

            text.text = "Fény(LUX): $light1\n${brightness(light1)}"
            pb.setProgressWithAnimation(light1)



        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

}