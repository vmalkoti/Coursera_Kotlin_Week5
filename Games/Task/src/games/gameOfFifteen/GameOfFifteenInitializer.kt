package games.gameOfFifteen

interface GameOfFifteenInitializer {
    /*
     * Even permutation of numbers 1..15
     * used to initialized the first 15 cells on a board.
     * The last cell is empty.
     */
    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {
    /*
     * Generate a random permutation from 1 to 15.
     * `shuffled()` function might be helpful.
     * If the permutation is not even, make it even (for instance,
     * by swapping two numbers).
     */
    override val initialPermutation by lazy {
        val initial = (1..15).shuffled().toMutableList()
        if(!isEven(initial)) {
            val swapPair = initial.withIndex()
                    .associateBy(
                            {it.value},
                            { pair -> initial.drop(pair.index + 1)
                                    .firstOrNull { it < pair.value }?:0})
                    .filterValues { it!=0 }.entries.first().toPair()

            initial.replaceAll { when(it) {
                swapPair.first -> swapPair.second
                swapPair.second -> swapPair.first
                else -> it
            } }
        }
        initial.toList()
    }
}

