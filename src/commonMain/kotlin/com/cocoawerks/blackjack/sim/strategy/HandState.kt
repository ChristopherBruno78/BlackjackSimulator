package com.cocoawerks.blackjack.sim.strategy

import com.cocoawerks.blackjack.sim.cards.HandHash
import kotlinx.serialization.Serializable

@Serializable
data class HandState(val hand: HandHash, val upCard: Int? = null)
