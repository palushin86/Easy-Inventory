package ru.palushin86.inventory.ui.items

import androidx.lifecycle.ViewModel
import ru.palushin86.inventory.App
import ru.palushin86.inventory.db.entities.FilterEntityDb
import ru.palushin86.inventory.db.entities.FilterSetEntityDb
import ru.palushin86.inventory.entities.FilterSet
import ru.palushin86.inventory.entities.Inventory
import ru.palushin86.inventory.entities.Parameter
import ru.palushin86.inventory.entities.Tag

class ItemsViewModel : ViewModel() {
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
                    Parameter(
                        tag = dao.getTag(entity.tagId),
                        value = entity.value
                    )
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

        return filtered
    }

    fun getFilters(query: String): List<Parameter> {
        val result = mutableListOf<Parameter>()
        val param = getFilteredInventories()
            .flatMap { it.parameters }
            .distinct()
            .filter {
                (it.tag.key.contains(query, true) ||
                        it.value.contains(query, true)) && !filters.contains(it)
            }
        val inve = getFilteredInventories()
            .distinct()
            .map {
                Parameter(
                    Tag(-1, "title", false),
                    it.title
                )
            }
            .filter {
                (it.tag.key.contains(query, true) ||
                        it.value.contains(query, true)) && !filters.contains(it)
            }

        result.addAll(param)
        result.addAll(inve)
        return result
    }

    fun insertFilterSet(filterSet: FilterSet) {
        val fs = FilterSetEntityDb(name = filterSet.name)
        val filterSetId = dao.insert(fs).toInt()
        val fl = filterSet.set.map {
            FilterEntityDb(filterSetId = filterSetId, tagId = it.tag.id, value = it.value)
        }
        dao.insertFilters(fl)
    }

    fun getFilterSets(): List<FilterSet> {
        return dao.getFilterSets().map {
            val set = dao.getFilters(it.id!!).map {
                val tag = dao.getTag(it.tagId)
                Parameter(tag, it.value)
            }
            FilterSet(
                it.name,
                set
            )
        }
    }

}