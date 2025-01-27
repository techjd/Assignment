package com.myjar.jarassignment.data.repository

import android.util.Log
import com.google.gson.Gson
import com.myjar.jarassignment.data.api.ApiService
import com.myjar.jarassignment.data.local.LocalData
import com.myjar.jarassignment.data.local.LocalPreferences
import com.myjar.jarassignment.data.model.ComputerItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

interface JarRepository {
    suspend fun fetchResults(): Flow<List<ComputerItem>>
}

const val TAG = "JarRepository"

class JarRepositoryImpl(
    private val apiService: ApiService,
    private val localPreferences: LocalPreferences
) : JarRepository {
    private val gson: Gson = Gson()

    override suspend fun fetchResults(): Flow<List<ComputerItem>> = flow {
        try {
            val data = apiService.fetchResults()
            emit(apiService.fetchResults())
            localPreferences.saveComputerData(gson.toJson(LocalData(data)))
        } catch (e: Exception) {
            val cachedData = localPreferences.getComputerData().first()
            if (cachedData != null) {
                val data = gson.fromJson(cachedData, LocalData::class.java)
                emit(data.computerItems)
            } else {
                emit(emptyList())
            }
        }
    }
}