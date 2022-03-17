package br.edu.infnet.dr4_tp1.ui.registrador

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.infnet.dr4_tp1.R
import java.io.File
import java.io.FileOutputStream
import java.util.*

class RegistradorFragment : Fragment(), LocationListener {

    val FINE_REQUEST = 2

    private lateinit var btnRegistrar: Button
    private lateinit var btnListar: Button
    private var latitude: String = ""
    private var longitude: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.registrador_fragment, container, false)
        btnRegistrar = view.findViewById(R.id.registrador_fragment_btn_registrar)
        btnListar = view.findViewById(R.id.registrador_fragment_btn_listagem)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRegistrar.setOnClickListener {
            getLocationByGps()
            salvarArquivo()
        }
        btnListar.setOnClickListener {
            findNavController().navigate(R.id.listFragment)
        }
    }

    private fun getLocationByGps() {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(isProviderEnabled) {
            if(requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    2000L,
                    0f,
                    this
                )
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                latitude = location?.latitude.toString()
                longitude = location?.longitude.toString()

            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_REQUEST)
            }
        }
    }

    private fun salvarArquivo() {
        val horarioString = horarioAtual(Calendar.getInstance())
        val extArq = ".crd"
        if (isExternalStorageWritable()) {
            val arquivo = criaArquivo(horarioString, extArq)
            escreveEmArquivo(arquivo, "latitude:${latitude}\nlongitude:${longitude}\n")
            makeToast("Localização salva com sucesso no arquivo ${arquivo.name}.")

        } else {
            makeToast("Não foi possível salvar o arquivo de localização.")
        }
    }

    private fun horarioAtual(calendar: Calendar): String {
        val ano = calendar.get(Calendar.YEAR)
        val mes = calendar.get(Calendar.MONTH) + 1
        val dia = calendar.get(Calendar.DAY_OF_MONTH)
        var hora = calendar.get(Calendar.HOUR_OF_DAY) - 3
        if(hora < 0) hora += 24
        val minuto = calendar.get(Calendar.MINUTE)
        val segundo = calendar.get(Calendar.SECOND)

        return "${ano}-${mes}-${dia}-${hora}-${minuto}-${segundo}"

    }

    private fun criaArquivo(nomeArq: String, extArq: String): File {
        val extDir = activity?.getExternalFilesDir(null)
        val arq = File(extDir, nomeArq + extArq)
        if (!arq.exists()) arq.createNewFile()
        return arq
    }

    private fun escreveEmArquivo(arquivo: File, texto: String) {
        val fos = FileOutputStream(arquivo)
        fos.write(texto.toByteArray())
        fos.close()
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocationByGps()
        }
    }

    override fun onLocationChanged(p0: Location) {
        TODO("Not yet implemented")
    }

    fun makeToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}