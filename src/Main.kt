package jp.wozniak.karasco

fun main() {
    println(calculate(77007))
}

fun calculate(score: Int): Set<Hand> {
    val digits: List<Int> = score.toString().toList().map { it.toString().toInt() }

    val hands = mutableSetOf<Hand>()
    hands.sumCheck(digits)
    hands.occurrenceCheck(digits)
    hands.continuousCheck(digits)

    return hands
}

fun MutableSet<Hand>.sumCheck(digits: List<Int>) {
    val hands = this
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
}

fun MutableSet<Hand>.occurrenceCheck(digits: List<Int>) {
    val hands = this
    val occurrences =
        digits.groupingBy { it }.eachCount().map { Occurrence(it.key, it.value) }.sortedByDescending { it.times }
    println(occurrences)

    when (occurrences[0].times) {
        5 -> {
            if (occurrences[0].digit == 7) {
                hands.add(SpecialHand.AllSeven)
            } else {
                hands.add(SpecialHand.FiveCards)
            }
        }
        4 -> {
            hands.add(NormalHand.FourCards)
        }
        3 -> {
            when (occurrences[1].times) {
                2 -> hands.add(NormalHand.FullHouse)
                1 -> hands.add(NormalHand.ThreeCards)
            }
        }
        2 -> {
            when (occurrences[1].times) {
                2 -> hands.add(NormalHand.TwoPairs)
            }
        }
    }
}

fun MutableSet<Hand>.continuousCheck(digits: List<Int>) {
    val hands = this
    val continuousOccurrences = mutableListOf<Occurrence>()
    for (digit in digits) {
        val lastOccurrence = continuousOccurrences.lastOrNull()
        if (lastOccurrence != null && digit == lastOccurrence.digit) {
            lastOccurrence.increment()
        } else {
            continuousOccurrences.add(Occurrence(digit, 1))
        }
    }
    continuousOccurrences.sortByDescending { it.times }
    println(continuousOccurrences)

    val mostOccurred = continuousOccurrences[0]
    when (mostOccurred.times) {
        4 -> {
            if (hands.contains(NormalHand.FourCards)) {
                hands.add(NormalHand.Flash)
            }
        }
        3 -> {
            when (continuousOccurrences[1].times) {
                2 -> {
                    if (hands.contains(NormalHand.FullHouse)) {
                        hands.add(NormalHand.Flash)
                    }
                }
                1 -> {
                    if (hands.contains(NormalHand.ThreeCards)) {
                        hands.add(NormalHand.Flash)
                    }

                }
            }
            if (mostOccurred.digit == 7) {
                hands.add(NormalHand.ThreeSevens)
            }
        }
        2 -> {
            when (continuousOccurrences[1].times) {
                2 -> {
                    if (hands.contains(NormalHand.TwoPairs)) {
                        hands.add(NormalHand.Flash)
                    }
                }
            }
        }
    }
}

class Occurrence(val digit: Int, var times: Int) {
    fun increment() = this.times++
    override fun toString(): String = "$digit($times)"
}
