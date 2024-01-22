package com.example.composemvvm.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composemvvm.R
import com.example.composemvvm.viewModel.PoetViewModel

@Composable
fun HomeScreen(onClick :(poetsName:String) -> Unit){
    val poetViewModel :  PoetViewModel = hiltViewModel()
    poetViewModel.getPoets()
    val poets: State<List<String>> = poetViewModel.poetStateFlow.collectAsState()
    LazyVerticalGrid(columns =GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.SpaceAround){
        items(poets.value){
            PoetsList(poetsName = it,onClick)
        }
    }
}

@Composable
fun PoetsList(poetsName:String,onClick :(poetsName:String) -> Unit){
    Box(modifier = Modifier
        .padding(6.dp)
        .size(160.dp)
        .clickable { onClick(poetsName) }
        .clip(RoundedCornerShape(12.dp))
        .paint(painter = painterResource(id = R.drawable.home_bg), contentScale = ContentScale.Crop)
        .border(1.dp, Color(0xFFEEEEEE))
        , contentAlignment = Alignment.BottomCenter
    ){
            Text(text = poetsName,
                color = Color.White
                , fontSize = 18.sp,
                modifier = Modifier.padding(0.dp,24.dp),
                style = MaterialTheme.typography.bodyMedium
            )
    }
}

@Composable
fun DetailScreen(poetsName: String) {
    val poetViewModel :  PoetViewModel = hiltViewModel()
//    val poetsName = poetViewModel.getSavedState().get<String>("poets") ?: ""
    poetViewModel.getBooks(poetsName)
    val books = poetViewModel.booksStateFlow.collectAsState()
    LazyColumn(content = {
        items(books.value){poet->
            poet.books.forEach { bookName ->
                BooksListItem(bookName = bookName)
            }
        }
    })
}

@Composable
fun BooksListItem(bookName: String){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        border = BorderStroke(1.dp, Color(0xFFCCCCCCC)),
        content = {
            Text(
                text = bookName,
                modifier = Modifier
                    .padding(16.dp), style = MaterialTheme.typography.bodyMedium
            )
        }
    )
}

