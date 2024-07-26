package com.cocoawerks.blackjack.sim

import com.cocoawerks.blackjack.sim.cards.Card
import com.cocoawerks.blackjack.sim.cards.Hand
import com.cocoawerks.blackjack.sim.cards.Rank
import com.cocoawerks.blackjack.sim.entity.Dealer
import com.cocoawerks.blackjack.sim.entity.Player
import com.cocoawerks.blackjack.sim.log.*

class BlackjackGame(val rules: BlackjackRules, log: Boolean = true) {

    private var _players: MutableList<Player> = ArrayList()
    private val _dealer: Dealer = Dealer(this)

    var logger: Logger? = null
    var roundsPlayed: Int = 0

    init {
        if (log) {
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

    private fun log(event: Loggable) {
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
                hand.addCard(takeVisibleCardFromDealer())
            }
        }

        _dealer.dealerHand?.addCard(takeVisibleCardFromDealer())
        log(DealerUpCardEvent(upCard = _dealer.upCard!!))

        for (player in _players) {
            val hands = player.hands
            for (hand in hands) {
                hand.addCard(takeVisibleCardFromDealer())
            }
        }

        if (!rules.europeanNoHoleCard) {
            _dealer.dealerHand?.addCard(_dealer.dealCard())
        }

        if (_dealer.upCard?.rank == Rank.Ace) {
            offerInsuranceToPlayers()
        }
    }

    private fun offerInsuranceToPlayers() {
        log(DealerOffersInsuranceEvent())
        for (player in _players) {
            val takeInsurance = player.strategy.willTakeInsurance()
            log(PlayerInsuranceDecisionEvent(player.stats.copy(), takeInsurance))
            if (takeInsurance) {
                for (hand in player.hands) {
                    player.insureHand(hand)
                }
            }
        }
    }

    private fun checkDealerForBlackjack(): Boolean {
        if (_dealer.dealerHand == null) return false

        if (_dealer.dealerHand!!.isBlackjack) {
            log(HandChangedEvent(stats = _dealer.stats.copy(), hand = _dealer.dealerHand!!))
            log(HasABlackjackEvent(stats = _dealer.stats.copy()))
            revealCard(_dealer.holeCard)
            for (player in _players) {
                val hands = player.allHands
                for (hand in hands) {
                    if (!rules.europeanNoHoleCard) {
                        log(HandChangedEvent(stats = player.stats.copy(), hand = hand))
                    }
                    if (hand.isInsured) {
                        log(PlayerPaidInsuranceEvent(player.stats.copy()))
                        player.stats.bankroll += 1.5 * hand.wager
                    }
                    if (!hand.isBlackjack) {
                        player.processLoss(hand)
                        _dealer.processWin(hand)
                    } else {
                        if (!rules.europeanNoHoleCard) {
                            log(HasABlackjackEvent(stats = player.stats.copy()))
                        }
                        player.processPush(hand)
                        _dealer.processPush(hand)
                    }
                }
            }
            return true
        }

        if (_dealer.upCard?.rank == Rank.Ace) {
            log(DealerDoesNotHaveBlackjackEvent())
        }

        return false
    }

    private fun playHands() {
        for (player in _players) {
            val hands = player.hands
            for (hand in hands) {
                log(HandChangedEvent(stats = player.stats.copy(), hand = hand))
                player.playHand(hand, forGame = this)
            }
        }
    }

    private fun evaluateRound() {
        log(HandChangedEvent(stats = _dealer.stats.copy(), hand = _dealer.dealerHand!!))
        val madeHands = ArrayList<Hand>()
        for (player in _players) {
            val hands = player.allHands
            for (hand in hands) {
                if (hand.isBlackjack && rules.europeanNoHoleCard) {
                    player.processWin(hand, rules)
                    dealer.processLoss(hand, rules)
                }
                if (!hand.isBusted && !hand.isBlackjack) {
                    madeHands.add(hand)
                }
            }
        }

        if (madeHands.size > 0) {
            revealCard(_dealer.holeCard)
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

    fun playRound(debug: Boolean = false) {
        clear()

        if (!debug) {

            if (roundsPlayed == 0) {
                _dealer.shuffle()
                for (player in _players) {
                    player.strategy.reset()
                }
            } else {
                val shuffle = _dealer.shuffleIfNeeded(rules.deckPenetration)
                if (shuffle) {
                    for (player in _players) {
                        player.strategy.reset()
                    }
                }
            }
        }

        logger?.logNewRound()
        dealerTakeBets()
        dealInitialCards()
        if (!rules.europeanNoHoleCard) {
            if (!checkDealerForBlackjack()) {
                playHands()
                evaluateRound()
            }
        } else {
            playHands()
            _dealer.dealerHand?.addCard(dealer.dealCard())
            if (!checkDealerForBlackjack()) {
                evaluateRound()
            }
        }

        roundsPlayed += 1

        for (player in _players) {
            player.observeGame(this)
        }

        logger?.logEndRound()

    }

    fun takeVisibleCardFromDealer(): Card? {
        val card = _dealer.dealCard()
        revealCard(card)
        return card
    }

    private fun revealCard(card: Card?) {
        for (player in _players) {
            player.observeCard(card)
        }
    }

    fun printLog() {
        println(logger?.toString())
    }
}
