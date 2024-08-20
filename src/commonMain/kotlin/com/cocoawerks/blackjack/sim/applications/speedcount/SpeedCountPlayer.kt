package com.cocoawerks.blackjack.sim.applications.speedcount

import com.cocoawerks.blackjack.sim.BlackjackGame
import com.cocoawerks.blackjack.sim.BlackjackRules
import com.cocoawerks.blackjack.sim.strategy.BetSpread
import com.cocoawerks.blackjack.sim.entity.Player

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