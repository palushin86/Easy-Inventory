package ru.palushin86.inventory.ui.creator

import androidx.lifecycle.ViewModel
import ru.palushin86.inventory.App
import ru.palushin86.inventory.db.entities.InventoryEntityDb
import ru.palushin86.inventory.db.entities.ParameterEntityDb
import ru.palushin86.inventory.entities.Inventory
import ru.palushin86.inventory.entities.Parameter
import ru.palushin86.inventory.entities.Tag

class CreatingViewModel : ViewModel() {
    private val dao = App.database.appDao()

    fun getParameterTypes(): List<Tag> {
        val parameterTypes = mutableListOf<Tag>()

        dao.getParameterTypes().forEach {
            parameterTypes.add(Tag(id = it.id, key = it.key, isAutocomplete = it.isAutocomplete))
        }

        return parameterTypes
    }

    fun addInventory(inventory: Inventory) {
        val parameterEntities = mutableListOf<ParameterEntityDb>()
        val inventoryEntity = InventoryEntityDb(inventory)

        val inventoryId = dao.insert(inventoryEntity).toInt()

        inventory.parameters.forEach {
            parameterEntities.add(
                ParameterEntityDb(
                    key = it.tag,
                    value = it.value,
                    inventoryId = inventoryId
                )
            )
        }

        dao.insert(parameterEntities)
    }

    fun getInventory(inventoryId: Int): Inventory {
        val entity = dao.getEquipment(inventoryId)
        val parameters = mutableListOf<Parameter>()

        val allParameters = dao.getParameters().map { Parameter(it.key, it.value) }

        parameters.addAll(allParameters)

        dao.getParameters(inventoryId).forEach {
            parameters.add(Parameter(it.key, it.value))
        }

        return Inventory(
            id = entity.id,
            title = entity.title,
            parameters = parameters.distinctBy { it.tag }.toList()
        )
    }

    fun replaceInventory(inventory: Inventory) {
        val parameterEntities = mutableListOf<ParameterEntityDb>()
        val inventoryEntity = InventoryEntityDb(inventory)

        val inventoryId = dao.insert(inventoryEntity).toInt()

        inventory.parameters.forEach {
            parameterEntities.add(
                ParameterEntityDb(
                    key = it.tag,
                    value = it.value,
                    inventoryId = inventoryId
                )
            )
        }

        dao.insert(parameterEntities)
    }

}