package com.example.composetutorial

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import com.example.composetutorial.UserDatabase
import com.example.composetutorial.databaseEntity.User
import kotlinx.serialization.Serializable
import java.util.jar.Manifest

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var mTemp: Sensor? = null

    private lateinit var obj: CreateNotification

    override fun onCreate(savedInstanceState: Bundle?) {
        //splash screen
        installSplashScreen()
        super.onCreate(savedInstanceState)
        //ask permission for camera
        if(!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, Camera_permission, 0
            )
        }
        //notification stuff
        obj = CreateNotification(applicationContext)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mTemp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        enableEdgeToEdge()
        setContent {

            CreateNotificationChannel()
            val db = Room.databaseBuilder(
                applicationContext,
                UserDatabase::class.java, "profile-database"
            ).allowMainThreadQueries().build()
            if (db.userDao().getAll().isNullOrEmpty()) {
                db.userDao().insertUser(User("default_name", "empty"))
            }
            NavigationSystem(db)
        }
    }

    @Composable
    fun NavigationSystem(db: UserDatabase) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "Main_Screen", builder = {
            composable("Main_Screen") {
                MainScreen(navController, db)
            }
            composable("Conversation_Screen") {
                ConversationScreen(navController, db)
            }
            composable("Camera_Screen") {
                CameraScreen(applicationContext, navController)
            }
        })
    }

    @Composable
    private fun CreateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NotificationConstants.channel_name
            val descriptionText = NotificationConstants.description
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(NotificationConstants.CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        else {
            print("joitain")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        if (accuracy > 3) {
            Log.d("H", "E")
        }
    }

    override fun onSensorChanged(event: android.hardware.SensorEvent?) {
        val celcius = event?.values[0]
        if (celcius != null) {
            if (celcius >= 32) {
                obj.ShowNotification(celcius.toInt())
            }
        }
    }
    override fun onResume() {
        super.onResume()
        mTemp?.also {
                temp -> sensorManager.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return Camera_permission.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val Camera_permission = arrayOf(
            android.Manifest.permission.CAMERA
        )
    }
}







