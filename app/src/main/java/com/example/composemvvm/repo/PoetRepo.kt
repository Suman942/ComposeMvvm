package com.example.composemvvm.repo

import android.util.Log
import com.example.composemvvm.models.Poet
import com.example.composemvvm.models.PoetsModel
import com.example.composemvvm.network.ApiInterface
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class PoetRepo @Inject constructor(private val apiInterface: ApiInterface) {

     val _poets = MutableStateFlow<List<String>>(emptyList())
    suspend fun getPoets(){
        val response = apiInterface.getPoets()
        if (response.isSuccessful && response.body() != null){
            Log.d("HomeActivity","result: ${response.body()}")
            _poets.value = response.body()!!
        }
    }

    val _books = MutableStateFlow<List<Poet>>(emptyList())
    suspend fun getBooks(name:String){
    val response = apiInterface.getBooks(name)
    Log.d("HomeActivity"," getBooks result: ${response.errorBody()?.string()}")

        if (response.isSuccessful && response.body() != null){
            Log.d("HomeActivity"," getBooks result: ${response.body()}")
            _books.value = response.body()!!
        }
    }
}