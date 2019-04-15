package fewchore.com.exolve.fewchore.child_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.makeText
import fewchore.com.exolve.fewchore.R

import fewchore.com.exolve.fewchore.adapter.LoanHistoryAdapter
import fewchore.com.exolve.fewchore.apis.HistoryAsnc
import fewchore.com.exolve.fewchore.apis.StatusAsync
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.listener.HistoryListener
import fewchore.com.exolve.fewchore.listener.LoanListener
import fewchore.com.exolve.fewchore.listener.StatusListener
import fewchore.com.exolve.fewchore.model.History
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DashHomeFragment : Fragment(), HistoryListener,StatusListener{

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var recyclerView: RecyclerView? = null
    private var greetings: TextView? = null
    private var textName: TextView? = null
    private var textLog: TextView? = null
    private var textEmpty: TextView ?= null
    private var firstLinear: LinearLayout?= null
    private var returnLinear: LinearLayout?= null
    private var relativeProgress: RelativeLayout ?= null
    private var appUtils: AppUtils ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
                param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ShowToast")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_dash_home, container, false) as View

        HistoryAsnc.historyListener = this
        StatusAsync.statusListener = this


        appUtils = AppUtils(this.activity!!)
        recyclerView = view.findViewById(R.id.recyclerView)
        greetings = view.findViewById(R.id.textGreets) as TextView
        textLog= view.findViewById(R.id.textSeen)
        textName = view.findViewById(R.id.textName)
        relativeProgress = view.findViewById(R.id.relativeProgress)
        textEmpty = view.findViewById(R.id.textEmpty)



        val buttonTakeLoan = view.findViewById<Button>(R.id.buttonTakeLoan)
        buttonTakeLoan.setOnClickListener {
           loanListener!!.loanListener(true)
        }


        val name = AppUtils.getMyFirstName(context)
        textName!!.text = name
        val c = Calendar.getInstance()
        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

        if (timeOfDay in 0..11) {
            greetings!!.text = getString(R.string.morning)
        } else if (timeOfDay in 12..15) {
            greetings!!.text = getString(R.string.afternoon)
        } else if (timeOfDay in 16..23) {
            greetings!!.text = getString(R.string.evening)
        }

         firstLinear = view.findViewById(R.id.firstlinear)
         returnLinear = view.findViewById(R.id.linear1)

       // relativeProgress!!.visibility = View.VISIBLE
       //
        when {
            AppUtils.getMyLoanStatus(activity) == "1" -> {
              /*  returnLinear!!.visibility = View.VISIBLE
                firstLinear!!.visibility = View.GONE*/

                HistoryAsnc(activity).execute()
            }
            AppUtils.getMyLoanStatus(activity) == "0" -> {
              /*  returnLinear!!.visibility = View.GONE
                firstLinear!!.visibility = View.VISIBLE

                relativeProgress!!.visibility = View.VISIBLE*/

                StatusAsync(activity).execute()
            }
        }

        return view
    }

    override fun historyListener(history: List<History>?, status: Boolean) {
        relativeProgress!!.visibility = View.GONE
        if (status) {
            if (history != null &&(!history.isEmpty())) {
                if (activity != null) {
                    returnLinear!!.visibility = View.VISIBLE
                    firstLinear!!.visibility = View.GONE
                    val audioAdapter = LoanHistoryAdapter(history, activity!!)
                    recyclerView!!.layoutManager = LinearLayoutManager(activity)
                    recyclerView!!.adapter = audioAdapter
                    recyclerView!!.requestFocus()
                }
            }
        }
        else{
            returnLinear!!.visibility = View.GONE
            textEmpty!!.visibility = View.VISIBLE
            makeText(activity, "No available History yet", Toast.LENGTH_LONG).show()

        }
    }

    override fun statusListener(status_msg: String, status: Boolean?, user_status: String?) {
        if (status_msg=="1"){
            returnLinear!!.visibility = View.VISIBLE
            firstLinear!!.visibility = View.GONE
            HistoryAsnc(activity).execute()
        }
        else if (status_msg=="0"){
            relativeProgress!!.visibility = View.GONE

            returnLinear!!.visibility = View.GONE
            firstLinear!!.visibility = View.VISIBLE
        }
    }

    companion object {

         var loanListener: LoanListener ?= null

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                DashHomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
