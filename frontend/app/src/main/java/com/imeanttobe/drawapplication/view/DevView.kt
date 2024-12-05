package com.imeanttobe.drawapplication.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.imeanttobe.drawapplication.data.model.Post
import com.imeanttobe.drawapplication.data.navigation.NavItem

@Composable
fun DevView(
    modifier: Modifier = Modifier,
    navigateTo: (String) -> Unit
) {
    val context = LocalContext.current

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
                onClick = { addNewPost(context = context) }
            ) {
                Text(text = "Add new post")
            }
        }
    }
}

fun addNewPost(context: Context) {
    val postReferenceName = "post"
    val user = FirebaseAuth.getInstance().currentUser ?: return
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val key = firebaseDatabase.getReference(postReferenceName).push().key
    val post = Post(
        id = key!!,
        userId = user.uid,
        description = "This is a description. If it is too long, text will be truncated and it will be shown with 3-dots. It is a long description.",
        imageUri = ""
    )

    firebaseDatabase
        .getReference(postReferenceName)
        .child(key)
        .setValue(post)
        .addOnSuccessListener {
            Toast.makeText(context, "Post added successfully", Toast.LENGTH_SHORT).show()
        }
}