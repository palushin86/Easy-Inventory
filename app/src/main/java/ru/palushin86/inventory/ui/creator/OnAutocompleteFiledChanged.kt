package ru.palushin86.inventory.ui.creator

interface OnAutocompleteFiledChanged {
    fun updateDropList(tagId: Int, text: String)
}