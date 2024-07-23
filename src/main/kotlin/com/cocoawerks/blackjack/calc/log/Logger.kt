package com.cocoawerks.blackjack.calc.log

class Logger {

    val log:MutableList<Loggable> = ArrayList()

    private var currentRound:Round? = null
    private var outOfRound = true
    private var roundIndex:Int = 0

    fun logNewRound() {
        currentRound = Round(roundIndex+1)
        roundIndex += 1
        outOfRound = false
    }

    fun logEndRound() {
        outOfRound = true
        if(currentRound != null) {
            log.add(currentRound!!)
            log.add(EndOfRoundEvent())
        }
        currentRound = null
    }

    fun log(event:Loggable) {
        if (outOfRound) {
            log.add(event)
        }
        else {
            currentRound?.addEvent(event)
        }
    }

    override fun toString(): String {
        var str = ""
        for (loggable in log) {
            str += loggable.description
            str += "\n"
        }
        return str
    }
}
