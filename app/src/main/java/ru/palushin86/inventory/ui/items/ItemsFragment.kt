package ru.palushin86.inventory.ui.items

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.database.MatrixCursor
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.fragment_items.*
import ru.palushin86.inventory.R
import ru.palushin86.inventory.entities.FilterSet
import ru.palushin86.inventory.entities.Parameter
import ru.palushin86.inventory.ui.SwipeToDeleteCallback
import ru.palushin86.inventory.ui.creator.CreatingFragment
import ru.palushin86.inventory.ui.onItemClick
import ru.palushin86.inventory.ui.textChanged

class ItemsFragment : Fragment(), DeleteInventoryListener, DeleteFilterListener, LongClickListener, FilterSetActions {
    private lateinit var itemsViewModel: ItemsViewModel
    private lateinit var filtersAdapter: FiltersAdapter
    private lateinit var filterSetAdapter: FilterSetAdapter
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
        itemsViewModel = ViewModelProvider
            .NewInstanceFactory()
            .create(ItemsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filtersRecyclerView = view.findViewById(R.id.rv_filters)
        val filters = itemsViewModel.filters
        filtersRecyclerView.layoutManager = GridLayoutManager(context, 3)
        filtersRecyclerView.itemAnimator = DefaultItemAnimator()
        filtersAdapter = FiltersAdapter(filters, this)
        filtersRecyclerView.adapter = filtersAdapter

        val fastFilters = itemsViewModel.getFilterSets()
        rv_prepared_filters.layoutManager = GridLayoutManager(context, 3)
        rv_prepared_filters.itemAnimator = DefaultItemAnimator()
        filterSetAdapter = FilterSetAdapter(fastFilters, this)
        rv_prepared_filters.adapter = filterSetAdapter

        val inventoriesRecyclerView = view.findViewById<RecyclerView>(R.id.rv_items)
        inventoriesRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        inventoriesRecyclerView.itemAnimator = DefaultItemAnimator()
        inventoriesAdapter = ParentAdapter(itemsViewModel.getFilteredInventories(), this)
        inventoriesRecyclerView.adapter = inventoriesAdapter

        val swipeHandler = object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Подтвердите удаление")
                    .setPositiveButton("Удалить") { _, _ ->
                        val position = viewHolder.adapterPosition
                        val deleteInventory = itemsViewModel.getFilteredInventories()[position]
                        deleteInventory.id
                            ?.let {
                                itemsViewModel.deleteEquipment(it)
                                inventoriesAdapter.setItems(itemsViewModel.getFilteredInventories())
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
        bnt_add_filter_set.setOnClickListener { createFilterSet() }
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

        searchViewItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                layout_filters.visibility = RecyclerView.VISIBLE
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                layout_filters.visibility = RecyclerView.GONE
                return true
            }
        } )

        val searchText =
            searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete
        searchText.threshold = 1

        searchView.textChanged { query ->
            runFilter(searchView, query)
        }
    }

    private fun runFilter(searchView: SearchView, query: String) {
        val elements = itemsViewModel.getFilters(query)
        val columns = arrayOf("_id", "text", "value")
        val cursor = MatrixCursor(columns)

        elements.forEachIndexed { index, parameter ->
            cursor.addRow(arrayOf(index, parameter.tag.key, parameter.value));
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

        if (!itemsViewModel.filters.contains(parameter)) {
            itemsViewModel.filters.add(parameter)
            updateFiltersView()
        }

    }

    private fun updateFiltersView() {
        filtersAdapter.notifyDataSetChanged()

        if (itemsViewModel.filters.size == 0) {
            filtersRecyclerView.visibility = RecyclerView.GONE
        } else {
            filtersRecyclerView.visibility = RecyclerView.VISIBLE
        }

        inventoriesAdapter.setItems(itemsViewModel.getFilteredInventories())
    }

    override fun delete(position: Int) {
        itemsViewModel.getFilteredInventories()[position].id
            ?.let {
                itemsViewModel.deleteEquipment(id)
            }
    }

    override fun deleteFilter(position: Int) {
        itemsViewModel.filters.removeAt(position)
        updateFiltersView()
    }

    override fun longClicked(position: Int) {
        val id = itemsViewModel.getFilteredInventories()[position].id
        openCreatingFragment(id)
    }

    override fun createFilterSet() {
        val inflater = activity?.layoutInflater
        val dialogLayout = inflater?.inflate(R.layout.dialog_add_parameter, null)
        val etParameterType  = dialogLayout?.findViewById<EditText>(R.id.et_key)
        AlertDialog.Builder(context)
            .setTitle("Введите имя для нового сета")
            .setCancelable(false)
            .setPositiveButton(android.R.string.yes) { _, _ ->
                createNewFilterSet(etParameterType?.text.toString()) }
            .setNegativeButton(android.R.string.no) { _, _ -> }
            .setView(dialogLayout)
            .create()
            .show()
        itemsViewModel.filters
    }

    private fun createNewFilterSet(name: String) {
        val filterSet = FilterSet(name, itemsViewModel.filters)
        itemsViewModel.insertFilterSet(filterSet)

        filterSetAdapter.setItems(itemsViewModel.getFilterSets())
    }

    override fun onClick(position: Int) {
        val set = itemsViewModel.getFilterSets()[position]
        println(position)
        println(set)
        itemsViewModel.filters.clear()
        itemsViewModel.filters.addAll(set.set)
        updateFiltersView()
    }
}






