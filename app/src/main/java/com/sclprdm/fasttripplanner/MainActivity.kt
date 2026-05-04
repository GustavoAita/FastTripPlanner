package com.sclprdm.fasttripplanner

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sclprdm.fasttripplanner.databinding.ActivityMainBinding
import android.content.Intent

// MainActivity - tela 1 - dados da viagem
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val KEY_DESTINATION = "destination_state"
        private const val KEY_DAYS = "days_state"
        private const val KEY_BUDGET = "budget_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // retorna estado
        if (savedInstanceState != null) {
            binding.etDestination.setText(savedInstanceState.getString(KEY_DESTINATION, ""))
            binding.etDays.setText(savedInstanceState.getString(KEY_DAYS, ""))
            binding.etBudget.setText(savedInstanceState.getString(KEY_BUDGET, ""))
        }

        // listener pro botao proximo
        binding.btnNext.setOnClickListener {
            validateAndProceed()
        }
    }

    // validador campos
    private fun validateAndProceed() {
        val destination = binding.etDestination.text.toString().trim()
        val daysStr = binding.etDays.text.toString().trim()
        val budgetStr = binding.etBudget.text.toString().trim()

        hideAllErrors()

        // destino
        if (!isDestinationValid(destination)) {
            binding.tvErrorDestination.text = getString(R.string.error_empty_destination)
            binding.tvErrorDestination.visibility = View.VISIBLE
            return
        }

        // dias
        if (!isDaysValid(daysStr)) {
            binding.tvErrorDays.text = getString(R.string.error_invalid_days)
            binding.tvErrorDays.visibility = View.VISIBLE
            return
        }

        // orcamento
        if (!isBudgetValid(budgetStr)) {
            binding.tvErrorBudget.text = getString(R.string.error_invalid_budget)
            binding.tvErrorBudget.visibility = View.VISIBLE
            return
        }

        proceedToNextScreen(destination, daysStr.toInt(), budgetStr.toDouble())
    }

    private fun isDestinationValid(destination: String): Boolean {
        return destination.isNotEmpty()
    }

    private fun isDaysValid(days: String): Boolean {
        return try {
            days.isNotEmpty() && days.toInt() > 0
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun isBudgetValid(budget: String): Boolean {
        return try {
            budget.isNotEmpty() && budget.toDouble() > 0
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun hideAllErrors() {
        binding.tvErrorDestination.visibility = View.GONE
        binding.tvErrorDays.visibility = View.GONE
        binding.tvErrorBudget.visibility = View.GONE
    }

    private fun proceedToNextScreen(destination: String, days: Int, budget: Double) {
        val intent = Intent(this, OptionsActivity::class.java).apply {
            putExtra(Constants.EXTRA_DESTINATION, destination)
            putExtra(Constants.EXTRA_DAYS, days)
            putExtra(Constants.EXTRA_BUDGET, budget)
        }
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(KEY_DESTINATION, binding.etDestination.text.toString())
        outState.putString(KEY_DAYS, binding.etDays.text.toString())
        outState.putString(KEY_BUDGET, binding.etBudget.text.toString())
    }
}