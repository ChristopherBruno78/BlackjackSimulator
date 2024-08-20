package com.cocoawerks.blackjack.sim.strategy

import com.cocoawerks.blackjack.sim.cards.HandHash
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("hand_state")
data class HandState(val hand: HandHash, val upCard: Int? = null)
