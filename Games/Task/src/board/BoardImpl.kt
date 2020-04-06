package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

/**
 * SquareBoard Implementation class
 */
open class SquareBoardImpl(override val width: Int) : SquareBoard {
    protected val boardCells = createBoardCells(width)

    private fun createBoardCells(width: Int): List<Cell> {
        val cells = mutableListOf<Cell>()
        for (i in 1..width) {
            for (j in 1..width) {
                cells.add(Cell(i, j))
            }
        }
        return cells
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? =
            boardCells.firstOrNull { cell -> cell.i == i && cell.j == j }

    override fun getCell(i: Int, j: Int): Cell =
            boardCells.firstOrNull { cell -> cell.i == i && cell.j == j }
                    ?: throw IllegalArgumentException("Invalid cell position $i, $j")

    override fun getAllCells(): Collection<Cell> = boardCells

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> =
            boardCells.filter { it.i == i && it.j in jRange }.let {
            if (jRange.first > jRange.last) it.reversed() else it
        }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> =
            boardCells.filter { it.i in iRange && it.j==j }.let {
                if(iRange.first > iRange.last) it.reversed() else it
            }

    override fun Cell.getNeighbour(direction: Direction): Cell? =
            when (direction) {
                UP -> getCellOrNull(i - 1, j)
                LEFT -> getCellOrNull(i, j - 1)
                DOWN -> getCellOrNull(i + 1, j)
                RIGHT -> getCellOrNull(i, j + 1)
            }
}


/**
 * Gameboard implementation class
 */
class GameBoardImpl<T>(width: Int): SquareBoardImpl(width), GameBoard<T> {
    private val cellValueMap = mutableMapOf<Cell, T?>()

    init {
        boardCells.forEach { cell-> cellValueMap[cell] = null }
    }


    override fun get(cell: Cell): T? = cellValueMap[cell]

    override fun set(cell: Cell, value: T?) = cellValueMap.set(cell, value)

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
            cellValueMap.filterValues(predicate).keys

    override fun find(predicate: (T?) -> Boolean): Cell? =
            cellValueMap.filterValues(predicate).keys.firstOrNull()

    override fun any(predicate: (T?) -> Boolean): Boolean =
            cellValueMap.filterValues(predicate).isNotEmpty()

    override fun all(predicate: (T?) -> Boolean): Boolean =
            cellValueMap.values.all(predicate)

}