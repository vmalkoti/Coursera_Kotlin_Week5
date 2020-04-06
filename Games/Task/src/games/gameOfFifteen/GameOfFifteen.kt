package games.gameOfFifteen

import board.*
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)


/**
 * Game class implementation
 */
class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val width = 4
    private val board = createGameBoard<Int>(width)

    override fun initialize() {
        val boardRows = initializer.initialPermutation.chunked(4)
        for (i in 1..4) {
            for (j in 1..4) {
                board[Cell(i, j)] = boardRows[i - 1].getOrNull(j - 1)
            }
        }
    }

    // Error message in test says "The move for game of fifteen should be always possible"
    override fun canMove(): Boolean = true
            /* isEven(board.getAllCells().mapNotNull { board[it] })
            * // calling isEven caused unit test failures for odd parities so commented this
            * */

    override fun hasWon(): Boolean =
            board.getAllCells().map { board[it] }
                    .withIndex().all {
                        ((it.index + 1)%(width*width)) - ((it.value ?: 0)%(width*width)) == 0
                    }

    override fun processMove(direction: Direction) {
        with(board) {
            val emptyCell = filter { it == null }.first()
            emptyCell.getNeighbour(direction.reversed())
                    ?.let { moveCell ->
                        set(emptyCell, get(moveCell))
                        set(moveCell, null)
                    }
        }
    }

    override fun get(i: Int, j: Int): Int? = board[Cell(i, j)]

}