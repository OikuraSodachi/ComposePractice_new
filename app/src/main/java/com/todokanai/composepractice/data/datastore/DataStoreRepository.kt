package com.todokanai.composepractice.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.todokanai.composepractice.application.MyApplication
import com.todokanai.composepractice.myobjects.Constants.BY_DEFAULT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DataStoreRepository {
    companion object{
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mydatastore")
        val DATASTORE_SORT_BY = stringPreferencesKey("datastore_sort_by")
        val DATASTORE_COPY_OVERWRITE = booleanPreferencesKey("datastore_copy_overwrite")
    }
    private val myContext = MyApplication.appContext

    fun saveSortBy(value:String){
        CoroutineScope(Dispatchers.IO).launch {
            myContext.dataStore.edit{
                it[DATASTORE_SORT_BY] = value
            }
        }
    }

    suspend fun sortBy() : String {
        return myContext.dataStore.data.first()[DATASTORE_SORT_BY] ?: BY_DEFAULT
    }

    val sortBy: Flow<String> = myContext.dataStore.data.map{
        it[DATASTORE_SORT_BY]?: BY_DEFAULT
    }

    fun saveCopyOverwrite(value:Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            myContext.dataStore.edit{
                it[DATASTORE_COPY_OVERWRITE] = value
            }
        }
    }

    suspend fun copyOverwrite(): Boolean {
        return myContext.dataStore.data.first()[DATASTORE_COPY_OVERWRITE] ?: false
    }
}