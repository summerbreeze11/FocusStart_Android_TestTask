package com.example.myapplication.retrofit

import java.util.Date


data class Card(
    var date: String,
    val bank: Bank,
    val brand: String,
    val country: Country,
    val number: Number,
    val prepaid: Boolean,
    val scheme: String,
    val type: String,
)