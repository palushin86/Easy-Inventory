package ru.palushin86.inventory.ui.creator

import ru.palushin86.inventory.R

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import android.view.ViewGroup
import android.view.View
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.item_parameter_creator.view.*
import kotlinx.android.synthetic.main.tags_item_tag.view.parameters_tv_key
import ru.palushin86.inventory.entities.Inventory

class CreatorAdapter(
    inventory: Inventory,
    private val changeListener: ParameterChangeListener
) : RecyclerView.Adapter<CreatorAdapter.ViewHolder>() {
    val parameters = inventory.parameters

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_parameter_creator, parent, false)
        return ViewHolder(v, changeListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parent = parameters[position]
        holder.key.text = parent.tag
        holder.value.text = parent.value


/*
        holder.itemView.parameters_tv_value.doOnTextChanged { text, start, count, after ->

        }*/
    }

    override fun getItemCount(): Int {
        return parameters.size
    }

    inner class ViewHolder(itemView: View, changeListener: ParameterChangeListener) : RecyclerView.ViewHolder(itemView) {
        val key: TextView = itemView.parameters_tv_key
        val value: TextView = itemView.parameters_tv_value

        init {

            value.doOnTextChanged { text, _, _, _ ->
                changeListener.change(adapterPosition, text.toString())
            }
        }
    }
}