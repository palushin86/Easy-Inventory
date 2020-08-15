package ru.palushin86.inventory.ui.tags

import ru.palushin86.inventory.R

import kotlinx.android.synthetic.main.tags_item_tag.view.*

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import android.view.ViewGroup
import android.view.View
import ru.palushin86.inventory.entities.ParameterType

class TagsAdapter(
    private var parameters: List<ParameterType>
) : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.tags_item_tag, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parent = parameters[position]
        holder.key.text = parent.key
    }

    override fun getItemCount(): Int {
        return parameters.size
    }

    fun setItems(parameterType: List<ParameterType>) {
        parameters = parameterType
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val key: TextView = itemView.parameters_tv_key
    }
}