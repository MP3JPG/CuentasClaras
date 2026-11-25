package com.cuentasclaras.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cuentasclaras.app.presentation.navigation.AppNavGraph
import com.cuentasclaras.app.ui.theme.CuentasClarasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CuentasClarasTheme {
                AppNavGraph()
            }
        }
    }
}