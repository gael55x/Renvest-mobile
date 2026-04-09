package com.business.renvest.data

sealed class RenvestResult<out T> {
    data class Ok<T>(val value: T) : RenvestResult<T>()

    sealed class Err : RenvestResult<Nothing>() {
        data class Storage(val reason: String) : Err()
        data class Network(val reason: String) : Err()
        data class Validation(val reason: String) : Err()
    }
}

inline fun <T> RenvestResult<T>.onOk(block: (T) -> Unit): RenvestResult<T> {
    if (this is RenvestResult.Ok) block(value)
    return this
}

inline fun <T> RenvestResult<T>.onErr(block: (RenvestResult.Err) -> Unit): RenvestResult<T> {
    if (this is RenvestResult.Err) block(this)
    return this
}

fun RenvestResult<*>.notifyErrorIfNotOk(notify: (String) -> Unit) {
    when (this) {
        is RenvestResult.Ok -> Unit
        is RenvestResult.Err.Storage -> notify(reason)
        is RenvestResult.Err.Network -> notify(reason)
        is RenvestResult.Err.Validation -> notify(reason)
    }
}
