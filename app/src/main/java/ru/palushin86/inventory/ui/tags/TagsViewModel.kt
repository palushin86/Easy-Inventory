package ru.palushin86.inventory.ui.tags

import ru.palushin86.inventory.db.entities.TagEntityDb
import ru.palushin86.inventory.entities.Tag
import ru.palushin86.inventory.App

import androidx.lifecycle.ViewModel

class TagsViewModel : ViewModel() {
    private val dao = App.database.appDao()

    fun getParameterTypes(): List<Tag> {
        val x = dao.getParameterTypes().map { Tag(id = it.id, key = it.key, isAutocomplete = it.isAutocomplete) }
        println(x)
        return x
    }

    fun addTag(tag: Tag): Long {
        return dao.insert(TagEntityDb(tag))
    }

    fun removeTag(id: Int) {
        dao.deleteParameterType(id)
    }

    fun updateTag(tag: Tag) {
        dao.updateTag(tag.id!!, tag.isAutocomplete)
    }


}