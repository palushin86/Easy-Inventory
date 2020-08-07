package ru.palushin86.inventory.ui.parameters

import ru.palushin86.inventory.db.entities.ParameterTypeEntityDb
import ru.palushin86.inventory.entities.ParameterType
import ru.palushin86.inventory.App

import androidx.lifecycle.ViewModel

class ParametersViewModel : ViewModel() {
    private val dao = App.database.appDao()

    fun getParameterTypes(): List<ParameterType> {
        val parameterTypes = mutableListOf<ParameterType>()

        dao.getParameterTypes().forEach {

            parameterTypes.add(ParameterType(id = it.id, key = it.key))
        }
        return parameterTypes
    }

    fun addParameterType(parameterType: ParameterType): Long {
        return dao.insert(ParameterTypeEntityDb(parameterType))
    }

    fun removeParameterType(id: Int) {
        dao.deleteParameterType(id)
    }
}