import com.cocoawerks.blackjack.calc.BlackjackGame
import com.cocoawerks.blackjack.calc.BlackjackRules
import com.cocoawerks.blackjack.calc.cards.*
import com.cocoawerks.blackjack.calc.entity.Dealer
import com.cocoawerks.blackjack.calc.entity.Player
import com.cocoawerks.blackjack.calc.entity.SpeedCountPlayer
import com.cocoawerks.blackjack.calc.strategy.*
import kotlin.math.sqrt
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
        assert(deck.numberOfCardsRemaining == 52 * deck.numberOfDecks - 2)
        assert(deck.pen == (1.0 - (52 * deck.numberOfDecks - 2) / (52.0 * deck.numberOfDecks)))
    }


    @Test
    fun testDealer() {
        val game = BlackjackGame(BlackjackRules())
        game.dealer.startHand()
        game.dealer.dealerHand?.addCard(Card(Suit.Club, Rank.Four))
        game.dealer.dealerHand?.addCard(Card(Suit.Heart, Rank.Four))

        game.dealer.playHand()

        println(game.dealer.dealerHand)

    }

    @Test
    fun testPlayer() {

        val player = Player(name = "Chris", strategy = BasicStrategy(BlackjackRules()))

        val hand = Hand(wager = 1.0)
        hand.addCard(Card(Suit.Club, Rank.Eight))
        hand.addCard(Card(suit = Suit.Diamond, Rank.Two))

        val hand2 = Hand(wager = 1.0)
        hand2.addCard(Card(Suit.Heart, Rank.Seven))
        hand2.addCard(Card(suit = Suit.Diamond, Rank.Three))

        val a = player.strategy.getPlayAction(HandState(hand2.hash, upCard = Rank.Four))

        assert(a == Action.DoubleOrHit)

    }


    @Test
    fun testGame() {
        val game = BlackjackGame(rules = BlackjackRules())
        val player = Player(name = "Chris", strategy = BasicStrategy(BlackjackRules()))
        game.addPlayer(player)

        val rules = BlackjackRules()
        val betSpread = BetSpread(
            hashMapOf(
                0 to 1.0,
                1 to 1.0,
                2 to 1.0,
                3 to 6.0,
                4 to 12.0,
                5 to 24.0,
                6 to 50.0
            )
        )

        val counter = Player(name = "Counter", strategy = HiLoCountingStrategy(betSpread, rules))
        game.addPlayer(counter)

        for (i in 1..1) {
            game.playRound()
        }
        game.printLog()
    }


    @Test
    fun testDoubleRules() {
        val rules = BlackjackRules()

        val player = Player(name = "Chris", strategy = BasicStrategy(rules))

        player.bet()
        val hand = player.hands[0]
        hand.addCard(Card(Suit.Spade, Rank.Eight))
        hand.addCard(Card(Suit.Heart, Rank.Three))

        assert(rules.canDouble(hand) == true)

        val player2 = Player(name = "Steve", strategy = BasicStrategy(BlackjackRules()))


        player2.bet()
        val hand2 = player2.hands[0]
        hand2.addCard(Card(Suit.Spade, Rank.Eight))
        hand2.addCard(Card(Suit.Heart, Rank.Eight))

        player2.splitHand(hand2, Card(Suit.Club, Rank.Three), Card(Suit.Heart, Rank.Ten))

        val subHand2 = hand2.splits[0]

        assert(rules.canDouble(subHand2) == true)
        rules.canDoubleAfterSplit = false
        assert(rules.canDouble(subHand2) == false)

        val player3 = Player(name = "Scott", strategy = BasicStrategy(BlackjackRules()))
        player3.bet()
        val hand3 = player3.hands[0]
        hand3.addCard(Card(Suit.Spade, Rank.Five))
        hand3.addCard(Card(Suit.Heart, Rank.Ace))

        rules.doublesRestrictedToHardTotals = arrayOf(9, 10, 11)

        assert(rules.canDouble(hand3) == false)

        val player4 = Player(name = "Sam", strategy = BasicStrategy(BlackjackRules()))


        player4.bet()
        val hand4 = player4.hands[0]
        hand4.addCard(Card(Suit.Spade, Rank.Five))
        hand4.addCard(Card(Suit.Heart, Rank.Six))

        assert(rules.canDouble(hand4) == true)

    }

    @Test
    fun testSplitRules() {
        val rules = BlackjackRules()

        var dealer = Dealer(BlackjackGame(rules))
        dealer.startHand()

        val player = Player(name = "Chris", strategy = BasicStrategy(BlackjackRules()))

        player.bet()
        val hand = player.hands[0]
        hand.addCard(Card(Suit.Spade, Rank.Ace))
        hand.addCard(Card(Suit.Heart, Rank.Ace))

        assert(rules.canSplit(hand) == true)

        player.splitHand(hand, dealer.dealCard(), dealer.dealCard())

        assert(hand.numberOfHands == 2)

        assert(rules.canSplit(hand.splits[0]) == false)
        assert(rules.canPlay(hand.splits[0]) == false)

        val player2 = Player(name = "Steve", strategy = BasicStrategy(BlackjackRules()))
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

    @Test
    fun testCounter() {
        val rules = BlackjackRules()
//        val betSpread = BetSpread(
//            hashMapOf(
//                0 to 1.0,
//                1 to 2.0,
//                2 to 3.0,
//                3 to 6.0,
//                4 to 12.0,
//                5 to 18.0,
//                6 to 30.0,
//                7 to 36.0,
//                8 to 50.0
//            )
//        )

        val betSpread = BetSpread(
            hashMapOf(
                0 to 1.0,
                1 to 4.0

            )
        )

        val bankrolls: MutableList<Int> = ArrayList()
        var lostMoneyCount = 0
        for (j in 1..25000) {
            val game = BlackjackGame(rules)
            val counter = Player(name = "ChrisCounter",
                strategy = HiLoCountingStrategy(betSpread, rules, illustriousEighteen = true),
                startingBankroll = 1000.00)
            game.addPlayer(counter)
            for (i in 1..4000) {
                game.playRound()
                if (counter.stats.bankroll <= 0) {
                    break
                }
            }
            if (counter.stats.bankroll <= 0.0) {
                lostMoneyCount += 1
            }
            bankrolls.add(counter.stats.bankroll.toInt())
        }

        var sum = 0
        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE
        for (b in bankrolls) {
            sum += b
            if (b < min) {
                min = b
            }
            if (b > max) {
                max = b
            }
        }
        val mean = sum.toDouble() / (bankrolls.size)
        println("min = ${min - 1000}")
        println("max = ${max - 1000}")
        println("average = ${mean - 1000.0}")

        var ss = 0.0
        for (b in bankrolls) {
            ss += ((b - 1000.0 - mean) * (b - 1000.0 - mean))
        }

        val variance = ss / (bankrolls.size - 1)
        val sqrt = sqrt(variance)

        println("stdev = $sqrt")
        println("$lostMoneyCount were ruined")

    }

    @Test
    fun testSpeedCountPlayer() {
        val rules = BlackjackRules()
        val betSpread = BetSpread(
            hashMapOf(
                0 to 1.0,
                11 to 1.0,
                12 to 2.0,
                13 to 4.0,
                14 to 8.0,
                15 to 16.0,
                16 to 32.0
            )
        )

        val bankrolls: MutableList<Int> = ArrayList()
        for (j in 1..50000) {
            val game = BlackjackGame(rules)
            val counter = SpeedCountPlayer(name = "ChrisCounter", betSpread, rules)
            game.addPlayer(counter)
            for (i in 1..80) {
                game.playRound()
                if (counter.stats.bankroll < -1000) {
                    break
                }
            }
            //println("Game $j")
            bankrolls.add(counter.stats.bankroll.toInt())
        }

        var sum = 0
        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE
        for (b in bankrolls) {
            sum += b
            if (b < min) {
                min = b
            }
            if (b > max) {
                max = b
            }
        }
        val mean = sum.toDouble() / (bankrolls.size)
        println("min = $min")
        println("max = $max")
        println("sum = ${sum}")
        println("average = ${mean}")

        var ss = 0.0
        for (b in bankrolls) {
            ss += ((b - mean) * (b - mean))
        }

        val variance = ss / (bankrolls.size - 1)
        val sqrt = sqrt(variance)

        println("stdev = $sqrt")

    }

    @Test
    fun testDeviations() {
        val rules = BlackjackRules()

        val game = BlackjackGame(rules)

        val betSpread = BetSpread(
            hashMapOf(
                0 to 1.0,
                11 to 1.0,
                12 to 2.0,
                13 to 4.0,
                14 to 8.0,
                15 to 16.0,
                16 to 32.0
            )
        )

        val player = Player("Chris", strategy = HiLoCountingStrategy(betSpread, rules))
        game.addPlayer(player)

        game.dealer.deck.stackWithCards(
            Card(Suit.Heart, Rank.Six),
            Card(Suit.Heart, Rank.Three),
            Card(Suit.Heart, Rank.Five),
            Card(Suit.Heart, Rank.Four),
            Card(Suit.Heart, Rank.Six),
            Card(Suit.Heart, Rank.Ten),
            Card(Suit.Heart, Rank.Seven),
            Card(Suit.Spade, Rank.Ten),
            Card(Suit.Club, Rank.Nine),
            Card(Suit.Diamond, Rank.Ten),
            Card(Suit.Heart, Rank.Nine),
            Card(Suit.Heart, Rank.Ten),
            Card(Suit.Heart, Rank.King),
            Card(Suit.Heart, Rank.Two),
            Card(Suit.Heart, Rank.Five),
            Card(Suit.Spade, Rank.Ace),
            Card(Suit.Heart, Rank.Two),
            Card(Suit.Heart, Rank.Ace),
            Card(Suit.Heart, Rank.Four),
            Card(Suit.Heart, Rank.Two),
            Card(Suit.Heart, Rank.Five),
            Card(Suit.Spade, Rank.Ace),
            Card(Suit.Heart, Rank.Two)
        )

        for (i in 1..2) {
            game.playRound(true)
        }

        game.printLog()
    }

    @Test
    fun testENHC() {
        val rules = BlackjackRules()
        rules.europeanNoHoleCard = true

        val game = BlackjackGame(rules)

        val player = Player("Chris", strategy = BasicStrategy(rules))
        game.addPlayer(player)

        game.dealer.deck.stackWithCards(
            Card(Suit.Club, Rank.King),
            Card(Suit.Diamond, Rank.Ace),
            Card(Suit.Heart, Rank.Ace),
            Card(Suit.Heart, Rank.Five),
            Card(Suit.Heart, Rank.King),
            Card(Suit.Heart, Rank.Two),
            Card(Suit.Heart, Rank.Five),
            Card(Suit.Spade, Rank.Ace),
            Card(Suit.Heart, Rank.Two),
            Card(Suit.Heart, Rank.Ace),
            Card(Suit.Heart, Rank.Four),
            Card(Suit.Heart, Rank.Two),
            Card(Suit.Heart, Rank.Five),
            Card(Suit.Spade, Rank.Ace),
            Card(Suit.Heart, Rank.Two)
        )

        game.playRound(true)
        game.printLog()

    }


}