package ru.palushin86.inventory.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.palushin86.inventory.entities.Inventory
import ru.palushin86.inventory.entities.Parameter
import java.util.*

@Entity
data class InventoryEntityDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String
) {
    constructor(inventory: Inventory) : this(title = inventory.title)

    override fun equals(other: Any?): Boolean = (other is InventoryEntityDb) && id == other.id
}