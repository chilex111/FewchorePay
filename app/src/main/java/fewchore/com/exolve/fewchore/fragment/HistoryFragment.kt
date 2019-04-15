package fewchore.com.exolve.fewchore.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.adapter.LoanHistoryAdapter
import fewchore.com.exolve.fewchore.apis.HistoryAsnc
import fewchore.com.exolve.fewchore.apis.StatusAsync
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.listener.HistoryListener
import fewchore.com.exolve.fewchore.listener.StatusListener
import fewchore.com.exolve.fewchore.model.History
import kotlinx.android.synthetic.main.template_recyclerview_white.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
//private var recyclerView: RecyclerView? = null


class HistoryFragment : Fragment(), HistoryListener,StatusListener {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var textEmpty : TextView ?= null
    private var relativeProgress : RelativeLayout ?= null
    private var appUtils : AppUtils ?= null
    private var recyclerView : RecyclerView ?= null
    private var header : LinearLayout?= null

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
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.template_recyclerview_white, container, false)
        appUtils = AppUtils(this.activity!!)

        HistoryAsnc.historyListener = this
        StatusAsync.statusListener = this


        recyclerView = view.findViewById(R.id.recyclerView)
        textEmpty =  view.findViewById(R.id.textEmpty)
        header = view.findViewById(R.id.linearHead)
        relativeProgress = view.findViewById(R.id.relativeProgress)

        if (AppUtils.getMyLoanStatus(activity) == "1") {


        } else if (AppUtils.getMyLoanStatus(activity) == "0") {
            textEmpty!!.visibility = View.VISIBLE

            relativeProgress!!.visibility = View.VISIBLE
            StatusAsync(activity).execute()

        }
        textEmpty!!.visibility = View.GONE

        relativeProgress!!.visibility = View.VISIBLE
        HistoryAsnc(activity).execute()



        return view
    }

    override fun statusListener(status_msg: String, status: Boolean?, user_status: String?) {
        if (AppUtils.getMyLoanStatus(activity)=="1"){

            textEmpty!!.visibility = View.GONE
            relativeProgress!!.visibility = View.VISIBLE
            HistoryAsnc(activity).execute()
        }
        else if (AppUtils.getMyLoanStatus(activity)=="0"){
            linearHead.visibility = View.GONE
            relativeProgress!!.visibility = View.GONE
            textEmpty!!.visibility =View.VISIBLE
        }
    }

    override fun historyListener(history: List<History>?, status: Boolean) {
        if (status) {
            relativeProgress!!.visibility = View.GONE

            val audioAdapter = LoanHistoryAdapter(history!!, this.activity!!)
            recyclerView!!.layoutManager = LinearLayoutManager(activity)
            recyclerView!!.adapter = audioAdapter
            recyclerView!!.requestFocus()
        }
        else{
            textEmpty!!.visibility =View.VISIBLE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HistoryFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
