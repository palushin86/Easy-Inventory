package ru.palushin86.inventory.entities

data class Tag (
    val id: Int? = null,
    val key: String,
    var isAutocomplete: Boolean
)