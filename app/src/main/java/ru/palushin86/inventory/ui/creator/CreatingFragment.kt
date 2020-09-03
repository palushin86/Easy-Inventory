package ru.palushin86.inventory.ui.creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_creator.*
import kotlinx.android.synthetic.main.tags_item_tag.*
import ru.palushin86.inventory.R
import ru.palushin86.inventory.entities.Inventory
import ru.palushin86.inventory.entities.Parameter

class CreatingFragment : Fragment(), ParameterChangeListener, OnAutocompleteFiledChanged {
    private lateinit var creatingViewModel: CreatingViewModel
    private lateinit var parametersAdapter: CreatorAdapter
    lateinit var inventory: Inventory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        creatingViewModel = ViewModelProvider
            .NewInstanceFactory()
            .create(CreatingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_creator, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_gallery)
        creatingViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parametersRecyclerView = view.findViewById<RecyclerView>(R.id.creator_rv_parameters)
        parametersRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        parametersRecyclerView.itemAnimator = DefaultItemAnimator()

        val inventoryId: Int? = arguments?.getInt("inventory_id")

        if (inventoryId == null) {
            val parameters = mutableListOf<Parameter>()
            creatingViewModel.getTags().forEach { tag ->
                parameters.add(
                    Parameter(tag, "")
                )
            }
            inventory = Inventory(title = "Name", parameters = parameters)
        } else {
            inventory = creatingViewModel.getInventory(inventoryId)
            creator_et_item_name.setText(inventory.title)
        }

        parametersAdapter = CreatorAdapter(inventory, this, this)
        parametersRecyclerView.adapter = parametersAdapter
        setListeners()
    }

    private fun setListeners() {
        creator_insert_inventory.setOnClickListener { onInsertBtnClicked() }
        creator_cancel_inventory.setOnClickListener { onCancelBtnClicked() }
    }


    override fun change(position: Int, text: String) {
        (inventory.parameters as MutableList)[position].value = text
    }

    private fun onInsertBtnClicked() {
        inventory.title = creator_et_item_name.text.toString()
        creatingViewModel.addInventory(inventory)
        parentFragmentManager.popBackStack()
    }

    private fun onCancelBtnClicked() {
        parentFragmentManager.popBackStack()
    }

    override fun updateDropList(tagId: Int, text: String) {

    }

}
