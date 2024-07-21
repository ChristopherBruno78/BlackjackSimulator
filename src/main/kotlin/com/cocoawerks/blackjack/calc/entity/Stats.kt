package com.cocoawerks.blackjack.calc.entity

data class Stats(
    var name:String,
    var wins: Int = 0,
    var losses: Int = 0,
    var pushes: Int = 0,
    var surrenders: Int = 0,
    var bankroll: Double = 0.0
)
