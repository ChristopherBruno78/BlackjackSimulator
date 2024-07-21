package com.cocoawerks.blackjack.calc.log

class Logger {
    val rounds:MutableList<Round> = ArrayList()
    var round = -1

    fun logNewRound() {
        rounds.add(Round(rounds.size+1))
        round += 1
    }

    fun log(event:Event) {
        if (round > -1 && round < rounds.size) {
            rounds[round].addEvent(event)
        }
    }

    override fun toString(): String {
        var str = ""
        for (round in rounds) {
            str += round.toString()
        }
        return str
    }
}
