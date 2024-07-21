package com.cocoawerks.blackjack.calc.cards

class DeckShoe(val numberOfDecks: Int = 1) {
    val cards: MutableList<Card> = ArrayDeque()

    init {
        shuffle()
    }

    fun shuffle() {
        cards.clear()
        for (i in 1..numberOfDecks) {
            Suit.entries.forEach { suit ->
                Rank.entries.forEach { rank -> cards.add(Card(suit, rank)) }
            }
        }
        cards.shuffle()
    }

    fun drawCard(): Card? {
        if (cards.size > 0) {
            return cards.removeFirst()
        }
        return null
    }

    val numberOfCardsRemaining: Int
        get() {
            return cards.size
        }

    val pen: Double
        get() {
            return (1.0 - numberOfCardsRemaining / (numberOfDecks * 52.0))
        }
}
