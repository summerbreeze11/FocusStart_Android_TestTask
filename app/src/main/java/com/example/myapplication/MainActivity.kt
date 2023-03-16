package com.example.myapplication

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.retrofit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

//TODO ВРЕМЯ ОБЩЕЕ НА ВСЕХ ЗАПИСЯХ НАДО ПОФИКСТЬ + ДОБАВИТЬ ВСЕ СТРОКИ В СПИСОК

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val adapter = CardAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val retrofit = Retrofit.Builder().baseUrl("https://lookup.binlist.net/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val commoninfapi = retrofit.create(CommonInfapi::class.java)
        val cardnumber = binding.edFieldInfo.text
        binding.bGetInfo.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var common = commoninfapi.getCommon("$cardnumber")
                    runOnUiThread {
                        ///////
                        var manager = LinearLayoutManager(this@MainActivity)
                        binding.rcView.layoutManager = LinearLayoutManager(this@MainActivity)
                        binding.rcView.adapter = adapter
                        common.date = Date().toString()
                        /////////
                        try {
                            binding.tvLength.text = common.number.length
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = "Invalid data"
                        }
                        if (common.number.luhn) binding.tvluhn.text = "Yes"
                        else binding.tvluhn.text = "No"
                        try {
                            binding.tvScheme.text = common.scheme
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = "Invalid data"
                        }
                        try {
                            binding.tvType.text = common.type
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = "Invalid data"
                        }
                        try {
                            binding.tvBrand.text = common.brand
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = "Invalid data"
                        }
                        if (common.number.luhn) binding.tvPrepaid.text = "Yes"
                        else binding.tvPrepaid.text = "No"
                        try {
                            binding.tvCountry.text =
                                "${common.country.emoji}  ${common.country.name}"
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = "Invalid data"
                        }
                        try {
                            binding.tvLatiludeLongitude.text =
                                "(latitude:${common.country.latitude}, longitude:${common.country.longitude}"
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = "Invalid data"
                        }
                        try {
                            binding.tvBankNameCity.text =
                                "${common.bank.name}, ${common.bank.city}"
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = "Invalid data"
                        }
                        try {
                            binding.tvUrl.text = common.bank.url
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = "Invalid data"
                        }
                        try {
                            binding.tvPhone.text = common.bank.phone
                        } catch (e: java.lang.NullPointerException) {
                            binding.edFieldInfo.error = "Invalid data"
                        }
                        adapter.addCardRecord(common)
                        Log.d("Log", "$common")
                    }
                } catch (e: retrofit2.HttpException) {
                    runOnUiThread {
                        binding.edFieldInfo.error = "Invalid data"
                    }
                } catch (e: com.google.gson.stream.MalformedJsonException) {
                    runOnUiThread {
                        binding.edFieldInfo.error = "Invalid data"
                    }
                }
            }
        }
    }
//    private fun init(){
//        binding.apply {
//            rcView.layoutManager = LinearLayoutManager(this@MainActivity)
//            rcView.adapter = adapter
//            bGetInfo.setOnClickListener{
//                adapter.addCardRecord()
//            }
//        }
//    }
}
