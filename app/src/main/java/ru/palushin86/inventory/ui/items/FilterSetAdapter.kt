package ru.palushin86.inventory.ui.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_filter.view.*
import ru.palushin86.inventory.R
import ru.palushin86.inventory.entities.FilterSet

class FilterSetAdapter(
    private var filterSets : List<FilterSet>,
    private val listener: FilterSetActions
) : RecyclerView.Adapter<FilterSetAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return filterSets.size
    }

    fun setItems(items: List<FilterSet>) {
        (filterSets as MutableList).clear()
        (filterSets as MutableList).addAll(items)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parameter = filterSets[position]
        holder.key.text = parameter.name
        holder.value.text = parameter.name
        holder.itemView.setOnClickListener {
            listener.onClick(position)
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val key: TextView = itemView.filter_key
        val value: TextView = itemView.filter_value
    }
}