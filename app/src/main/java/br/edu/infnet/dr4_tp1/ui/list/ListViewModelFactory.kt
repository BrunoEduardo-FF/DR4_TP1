package br.edu.infnet.dr4_tp1.ui.list

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListViewModelFactory(private val activity: FragmentActivity?): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(activity) as T
        }
        throw IllegalArgumentException("Classe não conhecida.")
    }
}