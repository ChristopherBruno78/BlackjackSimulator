package com.cocoawerks.blackjack.sim.strategy

enum class Action {
    Hit,
    Stand,
    SplitOrStand,
    SplitOrHit,
    DoubleOrStand,
    DoubleOrHit,
    SurrenderOrStand,
    SurrenderOrHit,
    SurrenderOrSplit,

    // not to be used in strategy setting
    Double,
    Split,
    Surrender,
}
