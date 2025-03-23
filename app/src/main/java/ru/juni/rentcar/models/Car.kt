package ru.juni.rentcar.models

data class Car(
    val id: String,
    val brand: String,
    val model: String,
    val price: Int,
    val imageUrl: String,
    val transmission: String,
    val fuelType: String,
    val year: Int,
    val doors: Int,
    val seats: Int,
    val description: String
) 