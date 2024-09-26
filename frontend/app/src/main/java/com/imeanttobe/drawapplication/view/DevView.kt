package com.imeanttobe.drawapplication.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imeanttobe.drawapplication.data.NavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateTo: (String) -> Unit
) {
    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(NavItem.items.subList(1, 6)) { item ->
                Button(
                    onClick = { navigateTo(item.route) },
                    modifier = Modifier.padding(bottom = 5.dp)
                ) {
                    Text(text = "Navigate to ${item.label}")
                }
            }
        }
    }
}