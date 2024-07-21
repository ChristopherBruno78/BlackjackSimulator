
import com.cocoawerks.blackjack.calc.BlackjackGame
import com.cocoawerks.blackjack.calc.BlackjackRules
import com.cocoawerks.blackjack.calc.cards.*
import com.cocoawerks.blackjack.calc.entity.Dealer
import com.cocoawerks.blackjack.calc.entity.Player
import com.cocoawerks.blackjack.calc.strategy.Action
import com.cocoawerks.blackjack.calc.strategy.BasicStrategy4PlusDecks
import com.cocoawerks.blackjack.calc.strategy.HandState
import kotlin.test.Test


class BlackjackCalculatorTests {

    @Test
    fun testCards() {
        val suit = Suit.Club
        val rank = Rank.Ten
        println(suit.abbr)
        println(rank.abbr)
        println(rank.value())

        val card = Card(Suit.Club, Rank.Ten)

        println(card.toString())

    }

    @Test
    fun testHand() {
        val hand = Hand(wager = 1.0)
        hand.addCard(Card(Suit.Heart, Rank.Six))
        hand.addCard(Card(Suit.Club, Rank.Jack))

        assert(hand.value() == 16)
    }

    @Test
    fun testSoftHand() {
        val hand4 = Hand()
        hand4.addCard(Card(suit = Suit.Diamond, rank = Rank.Ace))
        hand4.addCard(Card(Suit.Spade, Rank.Ace))

        assert(hand4.isSoft)
        assert(hand4.value() == 12)



    }

    @Test
    fun testDeckShoe() {
        val deck = DeckShoe(numberOfDecks = 6)
        val hand = Hand(wager = 1.0)
        hand.addCard(deck.drawCard())
        hand.addCard(deck.drawCard())

        println(hand)

        assert(deck.numberOfCardsRemaining == 52*6 - 2)
        assert(deck.pen == (1.0 - (52*6 - 2)/(52.0*6)))
    }


    @Test
    fun testDealer() {
        val dealer = Dealer(BlackjackRules())
        dealer.startHand()
        dealer.dealerHand?.addCard(Card(Suit.Club, Rank.Four))
        dealer.dealerHand?.addCard(Card(Suit.Heart, Rank.Four))

        dealer.playHand()

        println(dealer.dealerHand)

    }

    @Test
    fun testPlayer() {


        val player = Player(name = "Chris", strategy = BasicStrategy4PlusDecks())

        val hand = Hand(wager = 1.0)
        hand.addCard(Card(Suit.Club, Rank.Eight))
        hand.addCard(Card(suit = Suit.Diamond, Rank.Two))


        val hand2 = Hand(wager = 1.0)
        hand2.addCard(Card(Suit.Heart, Rank.Seven))
        hand2.addCard(Card(suit = Suit.Diamond, Rank.Three))


       val a =  player.strategy.getPlayAction(HandState(hand2.hash, upCard = Rank.Four))

       assert(a == Action.DoubleOrHit)

    }

    @Test
    fun testGame() {
        val game = BlackjackGame(rules = BlackjackRules())
        val player = Player(name = "Chris", strategy = BasicStrategy4PlusDecks())
        game.addPlayer(player)

        game.playRound()
        game.printLog()
    }



}