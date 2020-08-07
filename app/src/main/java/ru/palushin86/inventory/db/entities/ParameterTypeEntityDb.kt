package ru.palushin86.inventory.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.palushin86.inventory.entities.ParameterType

@Entity
class ParameterTypeEntityDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val key: String
) {
    constructor(parameterType: ParameterType) : this(key = parameterType.key)
}