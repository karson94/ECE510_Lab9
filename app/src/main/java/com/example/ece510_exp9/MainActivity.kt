package com.example.ece510_exp9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ece510_exp9.ui.theme.ECE510_exp9Theme
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.ui.graphics.vector.ImageVector


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            ECE510_exp9Theme {
                SensorDataScreen()
            }
        }
    }
}

@Composable
fun SensorDataScreen() {
    var tvoc by remember { mutableStateOf(0) }
    var eco2 by remember { mutableStateOf(0) }
    var soundLevel by remember { mutableStateOf(0) }
    var timestamp by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("sensor_data").child("latest").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tvoc = snapshot.child("tvoc").getValue(Int::class.java) ?: 0
                eco2 = snapshot.child("eco2").getValue(Int::class.java) ?: 0
                soundLevel = snapshot.child("sound_level").getValue(Int::class.java) ?: 0
                timestamp = snapshot.child("timestamp").getValue(String::class.java) ?: "N/A"
            }

            override fun onCancelled(error: DatabaseError) {
                timestamp = "Error: ${error.message}"
            }
        })
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Environmental Sensor Data",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Divider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), thickness = 1.dp)

            SensorDataItem(
                label = "TVOC",
                value = "$tvoc",
                icon = Icons.Default.Cloud,
                valueColor = getAirQualityColor(tvoc)
            )
            SensorDataItem(
                label = "eCO₂",
                value = "$eco2 ppm",
                icon = Icons.Default.CloudQueue,
                valueColor = getAirQualityColor(eco2)
            )
            SensorDataItem(
                label = "Sound Value",
                value = "$soundLevel",
                icon = Icons.Default.VolumeUp,
                valueColor = MaterialTheme.colorScheme.secondary
            )
            SensorDataItem(
                label = "Timestamp",
                value = timestamp,
                icon = Icons.Default.AccessTime,
                valueColor = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun SensorDataItem(label: String, value: String, icon: ImageVector, valueColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label, style = MaterialTheme.typography.bodyLarge)
            }
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = valueColor
            )
        }
    }
}

@Composable
fun getAirQualityColor(value: Int): Color {
    return when {
        value < 400 -> Color(0xFF4CAF50) // Green
        value < 1000 -> Color(0xFFFFC107) // Amber
        else -> Color(0xFFF44336) // Red
    }
}
