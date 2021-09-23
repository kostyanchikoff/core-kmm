package com.kostyanchikoff.core.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import com.kostyanchikoff.core.android.githubUsers.widgets.AppNavController
import com.kostyanchikoff.core.android.ui.theme.CoreTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoreTheme {
                Scaffold(
                    scaffoldState = rememberScaffoldState()
                ) {
                    AppNavController()
                }
            }
        }
    }
}


