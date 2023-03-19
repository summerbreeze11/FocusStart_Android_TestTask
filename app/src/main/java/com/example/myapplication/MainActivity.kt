package com.example.myapplication


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.constance.constants
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.retrofit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val adapter = CardAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = constants.main_menu_title
        getInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.history -> {
                var buff = tryOpenFile()
                val intent = Intent(this, HistoryActivity::class.java)
                intent.putExtra("key", "$buff")
                startActivity(intent)
            }
        }
        return true
    }

    private fun getInfo(){
        val retrofit = Retrofit.Builder().baseUrl(constants.base_url)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val commoninfapi = retrofit.create(CommonInfapi::class.java)
        val cardnumber = binding.edFieldInfo.text
        binding.bGetInfo.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var common = commoninfapi.getCommon("$cardnumber")
                    runOnUiThread {
                        var manager = LinearLayoutManager(this@MainActivity)
                        binding.rcView.layoutManager = LinearLayoutManager(this@MainActivity)
                        binding.rcView.adapter = adapter
                        common.date = Date().toString()
                        common.cardnumber = cardnumber.toString()
                        try {
                            binding.tvLength.text = common.number.length
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = constants.error_msg
                        }
                        if (common.number.luhn) binding.tvluhn.text = "Yes"
                        else binding.tvluhn.text = "No"
                        try {
                            binding.tvScheme.text = common.scheme
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = constants.error_msg
                        }
                        try {
                            binding.tvType.text = common.type
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = constants.error_msg
                        }
                        try {
                            binding.tvBrand.text = common.brand
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = constants.error_msg
                        }
                        if (common.number.luhn) binding.tvPrepaid.text = "Yes"
                        else binding.tvPrepaid.text = "No"
                        try {
                            binding.tvCountry.text =
                                "${common.country.emoji}  ${common.country.name}"
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = constants.error_msg
                        }
                        try {
                            binding.tvLatiludeLongitude.text =
                                "(latitude:${common.country.latitude}, longitude:${common.country.longitude}"
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = constants.error_msg
                        }
                        try {
                            binding.tvBankNameCity.text =
                                "${common.bank.name}, ${common.bank.city}"
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = constants.error_msg
                        }
                        try {
                            binding.tvUrl.text = common.bank.url
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = constants.error_msg
                        }
                        try {
                            binding.tvPhone.text = common.bank.phone
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = constants.error_msg
                        }
                        adapter.addCardRecord(common)
                        trySaveIntoFile(constants.FILE_NAME,"$common")
                    }
                } catch (e: retrofit2.HttpException) {
                    runOnUiThread {
                        binding.edFieldInfo.error = constants.error_msg
                    }
                } catch (e: com.google.gson.stream.MalformedJsonException) {
                    runOnUiThread {
                        binding.edFieldInfo.error = constants.error_msg
                    }
                }
            }
        }
    }

    private fun trySaveIntoFile(fileName: String, common : String) {
            try {
                saveIntoFile(fileName, common)
            } catch (e: Exception) {
                showError(constants.error_file_msg)
            }

    }
    private fun showError(res: String) {
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
    }
    private fun saveIntoFile(fileName: String, common : String) {

        val textFromFile =
            File(applicationContext.filesDir, fileName)
                .bufferedReader()
                .use { it.readText(); }
        var buff = textFromFile + "\n" +  common
        val fos = openFileOutput(fileName, Context.MODE_PRIVATE)
        fos.write(buff.toByteArray())
        fos.close()
    }

    private fun tryOpenFile(): String {
            try {
                val temp = openFile()
                return temp
            } catch (e: Exception) {
                showError(constants.error_file_msg)
            }
        return constants.error_file_msg
    }

    private fun openFile() : String {
        val file = File(filesDir, constants.FILE_NAME)
        val data = FileInputStream(file).use {
            String(it.readBytes())
        }
        return data
    }

}
