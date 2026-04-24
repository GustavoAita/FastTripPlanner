package com.sclprdm.fasttripplanner

object Constants {

    // Constantes p/ passar dados entre activities
    const val EXTRA_DESTINATION = "destination"
    const val EXTRA_DAYS = "days"
    const val EXTRA_BUDGET = "budget"
    const val EXTRA_ACCOMMODATION = "accommodation"
    const val EXTRA_TRANSPORT = "transport"
    const val EXTRA_FOOD = "food"
    const val EXTRA_TOURS = "tours"

    // Multiplicador de acomodação
    const val ACCOMMODATION_ECONOMIC = 1.0
    const val ACCOMMODATION_COMFORT = 1.5
    const val ACCOMMODATION_LUXURY = 2.2

    // Custos extras
    const val EXTRA_COST_TRANSPORT = 300.0
    const val EXTRA_COST_FOOD_PER_DAY = 50.0
    const val EXTRA_COST_TOURS_PER_DAY = 120.0

    // Tipos de acomodação
    const val ACCOMMODATION_TYPE_ECONOMIC = "Economico"
    const val ACCOMMODATION_TYPE_COMFORT = "Confortavel"
    const val ACCOMMODATION_TYPE_LUXURY = "Luxuoso"
}