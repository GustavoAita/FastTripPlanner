package com.sclprdm.fasttripplanner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sclprdm.fasttripplanner.databinding.ActivitySummaryBinding


 // SummaryActivity - tela 3: resumo com o calculo
class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding
    private var destination: String = ""
    private var days: Int = 0
    private var budget: Double = 0.0
    private var accommodation: String = ""
    private var transportSelected: Boolean = false
    private var foodSelected: Boolean = false
    private var toursSelected: Boolean = false
    private var totalCost: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // pega dados do intent
        retrieveIntentData()

        // chega rotacao
        if (savedInstanceState != null) {
            totalCost = savedInstanceState.getDouble("total_cost", 0.0)
        } else {
            // calculo
            calculateTotalCost()
        }

        // mostra info
        displayTripSummary()

        // setta botao reset
        binding.btnRestart.setOnClickListener {
            restartPlanning()
        }
    }

    private fun retrieveIntentData() {
        destination = intent.getStringExtra(Constants.EXTRA_DESTINATION) ?: ""
        days = intent.getIntExtra(Constants.EXTRA_DAYS, 0)
        budget = intent.getDoubleExtra(Constants.EXTRA_BUDGET, 0.0)
        accommodation = intent.getStringExtra(Constants.EXTRA_ACCOMMODATION) ?: ""
        transportSelected = intent.getBooleanExtra(Constants.EXTRA_TRANSPORT, false)
        foodSelected = intent.getBooleanExtra(Constants.EXTRA_FOOD, false)
        toursSelected = intent.getBooleanExtra(Constants.EXTRA_TOURS, false)
    }

    /**
     * calculo
     *
     * Formula:
     * custo_base = dias * orcamento_dia
     * custo_acomodacao = custo_base * multiplcador
     * custo_extra = transporte + comida + passeio
     * custo_total = custo_acomodacao + custo_extra
     */
    private fun calculateTotalCost() {
        val baseCost = days * budget

        val accommodationMultiplier = getAccommodationMultiplier(accommodation)

        val accommodatedCost = baseCost * accommodationMultiplier

        var extrasCost = 0.0

        // adiciona o custo do transporte
        if (transportSelected) {
            extrasCost += Constants.EXTRA_COST_TRANSPORT
        }

        // adiciona custo comida por dia
        if (foodSelected) {
            val foodCost = Constants.EXTRA_COST_FOOD_PER_DAY * days
            extrasCost += foodCost
        }

        // adiciona custo de passeio por dia
        if (toursSelected) {
            val toursCost = Constants.EXTRA_COST_TOURS_PER_DAY * days
            extrasCost += toursCost
        }

        totalCost = accommodatedCost + extrasCost
    }

    //pega as constantes de multiplicador de acomodacao
    private fun getAccommodationMultiplier(accommodationType: String): Double {
        return when (accommodationType) {
            Constants.ACCOMMODATION_TYPE_ECONOMIC -> Constants.ACCOMMODATION_ECONOMIC
            Constants.ACCOMMODATION_TYPE_COMFORT -> Constants.ACCOMMODATION_COMFORT
            Constants.ACCOMMODATION_TYPE_LUXURY -> Constants.ACCOMMODATION_LUXURY
            else -> Constants.ACCOMMODATION_ECONOMIC
        }
    }

    // faz uma lista pra mostrar quais extras tem
    private fun getSelectedExtrasString(): String {
        val extras = mutableListOf<String>()

        if (transportSelected) {
            extras.add("Transporte")
        }
        if (foodSelected) {
            extras.add("Comida")
        }
        if (toursSelected) {
            extras.add("Passeios")
        }

        return if (extras.isEmpty()) {
            "-"
        } else {
            extras.joinToString(", ")
        }
    }

    //seta um textview pra mostrar as infos
    private fun displayTripSummary() {
        // destino
        binding.tvDestination.text = destination

        // dias
        binding.tvDays.text = "$days dia(s)"

        // custo por idia
        binding.tvBudget.text = "R$${"%.2f".format(budget)}/dia"

        // acomodacao
        binding.tvAccommodation.text = accommodation

        // extras
        binding.tvExtras.text = getSelectedExtrasString()

        // custo total
        binding.tvTotalCost.text = "R$${"%.2f".format(totalCost)}"
    }

    //logica pra reseta e volta pra main
    private fun restartPlanning() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        finish()
    }

    // rotacao
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("total_cost", totalCost)
    }
}