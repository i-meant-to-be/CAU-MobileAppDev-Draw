package com.imeanttobe.drawapplication.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imeanttobe.drawapplication.data.navigation.NavItem

@Composable
fun DevView(
    modifier: Modifier = Modifier,
    navigateTo: (String) -> Unit
) {
    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            for (item in NavItem.items) {
                Button(
                    onClick = { navigateTo(item.route) }
                ) {
                    Text(text = "Navigate to ${item.label}")
                }
            }

            Button(
                onClick = {  }
            ) {
                Text(text = "Add new post")
            }
        }
    }
}