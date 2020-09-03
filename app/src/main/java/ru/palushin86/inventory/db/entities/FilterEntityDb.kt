package ru.palushin86.inventory.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FilterEntityDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val filterSetId: Int,
    val tagId: Int,
    val value: String
)