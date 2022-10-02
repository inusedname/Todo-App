package com.vstd.todo.others.utilities

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.forceNotify() {
    this.value = this.value
}