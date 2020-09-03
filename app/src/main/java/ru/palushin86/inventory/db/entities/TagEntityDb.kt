package ru.palushin86.inventory.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.palushin86.inventory.entities.Tag

@Entity
data class TagEntityDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val key: String,
    var isAutocomplete: Boolean
)