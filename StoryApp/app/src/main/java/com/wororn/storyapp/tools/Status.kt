package com.wororn.storyapp.tools

sealed class Status <out R> private constructor() {
    object Process : Status<Nothing>()
    data class Fail (val error: String) : Status<Nothing>()
    data class Done<out T>(val data: T) : Status<T>()
}
