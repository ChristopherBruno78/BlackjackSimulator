package com.cocoawerks.blackjack.sim.strategy

import com.cocoawerks.blackjack.sim.cards.HandHash

data class HandState(val hand: HandHash, val upCard: Int? = null)
