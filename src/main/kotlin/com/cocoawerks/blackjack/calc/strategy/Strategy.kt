package com.cocoawerks.blackjack.calc.strategy

abstract class Strategy {

    private val playActions: MutableMap<HandState, Action> = HashMap()
    private val insuranceActions: MutableMap<Int, Boolean> = HashMap()
    private val surrenderActions: MutableMap<HandState, Boolean> = HashMap()
    private val wagerActions: MutableMap<Int, Double> = HashMap()
    private val numberOfBettingSpotsActions: MutableMap<Int, Int> = HashMap()

    var isCounting: Boolean = false

    fun setPlayAction(action: Action, forState: HandState) {
        playActions[forState] = action
    }

    fun getPlayAction(state: HandState): Action {
        return playActions[state]!!
    }

    fun setInsuranceAction(action: Boolean, atIndex: Int) {
        insuranceActions[atIndex] = action
    }

    fun getInsuranceAction(index: Int): Boolean {
        return insuranceActions[index]!!
    }

    fun setSurrenderAction(action: Boolean, forState: HandState) {
        surrenderActions[forState] = action
    }

    fun getSurrenderAction(state: HandState): Boolean {
        return surrenderActions[state]!!
    }

    fun setWagerAction(action: Double, forIndex: Int) {
        wagerActions[forIndex] = action
    }

    fun getWagerAction(index: Int): Double {
        if (!isCounting || !wagerActions.containsKey(index)) {
            return 1.0
        }
        return wagerActions[index]!!
    }

    fun setNumberOfBettingSpotsAction(action: Int, forIndex: Int) {
        numberOfBettingSpotsActions[forIndex] = action
    }

    fun getNumberOfBettingSpotsAction(index: Int): Int {
        if (!isCounting || !numberOfBettingSpotsActions.containsKey(index)) {
            return 1
        }
        return numberOfBettingSpotsActions[index]!!
    }
}
