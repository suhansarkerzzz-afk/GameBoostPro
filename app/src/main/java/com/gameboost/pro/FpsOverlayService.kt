package com.gameboost.pro

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView

class FpsOverlayService : Service() {
    private lateinit var wm: WindowManager
    private lateinit var label: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var ticks = 0
    private var last = 0L
    private val loop = object : Runnable {
        override fun run() {
            ticks++
            val now = System.currentTimeMillis()
            if (last == 0L) last = now
            if (now - last >= 1000) { label.text = "FPS: $ticks"; ticks = 0; last = now }
            handler.postDelayed(this, 16)
        }
    }
    override fun onCreate() {
        super.onCreate()
        if (!Settings.canDrawOverlays(this)) { stopSelf(); return }
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        label = TextView(this).apply { text = "FPS: --"; textSize = 16f; setTextColor(Color.GREEN); setBackgroundColor(Color.argb(190,0,0,0)); setPadding(16,8,16,8) }
        val type = if (android.os.Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE
        val p = WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, type, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT)
        p.gravity = Gravity.TOP or Gravity.START; p.x = 20; p.y = 100
        wm.addView(label, p); handler.post(loop)
    }
    override fun onDestroy() { handler.removeCallbacks(loop); if (::label.isInitialized) wm.removeView(label); super.onDestroy() }
    override fun onBind(intent: Intent?): IBinder? = null
}
