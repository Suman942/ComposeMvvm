package com.example.composemvvm.models.dating

sealed class ListItem {
    data class TextItem(val id: Int, val text: String) : ListItem()
    data class ImageItem(val id: Int, val imageUrl: String) : ListItem()
    data class VideoItem(val id: Int, val videoUrl: String) : ListItem()
}