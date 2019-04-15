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
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.listener.IdListener
import fewchore.com.exolve.fewchore.listener.StringListener
import fewchore.com.exolve.fewchore.model.Investments
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class InvestmentHistoryAdapter(private val historyModelList: List<Investments>?,
                               private val context: Context, private val from: Int)
    : Adapter<InvestmentHistoryAdapter.HomeViewHolder>() {

    companion object {
        var idlistener: IdListener? = null
    }
    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView : CardView = itemView.findViewById(R.id.cardHolder)
        var plan: TextView = itemView.findViewById(R.id.textPlan)
        val investedAmt: TextView = itemView.findViewById(R.id.textInvested)
        var totalEarning : TextView = itemView.findViewById(R.id.textTotal)
        var plan1: TextView = itemView.findViewById(R.id.textPlan1)
        val investedAmt1: TextView = itemView.findViewById(R.id.textInvested1)
        var totalEarning1 : TextView = itemView.findViewById(R.id.textTotal1)
        var due_date : TextView = itemView.findViewById(R.id.textDueDate)
        var lineShow: LinearLayout = itemView.findViewById(R.id.linearShow)
        var broadShow: RelativeLayout = itemView.findViewById(R.id.broadShow)
        var seemore : Button = itemView.findViewById(R.id.buttonDetails)


    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(paramViewGroup: ViewGroup, paramInt: Int): HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(paramViewGroup.context)
                .inflate(R.layout.custom_investment_history, paramViewGroup, false))
    }
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        if (from == 1){
            holder.broadShow.visibility = View.GONE
            holder.lineShow.visibility = View.VISIBLE
            val valie  = historyModelList!![position].investmentTotal.toDouble()
            val convertedVal = NumberFormat.getNumberInstance(Locale.US).format(valie)
            holder.plan.text = historyModelList[position].investmentName
            holder.totalEarning.text= "₦$convertedVal"

            holder.investedAmt.text= "₦"+historyModelList[position].investmentAmount
            when {
                historyModelList[position].statusTitle =="withdrawn" -> {
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey))
                }
                historyModelList[position].statusTitle =="active" -> {
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
                }
                historyModelList[position].statusTitle =="matured" -> {
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.dark_green))
                }
            }

        }
        if (from == 2){
            holder.broadShow.visibility = View.VISIBLE
            holder.lineShow.visibility = View.GONE

            holder.plan1.text = historyModelList!![position].investmentName
            val valie  = historyModelList[position].investmentTotal.toDouble()
            val convertedVal = NumberFormat.getNumberInstance(Locale.US).format(valie)
            holder.totalEarning1.text= "₦$convertedVal"
            holder.investedAmt1.text= "₦"+historyModelList[position].investmentAmount
            holder.due_date.text = historyModelList[position].investmentDuedate
            when {
                historyModelList[position].statusTitle =="withdrawn" -> {
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey))
                }
                historyModelList[position].statusTitle =="active" -> {
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
                }
                historyModelList[position].statusTitle =="matured" -> {
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.dark_green))
                }
            }
        }
        holder.seemore.setOnClickListener {
            idlistener!!.idListener(historyModelList!![position].investmentId)
        }

    }

    override fun getItemCount(): Int {
        return historyModelList!!.size
    }




}
