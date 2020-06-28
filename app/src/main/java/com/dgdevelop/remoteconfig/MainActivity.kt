package com.dgdevelop.remoteconfig

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig = Firebase.remoteConfig

       val remoteConfigSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 4200
        }

        /*val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .setMinimumFetchIntervalInSeconds(4200)
                .build()
        remoteConfig.setConfigSettings(configSettings)*/

       /* val remoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(4200)
                .build()*/

        remoteConfig.setConfigSettingsAsync(remoteConfigSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        //lyMain.setBackgroundColor(Color.parseColor(remoteConfig[BACKGROUND_COLOR].asString()))
        setConfigurationView()
    }

    private fun setConfigurationView(){
        lyMain.setBackgroundColor(Color.parseColor(remoteConfig[BACKGROUND_COLOR].asString()))
        Log.d(TAG, "Image Name: ${remoteConfig[BACKGROUND_IMAGE].asString()}")
        Log.d(TAG, "Image Name: ${remoteConfig[BACKGROUND_COLOR].asString()}")
        when {
            remoteConfig[BACKGROUND_IMAGE].asString() == "happyface" -> {
                ivLogo.setImageResource(R.drawable.happyface)
            }
            remoteConfig[BACKGROUND_IMAGE].asString() == "pikachuchistmas" -> {
                ivLogo.setImageResource(R.drawable.pikachuchistmas)
            }
            remoteConfig[BACKGROUND_IMAGE].asString() == "valentinesday" -> {
                ivLogo.setImageResource(R.drawable.valentinesday)
            }
        }
    }

    fun synchronizeData(view: View){
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Synchronize Done", Toast.LENGTH_SHORT).show()
                        val updated = task.result
                        Log.d(TAG, "Config params updated: $updated")
                    }else{
                        Toast.makeText(this, "Synchronize Failed", Toast.LENGTH_SHORT).show()
                    }
                    setConfigurationView()
                }
    }

    companion object{
        private const val BACKGROUND_COLOR = "color_background"
        private const val BACKGROUND_IMAGE = "image_background"
        private const val TAG = "MainActivity"
    }
}