package fewchore.com.exolve.fewchore.invest

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [InvestHomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [InvestHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class InvestHomeFragment : Fragment(), InvestmentHistoryListener, IdListener, SingleInvestmentListener {



    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var recyclerView: RecyclerView? = null
    private var greetings: TextView? = null
    private var textName: TextView? = null
    private var textLog: TextView? = null
    private var textEmpty: TextView?= null
    private var firstTimer: ConstraintLayout?= null
    private var returner: ConstraintLayout?= null
    private var relativeProgress: RelativeLayout?= null
    private var appUtils : AppUtils ?= null

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
        val v = inflater.inflate(R.layout.fragment_invest_home, container, false)

        InvestmentHistoryAsync.historyListener = this
        InvestmentHistoryAdapter.idlistener = this
        SingleInvestmentAsync.listener = this

        recyclerView = v.findViewById(R.id.recyclerView)
        greetings = v.findViewById(R.id.textGreets) as TextView
        textLog= v.findViewById(R.id.textSeen)
        textName = v.findViewById(R.id.textName)
        relativeProgress = v.findViewById(R.id.relativeProgress)
        textEmpty = v.findViewById(R.id.textEmpty)

        val name = AppUtils.getMyFirstName(context)
        textName!!.text = name
        val c = Calendar.getInstance()
        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

        when (timeOfDay) {
            in 0..11 -> greetings!!.text = getString(R.string.morning)
            in 12..15 -> greetings!!.text = getString(R.string.afternoon)
            in 16..23 -> greetings!!.text = getString(R.string.evening)
        }
        firstTimer = v.findViewById(R.id.starter)
        returner = v.findViewById(R.id.returner)

        firstTimer!!.visibility = View.GONE
        returner!!.visibility = View.GONE

        v.findViewById<Button>(R.id.buttonGetStarted).setOnClickListener {
            listner!!.idListener("Invest")
        }

        if (param1 == "1"){
            firstTimer!!.visibility = View.GONE
            returner!!.visibility = View.VISIBLE
            if (appUtils!!.hasActiveInternetConnection(activity!!)){
                relativeProgress!!.visibility = View.VISIBLE
                InvestmentHistoryAsync(activity).execute()
            }
            else
                Toast.makeText(activity, "You have no active Internet connection", Toast.LENGTH_SHORT).show()
        }else if (param1 == "0"){
            firstTimer!!.visibility = View.VISIBLE
            returner!!.visibility = View.GONE
        }

        return v
    }

    override fun onResume() {
        super.onResume()
        if (param1 == "1"){
            firstTimer!!.visibility = View.GONE
            returner!!.visibility = View.VISIBLE
            if (appUtils!!.hasActiveInternetConnection(activity!!)){
                InvestmentHistoryAsync(activity).execute()
            }
            else
                Toast.makeText(activity, "You have no active Internet connection", Toast.LENGTH_SHORT).show()
        }else if (param1 == "0"){
            firstTimer!!.visibility = View.VISIBLE
            returner!!.visibility = View.GONE
        }
    }
    override fun idListener(id: String) {
        if (appUtils!!.hasActiveInternetConnection(activity!!))
            SingleInvestmentAsync(id, activity!!).execute()
    }

    override fun historyListener(history: List<Investments>?, status: Boolean, msg: String?) {
        if (status){
            returner!!.visibility = View.VISIBLE
            firstTimer!!.visibility = View.GONE
            textEmpty!!.visibility =View.GONE

            relativeProgress!!.visibility = View.GONE
            val historyAdapter = InvestmentHistoryAdapter(history!!, activity!!, 1)
            recyclerView!!.layoutManager = LinearLayoutManager(activity)
            recyclerView!!.adapter = historyAdapter
            recyclerView!!.requestFocus()
        }
        else{
            if (msg.isNullOrEmpty())
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
            else
                textEmpty!!.visibility =View.VISIBLE

        }    }

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
        var listner:IdListener ?= null
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InvestHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String?) =
                InvestHomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }



}



