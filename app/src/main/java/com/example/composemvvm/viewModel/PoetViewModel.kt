package com.example.composemvvm.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composemvvm.models.Poet
import com.example.composemvvm.repo.PoetRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PoetViewModel @Inject constructor(private val repo: PoetRepo,private val savedStateHandle: SavedStateHandle) : ViewModel() {

    val poetStateFlow : StateFlow<List<String>>
        get() = repo._poets
    fun getPoets(){
        viewModelScope.launch {
            repo.getPoets()
        }
    }

    val booksStateFlow : StateFlow<List<Poet>>
        get() = repo._books
    fun getBooks(name:String){
        viewModelScope.launch {
            val filterName = "poets[?(@.name==\"$name\")]"
            repo.getBooks(filterName)
        }
    }

    fun getSavedState(): SavedStateHandle{
      return  savedStateHandle
    }
}