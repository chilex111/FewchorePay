package fewchore.com.exolve.fewchore.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.listener.CardBooleanListener
import fewchore.com.exolve.fewchore.model.Card

class LookUpCardAdapter(private val modelList: List<Card>, private val context: Context)
    : RecyclerView.Adapter<LookUpCardAdapter.CardViewHolder>(){

    companion object {
        var listener: CardBooleanListener? = null
        var cardPosition: Int? = -1
        @SuppressLint("StaticFieldLeak")
        var viewHolder: CardViewHolder? = null

    }


    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var buttonActivate: Button = itemView.findViewById(R.id.buttonActivate)
        var textNo: TextView ?= null
        val textExpiry:TextView
        val textCVV: TextView


        init {
            this.textNo = itemView.findViewById(R.id.textCardNo)
            this.textExpiry = itemView.findViewById(R.id.textExpiry)
            this.textCVV = itemView.findViewById(R.id.textCardCVV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.custom_look_up, parent, false)


        return CardViewHolder(v)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        viewHolder = holder

        val cardNum =modelList[position].maskedPan
        val cardCVV =  "***"
        val cardExp =  "**/**"
        val cardId = modelList[position].identifier
        val valueCVV = modelList[position].requireCvv
        val valueExp = modelList[position].requireExpiry
        val valuePIN = modelList[position].requirePin


        holder.textNo!!.text = cardNum
        holder.textCVV.text =cardCVV
        holder.textExpiry.text= cardExp


        try {
            holder.buttonActivate.setOnClickListener {
                listener!!.lookUpListener(cardId,valueCVV,valueExp,valuePIN)
            }

        }catch (e: Exception){
            Log.i("CARD_TAG", e.message)
        }

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

  

}

