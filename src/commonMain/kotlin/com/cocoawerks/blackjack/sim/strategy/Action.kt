package com.cocoawerks.blackjack.sim.strategy

enum class Action {
    Hit,
    Stand,
    Split,
    SplitOrHit,
    DoubleOrStand,
    DoubleOrHit,
    SurrenderOrStand,
    SurrenderOrHit,
    SurrenderOrSplit,

    // not to be used in strategy setting
    Double,
    Surrender,
}
