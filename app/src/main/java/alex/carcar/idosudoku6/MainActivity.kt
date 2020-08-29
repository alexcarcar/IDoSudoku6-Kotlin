package alex.carcar.idosudoku6

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var boardView: RecyclerView
    private var aBoard: ABoard? = ABoard(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Board.create()
        aBoard = ABoard(mapSquares(Board.data))
        boardView = findViewById(R.id.board_recycler_view)
        boardView.layoutManager = GridLayoutManager(applicationContext, 6)
        boardView.hasFixedSize()
        boardView.adapter = aBoard
    }


    private fun mapSquares(board: Array<IntArray>?): List<Square> {
        val squares = mutableListOf<Square>()
        var i: Int = -1
        var j: Int
        board!!.forEach { row ->
            i++
            j = -1
            row.forEach { x ->
                j++
                squares.add(Square(i, j, x))
            }
        }
        return squares
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun clickNewGame(item: MenuItem) {}
    fun clickSwitchSymbols(item: MenuItem) {}
    fun clickAbout(item: MenuItem) {}
    fun clickSound(item: MenuItem) {}

    private inner class ASquare(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var square: Square
        private val squareView: Button = itemView.findViewById(R.id.square)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(square: Square) {
            val gx = if (square.col < 3) 0 else 1
            val gy = if (square.row < 2) 0 else if (square.row < 4) 1 else 2
            val g1 = (gx + gy) % 2 == 0
            squareView.setBackgroundResource(
                if ((square.row + square.col) % 2 == 0)
                    if (g1) R.color.group1Light else R.color.group2Light
                else
                    if (g1) R.color.group1Dark else R.color.group2Dark
            )
            this.square = square
            val squareSize = boardView.width / 6
            squareView.width = squareSize
            squareView.height = squareSize
            squareView.setTextSize(TypedValue.COMPLEX_UNIT_PX, squareSize * 0.5F)
            squareView.text = this.square.value.toString()
            squareView.visibility = if (!square.isHidden) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(v: View?) {
            Log.d(TAG, square.value.toString())
        }
    }

    private inner class ABoard(var squares: List<Square>) :
        RecyclerView.Adapter<ASquare>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ASquare {
            val view = layoutInflater.inflate(R.layout.square, parent, false)
            return ASquare(view)
        }

        override fun getItemCount() = squares.size

        override fun onBindViewHolder(aSquare: ASquare, position: Int) {
            val square = squares[position]
            aSquare.bind(square)
        }
    }
}