package ru.palushin86.inventory.ui.parameters

import ru.palushin86.inventory.R
import ru.palushin86.inventory.entities.Parameter

import kotlinx.android.synthetic.main.parameters_item_parameter.view.*

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import android.view.ViewGroup
import android.view.View
import androidx.appcompat.app.AlertDialog
import ru.palushin86.inventory.entities.ParameterType

class ParametersAdapter(
    private var parameters: List<ParameterType>,
    private val deleteParameterTypeListener: DeleteParameterTypeListener
) : RecyclerView.Adapter<ParametersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.parameters_item_parameter, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parent = parameters[position]
        holder.key.text = parent.key
        holder.itemView.setOnClickListener {
            deleteParameterTypeListener.delete(position)
        }
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