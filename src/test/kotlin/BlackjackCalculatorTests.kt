
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


        assert(deck.numberOfCardsRemaining == 52*deck.numberOfDecks - 2)
        assert(deck.pen == (1.0 - (52*deck.numberOfDecks - 2)/(52.0*deck.numberOfDecks)))


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

        for(i in 1..3000) {
            game.playRound()
        }
        game.printLog()
    }


    @Test
    fun testDoubleRules() {
        val rules = BlackjackRules()

        val player = Player(name = "Chris", strategy = BasicStrategy4PlusDecks())

        player.bet()
        val hand = player.hands[0]
        hand.addCard(Card(Suit.Spade, Rank.Eight))
        hand.addCard(Card(Suit.Heart, Rank.Three))

        assert(rules.canDouble(hand) == true)

        val player2 = Player(name = "Steve", strategy = BasicStrategy4PlusDecks())


        player2.bet()
        val hand2 = player2.hands[0]
        hand2.addCard(Card(Suit.Spade, Rank.Eight))
        hand2.addCard(Card(Suit.Heart, Rank.Eight))

        player2.splitHand(hand2, Card(Suit.Club, Rank.Three), Card(Suit.Heart, Rank.Ten))

        val subHand2 = hand2.splits[0]

        assert(rules.canDouble(subHand2) == true)
        rules.canDoubleAfterSplit = false
        assert(rules.canDouble(subHand2) == false)

        val player3 = Player(name = "Scott", strategy = BasicStrategy4PlusDecks())
        player3.bet()
        val hand3 = player3.hands[0]
        hand3.addCard(Card(Suit.Spade, Rank.Five))
        hand3.addCard(Card(Suit.Heart, Rank.Ace))

        rules.doublesRestrictedToHardTotals = arrayOf(9, 10, 11)

        assert(rules.canDouble(hand3) == false)

        val player4 = Player(name = "Sam", strategy = BasicStrategy4PlusDecks())


        player4.bet()
        val hand4 = player4.hands[0]
        hand4.addCard(Card(Suit.Spade, Rank.Five))
        hand4.addCard(Card(Suit.Heart, Rank.Six))

        assert(rules.canDouble(hand4) == true)

    }

    @Test
    fun testSplitRules() {
        val rules = BlackjackRules()

        var dealer = Dealer(rules)
        dealer.startHand()

        val player = Player(name = "Chris", strategy = BasicStrategy4PlusDecks())

        player.bet()
        val hand = player.hands[0]
        hand.addCard(Card(Suit.Spade, Rank.Ace))
        hand.addCard(Card(Suit.Heart, Rank.Ace))

        assert(rules.canSplit(hand) == true)

        player.splitHand(hand, dealer.dealCard(), dealer.dealCard())

        assert(hand.numberOfHands == 2)

        assert(rules.canSplit(hand.splits[0]) == false)
        assert(rules.canPlay(hand.splits[0]) == false)

        val player2 = Player(name = "Steve", strategy = BasicStrategy4PlusDecks())
        player2.bet()
        val hand2 = player2.hands[0]
        hand2.addCard(Card(Suit.Spade, Rank.Eight))
        hand2.addCard(Card(Suit.Heart, Rank.Eight))

        assert(rules.canSplit(hand2) == true)


        player2.splitHand(hand2, Card(Suit.Club, Rank.Eight), Card(Suit.Heart, Rank.Eight))

        assert(hand2.numberOfHands == 2)

        val subHand2 = hand2.splits[0]
        val sub2Hand2 = hand2.splits[1]

        assert(rules.canSplit(subHand2) == true)
        assert(rules.canSplit(sub2Hand2) == true)

        player2.splitHand(subHand2, Card(Suit.Diamond, Rank.Eight), Card(Suit.Heart, Rank.Eight))

        assert(hand2.numberOfHands == 3)

        val sub3Hand2 = subHand2.splits[0]
        assert(rules.canSplit(sub3Hand2) == true)

        player2.splitHand(sub3Hand2, Card(Suit.Spade, Rank.Eight), Card(Suit.Diamond, Rank.Eight))

        println(hand2.numberOfHands)

        assert(hand2.numberOfHands == 4)

        val sub4Hand2 = sub3Hand2.splits[0]

        assert(rules.canSplit(sub4Hand2) == false)
    }




}