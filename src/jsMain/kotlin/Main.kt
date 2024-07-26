import com.cocoawerks.blackjack.sim.BlackjackGame
import com.cocoawerks.blackjack.sim.BlackjackRules
import com.cocoawerks.blackjack.sim.entity.Player
import com.cocoawerks.blackjack.sim.strategy.BasicStrategy

fun main() {

    val rules = BlackjackRules()

    val game = BlackjackGame(rules)

    val player = Player("Chris", strategy = BasicStrategy(rules))
    game.addPlayer(player)

    game.playRound()
    game.printLog()
}