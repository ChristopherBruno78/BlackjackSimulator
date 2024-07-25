package com.cocoawerks.blackjack.calc.log

class Round(val index: Int) : Loggable {
    val events: MutableList<Loggable> = ArrayList()

    init {
        events.add(NewRoundEvent(index))
    }

    fun addEvent(event: Loggable) {
        events.add(event)
    }

    override val description: String
        get() {
            var str = ""
            for (event in events) {
                str += event.description
                str += "\n"
            }
            return str
        }
}
