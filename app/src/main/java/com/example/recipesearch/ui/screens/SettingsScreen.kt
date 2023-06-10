package com.example.recipesearch.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipesearch.ui.viewmodels.SharedViewModel

@Composable
fun SettingsScreen(
    viewModel: SharedViewModel,
    navController: NavController
) {

//    val queryState by viewModel.queryState.observeAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .width(230.dp)
                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = "Recipes per page",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 18.sp,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Limit the number of recipes shown on a single page",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                DropdownSetting(
                    viewModel = viewModel,
                    settingKey = "results_per_page",
                    options = listOf("5", "10", "20", "40")
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .width(230.dp)
                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = "Maximum search results",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 18.sp,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Limit the number of results from a recipe search",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                DropdownSetting(
                    viewModel = viewModel,
                    settingKey = "max_results",
                    options = listOf("5", "10", "20", "40")
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .width(230.dp)
                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = "Ingredient measurement units",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 18.sp,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Show measurement units in metric or imperial units",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                DropdownSetting(
                    viewModel = viewModel,
                    modifier = Modifier
                        .width(100.dp),
                    settingKey = "units",
                    options = listOf("Metric", "Imperial")
                )
            }
        }
//        item {
//            SettingSwitch(
//                title = "Enable Dark Mode",
//                checked = isDarkTheme.value,
//                onCheckedChange = { isDarkTheme.value = it }
//            )
//        }
//        item {
//            SettingSlider(
//                title = "Volume",
//                sliderValue = volume.value,
//                onValueChange = { volume.value = it }
//            )
//        }
//        // More settings items...
    }
}

@Composable
fun DropdownSetting(
    viewModel: SharedViewModel,
    modifier: Modifier = Modifier,
    settingKey: String,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedSetting by remember { mutableStateOf(
        viewModel.getSettingByKey(settingKey).settingValue
    ) }

    fun onSettingChanged(option: String) {
        viewModel.updateSetting(
            settingKey = settingKey,
            newSettingValue = option
        )
        selectedSetting = option
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .wrapContentSize(Alignment.Center)
            .clickable { expanded = true }
    ) {
        Row(
            modifier = modifier
                .width(60.dp)
                .padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedSetting
            )

            val iconModifier = if (expanded) {
                Modifier.scale(
                    scaleX = 1f,
                    scaleY = -1f
                )
            } else {
                Modifier
            }
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Drop down menu",
                tint = Color.Black,
                modifier = iconModifier
                    .size(24.dp)
                    .padding(start = 8.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onSettingChanged(
                            option = option
                        )
                        expanded = false
                    },
                    text = {
                        Text(text = option)
                    }
                )
            }
        }
    }
}