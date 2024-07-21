package com.cocoawerks.blackjack.calc

import com.cocoawerks.blackjack.calc.cards.Hand
import com.cocoawerks.blackjack.calc.cards.Rank

data class BlackjackRules(
    var numberOfDecks: Int = 6,
    var dealerHitsSoft17: Boolean = true,
    var blackjackPayoff: Double = 1.5,
    var deckPenetration: Double = 0.75,
    var surrenderIsAllowed: Boolean = false,
    var doubleAfterSplitAllowed: Boolean = true,
    var numberOfSplitsAllowed: Int = 2,
    var acesSplitOnce: Boolean = true,
    var doublesRestrictedToHardTotals: Array<Int> = emptyArray(),
    var europeanNoHoleCard: Boolean = false
) {

    companion object   {
        fun defaultRules() = BlackjackRules()
    }

    fun isSurrenderAllowed(hand: Hand): Boolean {
        return hand.numberOfCards == 2 && surrenderIsAllowed
    }

    fun isSplitAllowed(hand: Hand): Boolean {
        if (!hand.isPair) {
            return false
        }
        val numberOfTimesSplit = hand.timesSplit
        if (hand.cardAt(0)?.rank == Rank.Ace) {
            if (numberOfTimesSplit > 1 && acesSplitOnce) {
                return false
            }
        }
        if (numberOfTimesSplit >= numberOfSplitsAllowed) {
            return false
        }
        return true
    }

    fun isDoubleAllowed(hand: Hand): Boolean {
        if (hand.numberOfCards != 2) {
            return false
        }

        if (!doubleAfterSplitAllowed && hand.timesSplit > 0) {
            return false
        }

        if (doublesRestrictedToHardTotals.size > 0) {
            return doublesRestrictedToHardTotals.indexOf(hand.value()) > -1
        }
        return true
    }
}
