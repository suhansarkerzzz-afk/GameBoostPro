package com.gameboost.pro

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

private val Bg = Color(0xFF0B0F14)
private val Card = Color(0xFF151C24)
private val Green = Color(0xFF38E58C)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { Theme { Dashboard() } } }
    @Composable private fun Theme(content: @Composable () -> Unit) { MaterialTheme(colorScheme = darkColorScheme(background = Bg, surface = Card, primary = Green), content = content) }
    @Composable private fun Dashboard(vm: GameBoostViewModel = viewModel()) {
        val s by vm.stats.collectAsState(); val boost by vm.boost.collectAsState()
        LazyColumn(Modifier.fillMaxSize().background(Bg).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Text("GAMEBOOST PRO", style = MaterialTheme.typography.headlineLarge, color = Green); Text("Performance Center v2.0", color = Color.LightGray) }
            item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) { Stat("RAM", "${s.ramUsedMb} MB", Modifier.weight(1f)); Stat("Battery", "${s.battery}%", Modifier.weight(1f)) } }
            item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) { Stat("Temp", "${s.batteryTemp}°C", Modifier.weight(1f)); Stat("Thermal", thermal(s.thermalStatus), Modifier.weight(1f)) } }
            item { Button(onClick = vm::toggleBoost, Modifier.fillMaxWidth()) { Text(if (boost) "GAME BOOST ENABLED" else "ENABLE GAME BOOST") } }
            item { OutlinedButton(onClick = { openOverlay() }, Modifier.fillMaxWidth()) { Text("FPS Overlay") } }
            item { OutlinedButton(onClick = { startActivity(Intent(Settings.ACTION_SOUND_SETTINGS)) }, Modifier.fillMaxWidth()) { Text("Volume Control") } }
            item { OutlinedButton(onClick = { startActivity(Intent(Settings.ACTION_MEMORY_SETTINGS)) }, Modifier.fillMaxWidth()) { Text("Memory Settings") } }
        }
    }
    @Composable private fun Stat(label: String, value: String, modifier: Modifier) { Card(modifier) { Column(Modifier.padding(16.dp)) { Text(label, color = Color.LightGray); Text(value, color = Green, style = MaterialTheme.typography.titleLarge) } } }
    private fun thermal(v: Int) = when (v) { 0 -> "None"; 1 -> "Light"; 2 -> "Moderate"; 3 -> "Severe"; 4 -> "Critical"; 5 -> "Emergency"; 6 -> "Shutdown"; else -> "Unknown" }
    private fun openOverlay() { if (!Settings.canDrawOverlays(this)) startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))) else startService(Intent(this, FpsOverlayService::class.java)) }
}
