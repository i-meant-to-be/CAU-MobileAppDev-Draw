package com.imeanttobe.drawapplication.view.bottomnav

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.data.model.ImageItem
import com.imeanttobe.drawapplication.data.model.Post
import com.imeanttobe.drawapplication.viewmodel.ExploreViewModel

@Composable
fun ExploreView(
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ExploreViewSearchBox(
                searchText = viewModel.searchText,
                onSearchTextChange = { newValue -> viewModel.onSearchTextChanged(newValue) }
            )
            ExploreViewGrid()
        }
    }
}

@Composable
fun ExploreViewSearchBox(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        ExploreViewSearchBoxTextField(
            text = searchText,
            onValueChange = onSearchTextChange
        )
    }
}

@Composable
fun ExploreViewGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
    ) {
        items(10) {
            ExploreViewGridItem(
                post = Post(userId = 0, description = ""),
                image = ImageItem(postId = 0, imageUrl = "")
            )
        }
    }
}

@Composable
fun ExploreViewSearchBoxTextField(
    text: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
        value = text,
        onValueChange = onValueChange,
        singleLine = true,
        maxLines = 1,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
        decorationBox = @Composable { innerTextField ->
            Column(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(vertical = 5.dp, horizontal = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )

                Box() {
                    innerTextField()
                    if (text.isEmpty()) {
                        Text(
                            text = "Search",
                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ExploreViewGridItem(post: Post, image: ImageItem) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column(

        ) {
            Image(
                imageVector = Icons.Default.Image,
                contentDescription = "Image",
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = post.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.background(color = MaterialTheme.colorScheme.surface)
            )
        }
    }
}