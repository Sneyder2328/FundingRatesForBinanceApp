package com.sneyder.fundingrates.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

sealed class Resource<T>(
) {
    class Loading<T> : Resource<T>()
    data class Failure<T>(val error: Throwable) : Resource<T>()
    data class Success<T>(val data: T) : Resource<T>()
}

@ExperimentalCoroutinesApi
inline fun <reified T> Query.asFlow(): Flow<List<T>> {
    return callbackFlow {
        val subscription =
            this@asFlow.addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val collectionValues = value?.documents?.filterNotNull()?.map { it.toObject(T::class.java)!! }
                offer(collectionValues.orEmpty())
            }
        awaitClose { subscription.remove() }
    }
}

@JvmOverloads
fun <T> Flow<T>.asResource(
    context: CoroutineContext = EmptyCoroutineContext,
    timeoutInMs: Long = 5000L
): Flow<Resource<T>> = flow {
    emit(Resource.Loading<T>())
    try {
        collect { emit(Resource.Success(it)) }
    } catch (e: Exception) {
        emit(Resource.Failure<T>(e))

        e.printStackTrace()
    }

}
//} liveData(context, timeoutInMs) {
//    emit(Resource.Loading<T>())
//    try {
//        collect { emit(Resource.Success(it)) }
//    } catch (e: Exception){
//        e.printStackTrace()
//        emit(Resource.Failure<T>(e))
//    }
//}

@JvmOverloads
fun <T> Flow<T>.asResourceLiveData(
    context: CoroutineContext = EmptyCoroutineContext,
    timeoutInMs: Long = 5000L
): LiveData<Resource<T>> = liveData(context, timeoutInMs) {
    emit(Resource.Loading<T>())
    try {
        collect { emit(Resource.Success(it)) }
    } catch (e: Exception) {
        e.printStackTrace()
        emit(Resource.Failure<T>(e))
    }
}

@JvmOverloads
fun <T> asResourceLiveData(
    block: suspend () -> T,
    context: CoroutineContext = EmptyCoroutineContext,
    timeoutInMs: Long = 5000L
): LiveData<Resource<T>> = liveData(context, timeoutInMs) {
    emit(Resource.Loading<T>())
    try {
        val data = block()
        emit(Resource.Success(data))
    } catch (e: Exception) {
        e.printStackTrace()
        emit(Resource.Failure<T>(e))
    }
}