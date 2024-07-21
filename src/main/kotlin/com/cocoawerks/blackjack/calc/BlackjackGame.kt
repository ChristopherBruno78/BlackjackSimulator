package com.cocoawerks.blackjack.calc

import com.cocoawerks.blackjack.calc.cards.Hand
import com.cocoawerks.blackjack.calc.entity.Dealer
import com.cocoawerks.blackjack.calc.entity.Player
import com.cocoawerks.blackjack.calc.log.*

class BlackjackGame(val rules: BlackjackRules, log: Boolean = true) {

    private var _players: MutableList<Player> = ArrayList()
    private val _dealer: Dealer = Dealer(rules)

    var logger:Logger? = null

    init {
        _dealer.shuffle()
        if( log ) {
            logger = Logger()
            dealer.logger = logger
        }
    }

    val dealer: Dealer
        get() = _dealer

    val players: List<Player>
        get() = _players

    fun addPlayer(player: Player) {
        _players.add(player)
        player.logger = logger
    }

    private fun log(event: Event) {
        logger?.log(event)
    }

    private fun dealerTakeBets() {
        for (player in _players) {
            player.bet()
        }
    }

    private fun dealInitialCards() {
        log(DealingCardsEvent())
        _dealer.startHand()
        for (player in _players) {
            val hands = player.hands
            for (hand in hands) {
                hand.addCard(_dealer.dealCard())
            }
        }

        _dealer.dealerHand?.addCard(_dealer.dealCard())
        log(DealerUpCardEvent(upCard = _dealer.upCard!!))

        for (player in _players) {
            val hands = player.hands
            for (hand in hands) {
                hand.addCard(_dealer.dealCard())
                log(HandChangedEvent(stats = player.stats.copy(), hand = hand))
            }
        }

        if(!rules.europeanNoHoleCard) {
            _dealer.dealerHand?.addCard(_dealer.dealCard())
        }
    }

    private fun checkDealerForBlackjack(): Boolean {
        if (_dealer.dealerHand == null) return false

        if (_dealer.dealerHand!!.isBlackjack) {
            log(HandChangedEvent(stats = _dealer.stats.copy(), hand = _dealer.dealerHand!!))
            log(HasABlackjackEvent(stats = _dealer.stats.copy()))
            for (player in _players) {
                val hands = player.allHands
                for (hand in hands) {
                    if (!hand.isBlackjack) {
                        player.processLoss(hand)
                        _dealer.processWin(hand)
                    } else {
                        log(HasABlackjackEvent(stats = player.stats.copy()))
                        player.processPush(hand)
                        _dealer.processPush(hand)
                    }
                }
            }
            return true
        }
        return false
    }

    private fun playHands() {
        for (player in _players) {
            val hands = player.hands
            for (hand in hands) {
                player.playHand(hand, forGame = this)
            }
        }
    }

    private fun evaluateRound() {
        val madeHands = ArrayList<Hand>()
        for (player in _players) {
            val hands = player.allHands
            for (hand in hands) {
                if (hand.isBusted) {
                    player.processLoss(hand)
                    _dealer.processWin(hand)
                } else if (hand.isBlackjack) {
                    player.processWin(hand, rules)
                    _dealer.processLoss(hand, rules)
                } else {
                    madeHands.add(hand)
                }
            }
        }

        if (madeHands.size > 0) {
            log(HandChangedEvent(stats = _dealer.stats.copy(), hand = _dealer.dealerHand!!))
            _dealer.playHand()
            val dealerTotal = _dealer.dealerHand!!.value()
            if (dealerTotal > 21) { // dealer busts
                for (hand in madeHands) {
                    val player = hand.owner as? Player
                    if (player != null) {
                        player.processWin(hand)
                        _dealer.processLoss(hand)
                    }
                }
            } else {
                for (hand in madeHands) {
                    val player = hand.owner as? Player
                    if (player != null) {
                        if (dealerTotal > hand.value()) {
                            player.processLoss(hand)
                            _dealer.processWin(hand)
                        } else if (dealerTotal < hand.value()) {
                            player.processWin(hand)
                            _dealer.processLoss(hand)
                        } else {
                            player.processPush(hand)
                            _dealer.processPush(hand)
                        }
                    }
                }
            }
        }
    }

    private fun clear() {
        _dealer.clearHands()
        for (player in _players) {
            player.clearHands()
        }
    }

    fun playRound() {
        logger?.logNewRound()
        clear()
        dealerTakeBets()
        dealInitialCards()
        if(!rules.europeanNoHoleCard) {
            if (!checkDealerForBlackjack()) {
                playHands()
                evaluateRound()
            }
        }
        else {
            playHands()
            _dealer.dealerHand?.addCard(dealer.dealCard())
            log(HandChangedEvent(stats = _dealer.stats.copy(), hand = _dealer.dealerHand!!))
            if(!checkDealerForBlackjack()) {
                evaluateRound()
            }
        }

        _dealer.shuffleIfNeeded(rules.deckPenetration)
    }

    fun printLog() {
        println(logger?.toString())
    }
}
