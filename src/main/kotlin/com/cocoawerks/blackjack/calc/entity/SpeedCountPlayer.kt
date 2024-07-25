package com.cocoawerks.blackjack.calc.entity

import com.cocoawerks.blackjack.calc.BlackjackGame
import com.cocoawerks.blackjack.calc.BlackjackRules
import com.cocoawerks.blackjack.calc.strategy.BetSpread
import com.cocoawerks.blackjack.calc.strategy.SpeedCountStrategy

class SpeedCountPlayer(name: String, betSpread: BetSpread, rules: BlackjackRules) :
    Player(name, strategy = SpeedCountStrategy(betSpread, rules)) {

    override fun observeGame(game: BlackjackGame) {
        var numHands = 1 //start with the dealer
        for (player in game.players) {
            numHands += player.allHands.size
        }
        (strategy as SpeedCountStrategy).subtractNumberOfHands(numHands)
    }

}