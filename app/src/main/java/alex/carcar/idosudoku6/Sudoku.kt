package carcar.alex.idosudoku6

internal object Sudoku {
    var board: Array<IntArray>? = null
    var hide: Array<BooleanArray>? = null
    private var BOARD_SIZE = 0
    private var GROUP_WIDTH = 0
    private var GROUP_HEIGHT = 0
    private var exclude: BooleanArray? = null
    private var checker: BooleanArray? = null
    private fun defineBoard(size: Int, width: Int) {
        board = null
        exclude = null
        checker = null
        hide = null
        BOARD_SIZE = size
        GROUP_WIDTH = width
        GROUP_HEIGHT = size / width
    }

    private fun clearBoard() {
        if (board == null) {
            board = Array(
                BOARD_SIZE
            ) { IntArray(BOARD_SIZE) }
            exclude = BooleanArray(BOARD_SIZE)
            checker = BooleanArray(BOARD_SIZE)
            hide = Array(
                BOARD_SIZE
            ) { BooleanArray(BOARD_SIZE) }
        }
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                board!![i][j] = 0
            }
        }
    }

    private fun clearChecker() {
        for (i in 0 until BOARD_SIZE) {
            checker!![i] = false
        }
    }

    private val isComplete: Boolean
        get() {
            for (i in 0 until BOARD_SIZE) {
                if (!checker!![i]) {
                    return false
                }
            }
            return true
        }

    private fun mark(value: Int) {
        if (value == 0) return  // square is not set
        checker!![value - 1] = true
    }

    fun hide(percent: Double) {
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                if (Math.random() < percent) {
                    hide!![i][j] = true
                    board!![i][j] = 0
                } else {
                    hide!![i][j] = false
                }
            }
        }
    }

    private fun printBoard() {
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                print(board!![i][j])
            }
            println("")
        }
    }

    private fun fillBoard(): Int {
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                val v = fillSquare(i, j)
                if (v == -1) return -1
                board!![i][j] = v
            }
        }
        return 1
    }

    private fun fillSquare(i: Int, j: Int): Int {
        val exclusions = fillExcluded(i, j)
        if (exclusions == BOARD_SIZE) {
            return -1 // Stuck, try again
        }
        var n: Int
        do {
            n = Math.floor(Math.random() * BOARD_SIZE).toInt() + 1
        } while (isExcluded(n))
        return n
    }

    /* Methods to exclude numbers from selection */ /* ================================================ */
    private fun fillExcluded(i: Int, j: Int): Int {
        for (x in 0 until BOARD_SIZE) {
            exclude!![x] = false
        }
        for (x in 0 until BOARD_SIZE) {
            doExclude(board!![i][x])
            doExclude(board!![x][j])
        }
        excludeGroup(i, j)
        var n = 0
        for (x in 0 until BOARD_SIZE) {
            if (exclude!![x]) n++
        }
        return n
    }

    private fun checkGroup(i: Int, j: Int): Boolean {
        clearChecker()
        val x = j / GROUP_WIDTH * GROUP_WIDTH
        val y = i / GROUP_HEIGHT * GROUP_HEIGHT
        for (dy in 0 until GROUP_HEIGHT) for (dx in 0 until GROUP_WIDTH) mark(
            board!![y + dy][x + dx]
        )
        return isComplete
    }

    private fun checkGroups(): Boolean {
        var i = 0
        while (i < BOARD_SIZE) {
            var j = 0
            while (j < BOARD_SIZE) {
                if (!checkGroup(i, j)) return false
                j += GROUP_WIDTH
            }
            i += GROUP_HEIGHT
        }
        return true
    }

    private fun excludeGroup(i: Int, j: Int) {
        val x = j / GROUP_WIDTH * GROUP_WIDTH
        val y = i / GROUP_HEIGHT * GROUP_HEIGHT
        for (dy in 0 until GROUP_HEIGHT) for (dx in 0 until GROUP_WIDTH) doExclude(
            board!![y + dy][x + dx]
        )
    }

    private fun doExclude(n: Int) {
        if (n != 0) exclude!![n - 1] = true
    }

    fun isHidden(x: Int, y: Int): Boolean {
        return hide!![x][y]
    }

    private fun isExcluded(n: Int): Boolean {
        return exclude!![n - 1]
    }

    /* ================================================ */
    fun create() {
        defineBoard(6, 3)
        do {
            clearBoard()
        } while (fillBoard() == -1)
        printBoard()
    }

    fun put(x: Int, y: Int, value: Int) {
        board!![x][y] = value
    }

    fun complete(): Boolean {
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                if (board!![i][j] == 0) {
                    return false
                }
            }
        }
        return true
    }

    fun win(): Boolean {

        // check rows
        for (i in 0 until BOARD_SIZE) {
            clearChecker()
            for (j in 0 until BOARD_SIZE) {
                mark(board!![i][j])
            }
            if (!isComplete) {
                return false
            }
        }

        // check columns
        for (i in 0 until BOARD_SIZE) {
            clearChecker()
            for (j in 0 until BOARD_SIZE) {
                mark(board!![j][i])
            }
            if (!isComplete) {
                return false
            }
        }

        // check groups
        return checkGroups()
    }
}