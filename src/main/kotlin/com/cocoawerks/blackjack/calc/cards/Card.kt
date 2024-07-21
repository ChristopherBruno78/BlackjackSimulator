package com.cocoawerks.blackjack.calc.cards

data class Card(val suit: Suit, val rank: Rank) {

    fun value(): Int {
        return rank.value()
    }

    override fun toString(): String {
        return "${rank.abbr}${suit.abbr}"
    }
}
