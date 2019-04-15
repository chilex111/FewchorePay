package fewchore.com.exolve.fewchore.invest

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.model.InvestmentDetailConverted


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val INVEST_DETAILS = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [IntestmentDetailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [IntestmentDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class IntestmentDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var detailConverted: InvestmentDetailConverted? = null
    private var appUtils : AppUtils ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            detailConverted = it.getParcelable(INVEST_DETAILS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.dialog_see_investment_detail, container, false)

        appUtils = AppUtils(activity!!)
        val plan = v.findViewById(R.id.textPlan)as TextView
        val plan1 = v.findViewById(R.id.textPlan1)as TextView
        val start = v.findViewById(R.id.textDateCreated)as TextView
        val matured = v.findViewById(R.id.textDueDate)as TextView
        val invested = v.findViewById(R.id.textInvested)as TextView
        val interst = v.findViewById(R.id.textInterest)as TextView
        val sumTotal = v.findViewById(R.id.textTotal)as TextView
        val durationTime = v.findViewById(R.id.textDuration)as TextView
        val acctNo = v.findViewById(R.id.textAcctNo)as TextView
        val request = v.findViewById(R.id.withdraw)as Button

        plan.text = detailConverted!!.investmentName
        plan1.text = detailConverted!!.investmentName
        start.text = detailConverted!!.investmentCreated
        matured.text = detailConverted!!.investmentDuedate
        invested.text = detailConverted!!.investmentAmount
        interst.text = detailConverted!!.investmentInterest
        sumTotal.text = detailConverted!!.investmentTotal
        durationTime.text = detailConverted!!.investmentDuration
        acctNo.text = detailConverted!!.userbankAccno

        request.setOnClickListener {
            withdrawOption(detailConverted!!.investmentId!!)
        }
        return v
    }


    private fun withdrawOption(investmentId: String) {
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.dialog_withdraw_investment)
        dialog.findViewById<Button>(R.id.withdraw).setOnClickListener {
            if (appUtils!!.hasActiveInternetConnection(activity!!))
            WithdrawalAsync(investmentId).execute()

        }
        dialog.findViewById<ImageButton>(R.id.close).setOnClickListener {
            dialog.dismiss()

        }
        dialog.show()
    }

    @SuppressLint("StaticFieldLeak")
    inner class WithdrawalAsync(private var investmentId: String?): AsyncTask<Void, Int, String>() {

        override fun doInBackground(vararg p0: Void?): String? {
            val map = HashMap<String, Any?>()
            map["investment_id"] =investmentId
            val url = Const.FEWCHORE_URL+""
            try{
            return HttpUtility.sendPostRequest(url, map)
        }catch (e:Exception){ }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){

            }
        }

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment IntestmentDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?, param2: InvestmentDetailConverted) =
                IntestmentDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putParcelable(INVEST_DETAILS, param2)
                    }
                }
    }
}
