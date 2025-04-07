package com.example.mario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mario.ui.theme.MarioTheme

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp



class MainActivity : ComponentActivity()
{



    override fun onCreate(savedInstanceState: Bundle?) 
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarioTheme {
                LogScreen() // Mostra la schermata dei log
            }
        }
    }

    // override fun onCreate(savedInstanceState: Bundle?)
    // {
    //     super.onCreate(savedInstanceState)
    //     enableEdgeToEdge()
    //     setContent {
    //         MarioTheme {
    //             Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    //                 Greeting(
    //                     name = "Android",
    //                     modifier = Modifier.padding(innerPadding)
    //                 )
    //             }
    //         }
    //     }
    // }

}



// @Composable
// fun LogScreen() 
// {
//     val logMessages = remember { mutableStateListOf<String>() }
//     val maxLogSize = 100 // Numero massimo di messaggi da visualizzare

//     // Colleziona i nuovi messaggi di log e aggiorna lo stato
//     val newLog = LogChannel.logFlow.collectAsState(initial = "")
//     LaunchedEffect(newLog.value) 
//     {
//         if (newLog.value.isNotEmpty()) 
//         {
//             logMessages.add(newLog.value)
//             if (logMessages.size > maxLogSize) 
//             {
//                 // Rimuovi il primo elemento (il più vecchio) se la lista supera la dimensione massima
//                 logMessages.removeAt(0)
//             }
//         }
//     }

//     Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//         LazyColumn(
//             modifier = Modifier
//                 .padding(innerPadding)
//                 .padding(16.dp)
//         ) {
//             Text(text = "Log Messaggi:", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)
//             logMessages.forEach { message ->
//                 Text(text = message)
//             }
//     }
// }



@Composable
fun LogScreen() {
    val logMessages = remember { mutableStateListOf<String>() }

    // Colleziona i nuovi messaggi di log e aggiorna lo stato
    val newLog = LogChannel.logFlow.collectAsState(initial = "")
    LaunchedEffect(newLog.value) {
        if (newLog.value.isNotEmpty()) {
            logMessages.add(newLog.value)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp) // Aggiungi un po' di padding intorno alla lista
        ) {
            Text(text = "Log Messaggi:", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)
            logMessages.forEach { message ->
                Text(text = message)
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier)
{
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() 
{
    MarioTheme {
        Greeting("Android")
    }
}