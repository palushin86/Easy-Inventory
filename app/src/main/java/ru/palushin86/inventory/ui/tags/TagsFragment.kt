package ru.palushin86.inventory.ui.tags

import ru.palushin86.inventory.R
import ru.palushin86.inventory.ui.SwipeToDeleteCallback

import kotlinx.android.synthetic.main.fragment_tags.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

class TagsFragment : Fragment(), OnAutocompleteStatusChanged {
    private lateinit var tagsViewModel: TagsViewModel
    private lateinit var tagsAdapter: TagsAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        tagsViewModel = ViewModelProvider.NewInstanceFactory().create(TagsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_tags, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parametersRecyclerView = view.findViewById<RecyclerView>(R.id.rv_parameters_parameters)
        parametersRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        parametersRecyclerView.itemAnimator = DefaultItemAnimator()
        val items = tagsViewModel.getParameterTypes()
        tagsAdapter = TagsAdapter(items, this)
        parametersRecyclerView.adapter = tagsAdapter

        val swipeHandler = object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Подтвердите удаление")
                    .setPositiveButton("Удалить") { _, _ ->
                        deleteTag(viewHolder.adapterPosition)

                    }
                    .setNegativeButton("Отмена") { _, _ ->
                        tagsAdapter.notifyDataSetChanged()
                    }
                    .create()
                    .show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(parametersRecyclerView)

        setListeners()
    }

    private fun deleteTag(position: Int) {
        val removableTag = tagsViewModel.getParameterTypes()[position]
        tagsViewModel.removeTag(removableTag.id!!)
        tagsAdapter.setItems(tagsViewModel.getParameterTypes())
    }

    private fun setListeners() {
        add_parameter.setOnClickListener { onAddParameterBtnClicked() }
    }

    private fun onAddParameterBtnClicked() {
        val inflater = activity?.layoutInflater

        if (inflater != null) {
            val dialogLayout = inflater.inflate(R.layout.dialog_add_parameter, null)
            val etParameterType  = dialogLayout.findViewById<EditText>(R.id.et_key)
            AlertDialog.Builder(context)
                .setTitle("Ввод нового типа оборудования")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    createTag(etParameterType.text.toString()) }
                .setNegativeButton(android.R.string.no) { _, _ -> }
                .setView(dialogLayout)
                .create()
                .show()
        }
    }

    private fun createTag(tagName: String) {
        tagsViewModel.addTag(tagName, false)
        tagsAdapter.setItems(tagsViewModel.getParameterTypes())
        tagsAdapter.notifyDataSetChanged()
    }

    override fun change(position: Int, status: Boolean) {
        val tag = tagsViewModel.getParameterTypes()[position]
        tag.isAutocomplete = status
        tagsViewModel.updateTag(tag)
    }

}
