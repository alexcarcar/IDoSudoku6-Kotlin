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

    private lateinit var boardRecyclerView: RecyclerView
    private var adapter: BoardAdapter? = BoardAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Board.create()
        adapter = BoardAdapter(convertToSquares(Board.data))
        boardRecyclerView = findViewById(R.id.board_recycler_view)
        boardRecyclerView.layoutManager = GridLayoutManager(applicationContext, 6)
        boardRecyclerView.hasFixedSize()
        boardRecyclerView.adapter = adapter
    }


    private fun convertToSquares(board: Array<IntArray>?): List<Square> {
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

    fun onNewGameClick(item: MenuItem) {}
    fun onSwitchSymbolsClick(item: MenuItem) {}
    fun onAboutClick(item: MenuItem) {}
    fun onSoundClick(item: MenuItem) {}

    private inner class SquareHolder(view: View) : RecyclerView.ViewHolder(view),
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
            val squareSize = boardRecyclerView.width / 6
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

    private inner class BoardAdapter(var squares: List<Square>) :
        RecyclerView.Adapter<SquareHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SquareHolder {
            val view = layoutInflater.inflate(R.layout.square, parent, false)
            return SquareHolder(view)
        }

        override fun getItemCount() = squares.size

        override fun onBindViewHolder(holder: SquareHolder, position: Int) {
            val square = squares[position]
            holder.bind(square)
        }
    }
}