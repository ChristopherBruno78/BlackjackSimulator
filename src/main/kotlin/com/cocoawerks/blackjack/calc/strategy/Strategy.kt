package com.cocoawerks.blackjack.calc.strategy


interface Strategy {
     fun willTakeInsurance():Boolean
     fun getPlayAction(state: HandState):Action
     fun setPlayAction(action: Action, forState: HandState)
     fun getBet(): Double
     fun getNumberOfBettingSpots():Int
     fun reset()
}