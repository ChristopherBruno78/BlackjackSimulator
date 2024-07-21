package com.cocoawerks.blackjack.calc.cards

import com.cocoawerks.blackjack.calc.entity.Entity

class Hand(val wager: Double = 0.0) {
    val cards: MutableList<Card> = ArrayList()
    val splits: MutableList<Hand> = ArrayList()

    var isDouble: Boolean = false
    var isSurrendered: Boolean = false
    var isWin: Boolean = false
    var isPush: Boolean = false
    var timesSplit: Int = 0

    var owner: Entity? = null

    fun addCard(card: Card?) {
        if (card != null) {
            cards.add(card)
        }
    }

    fun addSplit(hand: Hand) {
        splits.add(hand)
    }

    fun cardAt(index: Int): Card? {
        if (index > -1 && index < cards.size) {
            return cards[index]
        }
        return null
    }

    private fun calculateValue(): Pair<Int, Int> {
        var softValue = 0
        var hardValue = 0

        for (card in cards) {
            if (card.rank == Rank.Ace && softValue + 11 < 22) {
                softValue += 10
            }
            softValue += card.value()
            hardValue += card.value()
        }

        if (softValue > 21) {
            return Pair(hardValue, hardValue)
        }

        return Pair(softValue, hardValue)
    }

    fun value(): Int {
        val value = calculateValue()
        if (value.first > 21) {
            return value.second
        }
        return value.first
    }

    val isSoft: Boolean
        get() {
            val value = calculateValue()
            return value.second < value.first
        }

    val isPair: Boolean
        get() {
            if (numberOfCards == 2) {
                val card1 = cardAt(0)
                val card2 = cardAt(1)
                if (card1?.rank == card2?.rank) {
                    return true
                }
            }
            return false
        }

    val isBlackjack: Boolean
        get() {
            return numberOfCards == 2 && value() == 21
        }

    val isBusted: Boolean
        get() {
            return value() > 21
        }

    val finalWager: Double
        get() {
            if (isDouble) {
                return 2.0 * wager
            }
            return wager
        }

    val numberOfCards: Int
        get() {
            return cards.size
        }

    val hash: HandHash
        get() {
            return HandHash(isSoft = isSoft, isPair = isPair, total = value())
        }

    override fun toString(): String {
        var str = ""
        var i = 0
        for (card in cards) {
            if (i > 0) {
                str += ","
            }
            str += card.toString()
            i += 1
        }
        if (isBlackjack) {
            str += " (Blackjack!)"
        } else {
            str += " (${if (isSoft) "soft " else ""}${this.value()})"
        }

        return str
    }
}
