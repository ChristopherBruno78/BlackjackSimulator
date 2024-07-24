package com.cocoawerks.blackjack.calc.entity

import com.cocoawerks.blackjack.calc.BlackjackGame
import com.cocoawerks.blackjack.calc.cards.Card
import com.cocoawerks.blackjack.calc.cards.Hand
import com.cocoawerks.blackjack.calc.log.*
import com.cocoawerks.blackjack.calc.strategy.Action
import com.cocoawerks.blackjack.calc.strategy.CountingStrategy
import com.cocoawerks.blackjack.calc.strategy.HandState
import com.cocoawerks.blackjack.calc.strategy.Strategy

open class Player(name: String, strategy: Strategy) : Entity(name, strategy) {

    fun bet() {
        val wager = strategy.getBet()
        val spots = strategy.getNumberOfBettingSpots()
        if (wager > 0) {
            for (i in 1..spots) {
                val hand = Hand(wager = wager)
                hand.owner = this
                hands.add(hand)
                log(BetPlacedEvent(stats = stats.copy(), wager = wager))
            }
        }
        else {
            log(BetPlacedEvent(stats = stats.copy(), wager = 0.0))
        }
        stats.bets.add(wager)
    }

    fun splitHand(hand: Hand, card1: Card?, card2: Card?): Array<Hand> {
        if (hand.isPair) {
            val hand1 = Hand(wager = hand.wager)
            hand1.addCard(hand.cardAt(0))
            hand1.addCard(card1)
            hand1.owner = this

            val hand2 = Hand(wager = hand.wager)
            hand2.addCard(hand.cardAt(1))
            hand2.addCard(card2)
            hand2.owner = this

            hand1.rootHand = hand.rootHand
            hand2.rootHand = hand.rootHand

            hand.splits.add(hand1)
            hand.splits.add(hand2)

            return hand.splits.toTypedArray()
        }
        return emptyArray()
    }

    private fun playHit(hand: Hand, forGame: BlackjackGame): Action? {
        log(PlayActionEvent(stats = stats.copy(), action = Action.Hit))
        hand.addCard(forGame.takeVisibleCardFromDealer())
        log(HandChangedEvent(stats = stats.copy(), hand = hand))
        if (handleIfBusted(hand, forGame)) {
            return Action.Stand
        }
        return playHand(hand, forGame)
    }

    private fun handleIfBusted(hand: Hand, game:BlackjackGame): Boolean {
        if(hand.isBusted) {
            log(HandBustsEvent(stats = stats.copy()))
            processLoss(hand)
            game.dealer.processWin(hand)
            return true
        }
        return false
    }

    private fun handleIfBlackjack(hand: Hand, game:BlackjackGame): Boolean {
        if (hand.isBlackjack) {
            log(HasABlackjackEvent(stats = stats.copy()))
            processWin(hand, game.rules)
            game.dealer.processLoss(hand, game.rules)
            return true
        }
        return false
    }

    private fun playDouble(hand: Hand, forGame: BlackjackGame): Action {
        log(PlayActionEvent(stats = stats.copy(), action = Action.Double))
        hand.isDouble = true
        hand.addCard(forGame.takeVisibleCardFromDealer())
        log(HandChangedEvent(stats = stats.copy(), hand = hand))
        handleIfBusted(hand, forGame)
        return Action.Double
    }

    private fun playSplit(hand: Hand, forGame: BlackjackGame): Action? {
        if(!hand.isSplittable) return null
        log(PlayActionEvent(stats = stats.copy(), action = Action.Split))
        val splitHands = splitHand(hand, forGame.takeVisibleCardFromDealer(), forGame.takeVisibleCardFromDealer())
        if (splitHands.size == 2) {
            log(HandChangedEvent(stats = stats.copy(), hand = splitHands[0]))
            playHand(splitHands[0], forGame)
            log(HandChangedEvent(stats = stats.copy(), hand = splitHands[1]))
            playHand(splitHands[1], forGame)
            return Action.Split
        }
        return null
    }

    fun observeCard(card: Card?) {
        if(strategy is CountingStrategy) {
            strategy.updateRunningCount(card!!)
        }
    }

    open fun observeGame(game:BlackjackGame) { }

    fun insureHand(hand:Hand) {
        hand.isInsured = true
        stats.bankroll -= hand.wager/2.0
    }

    override fun playHand(hand: Hand, forGame: BlackjackGame?): Action? {

        if (forGame == null) throw RuntimeException("Game is null")

        if (handleIfBlackjack(hand, forGame)) {
            return Action.Stand
        }

        val rules = forGame.rules
        if(!rules.canPlay(hand)) {
            log(PlayActionEvent(stats = stats.copy(), action = Action.Stand))
            return Action.Stand
        }

        val action = makeDecision(HandState(hand.hash, upCard = forGame.dealer.upCard?.rank))
        if (action == Action.SurrenderOrHit) {
            if (rules.canSurrender(hand)) {
                processSurrender(hand)
                return Action.Surrender
            } else {
                return playHit(hand, forGame)
            }
        } else if (action == Action.SurrenderOrSplit) {
            if (rules.canSurrender(hand)) {
                processSurrender(hand)
                return Action.Surrender
            } else if (hand.isPair) {
                playSplit(hand, forGame)
                return Action.Split
            }
        } else if (action == Action.SurrenderOrStand) {
            if (rules.canSurrender(hand)) {
                processSurrender(hand)
                return Action.Surrender
            }
        } else if (action == Action.Hit) {
            return playHit(hand, forGame)
        } else if (action == Action.DoubleOrHit) {
            if (rules.canDouble(hand)) {
                return playDouble(hand, forGame)
            } else {
                return playHit(hand, forGame)
            }
        } else if (action == Action.DoubleOrStand) {
            if (rules.canDouble(hand)) {
                return playDouble(hand, forGame)
            }
        } else if (action == Action.SplitOrHit) {
            if (rules.canSplit(hand) && rules.canDoubleAfterSplit) {
                return playSplit(hand, forGame)
            } else {
                return playHit(hand, forGame)
            }
        } else if (action == Action.SplitOrStand) {
            if (rules.canSplit(hand)) {
                return playSplit(hand, forGame)
            }
        }
        log(PlayActionEvent(stats = stats.copy(), action = Action.Stand))
        return Action.Stand
    }

    override fun toString(): String {
        var str = super.toString()
        str += "bankroll: ${stats.bankroll}\n"
        return str
    }
}
