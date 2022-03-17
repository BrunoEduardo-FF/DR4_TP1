package br.edu.infnet.dr4_tp1.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.edu.infnet.dr4_tp1.R

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private lateinit var lista: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_fragment, container, false)
        lista = view.findViewById(R.id.list_fragment_listview)
        viewModel = ViewModelProvider(this, ListViewModelFactory(activity))
            .get(ListViewModel::class.java)

        viewModel.lista.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                lista.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it)
            }
        }

        return view
    }
}