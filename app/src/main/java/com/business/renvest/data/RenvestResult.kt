package com.business.renvest.data

sealed class RenvestResult<out T> {
    data class Ok<T>(val value: T) : RenvestResult<T>()

    sealed class Err : RenvestResult<Nothing>() {
        data class Storage(val reason: String) : Err()
        data class Network(val reason: String) : Err()
        data class Validation(val reason: String) : Err()
    }
}

fun RenvestResult<*>.notifyErrorIfNotOk(notify: (String) -> Unit) {
    when (this) {
        is RenvestResult.Ok -> Unit
        is RenvestResult.Err.Storage -> notify(reason)
        is RenvestResult.Err.Network -> notify(reason)
        is RenvestResult.Err.Validation -> notify(reason)
    }
}
