package com.myjar.jarassignment.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjar.jarassignment.createRetrofit
import com.myjar.jarassignment.data.Dependencies
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.repository.JarRepository
import com.myjar.jarassignment.data.repository.JarRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


const val TAG = "JarViewModel"

class JarViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<ComputerItem>>(emptyList())
    val items = _items.asStateFlow()

    private val _listStringData = MutableStateFlow<List<ComputerItem>>(emptyList())

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val repository: JarRepository =
        JarRepositoryImpl(createRetrofit(), Dependencies.getLocalPreferences())

    init {
        observeQuery()
    }

    private fun observeQuery() {
        viewModelScope.launch {
            _query
                .debounce(300L)
                .flatMapLatest { userQuery ->
                    var filteredList = _listStringData.value.filter {
                        it.name.lowercase()
                            .contains(userQuery.lowercase()) || it.data?.description?.lowercase()
                            ?.contains(userQuery.lowercase()) ?: false
                                || it.data?.color?.lowercase()
                            ?.contains(userQuery.lowercase()) ?: false
                    }
                    if (filteredList.isEmpty()) {
                        filteredList = _listStringData.value
                    }
                    flow { emit(filteredList) }
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _items.value = it
                }
        }
    }

    fun updateQuery(query: String) {
        _query.value = query
    }

    fun fetchData() {
        viewModelScope.launch {
            repository.fetchResults()
                .collect {
                    _items.value = it
                    _listStringData.value = it
                }
        }
    }
}