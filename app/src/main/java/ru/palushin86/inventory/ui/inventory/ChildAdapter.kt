package ru.palushin86.inventory.ui.inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_parameter.view.*
import ru.palushin86.inventory.R
import ru.palushin86.inventory.entities.Parameter

class ChildAdapter(private val children : List<Parameter>)
    : RecyclerView.Adapter<ChildAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.item_parameter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val child = children[position]
        holder.imageView!!.text = child.key
        holder.textView.text = child.value
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val textView : TextView = itemView.tv_key
        val imageView: TextView? = itemView.tv_value

    }
}