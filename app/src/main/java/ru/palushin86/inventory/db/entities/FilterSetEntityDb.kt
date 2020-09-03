package ru.palushin86.inventory.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FilterSetEntityDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String
)