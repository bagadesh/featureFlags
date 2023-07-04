package com.bagadesh.featureflags

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.featureflags.data.FeatureFlagSdkImpl
import com.bagadesh.featureflags.FeatureFlagTester.TestState.*
import com.bagadesh.featureflags.ui.theme.FeatureFlagsTheme
import com.featureflags.ui.screen.FeatureFlagScreenUi
import com.featureflags.ui.vm.FeatureFlagViewModel

class MainActivity : ComponentActivity() {

    private lateinit var featureFlagTester: FeatureFlagTester

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeatureFlagSdkImpl.init(
            applicationContext,
            defaultConfigResource = com.featureflags.ui.R.raw.feature_flag,
        )
        val vm = FeatureFlagViewModel(reader = FeatureFlagSdkImpl.getReader(), writer = FeatureFlagSdkImpl.getWriter())
        featureFlagTester = FeatureFlagTester()
        setContent {
            val navController = rememberNavController()
            FeatureFlagsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    NavHost(navController = navController, startDestination = "home") {
                        composable(route = "home") {
                            val state by featureFlagTester.state.collectAsState(initial = Loading(""))
                            Column(
                                modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(20.dp)
                                        .animateContentSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    when (state) {
                                        is Failed -> {
                                            Text(
                                                text = (state as Failed).message, modifier = Modifier.fillMaxWidth(),
                                                textAlign = TextAlign.Center
                                            )
                                        }

                                        is Loading -> {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(20.dp),
                                                    strokeWidth = 2.dp
                                                )
                                                Spacer(modifier = Modifier.size(10.dp))
                                                Text(
                                                    text = (state as Loading).message, modifier = Modifier.fillMaxWidth(),
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }

                                        is Success -> {
                                            Text(
                                                text = (state as Success).message, modifier = Modifier.fillMaxWidth(),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                                Button(
                                    onClick = {
                                        navController.navigate("featureFlags")
                                    },
                                    shape = RoundedCornerShape(15.dp),
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                ) {
                                    Text(text = "Show Feature Flag Editor")
                                }
                            }
                        }
                        composable(route = "featureFlags") {
                            FeatureFlagScreenUi(viewModel = vm)
                        }
                    }
                }
            }
        }
    }
}