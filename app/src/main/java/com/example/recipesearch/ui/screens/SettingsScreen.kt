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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
            Row {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
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
                DropdownSetting()
            }
        }
        item {
            Row {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
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
                DropdownSetting()
            }
        }
        item {
            Row {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
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
                DropdownSetting()
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
fun DropdownSetting() {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
    val items = listOf("5", "10", "20", "50", "All")

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .wrapContentSize(Alignment.Center)
            .clickable { expanded = true }
    ) {
        Row(
            modifier = Modifier
                .width(60.dp)
                .padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = items[selectedIndex]
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Drop down menu",
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 8.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEachIndexed { index, option ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
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