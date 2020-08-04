package com.example.sendbird

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import com.sendbird.calls.*
import com.sendbird.calls.handler.AuthenticateHandler
import com.sendbird.calls.handler.DialHandler
import com.sendbird.calls.handler.DirectCallListener
import com.sendbird.calls.handler.SendBirdCallListener
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import kotlin.math.log
import io.flutter.embedding.android.FlutterView;


class MainActivity: FlutterActivity() {
//    val CHANNEL = "com.example.sendbird/sendbird"
    private val CHANNEL = "samples.flutter.dev/battery"
    val APP_ID = "BD71E5CF-3DBF-4E07-9478-E0800950781F"
    val API_TOKENS = "e0afafb6dfa48c82e8516838bde9e41eef37c5bd"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
                .setMethodCallHandler { call, result ->
                    if(call.method.equals("sendBird")){
//                        val value = init(call.argument("APP_ID"))
//                        result.success(value);
//                        authenticate()
                        val intent = Intent(applicationContext, CameraActivity::class.java)
                        startActivity(intent)
                    }else if(call.method.equals("getBatteryLevel")){
                        val batteryLevel = getBatteryLevel()

                        if (batteryLevel != -1) {
                            result.success(batteryLevel)
                        } else {
                            result.error("UNAVAILABLE", "Battery level not available.", null)
                        }

                    } else{
                        result.notImplemented()
                    }
                }
    }

    private fun getBatteryLevel(): Int {
        var batteryLevel = -1
        batteryLevel = if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 /
                    intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        }
        return batteryLevel
    }

    fun init(APP_ID: String?) : String {
        return APP_ID!!;
//        SendBirdCall.init(applicationContext, APP_ID)
    }

    fun authenticate(USER_ID: String, ACCESS_TOKEN: String, PUSH_TOKEN: String, isUnique: Boolean){
        val params = AuthenticateParams(USER_ID)
                .setAccessToken(ACCESS_TOKEN)
                .setPushToken(PUSH_TOKEN, isUnique)

        SendBirdCall.authenticate(params, object : AuthenticateHandler{
            override fun onResult(p0: User?, p1: SendBirdException?) {
                if (p1 != null){
                }
            }

        })
    }

    fun listener(UNIQUE_HANDLER_ID: String){
        SendBirdCall.addListener(UNIQUE_HANDLER_ID, object : SendBirdCallListener() {
            override fun onRinging(p0: DirectCall?) {
                TODO("Not yet implemented")
            }
        })
    }

    fun call(CALLER_ID: String){
        val params = DialParams(CALLER_ID)
        params.setVideoCall(true)
        params.setCallOptions(CallOptions())

        val call = SendBirdCall.dial(params, object : DialHandler {
            override fun onResult(p0: DirectCall?, p1: SendBirdException?) {

            }
        })

        call.setListener(object: DirectCallListener(){
            override fun onEnded(p0: DirectCall?) {
                TODO("Not yet implemented")
            }

            override fun onConnected(p0: DirectCall?) {
                TODO("Not yet implemented")
            }

            override fun onEstablished(call: DirectCall?) {
                super.onEstablished(call)
            }
        })
    }



}
