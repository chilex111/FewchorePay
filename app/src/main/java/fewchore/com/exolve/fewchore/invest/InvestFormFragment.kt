package fewchore.com.exolve.fewchore.invest

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.enums.NavigationDirection
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.listener.FragmentListener
import fewchore.com.exolve.fewchore.model.FormModel
import java.text.DecimalFormat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val INVEST_FORM = "param2"


class InvestFormFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var formModel: FormModel? = null
    private var listener: FragmentListener? = null
    private var durationSelected: String? = null
    private var selected_val: String? = null

    private var appUtils: AppUtils ?= null

    private var planName: EditText?= null
    private var description: EditText?= null
    private var amount: EditText?= null
    private var duration: Spinner?= null
    private var textTotalAmt: TextView?= null
    private var textInterest: TextView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            formModel = it.getParcelable(INVEST_FORM)
        }
        appUtils = AppUtils(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_invest_form, container, false)
        planName = v.findViewById(R.id.editPlanName)
        description = v.findViewById(R.id.editShortDescript)
        amount = v.findViewById(R.id.editAmount)
        duration = v.findViewById(R.id.spinnerDuration)
        textTotalAmt = v.findViewById(R.id.textTotal)
        textInterest = v.findViewById(R.id.textInterest)
        viewAll()
        v.findViewById<Button>(R.id.buttonNext).setOnClickListener {
            submitForm()
        }
        return v
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            var isManualChange = false

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                if (isManualChange) {
                    isManualChange = false
                    return
                }

                try {
                    val value = s.toString().replace(",", "")
                    val reverseValue = StringBuilder(value).reverse()
                            .toString()
                    val finalValue = StringBuilder()
                    for (i in 1..reverseValue.length) {
                        val `val` = reverseValue[i - 1]
                        finalValue.append(`val`)
                        if (i % 3 == 0 && i != reverseValue.length && i > 0) {
                            finalValue.append(",")
                        }
                    }
                    isManualChange = true
                    amount!!.setText(finalValue.reverse())
                    amount!!.setSelection(finalValue.length)
                } catch (e: Exception) {
                    // Do nothing since not a number
                }

            }
            override fun afterTextChanged(s: Editable?) {


                afterTextChanged.invoke(s.toString())
            }
        })
    }
    private fun clearAmt() {
        amount!!.text.clear()
        amount!!.requestFocus()
        textTotalAmt!!.text = "--"

    }
    private fun viewAll() {
        amount!!.afterTextChanged {
            if (!amount!!.text.toString().isEmpty()) {
                // if (!durationSelected!!.isEmpty()) {
                /*Formula for interest
                * Total = AmountInvested x (interest/100) x (duration/365)
                * */

                val textV = amount!!.text.toString().replace(",", "")
                val amount = Integer.valueOf(textV)
                if (!textInterest!!.text.isNullOrEmpty()) {
                    val interest = textInterest!!.text.toString().replace("%", "").replace(" P.A", "").toDouble()
                    val interest_decimal = interest / 100.0f


                    val year = durationSelected!!.replace("days", "").replace(" ", "").toInt() // removes the "days" in value
                    val calculateYear = year / 365f // get the year calculation for the totalEarning in days
                    val interest_year = interest_decimal * calculateYear
                    val totalReturn = amount * interest_year // calculates the totalEarning for selected duration
                    val total = totalReturn + textV.toFloat()
                    val decimalFormat = DecimalFormat("#,###.00")
                    val am = decimalFormat.format(java.lang.Double.parseDouble(total.toString()))

                    textTotalAmt!!.text = am.toString()
                }
                /*}else{
                    appUtils!!.showAlert("Please select a investedAmt for Investment")
                }*/
            }

        }
        val durationList = ArrayList<String>()
        durationList.add("Loan Duration")
        durationList.add("1 months")
        durationList.add("3 months")
        durationList.add("6 months")
        durationList.add("12 months")

        val titleAdapter = ArrayAdapter(activity,android.R.layout.simple_spinner_dropdown_item, durationList)
        titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        duration!!.adapter = titleAdapter


        duration!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                if (i == 0) {
                    Log.i("PersonalFragment", "Nothing selected")
                } else {
                    selected_val = adapterView.selectedItem.toString()
                    if (selected_val == "1 months"){
                        textInterest!!.text = "8% P.A"
                        durationSelected = "30 days"
                    }
                    if (selected_val == "3 months"){
                        textInterest!!.text = "10% P.A"
                        durationSelected = "90 days"
                    }
                    if (selected_val == "6 months"){
                        textInterest!!.text = "11.5% P.A"
                        durationSelected = "180 days"
                    }
                    if (selected_val == "12 months"){
                        textInterest!!.text = "13% P.A"
                        durationSelected = "365 days"
                    }
                    if (!amount!!.text.toString().isEmpty()){
                        val textV = amount!!.text.toString().replace(",", "").toInt()
                        val interest = textInterest!!.text.toString().replace("%","").replace(" P.A","").toDouble()
                        val deci = interest / 100.0f

                        val year = durationSelected!!.replace("days", "").replace(" ", "") // removes the "days" in value
                        val interestYear = Integer.valueOf(year)
                        val calculateYear = interestYear / 365f // get the year calculation for the totalEarning in days
                        val first = deci * calculateYear
                        val second = first
                        val totalReturn = textV * second // calculates the totalEarning for the selected period of time
                        val total = totalReturn + textV.toFloat()

                        val decimalFormat = DecimalFormat("#,###.00")
                        val am = decimalFormat.format(java.lang.Double.parseDouble(total.toString()))

                        textTotalAmt!!.text = am.toString()
                    }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }
    }

    private fun submitForm() {
        if(!amount!!.text.toString().isEmpty()) {
            val planText = planName!!.text.toString()
            val descriptionText = description!!.text.toString()
          //  val amtText = amount!!.text.toString()
            val totalAmtText = textTotalAmt!!.text.toString().replace(",", "").replace(".00", "")

            val invest = Integer.valueOf(amount!!.text.toString().replace(",", "").replace(".00", ""))
            val totalInt = totalAmtText.toDouble()
            val interest = totalInt - invest.toDouble()
            if (invest < 50000) {
                val paramString = "Please you can only invest minimum of N50,000"

                val localBuilder = AlertDialog.Builder(activity!!)
                localBuilder.setMessage(paramString)
                localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                    clearAmt()
                }
                localBuilder.create().show()
            } else {
                formModel = FormModel()
                formModel!!.duration = durationSelected
                formModel!!.planName = planText
                formModel!!.shortDescription = descriptionText
                formModel!!.investAmt = invest.toString()
                formModel!!.interest = interest.toString()
                formModel!!.totalInvest = totalAmtText

                listener!!.onFormDetailSubmit(formModel!!)
                listener!!.onFragmentNavigation(NavigationDirection.FORM_FORWARD)
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (parentFragment is FragmentListener) {
            listener = parentFragment as FragmentListener
        } /*else {
           // throw RuntimeException(context.toString() + " must implement Loan")
        }*/
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InvestFormFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?, param2: FormModel?) =
                InvestFormFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putParcelable(INVEST_FORM, param2)
                    }
                }
    }
}
