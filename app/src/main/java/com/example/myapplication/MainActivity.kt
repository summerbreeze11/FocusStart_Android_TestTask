package com.example.myapplication

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.retrofit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var a = ArrayList<String>()
        var listcounter = 1
        val retrofit = Retrofit.Builder().baseUrl("https://lookup.binlist.net/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val commoninfapi = retrofit.create(CommonInfapi::class.java)
        val cardnumber = binding.edFieldInfo.text
        binding.bGetInfo.setOnClickListener {
            val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, a)
            binding.listView.adapter = adapter
            binding.listView.setOnItemClickListener { adapterView, view, i, l ->
            }
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var common = commoninfapi.getCommon("$cardnumber")
                    runOnUiThread {
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
                        a.add(" $listcounter - code$cardnumber ${common.toString()}")
                        listcounter++
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
}
