package ru.palushin86.inventory.ui.inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_tag.view.*
import ru.palushin86.inventory.R
import ru.palushin86.inventory.entities.Parameter

class ChildAdapter(private val children: List<Parameter>) :
    RecyclerView.Adapter<ChildAdapter.ViewHolder>() {
    var isVisibleKey: Boolean = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_tag, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val child = children[position]
        holder.tagKey!!.text = child.key
        holder.tagValue.text = child.value
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagValue: TextView = itemView.tv_value
        val tagKey: TextView? = itemView.tv_key

        /*init {
            itemView.setOnClickListener {
                changeVisibleState(itemView.tv_k)
            }
        }*/

        private fun changeVisibleState(tagKey: LinearLayout) {

            if (isVisibleKey) {
                tagKey.visibility = View.GONE
                isVisibleKey = false
            } else {
                tagKey.visibility = View.VISIBLE
                isVisibleKey = true
            }

        }

    }
}