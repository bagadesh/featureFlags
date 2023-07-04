package com.featureflags.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created by bagadesh on 28/06/23.
 */

@Composable
internal fun FeatureUi(
    modifier: Modifier = Modifier,
    name: String,
    description: String = "",
    isEnabled: Boolean,
    onChange: (Boolean) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(.8f)
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }
            //Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked = isEnabled, onCheckedChange = onChange,
                modifier = Modifier
                    .weight(.2f)
                    .padding(end = 10.dp)
                    .wrapContentSize()
            )
        }
    }
}

@Preview
@Composable
fun FeatureUiPreview() {
    FeatureUi(
        modifier = Modifier.fillMaxWidth(),
        name = "Feature Name",
        description = "This particular feature flag is useful to enable the V2 version of the API",
        isEnabled = true,
        onChange = {}
    )
}