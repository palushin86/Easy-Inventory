package ru.palushin86.inventory.ui.inventory

import androidx.lifecycle.ViewModel
import ru.palushin86.inventory.App
import ru.palushin86.inventory.entities.Inventory
import ru.palushin86.inventory.entities.Parameter

class InventoryViewModel : ViewModel() {
    val filters = mutableListOf<Parameter>()
    private val dao = App.database.appDao()

    fun deleteEquipment(id: Int) {
        dao.removeInventory(id)
    }

    fun getFilteredInventories(): List<Inventory> {
        val filtered = mutableListOf<Inventory>()
        val equipments = mutableListOf<Inventory>()

        dao.getEquipments().forEach {
            val param = mutableListOf<Parameter>()

            dao.getParameters(it.id!!).forEach { entity ->
                param.add(
                    Parameter(key = entity.key, value = entity.value)
                )
            }

            equipments.add(

                Inventory(
                    id = it.id,
                    title = it.title,
                    parameters = param
                )
            )
        }

        equipments.forEach { inventory ->

            if (inventory.parameters.containsAll(filters) || filters.map { it.value }.contains(inventory.title)) {
                filtered.add(inventory)
            }

        }

        println(filtered)

        return filtered
    }

    fun getFilters(query: String): List<Parameter> {
        val result = mutableListOf<Parameter>()
        val param = getFilteredInventories()
            .flatMap { it.parameters }
            .distinct()
            .filter {
                (it.key.contains(query, true) ||
                        it.value.contains(query, true)) && !filters.contains(it)
            }
        val inve = getFilteredInventories()
            .distinct()
            .map {
                Parameter(
                    "title",
                    it.title
                )
            }
            .filter {
                (it.key.contains(query, true) ||
                        it.value.contains(query, true)) && !filters.contains(it)
            }

        result.addAll(param)
        result.addAll(inve)
        return result
    }

}