package ru.palushin86.inventory.ui.parameters

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_parameters.*
import ru.palushin86.inventory.R
import ru.palushin86.inventory.entities.ParameterType

class ParametersFragment : Fragment(), DeleteParameterTypeListener {
    private lateinit var parametersViewModel: ParametersViewModel
    private lateinit var parametersAdapter: ParametersAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        parametersViewModel = ViewModelProvider
                .NewInstanceFactory()
                .create(ParametersViewModel::class.java)
        return inflater.inflate(R.layout.fragment_parameters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parametersRecyclerView = view.findViewById<RecyclerView>(R.id.rv_parameters_parameters)
        parametersRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        parametersRecyclerView.itemAnimator = DefaultItemAnimator()
        val items = parametersViewModel.getParameterTypes()
        parametersAdapter = ParametersAdapter(items, this)
        parametersRecyclerView.adapter = parametersAdapter

        setListeners()
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
                    createParameterType(etParameterType.text.toString()) }
                .setNegativeButton(android.R.string.no) { _, _ -> }
                .setView(dialogLayout)
                .create()
                .show()
        }
    }

    private fun createParameterType(text: String) {
        parametersViewModel.addParameterType(ParameterType(key = text))
        parametersAdapter.setItems(parametersViewModel.getParameterTypes())
        parametersAdapter.notifyDataSetChanged()
    }

    override fun delete(position: Int) {
        val builder =
            AlertDialog.Builder(context)
        builder.setMessage("Подтвердите удаление")
            .setPositiveButton("Удалить") { _, _ ->
                parametersViewModel.getParameterTypes()[position].id
                    ?.let {
                        parametersViewModel.removeParameterType(it)
                        parametersAdapter.setItems(parametersViewModel.getParameterTypes())
                    }
            }
            .setNegativeButton("Отмена") { _, _ -> }
            .create()
            .show()
    }
}
