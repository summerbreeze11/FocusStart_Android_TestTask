package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.example.myapplication.constance.constants
import com.example.myapplication.databinding.ActivityHistoryBinding
import java.io.File

class HistoryActivity : AppCompatActivity() {
    lateinit var bindingClass: ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = constants.history_menu_title
        val history = (intent.getStringExtra("key")).toString()
        val list = history.split("Card(")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,list)
        bindingClass.historylist.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.clean_history -> {
                cleanHistory()
                finish()
            }
        }
        return true
    }

    private fun cleanHistory() {

        val textFromFile =
            File(applicationContext.filesDir, constants.FILE_NAME)
                .bufferedReader()
                .use { it.readText(); }
        val fos = openFileOutput(constants.FILE_NAME, Context.MODE_PRIVATE)
        var clear = ""
        fos.write(clear.toByteArray())
        fos.close()
    }
}