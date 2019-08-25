fun main() {
    println(calculate(12345))
}

fun calculate(score: Int): Set<Hand> {
    val hands = mutableSetOf<Hand>()
    val digits: List<Int> = score.toString().toList().map { it.toString().toInt() }

    val sum: Int = digits.sum()
    when (sum) {
        in 0..17 -> {
            hands.add(NormalHand.Low)
        }
        21 -> {
            hands.add(NormalHand.BlackJack)
        }
        in 37..45 -> {
            hands.add(NormalHand.High)
        }
    }

    val occurrence = mutableMapOf<Int, Int>()
    for (digit in digits) {
        val current = occurrence[digit] ?: 0
        occurrence[digit] = current + 1
    }
    println(occurrence)

    return hands
}

interface Hand

enum class NormalHand(point: Int) : Hand {
    TwoPairs(1), ThreeCards(1), Straight(2), FullHouse(2), FourCards(3),
    HighScore(1), Push(2), BlackJack(2), High(1), Low(1),
    Flash(1), ThreeSevens(2)
}

enum class SpecialHand(bottles: Int) : Hand {
    StraightFlash(1), FiveCards(1),
    AllSeven(2), Perfect(2)
}