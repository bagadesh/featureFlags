package com.bagadesh.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bagadesh.ui.entity.UiFeature
import com.bagadesh.ui.entity.UiVariable
import com.bagadesh.ui.vm.FeatureFlagViewModel
import kotlin.random.Random

/**
 * Created by bagadesh on 26/06/23.
 */

val enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?
    get() = {
        slideInHorizontally(animationSpec = tween(500), initialOffsetX = { it })
    }

val exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?
    get() = {
        slideOutHorizontally(animationSpec = tween(500), targetOffsetX = { it })
    }

@Composable
fun FeatureFlagScreenUi(viewModel: FeatureFlagViewModel) {
    val features by viewModel.features.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable(
            route = "home",
        ) {
            FeatureFlagScreen(
                features = features,
                onChange = viewModel::onChange,
                onClick = { feature ->
                    navController.navigate(route = "variableScreen/${feature.key}")
                }
            )
        }
        composable(
            route = "variableScreen/{featureKey}", arguments = listOf(navArgument(name = "featureKey") { type = NavType.StringType }),
            enterTransition = enterTransition,
            popExitTransition = exitTransition,
        ) {
            val featureKey = it.arguments?.getString("featureKey").orEmpty()
            val feature = remember { features.find { it.key == featureKey } }
            BackHandler {
                navController.popBackStack()
            }
            if (feature == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No Feature Available")
                }
            } else {
                VariableScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background),
                    variables = feature.variables,
                    feature = feature,
                    onVariableValueChange = viewModel::onVariableValueChange,
                    errorMap = errorState[featureKey]?.variables ?: emptyMap()
                )
            }
        }
    }

}

@Composable
internal fun FeatureFlagScreen(
    modifier: Modifier = Modifier,
    features: List<UiFeature>,
    onChange: (String, Boolean) -> Unit,
    onClick: (UiFeature) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(features, { it.key }) { feature ->
            FeatureUi(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .padding(5.dp)
                    .clickable { onClick(feature) },
                name = feature.key,
                isEnabled = feature.isEnabled,
                description = feature.description,
                onChange = {
                    onChange(feature.key, it)
                }
            )
        }
    }
}


@Preview
@Composable
internal fun FeatureFlagScreenPreview() {
    FeatureFlagScreen(
        modifier = Modifier,
        features = mutableListOf<UiFeature>().apply {
            repeat(30) {
                add(
                    UiFeature(
                        key = "Random $it", isEnabled = Random.nextBoolean(),
                        variables = mutableListOf<UiVariable>().also { uiVariables ->
                            repeat(5) { variableId ->
                                uiVariables.add(UiVariable(key = "Variable $variableId", value = "Value $variableId", "string"))
                            }
                        },
                        description = "This particular feature flag is useful to enable the V2 version of the API"
                    )
                )
            }
        },
        onChange = { _, _ -> },
        onClick = {}
    )
}