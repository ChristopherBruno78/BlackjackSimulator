package com.cocoawerks.blackjack.calc.cards

enum class Rank(val abbr: String) {
    Ace("A") {
        override fun value(): Int {
            return 1
        }
    },
    Two("2") {
        override fun value(): Int {
            return 2
        }
    },
    Three("3") {
        override fun value(): Int {
            return 3
        }
    },
    Four("4") {
        override fun value(): Int {
            return 4
        }
    },
    Five("5") {
        override fun value(): Int {
            return 5
        }
    },
    Six("6") {
        override fun value(): Int {
            return 6
        }
    },
    Seven("7") {
        override fun value(): Int {
            return 7
        }
    },
    Eight("8") {
        override fun value(): Int {
            return 8
        }
    },
    Nine("9") {
        override fun value(): Int {
            return 9
        }
    },
    Ten("T") {
        override fun value(): Int {
            return 10
        }
    },
    Jack("J") {
        override fun value(): Int {
            return 10
        }
    },
    Queen("Q") {
        override fun value(): Int {
            return 10
        }
    },
    King("K") {
        override fun value(): Int {
            return 10
        }
    };

    abstract fun value(): Int
}
