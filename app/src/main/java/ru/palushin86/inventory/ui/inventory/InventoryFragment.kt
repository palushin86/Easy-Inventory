package ru.palushin86.inventory.ui.inventory

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.database.MatrixCursor
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.fragment_items.*
import ru.palushin86.inventory.R
import ru.palushin86.inventory.entities.Parameter
import ru.palushin86.inventory.ui.SwipeToDeleteCallback
import ru.palushin86.inventory.ui.creator.CreatingFragment
import ru.palushin86.inventory.ui.onItemClick
import ru.palushin86.inventory.ui.textChanged

class InventoryFragment : Fragment(), DeleteInventoryListener, DeleteFilterListener, LongClickListener {
    private lateinit var inventoryViewModel: InventoryViewModel
    private lateinit var filtersAdapter: FiltersAdapter
    private lateinit var inventoriesAdapter: ParentAdapter
    lateinit var filtersRecyclerView: RecyclerView

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inventoryViewModel = ViewModelProvider
            .NewInstanceFactory()
            .create(InventoryViewModel::class.java)
        return inflater.inflate(R.layout.fragment_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filtersRecyclerView = view.findViewById(R.id.rv_search_results)
        val filters = inventoryViewModel.filters
        filtersRecyclerView.layoutManager = GridLayoutManager(context, 3)
        filtersRecyclerView.itemAnimator = DefaultItemAnimator()
        filtersAdapter = FiltersAdapter(filters, this)
        filtersRecyclerView.adapter = filtersAdapter

        val inventoriesRecyclerView = view.findViewById<RecyclerView>(R.id.rv_items)
        inventoriesRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        inventoriesRecyclerView.itemAnimator = DefaultItemAnimator()
        inventoriesAdapter = ParentAdapter(inventoryViewModel.getFilteredInventories(), this)
        inventoriesRecyclerView.adapter = inventoriesAdapter

        val swipeHandler = object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Подтвердите удаление")
                    .setPositiveButton("Удалить") { _, _ ->
                        val position = viewHolder.adapterPosition
                        val deleteInventory = inventoryViewModel.getFilteredInventories()[position]
                        deleteInventory.id
                            ?.let {
                                inventoryViewModel.deleteEquipment(it)
                                inventoriesAdapter.setItems(inventoryViewModel.getFilteredInventories())
                            }
                    }
                    .setNegativeButton("Отмена") { _, _ ->
                        inventoriesAdapter.notifyDataSetChanged()
                    }
                    .create()
                    .show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(inventoriesRecyclerView)

        setListeners()
    }

    private fun setListeners() {
        add_equipment.setOnClickListener { addEquipmentBtnClicked() }
    }

    private fun addEquipmentBtnClicked() {
        openCreatingFragment(null)

    }

    private fun openCreatingFragment(id: Int?) {
        val fragment = CreatingFragment()
        id?.let {
            val bundle = Bundle()
            bundle.putInt("inventory_id", id)
            fragment.arguments = bundle
        }
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack("A")
            .commit()
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.inventory_toolbar_menu, menu)

        val searchViewItem = menu.findItem(R.id.action_search)
        val searchView = searchViewItem.actionView as SearchView

        val searchText =
            searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete
        searchText.threshold = 1

        searchView.textChanged { query ->
            runFilter(searchView, query)
        }
    }

    private fun runFilter(searchView: SearchView, query: String) {
        val elements = inventoryViewModel.getFilters(query)
        val columns = arrayOf("_id", "text", "value")
        val cursor = MatrixCursor(columns)

        elements.forEachIndexed { index, parameter ->
            cursor.addRow(arrayOf(index, parameter.key, parameter.value));
        }

        val cursorAdapter = SimpleCursorAdapter(
            context,
            R.layout.search_result_item,
            cursor,
            arrayOf("text", "value"),
            intArrayOf(R.id.search_result_item_key, R.id.search_result_item_value)
            , 0
        )

        searchView.suggestionsAdapter = cursorAdapter
        searchView.onItemClick { position ->
            addNewFilter(elements[position])
            searchView.setQuery("", false)
//            searchView.clearFocus()
        }
    }

    private fun addNewFilter(parameter: Parameter) {
        if (!inventoryViewModel.filters.contains(parameter)) {
            inventoryViewModel.filters.add(parameter)
            updateFiltersView()
        }
    }

    private fun updateFiltersView() {
        filtersAdapter.notifyDataSetChanged()

        if (inventoryViewModel.filters.size == 0) {
            filtersRecyclerView.visibility = RecyclerView.GONE
        } else {
            filtersRecyclerView.visibility = RecyclerView.VISIBLE
        }

        inventoriesAdapter.setItems(inventoryViewModel.getFilteredInventories())
    }

    override fun delete(position: Int) {
        inventoryViewModel.getFilteredInventories()[position].id
            ?.let {
                inventoryViewModel.deleteEquipment(id)
            }
    }

    override fun deleteFilter(position: Int) {
        inventoryViewModel.filters.removeAt(position)
        updateFiltersView()
        inventoriesAdapter.setItems(inventoryViewModel.getFilteredInventories())
    }

    override fun longClicked(position: Int) {
        val id = inventoryViewModel.getFilteredInventories()[position].id
        openCreatingFragment(id)
    }
}






