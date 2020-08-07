package ru.palushin86.inventory.entities

data class Inventory(
    val id: Int? = null,
    var title: String,
    val parameters: List<Parameter>
)