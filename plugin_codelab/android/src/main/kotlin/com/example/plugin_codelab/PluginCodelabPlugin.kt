package com.example.plugin_codelab

import androidx.annotation.NonNull


import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import io.flutter.plugin.common.MethodChannel.Result
import com.sun.swing.internal.plaf.synth.resources.synth

import java.util.ArrayList

import jdk.internal.net.http.common.Log.channel
import java.lang.Exception


internal abstract class Synthesizer {
  abstract fun start()
  abstract fun stop()
  abstract fun keyDown(key: Int): Int
  abstract fun keyUp(key: Int): Int
}
/** PluginCodelabPlugin */
class PluginCodelabPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private var channel: MethodChannel? = null
  private var synth: Synth? = null
  private val channelName = "plugin_codelab"

  private fun setup(plugin: PluginCodelabPlugin, binaryMessenger: BinaryMessenger) {
    plugin.channel = MethodChannel(binaryMessenger, channelName)
    plugin.channel.setMethodCallHandler(plugin)
    plugin.synth = Synth()
    plugin.synth.start()
  }

  fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPluginBinding) {
    setup(this, flutterPluginBinding.getBinaryMessenger())
  }

  fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE)
    } else if (call.method.equals("onKeyDown")) {
      try {
        val arguments = call.arguments as ArrayList<*>
        val numKeysDown: Int = synth.keyDown(arguments[0] as Int)
        result.success(numKeysDown)
      } catch (ex: Exception) {
        result.error("1", ex.message, ex.stackTrace)
      }
    } else if (call.method.equals("onKeyUp")) {
      try {
        val arguments = call.arguments as ArrayList<*>
        val numKeysDown: Int = synth.keyUp(arguments[0] as Int)
        result.success(numKeysDown)
      } catch (ex: Exception) {
        result.error("1", ex.message, ex.stackTrace)
      }
    } else {
      result.notImplemented()
    }
  }
}
