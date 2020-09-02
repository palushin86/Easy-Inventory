package ru.palushin86.inventory.ui.tags

interface OnAutocompleteStatusChanged {
    fun change(position: Int, status: Boolean)
}