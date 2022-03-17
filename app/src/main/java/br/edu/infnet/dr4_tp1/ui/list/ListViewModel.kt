package br.edu.infnet.dr4_tp1.ui.list

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class ListViewModel(private val activity: FragmentActivity?) : ViewModel() {
    private var _lista = MutableLiveData<List<String>>()
    var lista: LiveData<List<String>> = _lista

    init {
        populaLista()
    }

    private fun populaLista() {
        val extDir = activity?.getExternalFilesDir(null)
        val list: MutableList<String> = mutableListOf()
        for(file in extDir!!.list()) {
            list.add(file)
        }
        _lista.value = list
    }


}