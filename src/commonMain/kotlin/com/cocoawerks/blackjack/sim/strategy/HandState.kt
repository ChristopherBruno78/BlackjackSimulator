package com.cocoawerks.blackjack.sim.strategy

import com.cocoawerks.blackjack.sim.cards.HandHash
import com.cocoawerks.blackjack.sim.cards.Rank

data class HandState(val hand: HandHash, val upCard: Rank? = null)
