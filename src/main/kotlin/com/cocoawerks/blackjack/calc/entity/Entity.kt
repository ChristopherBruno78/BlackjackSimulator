package com.cocoawerks.blackjack.calc.entity

import com.cocoawerks.blackjack.calc.BlackjackGame
import com.cocoawerks.blackjack.calc.BlackjackRules
import com.cocoawerks.blackjack.calc.cards.Hand
import com.cocoawerks.blackjack.calc.log.*
import com.cocoawerks.blackjack.calc.stats.PlayerStats
import com.cocoawerks.blackjack.calc.strategy.Action
import com.cocoawerks.blackjack.calc.strategy.HandState
import com.cocoawerks.blackjack.calc.strategy.Strategy

abstract class Entity(val name: String, val strategy: Strategy) {

    var hands: MutableList<Hand> = ArrayList()

    private val _stats: PlayerStats = PlayerStats(name)

    var logger: Logger? = null

    val stats: PlayerStats
        get() = _stats

    val numberOfHands: Int
        get() {
            return hands.size
        }


    internal fun log(event: Loggable) {
        logger?.log(event)
    }


    /** All the entity's hands, including those create by splitting */
    val allHands: List<Hand>
        get() {
            val all = ArrayList<Hand>()
            for (hand in hands) {
                all.addAll(terminalHands(hand))
            }
            return all
        }


    private fun terminalHands(hand: Hand): List<Hand> {
        val h = ArrayList<Hand>()
        if (hand.splits.size == 0) {
            h.add(hand)
        } else {
            for (splitHand in hand.splits) {
                h.addAll(terminalHands(splitHand))
            }
        }
        return h
    }

    fun processLoss(hand: Hand, rules: BlackjackRules? = null) {
        if (hand.isBlackjack) {
            stats.bankroll -= ((rules?.blackjackPayoff ?: 1.0) * hand.finalWager)
        } else {
            stats.bankroll -= hand.finalWager
        }
        stats.losses += 1
        log(LossEvent(stats = stats.copy()))
    }

    fun processWin(hand: Hand, rules: BlackjackRules? = null) {
        if (hand.isBlackjack) {
            stats.bankroll += ((rules?.blackjackPayoff ?: 1.0) * hand.finalWager)
        } else {
            stats.bankroll += hand.finalWager
        }
        stats.wins += 1
        log(WinEvent(stats = stats.copy()))
    }

    fun processPush(hand: Hand) {
        stats.pushes += 1
        log(PushEvent(stats = stats.copy()))
    }

    fun processSurrender(hand: Hand) {
        stats.surrenders += 1
        stats.bankroll -= 0.5 * hand.finalWager
        log(SurrenderEvent(stats = stats.copy()))
    }

    abstract fun playHand(hand: Hand, forGame: BlackjackGame? = null): Action?

    fun makeDecision(state: HandState): Action {
        return strategy.getPlayAction(state)
    }

    fun clearHands() {
        hands.clear()
    }

    override fun toString(): String {
        var str = "${name} has hand"
        if (allHands.size > 1) {
            str += "s"
        }

        str += "\n"

        for (hand in allHands) {
            str += "- ${hand}\n"
        }
        return str
    }
}
