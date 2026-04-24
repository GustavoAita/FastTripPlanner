package com.sclprdm.fasttripplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sclprdm.fasttripplanner.databinding.ActivityOptionsBinding

 //OptionsActivity - Tela 2: Opçoes da Viagem
class OptionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOptionsBinding
    private var destination: String = ""
    private var days: Int = 0
    private var budget: Double = 0.0

    companion object {
        private const val KEY_ACCOMMODATION_SELECTED = "accommodation_selected"
        private const val KEY_TRANSPORT_CHECKED = "transport_checked"
        private const val KEY_FOOD_CHECKED = "food_checked"
        private const val KEY_TOURS_CHECKED = "tours_checked"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // pega dados da MainActivity
        retrieveIntentData()

        // retornar estado
        if (savedInstanceState != null) {
            restoreSavedState(savedInstanceState)
        }

        // settar botoes
        setupButtonListeners()

    }


     // passar dados via Intent:
    private fun retrieveIntentData() {
        destination = intent.getStringExtra(Constants.EXTRA_DESTINATION) ?: ""
        days = intent.getIntExtra(Constants.EXTRA_DAYS, 0)
        budget = intent.getDoubleExtra(Constants.EXTRA_BUDGET, 0.0)
    }

    private fun setupButtonListeners() {
        // volta MainActivity
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // calcula e vai para SummaryActivity
        binding.btnCalculate.setOnClickListener {
            proceedToSummary()
        }
    }


    private fun showReceivedData() {
        Toast.makeText(
            this,
            "Received: $destination, $days days, $$budget",
            Toast.LENGTH_SHORT
        ).show()
    }
    private fun getSelectedAccommodation(): String {
        return when (binding.rgAccommodation.checkedRadioButtonId) {
            binding.rbEconomic.id -> Constants.ACCOMMODATION_TYPE_ECONOMIC
            binding.rbComfort.id -> Constants.ACCOMMODATION_TYPE_COMFORT
            binding.rbLuxury.id -> Constants.ACCOMMODATION_TYPE_LUXURY
            else -> Constants.ACCOMMODATION_TYPE_ECONOMIC
        }
    }
    private fun proceedToSummary() {
        val accommodation = getSelectedAccommodation()
        val transport = binding.cbTransport.isChecked
        val food = binding.cbFood.isChecked
        val tours = binding.cbTours.isChecked

        // cria intent com os dados
        val intent = Intent(this, SummaryActivity::class.java).apply {
            putExtra(Constants.EXTRA_DESTINATION, destination)
            putExtra(Constants.EXTRA_DAYS, days)
            putExtra(Constants.EXTRA_BUDGET, budget)
            putExtra(Constants.EXTRA_ACCOMMODATION, accommodation)
            putExtra(Constants.EXTRA_TRANSPORT, transport)
            putExtra(Constants.EXTRA_FOOD, food)
            putExtra(Constants.EXTRA_TOURS, tours)
        }
        startActivity(intent)
    }

    // rotação
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(KEY_ACCOMMODATION_SELECTED, binding.rgAccommodation.checkedRadioButtonId)
        outState.putBoolean(KEY_TRANSPORT_CHECKED, binding.cbTransport.isChecked)
        outState.putBoolean(KEY_FOOD_CHECKED, binding.cbFood.isChecked)
        outState.putBoolean(KEY_TOURS_CHECKED, binding.cbTours.isChecked)
    }

    // restaura estado
    private fun restoreSavedState(savedInstanceState: Bundle) {
        val accommodationId = savedInstanceState.getInt(
            KEY_ACCOMMODATION_SELECTED,
            binding.rbEconomic.id
        )
        binding.rgAccommodation.check(accommodationId)

        binding.cbTransport.isChecked = savedInstanceState.getBoolean(KEY_TRANSPORT_CHECKED, false)
        binding.cbFood.isChecked = savedInstanceState.getBoolean(KEY_FOOD_CHECKED, false)
        binding.cbTours.isChecked = savedInstanceState.getBoolean(KEY_TOURS_CHECKED, false)
    }
}