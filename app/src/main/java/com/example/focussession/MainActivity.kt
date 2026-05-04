package com.example.focussession

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.focussession.ui.theme.FocusSessionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel : MainViewModel by viewModels()
        enableEdgeToEdge()
        setContent {
            FocusSessionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Home(
                        viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Home(viewModel: MainViewModel, modifier: Modifier) {

    var duration by remember { mutableStateOf ("") }

    val state = viewModel.timer.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }

    var startTimer by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Column(Modifier.fillMaxSize().background(Color.White), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        if (!startTimer) {
            Text("Time: ${state.value}")
        }
        Text("Please enter a duration time", Modifier.padding(top = 16.dp))

        TextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Please enter a duration for this task") },
            modifier = Modifier.padding(vertical = 13.dp)
        )

        Button(onClick = {
            if (startTimer) {
                viewModel.startTimer(duration.toInt())
                startTimer = false
                duration = ""
            } else {
                showDialog = true
            }
        }, modifier) {
            if (startTimer) {
                Text("Start Timer")
            } else {
                Text("Quit")
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {  showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        duration = ""
                        startTimer = true
                        showDialog = false
                        viewModel.resetTimer()
                    }) { Text("Confirm") }

                },
                title = { Text("Are you sure?") },
                text = { Text("Are you sure you want to quit this session? Your progress will be lost.") },
            )
        }

        Button(onClick = {
            if (startTimer) {
                Toast.makeText(context, "You have navigated to the second screen", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "You must finish the current session first", Toast.LENGTH_SHORT).show()
            }
        }, modifier) {
            Text("Start Next Session")
        }


    }
}