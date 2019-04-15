package fewchore.com.exolve.fewchore.invest

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.adapter.InvestmentHistoryAdapter
import fewchore.com.exolve.fewchore.apis.InvestmentHistoryAsync
import fewchore.com.exolve.fewchore.apis.SingleInvestmentAsync
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.listener.IdListener
import fewchore.com.exolve.fewchore.listener.InvestmentHistoryListener
import fewchore.com.exolve.fewchore.listener.SingleInvestmentListener
import fewchore.com.exolve.fewchore.model.InvestmentDetailConverted
import fewchore.com.exolve.fewchore.model.InvestmentDetails
import fewchore.com.exolve.fewchore.model.Investments


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [InvestmentHistoryFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [InvestmentHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class InvestmentHistoryFragment : Fragment(), InvestmentHistoryListener, IdListener, SingleInvestmentListener {



    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var appUtils: AppUtils ?= null
    private var frameLayout: RelativeLayout?= null

    private var textEmpty : TextView?= null
    private var relativeProgress : RelativeLayout?= null
    private var recyclerView : RecyclerView?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        appUtils = AppUtils(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_investment_history, container, false)

        InvestmentHistoryAsync.historyListener = this
        InvestmentHistoryAdapter.idlistener = this
        SingleInvestmentAsync.listener = this
        frameLayout = v.findViewById(R.id.home_history)

        if (param1=="1")
            frameLayout!!.setBackgroundColor(activity!!.resources.getColor(R.color.light_green))


        recyclerView = v.findViewById(R.id.recyclerView)
        textEmpty =  v.findViewById(R.id.textEmpty)
        relativeProgress = v.findViewById(R.id.relativeProgress)

        if (appUtils!!.hasActiveInternetConnection(activity!!)){
            InvestmentHistoryAsync(activity).execute()
        }
        else
            Toast.makeText(activity, "You have no active Internet connection", Toast.LENGTH_SHORT).show()
        return v
    }
    override fun idListener(id: String) {
        SingleInvestmentAsync(id, activity!!).execute()
    }
    override fun historyListener(history: List<Investments>?, status: Boolean, msg: String?) {

        if (status){
                relativeProgress!!.visibility = View.GONE
                val historyAdapter = InvestmentHistoryAdapter(history!!, this.activity!!, 2)
                recyclerView!!.layoutManager = LinearLayoutManager(activity)
                recyclerView!!.adapter = historyAdapter
                recyclerView!!.requestFocus()
        }
        else{
            if (msg.isNullOrEmpty())
            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
            else
                textEmpty!!.visibility =View.VISIBLE

        }
    }
    override fun singleInvestmentListener(investmentDetails: MutableList<InvestmentDetails>?, status: Boolean, msg: String?) {
       if (status) {
           val convertDetails = InvestmentDetailConverted()
           if (investmentDetails != null) {
               for (singleDetail in investmentDetails) {
                   convertDetails.bankName = singleDetail.bankName
                   convertDetails.investmentAmount = singleDetail.investmentAmount
                   convertDetails.investmentCreated = singleDetail.investmentCreated
                   convertDetails.investmentDuedate = singleDetail.investmentDuedate
                   convertDetails.investmentDuration = singleDetail.investmentDuration
                   convertDetails.investmentId = singleDetail.investmentId
                   convertDetails.investmentInterest = singleDetail.investmentInterest
                   convertDetails.investmentName = singleDetail.investmentName
                   convertDetails.investmentTotal = singleDetail.investmentTotal
                   convertDetails.statusTitle = singleDetail.statusTitle
                   convertDetails.userbankAccno = singleDetail.userbankAccno

               }
           }
           activity!!.supportFragmentManager.beginTransaction()
                   .add(R.id.container, IntestmentDetailFragment.newInstance(null, convertDetails))
                   .commit()
       }
        else{
           Toast.makeText(activity, msg,Toast.LENGTH_LONG).show()
       }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InvestmentHistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String?) =
                InvestmentHistoryFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
