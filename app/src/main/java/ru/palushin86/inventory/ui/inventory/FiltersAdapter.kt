package ru.palushin86.inventory.ui.inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_filter.view.*
import ru.palushin86.inventory.R
import ru.palushin86.inventory.entities.Parameter

class FiltersAdapter(
    private val parameters : List<Parameter>,
    private val deleteFilterListener: DeleteFilterListener
) : RecyclerView.Adapter<FiltersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return parameters.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parameter = parameters[position]
        holder.key.text = parameter.key
        holder.value.text = parameter.value
        holder.itemView.setOnClickListener {
            deleteFilterListener.deleteFilter(position)
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val key: TextView = itemView.filter_key
        val value: TextView = itemView.filter_value
    }
}