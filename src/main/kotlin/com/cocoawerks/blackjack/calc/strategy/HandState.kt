package com.cocoawerks.blackjack.calc.strategy

import com.cocoawerks.blackjack.calc.cards.HandHash
import com.cocoawerks.blackjack.calc.cards.Rank

data class HandState(val hand: HandHash, val upCard: Rank? = null, val countIndex: Int = 0)
