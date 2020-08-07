package ru.palushin86.inventory.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [
    ForeignKey(
        entity = InventoryEntityDb::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("inventoryId"),
        onDelete = CASCADE
    )
])
data class ParameterEntityDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val key: String,
    val value: String,
    val inventoryId: Int
) {
    override fun equals(other: Any?): Boolean = (other is ParameterEntityDb) && id == other.id

}