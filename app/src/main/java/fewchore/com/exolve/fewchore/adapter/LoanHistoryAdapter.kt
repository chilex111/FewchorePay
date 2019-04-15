package fewchore.com.exolve.fewchore.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.model.History


class LoanHistoryAdapter(private val historyModelList: List<History>?, private val context: Context) : Adapter<LoanHistoryAdapter.HomeViewHolder>() {


    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView : CardView = itemView.findViewById(R.id.cardHolder)
        var amount: TextView = itemView.findViewById(R.id.textAmount)
        val duration: TextView = itemView.findViewById(R.id.textDuration)
        val status: TextView = itemView.findViewById(R.id.textStatus)
        var interest : TextView = itemView.findViewById(R.id.textInterest)


    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(paramViewGroup: ViewGroup, paramInt: Int): HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(paramViewGroup.context)
                .inflate(R.layout.custom_loan_history, paramViewGroup, false))
    }
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        holder.amount.text = historyModelList!![position].loanAmount
        holder.interest.text= historyModelList[position].loanInterest
        holder.duration.text= historyModelList[position].loanPaybackduration
        val input =  historyModelList[position].statusTitle
        if (!input.isNullOrEmpty()) {
            val output = input!!.substring(0, 1).toUpperCase() + input.substring(1)
            when {
                historyModelList[position].statusTitle == "reject" -> {
                    holder.status.text = context.getString(R.string.rejected)
                }
                historyModelList[position].statusTitle == "disbursed" -> {
                    holder.status.text = context.getString(R.string.active)
                    holder.status.setTextColor(ContextCompat.getColor(context, R.color.green))
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
                }
                historyModelList[position].statusTitle == "paid" -> {
                    holder.status.text = context.getString(R.string.paid)
                    holder.status.setTextColor(ContextCompat.getColor(context, R.color.dark_green))
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.dark_green))
                }
                else -> holder.status.text = output
            }
        }

    }

    override fun getItemCount(): Int {
        return historyModelList!!.size
    }




}
