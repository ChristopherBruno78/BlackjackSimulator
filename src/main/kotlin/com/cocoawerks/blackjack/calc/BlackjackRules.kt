package com.cocoawerks.blackjack.calc

import com.cocoawerks.blackjack.calc.cards.Hand

data class BlackjackRules(
    var numberOfDecks: Int = 6,
    var dealerHitsSoft17: Boolean = true,
    var blackjackPayoff: Double = 1.5,
    var deckPenetration: Double = 0.75,
    var canLateSurrender: Boolean = false,
    var canDoubleAfterSplit: Boolean = true,
    var splitToThisManyHands: Int = 4,
    var canResplitAces: Boolean = false,
    var splitAcesReceiveOneCard: Boolean = true,
    var doublesRestrictedToHardTotals: Array<Int> = emptyArray(),
    var europeanNoHoleCard: Boolean = false
) {

    companion object {
        fun defaultRules() = BlackjackRules()
    }

    fun canSurrender(hand: Hand): Boolean {
        return hand.numberOfCards == 2 && canLateSurrender
    }

    fun canSplit(hand: Hand): Boolean {

        if (!hand.isSplittable) {
            return false
        }

        if (hand.isFromSplitAces && canResplitAces) { //spitting aces
            return false
        }

        if (hand.rootHand.numberOfHands >= splitToThisManyHands) {
            return false
        }
        return true
    }

    fun canDouble(hand: Hand): Boolean {
        if (hand.numberOfCards != 2) {
            return false
        }

        val rootHand = hand.rootHand

        if (!canDoubleAfterSplit && rootHand.numberOfHands > 1) {
            return false
        }

        if (hand.isFromSplitAces) { //split aces
            return !splitAcesReceiveOneCard
        }

        if (doublesRestrictedToHardTotals.size > 0) {
            return doublesRestrictedToHardTotals.indexOf(hand.value()) > -1
        }
        return true
    }

    fun canPlay(hand: Hand): Boolean {
        return !(hand.isFromSplitAces && splitAcesReceiveOneCard)
    }

}
