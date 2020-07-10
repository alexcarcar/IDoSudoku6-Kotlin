package alex.carcar.idosudoku6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun onNewGameClick(item: MenuItem) {}
    fun onSwitchSymbolsClick(item: MenuItem) {}
    fun onAboutClick(item: MenuItem) {}
    fun onSoundClick(item: MenuItem) {}
}