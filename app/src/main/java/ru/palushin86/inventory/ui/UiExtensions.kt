package ru.palushin86.inventory.ui

import androidx.appcompat.widget.SearchView

fun SearchView.onItemClick(function: (Int) -> Unit) {
    this.setOnSuggestionListener(
        object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                function(position)
                return true
            }
        }
    )
}

fun SearchView.textChanged(function: (String) -> Unit) {
    this.setOnQueryTextListener(object :
        SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            function(query)
            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            function(newText)
            return false
        }
    })
}