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

    fun stackWithCards(vararg cards: Card, shuffle: Boolean = false) {
        this.cards.clear()
        this.cards.addAll(0, cards.toList())
        if (shuffle) {
            this.cards.shuffle()
        }
    }

    val numberOfCardsRemaining: Int
        get() {
            return cards.size
        }

    val pen: Double
        get() {
            return (1.0 - numberOfCardsRemaining / (numberOfDecks * 52.0))
        }

    override fun toString(): String {
        var str = ""
        for (card in cards) {
            str += card.toString()
            str += "\n"
        }
        return str
    }
}
