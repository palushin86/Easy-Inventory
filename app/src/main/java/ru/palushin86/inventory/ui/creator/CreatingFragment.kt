package ru.palushin86.inventory.ui.creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_creator.*
import ru.palushin86.inventory.R
import ru.palushin86.inventory.entities.Inventory
import ru.palushin86.inventory.entities.Parameter

class CreatingFragment : Fragment(), ParameterChangeListener {
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
        parametersRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        parametersRecyclerView.itemAnimator = DefaultItemAnimator()

        val inventoryId: Int? = arguments?.getInt("inventory_id")
        if (inventoryId == null) {
            val parameters = mutableListOf<Parameter>()
            creatingViewModel.getParameterTypes().forEach { parameterType->
                parameters.add(
                    Parameter(parameterType.key, "")
                )
            }
            inventory = Inventory(title = "Name", parameters = parameters)

        } else {
            inventory = creatingViewModel.getInventory(inventoryId)
            creator_inventory_name.setText(inventory.title)
        }

        parametersAdapter = CreatorAdapter(inventory, this)
        parametersRecyclerView.adapter = parametersAdapter
        creator_insert_inventory.setOnClickListener { onInsertBtnClicked() }

    }

    override fun change(position: Int, text: String) {
        (inventory.parameters as MutableList)[position].value = text
    }

    private fun onInsertBtnClicked() {
        inventory.title = creator_inventory_name.text.toString()

        println(inventory)

        /*inventoryId?.let {
            creatingViewModel.replaceInventory(inventory)
        } ?: */creatingViewModel.addInventory(inventory)

        parentFragmentManager.popBackStack()
    }

}
