package com.cocoawerks.blackjack.sim.strategy.tables

import com.cocoawerks.blackjack.sim.cards.Rank
import com.cocoawerks.blackjack.sim.cards.hard
import com.cocoawerks.blackjack.sim.cards.pair
import com.cocoawerks.blackjack.sim.cards.soft
import com.cocoawerks.blackjack.sim.strategy.Action
import com.cocoawerks.blackjack.sim.strategy.HandState
import com.cocoawerks.blackjack.sim.strategy.Strategy

internal class BasicStrategy4PlusDecksTable(val strategy: Strategy) {

    init {
        initHardTotals()
        initSoftTotals()
        initPairs()
    }

    private fun initHardTotals() {
        // 5-8
        for (total in 5..8) {
            Rank.entries.forEach { rank ->
                strategy.setPlayAction(Action.Hit, HandState(hard(total), upCard = rank))
            }
        }

        // 9
        Rank.entries.forEach { rank ->
            val state = HandState(hard(9), upCard = rank)
            if (rank.value() > 2 && rank.value() < 7) {
                strategy.setPlayAction(Action.DoubleOrHit, state)
            } else {
                strategy.setPlayAction(Action.Hit, state)
            }
        }

        // 10
        Rank.entries.forEach { rank ->
            val state = HandState(hard(10), upCard = rank)
            if (rank.value() == 1 || rank.value() == 10) {
                strategy.setPlayAction(Action.Hit, state)
            } else {
                strategy.setPlayAction(Action.DoubleOrHit, state)
            }
        }

        // 11
        Rank.entries.forEach { rank ->
            val state = HandState(hard(11), upCard = rank)
            strategy.setPlayAction(Action.DoubleOrHit, state)
        }

        // 12
        Rank.entries.forEach { rank ->
            val state = HandState(hard(12), upCard = rank)
            if (rank.value() > 3 && rank.value() < 7) {
                strategy.setPlayAction(Action.Stand, state)
            } else {
                strategy.setPlayAction(Action.Hit, state)
            }
        }

        // 13-14
        for (total in 13..14) {
            Rank.entries.forEach { rank ->
                val state = HandState(hard(total), upCard = rank)
                if (rank.value() > 1 && rank.value() < 7) {
                    strategy.setPlayAction(Action.Stand, state)
                } else {
                    strategy.setPlayAction(Action.Hit, state)
                }
            }
        }

        //15
        Rank.entries.forEach { rank ->
            val state = HandState(hard(15), upCard = rank)
            if (rank.value() > 1 && rank.value() < 7) {
                strategy.setPlayAction(Action.Stand, state)
            } else if ((rank.value() > 6 && rank.value() < 10) || rank.value() == 1) {
                strategy.setPlayAction(Action.Hit, state)
            } else {
                strategy.setPlayAction(Action.SurrenderOrHit, state)
            }
        }

        //16
        Rank.entries.forEach { rank ->
            val state = HandState(hard(16), upCard = rank)
            if (rank.value() > 1 && rank.value() < 7) {
                strategy.setPlayAction(Action.Stand, state)
            } else if ((rank.value() > 6 && rank.value() < 9) || rank.value() == 1) {
                strategy.setPlayAction(Action.Hit, state)
            } else {
                strategy.setPlayAction(Action.SurrenderOrHit, state)
            }
        }

        //17
        Rank.entries.forEach { rank ->
            val state = HandState(hard(17), upCard = rank)
            if (rank.value() == 1) {
                strategy.setPlayAction(Action.SurrenderOrStand, state)
            } else {
                strategy.setPlayAction(Action.Stand, state)
            }
        }

        // 18-21
        for (total in 18..21) {
            Rank.entries.forEach { rank ->
                strategy.setPlayAction(Action.Stand, HandState(hard(total), upCard = rank))
            }
        }
    }

