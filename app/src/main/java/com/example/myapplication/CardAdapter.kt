package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.CardItemBinding
import com.example.myapplication.retrofit.Card
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList




class CardAdapter: RecyclerView.Adapter<CardAdapter.CardHolder>() {
    val cardList = ArrayList<Card>()
    class CardHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = CardItemBinding.bind(item)
        @SuppressLint("SetTextI18n")
        fun bind(card: Card) = with(binding){
            try {
                tvDate.text = card.date.toString()
                tvSchemeRec.text = "NETWORK: " + "\n" + changeIfNull(card.scheme)
                tvBrandRec.text = "BRAND:"+ "\n" + changeIfNull(card.brand)
                if (card.number.luhn)
                    tvCardNumLLRec.text = "CARD NUMBER:\nlength: " + changeIfNull(card.number.length) + " lunh: Yes"
                else tvCardNumLLRec.text = "CARD NUMBER:\nlength: " + changeIfNull(card.number.length) + " lunh: No"
                tvTypeRec.text = "TYPE: " + "\n" + changeIfNull(card.type)
                if(card.prepaid) tvPrepaidRec.text = "PREPAID: "+ "\n" + "Yes"
                else tvPrepaidRec.text = "PREPAID: "+ "\n" + "No"
                tvCountryRec.text = "COUNTRY: " + "\n" + changeIfNull(card.country.emoji) + changeIfNull(card.country.name)
                tvLatLongRec.text = "latitude: " + changeIfNull(card.country.latitude.toString()) +
                        " longitude: " + changeIfNull(card.country.longitude.toString())
                tvBankRec.text = "BANK: " + "\n" + changeIfNull(card.bank.name) + ", " + changeIfNull(card.bank.city)
                tvBankUrlRec.text =  changeIfNull(card.bank.url) + "\n" + changeIfNull(card.bank.phone)
            } catch (e: java.lang.NullPointerException) {
            }

        }
        private fun changeIfNull(str: String): String {
            if(str == null) return "?"
            else return str
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return CardHolder(view)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(cardList[position])
    }

    fun addCardRecord(card: Card){
        cardList.add(card)
        notifyDataSetChanged()
    }


}