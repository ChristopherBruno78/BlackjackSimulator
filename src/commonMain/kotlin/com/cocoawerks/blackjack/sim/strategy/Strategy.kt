package com.cocoawerks.blackjack.sim.strategy


interface Strategy {
    fun willTakeInsurance(): Boolean
    fun getPlayAction(state: HandState): Action
    fun getBet(): Double
    fun getNumberOfBettingSpots(): Int
    fun reset()
}