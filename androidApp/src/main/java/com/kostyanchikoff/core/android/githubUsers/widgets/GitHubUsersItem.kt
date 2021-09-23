package com.kostyanchikoff.core.android.githubUsers.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun GitHubUserItemWidget(
    avatarUrl: String,
) {
    GlideImage(imageModel = avatarUrl,contentScale = ContentScale.Crop)
}