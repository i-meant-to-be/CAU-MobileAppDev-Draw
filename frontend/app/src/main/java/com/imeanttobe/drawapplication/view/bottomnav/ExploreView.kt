package com.imeanttobe.drawapplication.view.bottomnav

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.imeanttobe.drawapplication.R
import com.imeanttobe.drawapplication.data.enum.UserType
import com.imeanttobe.drawapplication.data.model.ImageItem
import com.imeanttobe.drawapplication.data.model.Post
import com.imeanttobe.drawapplication.data.model.User
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
                searchText = viewModel.searchText.value,
                onSearchTextChange = { newValue -> viewModel.setSearchText(newValue) }
            )
            ExploreViewGrid(
                modifier = Modifier.padding(horizontal = 10.dp),
                isDialogOpen = viewModel.dialogState.value,
                setDialogState = { newValue -> viewModel.setDialogState(newValue) }
            )
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
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        ExploreViewSearchBoxTextField(
            text = searchText,
            onValueChange = onSearchTextChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreViewGrid(
    modifier: Modifier = Modifier,
    isDialogOpen: Boolean,
    setDialogState: (Boolean) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        items(10) {
            ExploreViewGridItem(
                post = Post(userId = 0, description = "If this description is too long, how this application looks like...?"),
                image = ImageItem(postId = 0, imageUrl = ""),
                user = User(name = "Username", email = "", type = UserType.ASSIST_ARTIST, userImageUrl = "", password = "", instagramId = ""),
                onImageClick = {
                    setDialogState(true)
                }
            )
        }
    }

    if (isDialogOpen) {
        Dialog(
            onDismissRequest = { setDialogState(false) }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.paintimage),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
            }
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
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {  }),
        decorationBox = @Composable { innerTextField ->
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100.dp))
                    .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    .padding(vertical = 5.dp, horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(5.dp))
                Box(contentAlignment = Alignment.CenterStart) {
                    innerTextField()
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.search),
                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ExploreViewGridItem(
    post: Post,
    image: ImageItem,
    user: User,
    onImageClick: () -> Unit
) {
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    val containerColor = MaterialTheme.colorScheme.primaryContainer

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ExploreViewUserInfoItem(
                userName = user.name,
                userType = user.type,
                userImageUrl = user.userImageUrl,
                contentColor = contentColor,
                onClick = {}
            )
            ExploreViewImageItem(
                post = post,
                imageItem = image,
                contentColor = contentColor,
                onImageClick = onImageClick
            )
        }
    }
}

@Composable
fun ExploreViewUserInfoItem(
    userName: String,
    userType: UserType,
    userImageUrl: String,
    contentColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.joker),
            contentDescription = "Image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Column() {
            Text(
                text = userName,
                style = MaterialTheme.typography.bodyLarge.copy(color = contentColor)
            )
            Text(
                text = when(userType) {
                    UserType.ADMIN -> stringResource(id = R.string.usertype_admin)
                    UserType.WEBTOON_ARTIST -> stringResource(id = R.string.usertype_webtoon_artist)
                    UserType.ASSIST_ARTIST -> stringResource(id = R.string.usertype_assist_artist)
                },
                style = MaterialTheme.typography.bodySmall.copy(color = contentColor)
            )
        }
    }
}

@Composable
fun ExploreViewImageItem(
    post: Post,
    imageItem: ImageItem,
    contentColor: Color,
    onImageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onImageClick() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.paintimage),
            contentDescription = "Image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = post.description,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall.copy(color = contentColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
    }
}