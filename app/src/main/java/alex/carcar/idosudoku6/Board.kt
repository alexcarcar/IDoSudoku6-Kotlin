package alex.carcar.idosudoku6

internal object Board {
    const val N = 6
    const val GROUP_WIDTH = 3
    const val GROUP_HEIGHT = 2

    val data: Array<IntArray> = Array(N) { IntArray(N) }
    private val hidden: Array<BooleanArray> = Array(N) { BooleanArray(N) }
    private val numbersUsed: BooleanArray = BooleanArray(N)
    private val excluded: BooleanArray = BooleanArray(N)

    /***
     * Create a new puzzle
     */
    val create = {
        do
            clear()
        while (!fill())
    }


    /***
     * Determine if the puzzle has been solved
     */
    fun winner(): Boolean {
        for (i in 0 until N) {
            clearNumbersUsed()
            // check each row
            for (j in 0 until N) mark(data[i][j])
            if (!allNumbersUsed()) return false
        }
        for (i in 0 until N) {
            clearNumbersUsed()
            // check each column
            for (j in 0 until N) mark(data[j][i])
            if (!allNumbersUsed()) return false
        }
        return checkGroups()
    }

    private fun allNumbersUsed(): Boolean {
        for (i in 0 until N) if (!numbersUsed[i]) return false
        return true
    }

    /***
     * Mark visible numbers as used.
     * @param number is hidden when number = 0 (and not marked.)
     */
    private fun mark(number: Int) {
        if (number != 0) numbersUsed[number - 1] = true;
    }

    /***
     * Hide a percentage of the board.
     * @param percent is between 0.00 to 1.00
     */
    fun hide(percent: Double) {
        for (i in 0 until N) {
            for (j in 0 until N) {
                if (Math.random() < percent) {
                    hidden[i][j] = true
                    data[i][j] = 0
                } else {
                    hidden[i][j] = false
                }
            }
        }
    }

    /***
     * Fill the board with numbers.
     */
    private fun fill(): Boolean {
        for (i in 0 until N) {
            for (j in 0 until N) {
                val v = fillSquare(i, j)
                if (v == -1) return false
                data[i][j] = v
            }
        }
        return true
    }

    private fun fillSquare(i: Int, j: Int): Int {
        val exclusions = fillExcluded(i, j)
        if (exclusions == N) {
            return -1 // Stuck, try again
        }
        var n: Int
        do {
            n = Math.floor(Math.random() * N).toInt() + 1
        } while (isExcluded(n))
        return n
    }

    /* Methods to exclude numbers from selection */
    private fun fillExcluded(i: Int, j: Int): Int {
        for (x in 0 until N) {
            excluded[x] = false
        }
        for (x in 0 until N) {
            doExclude(data[i][x])
            doExclude(data[x][j])
        }
        excludeGroup(i, j)
        var n = 0
        for (x in 0 until N) {
            if (excluded[x]) n++
        }
        return n
    }

    private fun checkGroup(i: Int, j: Int): Boolean {
        clearNumbersUsed()
        val x = j / GROUP_WIDTH * GROUP_WIDTH
        val y = i / GROUP_HEIGHT * GROUP_HEIGHT
        for (dy in 0 until GROUP_HEIGHT) for (dx in 0 until GROUP_WIDTH) mark(
            data[y + dy][x + dx]
        )
        return allNumbersUsed()
    }

    private fun checkGroups(): Boolean {
        var i = 0
        while (i < N) {
            var j = 0
            while (j < N) {
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
            data[y + dy][x + dx]
        )
    }


    private fun doExclude(n: Int) {
        if (n != 0) excluded[n - 1] = true
    }

    /***
     * Helper Methods
     */
    private val clear = { for (i in 0 until N) for (j in 0 until N) data[i][j] = 0 }
    private val clearNumbersUsed = { for (i in 0 until N) numbersUsed[i] = false }
    fun set(x: Int, y: Int, value: Int) { data[x][y] = value }
    private fun isExcluded(n: Int): Boolean = excluded[n - 1]
    fun isHidden(x: Int, y: Int): Boolean = hidden[x][y]
    fun complete(): Boolean {
        for (i in 0 until N) for (j in 0 until N) if (data[i][j] == 0) return false
        return true
    }
}