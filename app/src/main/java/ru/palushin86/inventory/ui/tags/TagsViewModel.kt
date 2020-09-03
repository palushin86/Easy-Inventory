package ru.palushin86.inventory.ui.tags

import ru.palushin86.inventory.db.entities.TagEntityDb
import ru.palushin86.inventory.entities.Tag
import ru.palushin86.inventory.App

import androidx.lifecycle.ViewModel

class TagsViewModel : ViewModel() {
    private val dao = App.database.appDao()

    fun getParameterTypes(): List<Tag> {
        return dao.getTags().map { Tag(id = it.id!!, key = it.key, isAutocomplete = it.isAutocomplete) }
    }

    fun addTag(tagName: String, isAutocomplete: Boolean): Long {
        return dao.insert(
            TagEntityDb(
                key = tagName,
                isAutocomplete = isAutocomplete)
        )
    }

    fun removeTag(id: Int) {
        dao.deleteTag(id)
    }

    fun updateTag(tag: Tag) {
        dao.updateTag(tag.id, tag.isAutocomplete)
    }


}