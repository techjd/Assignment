package com.myjar.jarassignment.data

sealed class Resource<T> {
    class Success<T>(val data: T?) : Resource<T>()
    class Error<T>(val reason: String, val data: T?) : Resource<T>()
}