package com.cocoawerks.blackjack.calc.strategy

import com.cocoawerks.blackjack.calc.cards.Rank
import com.cocoawerks.blackjack.calc.cards.hard
import com.cocoawerks.blackjack.calc.cards.pair
import com.cocoawerks.blackjack.calc.cards.soft

class BasicStrategy4PlusDecks : Strategy() {

    init {
        initHardTotals()
        initSoftTotals()
        initPairs()
    }

    private fun initHardTotals() {
        // 5-8
        for (total in 5..8) {
            Rank.entries.forEach { rank ->
                setPlayAction(Action.Hit, HandState(hard(total), upCard = rank))
            }
        }
        // 9
        Rank.entries.forEach { rank ->
            val state = HandState(hard(9), upCard = rank)
            if (rank.value() > 2 && rank.value() < 7) {
                setPlayAction(Action.DoubleOrHit, state)
            } else {
                setPlayAction(Action.Hit, state)
            }
        }

        // 10
        Rank.entries.forEach { rank ->
            val state = HandState(hard(10), upCard = rank)
            if (rank.value() == 1 || rank.value() == 10) {
                setPlayAction(Action.Hit, state)
            } else {
                setPlayAction(Action.DoubleOrHit, state)
            }
        }

        // 11
        Rank.entries.forEach { rank ->
            val state = HandState(hard(11), upCard = rank)
            setPlayAction(Action.DoubleOrHit, state)
        }

        // 12
        Rank.entries.forEach { rank ->
            val state = HandState(hard(12), upCard = rank)
            if (rank.value() > 3 && rank.value() < 7) {
                setPlayAction(Action.Stand, state)
            } else {
                setPlayAction(Action.Hit, state)
            }
        }

        // 13-16
        for (total in 13..16) {
            Rank.entries.forEach { rank ->
                val state = HandState(hard(total), upCard = rank)
                if (rank.value() > 1 && rank.value() < 7) {
                    setPlayAction(Action.Stand, state)
                } else {
                    setPlayAction(Action.Hit, state)
                }
            }
        }

        // 17-21
        for (total in 17..21) {
            Rank.entries.forEach { rank ->
                setPlayAction(Action.Stand, HandState(hard(total), upCard = rank))
            }
        }
    }

    private fun initSoftTotals() {
        // 13-14
        for (total in 13..14) {
            Rank.entries.forEach { rank ->
                val state = HandState(soft(total), upCard = rank)
                if (rank.value() < 5 || rank.value() > 6) {
                    setPlayAction(Action.Hit, state)
                } else {
                    setPlayAction(Action.DoubleOrHit, state)
                }
            }
        }

        // 15-16
        for (total in 15..16) {
            Rank.entries.forEach { rank ->
                val state = HandState(soft(total), upCard = rank)
                if (rank.value() < 4 || rank.value() > 6) {
                    setPlayAction(Action.Hit, state)
                } else {
                    setPlayAction(Action.DoubleOrHit, state)
                }
            }
        }

        // 17
        Rank.entries.forEach { rank ->
            val state = HandState(soft(17), upCard = rank)
            if (rank.value() < 3 || rank.value() > 6) {
                setPlayAction(Action.Hit, state)
            } else {
                setPlayAction(Action.DoubleOrHit, state)
            }
        }

        // 18
        Rank.entries.forEach { rank ->
            val state = HandState(soft(18), upCard = rank)
            if (rank.value() > 1 && rank.value() < 7) {
                setPlayAction(Action.DoubleOrStand, state)
            } else if (rank.value() > 6 && rank.value() < 9) {
                setPlayAction(Action.Stand, state)
            } else {
                setPlayAction(Action.Hit, state)
            }
        }

        // 19
        Rank.entries.forEach { rank ->
            val state = HandState(soft(19), upCard = rank)
            if (rank.value() == 6) {
                setPlayAction(Action.DoubleOrStand, state)
            } else {
                setPlayAction(Action.Stand, state)
            }
        }

        // 20-21
        for (total in 20..21) {
            Rank.entries.forEach { rank ->
                setPlayAction(Action.Stand, HandState(soft(total), upCard = rank))
            }
        }
    }

    private fun initPairs() {

        // 2/12 (A-A)
        Rank.entries.forEach { rank ->
            setPlayAction(Action.SplitOrHit, HandState(pair(12, aces = true), upCard = rank))
        }

        // 4
        Rank.entries.forEach { rank ->
            val state = HandState(pair(4), upCard = rank)
            if (rank.value() > 1 && rank.value() < 8) {
                setPlayAction(Action.SplitOrHit, state)
            } else {
                setPlayAction(Action.Hit, state)
            }
        }

        // 6
        Rank.entries.forEach { rank ->
            val state = HandState(pair(6), upCard = rank)
            if (rank.value() > 1 && rank.value() < 8) {
                setPlayAction(Action.SplitOrHit, state)
            } else {
                setPlayAction(Action.Hit, state)
            }
        }

        // 8
        Rank.entries.forEach { rank ->
            val state = HandState(pair(8), upCard = rank)
            if (rank.value() > 4 && rank.value() < 7) {
                setPlayAction(Action.SplitOrHit, state)
            } else {
                setPlayAction(Action.Hit, state)
            }
        }

        // 10
        Rank.entries.forEach { rank ->
            val state = HandState(pair(10), upCard = rank)
            if (rank.value() > 1 && rank.value() < 10) {
                setPlayAction(Action.DoubleOrHit, state)
            } else {
                setPlayAction(Action.Hit, state)
            }
        }

        // 12
        Rank.entries.forEach { rank ->
            val state = HandState(pair(12), upCard = rank)
            if (rank.value() > 1 && rank.value() < 7) {
                setPlayAction(Action.SplitOrHit, state)
            } else {
                setPlayAction(Action.Hit, state)
            }
        }

        // 14
        Rank.entries.forEach { rank ->
            val state = HandState(pair(14), upCard = rank)
            if (rank.value() > 1 && rank.value() < 8) {
                setPlayAction(Action.SplitOrHit, state)
            } else {
                setPlayAction(Action.Hit, state)
            }
        }

        // 16
        Rank.entries.forEach { rank ->
            setPlayAction(Action.SplitOrHit, HandState(pair(16), upCard = rank))
        }

        // 18
        Rank.entries.forEach { rank ->
            val state = HandState(pair(18), upCard = rank)
            if (rank.value() == 1 || rank.value() == 7 || rank.value() == 10) {
                setPlayAction(Action.Stand, state)
            } else {
                setPlayAction(Action.SplitOrHit, state)
            }
        }

        // 20
        Rank.entries.forEach { rank ->
            setPlayAction(Action.Stand, HandState(pair(20), upCard = rank))
        }
    }
}
