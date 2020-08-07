package ru.palushin86.inventory.ui.inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.item_inventory.view.*
import ru.palushin86.inventory.R
import ru.palushin86.inventory.entities.Inventory
import ru.palushin86.inventory.ui.OnSwipeTouchListener

class ParentAdapter(
    private val parents: List<Inventory>,
    private val longClickListener: LongClickListener
) : RecyclerView.Adapter<ParentAdapter.ViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()
    private var items = mutableListOf<Inventory>()

    init {
        items.addAll(parents)
    }

    var mExpandedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventory, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parent = items[position]
        holder.textView.text = parent.title
        val childLayoutManager =
            LinearLayoutManager(holder.recyclerView.context, RecyclerView.HORIZONTAL, false)
        childLayoutManager.initialPrefetchItemCount = 4
        holder.recyclerView.apply {
            layoutManager = childLayoutManager
            adapter = ChildAdapter(parent.parameters.filter { it.value.isNotBlank() })
            setRecycledViewPool(viewPool)
        }
        holder.itemView.setOnLongClickListener {
            longClickListener.longClicked(position)
            true
        }
        expander(holder, position)
    }

    private fun expander(holder: ViewHolder, position: Int) {
        val isExpanded = position == mExpandedPosition
        setVisibility(holder.itemView, if (isExpanded) TextView.VISIBLE else TextView.GONE)
        holder.itemView.isActivated = isExpanded
        holder.itemView.setOnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position
            TransitionManager.beginDelayedTransition(it.vg_parameters)
            notifyDataSetChanged()
        }
    }

    private fun setVisibility(it: View, visibilityMode: Int) {
        it.rv_parameters.visibility = visibilityMode
    }

    fun setItems(filteredInventories: List<Inventory>) {
        items.clear()
        items.addAll(filteredInventories)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.rv_parameters
        val textView: TextView = itemView.inventory_name
    }
}