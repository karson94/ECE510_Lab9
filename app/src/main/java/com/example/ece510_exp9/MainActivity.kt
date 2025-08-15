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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


data class SensorData(
    val tvoc_avg: Int = 0,
    val eco2_avg: Int = 0,
    val sound_level_avg: Double = 0.0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timestamp: String = "Loading..."
)

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
    var sensorData by remember { mutableStateOf(SensorData()) }

    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("sensor_data").child("current").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(SensorData::class.java)
                if (data != null) {
                    sensorData = data
                }
            }

            override fun onCancelled(error: DatabaseError) {
                sensorData = sensorData.copy(timestamp = "Error: ${error.message}")
            }
        })
    }

    val location = LatLng(sensorData.latitude, sensorData.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }

    // Update camera position when location changes
    LaunchedEffect(location) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 15f)
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
                value = "${sensorData.tvoc_avg}",
                icon = Icons.Default.Cloud,
                valueColor = getAirQualityColor(sensorData.tvoc_avg)
            )
            SensorDataItem(
                label = "eCO₂",
                value = "${sensorData.eco2_avg} ppm",
                icon = Icons.Default.CloudQueue,
                valueColor = getAirQualityColor(sensorData.eco2_avg)
            )
            SensorDataItem(
                label = "Sound Value",
                value = "${sensorData.sound_level_avg}",
                icon = Icons.Default.VolumeUp,
                valueColor = MaterialTheme.colorScheme.secondary
            )
            SensorDataItem(
                label = "Timestamp",
                value = sensorData.timestamp,
                icon = Icons.Default.AccessTime,
                valueColor = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (sensorData.latitude != 0.0 && sensorData.longitude != 0.0) {
                GoogleMap(
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = MarkerState(position = location),
                        title = "Sensor Location",
                        snippet = "Lat: ${sensorData.latitude}, Lng: ${sensorData.longitude}"
                    )
                }
            }
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
