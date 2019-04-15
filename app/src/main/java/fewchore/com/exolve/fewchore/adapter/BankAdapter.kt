package fewchore.com.exolve.fewchore.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.listener.StringListener
import fewchore.com.exolve.fewchore.model.BankModel

class BankAdapter
(private val bankModelList: List<BankModel>?, private val context: Context, private val pageValue: String?)
    : RecyclerView.Adapter<BankAdapter.BankViewHolder>() {

    companion object {
        var stringlistener: StringListener? = null
        var cardPosition: Int? = -1
    }

    class BankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView? = null
        var textBankName: TextView? = null
        var textAcctNo: TextView? = null
        var textAcctType: TextView?= null
        var buttonEdit: Button ?= null

        init {
            cardView = itemView.findViewById(R.id.cardBank)
            textBankName = itemView.findViewById(R.id.textBank)
            textAcctNo = itemView.findViewById(R.id.textAcctNo)
            textAcctType = itemView.findViewById(R.id.textAcctType)
            buttonEdit = itemView.findViewById(R.id.buttonEdit)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.custom_bank, parent, false)

        return BankViewHolder(v)
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {

        val name = bankModelList!![position].bankName
        val type = bankModelList[position].acctType
        val number = bankModelList[position].acctNo
        val bvn = bankModelList[position].bvn

        holder.textBankName!!.text = name
        holder.textAcctNo!!.text = number
        holder.textAcctType!!.text = type

        try {
            holder.cardView!!.setOnClickListener {
                if (pageValue == "2") {
                    stringlistener!!.accountDetailsListener(name, type, number, "Details",bvn)
                } else {
                    cardPosition = position
                    notifyDataSetChanged()

                if (cardPosition == position) {
                    holder.cardView!!.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
                    stringlistener!!.accountDetailsListener(name, type, number, null, null)
                } else {
                    holder.cardView!!.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            }
        }catch (e: Exception){
           // Log.i("BANK_TAG", e.message)
        }
        holder.buttonEdit!!.setOnClickListener{
            val string="EDIT"
            stringlistener!!.accountDetailsListener(name, type, number, string, bvn)
        }
    }

    override fun getItemCount(): Int {
        return bankModelList!!.size
    }
}

