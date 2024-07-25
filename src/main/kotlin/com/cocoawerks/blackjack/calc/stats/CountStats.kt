package com.cocoawerks.blackjack.calc.stats

data class CountStats(
    val indexDistribution: HashMap<Int, Int> = HashMap()

) {

    fun recordIndex(index: Int) {
        if (indexDistribution.containsKey(index)) {
            indexDistribution[index] = indexDistribution[index]!! + 1
        } else {
            indexDistribution[index] = 1
        }
    }
}