package com.cocoawerks.blackjack.calc.log

class Round(val index:Int) {
    val events:MutableList<Event> = ArrayList()

    init {
        events.add(NewRoundEvent(index))
    }

    fun addEvent(event:Event) {
        events.add(event)
    }

    override fun toString(): String {
        var str = ""
        for(event in events) {
            str += event.description
            str += "\n"
        }
        return str
    }
}