    private fun initSoftTotals() {
        // 13-14
        for (total in 13..14) {
            Rank.entries.forEach { rank ->
                val state = HandState(soft(total), upCard = rank)
                if (rank.value() < 5 || rank.value() > 6) {
                    strategy.setPlayAction(Action.Hit, state)
                } else {
                    strategy.setPlayAction(Action.DoubleOrHit, state)
                }
            }
        }

        // 15-16
        for (total in 15..16) {
            Rank.entries.forEach { rank ->
                val state = HandState(soft(total), upCard = rank)
                if (rank.value() < 4 || rank.value() > 6) {
                    strategy.setPlayAction(Action.Hit, state)
                } else {
                    strategy.setPlayAction(Action.DoubleOrHit, state)
                }
            }
        }

        // 17
        Rank.entries.forEach { rank ->
            val state = HandState(soft(17), upCard = rank)
            if (rank.value() < 3 || rank.value() > 6) {
                strategy.setPlayAction(Action.Hit, state)
            } else {
                strategy.setPlayAction(Action.DoubleOrHit, state)
            }
        }

        // 18
        Rank.entries.forEach { rank ->
            val state = HandState(soft(18), upCard = rank)
            if (rank.value() > 1 && rank.value() < 7) {
                strategy.setPlayAction(Action.DoubleOrStand, state)
            } else if (rank.value() > 6 && rank.value() < 9) {
                strategy.setPlayAction(Action.Stand, state)
            } else {
                strategy.setPlayAction(Action.Hit, state)
            }
        }

        // 19
        Rank.entries.forEach { rank ->
            val state = HandState(soft(19), upCard = rank)
            if (rank.value() == 6) {
                strategy.setPlayAction(Action.DoubleOrStand, state)
            } else {
                strategy.setPlayAction(Action.Stand, state)
            }
        }

        // 20-21
        for (total in 20..21) {
            Rank.entries.forEach { rank ->
                strategy.setPlayAction(Action.Stand, HandState(soft(total), upCard = rank))
            }
        }
    }

    private fun initPairs() {

        // 2/12 (A-A)
        Rank.entries.forEach { rank ->
            strategy.setPlayAction(Action.Split, HandState(pair(12, aces = true), upCard = rank))
        }

        // 4
        Rank.entries.forEach { rank ->
            val state = HandState(pair(4), upCard = rank)
            if (rank.value() in 2..3) {
                strategy.setPlayAction(Action.SplitOrHit, state)
            }
            else if (rank.value() in 4..7) {
                strategy.setPlayAction(Action.Split, state)
            }
            else {
                strategy.setPlayAction(Action.Hit, state)
            }
        }

        // 6
        Rank.entries.forEach { rank ->
            val state = HandState(pair(6), upCard = rank)
            if (rank.value() in 2..3) {
                strategy.setPlayAction(Action.SplitOrHit, state)
            }
            else if (rank.value() in 4..7) {
                strategy.setPlayAction(Action.Split, state)
            }
            else {
                strategy.setPlayAction(Action.Hit, state)
            }
        }

        // 8
        Rank.entries.forEach { rank ->
            val state = HandState(pair(8), upCard = rank)
            if (rank.value() in 5..6) {
                strategy.setPlayAction(Action.SplitOrHit, state)
            } else {
                strategy.setPlayAction(Action.Hit, state)
            }
        }

        // 10
        Rank.entries.forEach { rank ->
            val state = HandState(pair(10), upCard = rank)
            if (rank.value() in 2..9) {
                strategy.setPlayAction(Action.DoubleOrHit, state)
            } else {
                strategy.setPlayAction(Action.Hit, state)
            }
        }

        // 12
        Rank.entries.forEach { rank ->
            val state = HandState(pair(12), upCard = rank)
            if (rank.value() in 3..6) {
                strategy.setPlayAction(Action.Split, state)
            } else if (rank.value() == 2) {
                strategy.setPlayAction(Action.SplitOrHit, state)
            }
            else {
                strategy.setPlayAction(Action.Hit, state)
            }

        }

        // 14
        Rank.entries.forEach { rank ->
            val state = HandState(pair(14), upCard = rank)
            if (rank.value() in 2..7) {
                strategy.setPlayAction(Action.Split, state)
            } else {
                strategy.setPlayAction(Action.Hit, state)
            }
        }

        // 16
        Rank.entries.forEach { rank ->
            if (rank == Rank.Ace) {
                strategy.setPlayAction(Action.SurrenderOrSplit, HandState(pair(16), upCard = rank))
            } else {
                strategy.setPlayAction(Action.Split, HandState(pair(16), upCard = rank))
            }
        }

        // 18
        Rank.entries.forEach { rank ->
            val state = HandState(pair(18), upCard = rank)
            if (rank.value() == 1 || rank.value() == 7 || rank.value() == 10) {
                strategy.setPlayAction(Action.Stand, state)
            } else {
                strategy.setPlayAction(Action.Split, state)
            }
        }

        // 20
        Rank.entries.forEach { rank ->
            strategy.setPlayAction(Action.Stand, HandState(pair(20), upCard = rank))
        }
    }

}
