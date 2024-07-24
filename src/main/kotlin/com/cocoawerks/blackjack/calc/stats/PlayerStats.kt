package com.cocoawerks.blackjack.calc.stats

data class PlayerStats(
    var name:String,
    var wins: Int = 0,
    var losses: Int = 0,
    var pushes: Int = 0,
    var surrenders: Int = 0,
    var bankroll:Double = 0.0,
    var bustedHands:Int = 0,
    var bankrollAtShuffle:ArrayList<Double> = ArrayList(),
    var bets:ArrayList<Double> = ArrayList()
)
