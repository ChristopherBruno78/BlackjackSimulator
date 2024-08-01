package com.cocoawerks.blackjack.sim.cards

import com.cocoawerks.blackjack.sim.entity.Entity

class Hand(val wager: Double = 0.0) {
    val cards: MutableList<Card> = ArrayList()

    var isDouble: Boolean = false
    var isInsured: Boolean = false
    var ignorePair:Boolean = false

    var rootHand: Hand = this //for split hands
    val splits: MutableList<Hand> = ArrayList()

    var owner: Entity? = null

    fun addCard(card: Card?) {
        if (card != null) {
            cards.add(card)
        }
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
            if(ignorePair) {
                return false
            }
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
            return !isFromSplit && numberOfCards == 2 && value() == 21
        }

    val isBusted: Boolean
        get() {
            return value() > 21
        }

    val isSplittable: Boolean
        get() {
            return isPair && splits.isEmpty()
        }

    val isFromSplit: Boolean
        get() {
            return rootHand != this
        }

    val isFromSplitAces: Boolean
        get() {
            return isFromSplit && (cardAt(0)?.rank == Rank.Ace)
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

    //number of hands from splits
    val numberOfHands: Int
        get() {
            if (splits.isEmpty()) {
                return 1
            }
            var count = 0
            for (split in splits) {
                count += split.numberOfHands
            }
            return count
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
            val v = value()
            str += " (${if (isSoft) "soft " else ""}${v})"
        }

        return str
    }
}
