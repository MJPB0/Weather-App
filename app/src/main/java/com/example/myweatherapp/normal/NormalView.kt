package com.example.myweatherapp.normal

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import com.example.myweatherapp.AppState
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.NormalViewFragmentBinding
import com.example.myweatherapp.network.service.ApiService
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round
import kotlin.math.roundToInt

class NormalView : Fragment() {
    private var _binding: NormalViewFragmentBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NormalViewFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        updateData("")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loc = view.findViewById<EditText>(R.id.locationContent)
        loc.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().contains("\n")) {
                    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)

                    loc.setText(loc.text.toString().replace("\n", "").trimEnd())
                    updateData(loc.text.toString().trimEnd())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        view.findViewById<Button>(R.id.simpleViewButton).apply {
            setOnClickListener {
                view.findNavController().navigate(R.id.action_normalView_to_simpleView)
            }
        }
    }

    private fun parseDate(value: Date): String {
        var day = ""
        var month = ""

        when (value.day) {
            1 -> day = "Poniedziałek"
            2 -> day = "Wtorek"
            3 -> day = "Środa"
            4 -> day = "Czwartek"
            5 -> day = "Piątek"
            6 -> day = "Sobota"
            7 -> day = "Niedziela"
        }

        when (value.month) {
            0 -> month = "Styczeń"
            1 -> month = "Luty"
            2 -> month = "Marzec"
            3 -> month = "Kwiecień"
            4 -> month = "Maj"
            5 -> month = "Czerwiec"
            6 -> month = "Lipiec"
            7 -> month = "Sierpień"
            8 -> month = "Wrzesień"
            9 -> month = "Październik"
            10 -> month = "Listipad"
            11 -> month = "Grudzień"
        }
        return "$day, ${value.date} $month"
    }

    private fun updateData(location: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val data = ApiService().getRawData(if (location == "") "Gliwice" else location)
            data?.let { AppState.updateData(it) }
        }

        AppState.data.observe(viewLifecycleOwner) {
            val timeTf = java.text.SimpleDateFormat("HH:mm", Locale.GERMANY)
            val dateTimeTf = java.text.SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.GERMANY)

            binding.currentSkyContent.text = getString(R.string.normal_desc, it.weather[0].description.replaceFirstChar { c -> c.uppercase() })
            binding.currentDegreesContent.text = getString(R.string.normal_temp, it.main.temp_max.roundToInt())
            binding.maximumDegreesContent.text = getString(R.string.normal_temp_max, it.main.temp_max.roundToInt())
            binding.minimumDegreesContent.text = getString(R.string.normal_temp_min, it.main.temp_min.roundToInt())
            binding.sunRiseContent.text = getString(R.string.normal_sunrise, timeTf.format(Date(it.sys.sunrise * 1000)))
            binding.sunSetContent.text = getString(R.string.normal_sunset, timeTf.format(Date(it.sys.sunset * 1000)))
            binding.feelDegreesContent.text = getString(R.string.normal_temp_feel, it.main.feels_like.roundToInt())
            binding.pressureContent.text = getString(R.string.normal_pressure, it.main.pressure)
            binding.humidityContent.text = "${it.main.humidity}%"
            binding.windSpeedContent.text = "${(it.wind.speed * 3.6f).roundToInt()} km/h"
            binding.dataUpdateContent.text = getString(
                R.string.normal_read_data,
                dateTimeTf.format(Date(it.dt * 1000))
            )
            binding.currentDateLabel.setText(parseDate(Date(it.dt * 1000)))
        }
    }
}