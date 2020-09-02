package ru.palushin86.inventory.ui.tags

import ru.palushin86.inventory.R

import kotlinx.android.synthetic.main.tags_item_tag.view.*

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import android.view.ViewGroup
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import ru.palushin86.inventory.entities.Tag

class TagsAdapter(
    private var parameters: List<Tag>,
    private val onAutocompleteStatusChanged: OnAutocompleteStatusChanged
) : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.tags_item_tag, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parent = parameters[position]
        holder.key.text = parent.key
        holder.isAutocomplete.isChecked = parent.isAutocomplete

        holder.itemView.sw_autocomplete.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            onAutocompleteStatusChanged.change(position, b)
        }
    }

    override fun getItemCount(): Int {
        return parameters.size
    }

    fun setItems(tag: List<Tag>) {
        parameters = tag
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val key: TextView = itemView.parameters_tv_key
        var isAutocomplete: Switch = itemView.sw_autocomplete
    }
}