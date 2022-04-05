package com.example.myweatherapp.simple

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import com.example.myweatherapp.AppState
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.SimpleViewFragmentBinding
import com.example.myweatherapp.network.service.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

class SimpleView : Fragment() {
    private var _binding: SimpleViewFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SimpleViewFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        updateData("")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loc = view.findViewById<EditText>(R.id.simpleLocationContent)
        loc.addTextChangedListener(object:
            TextWatcher {
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

        view.findViewById<Button>(R.id.normalViewButton).apply {
            setOnClickListener {
                view.findNavController().navigate(R.id.action_simpleView_to_normalView)
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
        return "$day \n${value.date} $month \n${value.hours}:${value.minutes}"
    }

    private fun updateData(location: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val data = ApiService().getRawData(if (location == "") "Gliwice" else location)
            data?.let { AppState.updateData(it) }
        }

        AppState.data.observe(viewLifecycleOwner) {
            val timeTf = java.text.SimpleDateFormat("HH:mm", Locale.GERMANY)

            binding.simpleDegreesContent.text = getString(R.string.normal_temp_min, it.main.temp_max.roundToInt())
            binding.simpleSunRiseContent.text = getString(R.string.normal_sunrise, timeTf.format(Date(it.sys.sunrise * 1000)))
            binding.simpleSunSetContent.text = getString(R.string.normal_sunset, timeTf.format(Date(it.sys.sunset * 1000)))
            binding.simpleDateContent.setText(parseDate(Date(it.dt * 1000)))
        }
    }
}