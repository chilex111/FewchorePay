package fewchore.com.exolve.fewchore.child_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.*

import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.listener.FragmentListener
import fewchore.com.exolve.fewchore.model.FormModel
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.enums.NavigationDirection
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.helper.getDurationToSharedPrefs
import fewchore.com.exolve.fewchore.model.LoanAmountModel
import java.lang.IllegalStateException
import java.text.DecimalFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val LOAN_DETAILS = "param2"


class LoanFormFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var listener: FragmentListener ?= null
    private var spinnerDuration: Spinner? = null
    private var loanModel: FormModel? = null
    private var editAmount: EditText? = null
    private var appUtils: AppUtils ? = null
    private var buttonSubmit: ImageButton? =null
    private var totalAmount: TextView ?= null
    private var maxAmount : TextView?= null
    private var relativeProgress : RelativeLayout?= null

    private var idselected: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            loanModel = it.getParcelable(LOAN_DETAILS)
        }
        appUtils = AppUtils(this.activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_loan_form, container, false)



        spinnerDuration = view.findViewById(R.id.spinnerDuration) as Spinner

        buttonSubmit = view.findViewById(R.id.buttonSubmit)as ImageButton
        editAmount = view.findViewById(R.id.editAmount)as EditText
        totalAmount = view.findViewById(R.id.textTotal)
        maxAmount = view.findViewById(R.id.loanMax)
        relativeProgress = view.findViewById(R.id.relativeProgress)


        relativeProgress!!.visibility = View.VISIBLE
        MaxLoanAsync().execute()
        views()


        return view
    }


    @SuppressLint("StaticFieldLeak")
    inner class MaxLoanAsync : AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
            try {
                val userId = AppUtils.getMyUserId(activity)
                val url = Const.FEWCHORE_URL + "amount/" + userId

                return HttpUtility.getRequest(url)
            } catch (e: Exception){}
            return null
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                relativeProgress!!.visibility = View.GONE
                val gson = Gson()
                try{
                    val type  = object: TypeToken<LoanAmountModel>() {}.type
                    val loanAmountModel =gson.fromJson<LoanAmountModel>(result, type)
                    val max =  loanAmountModel.loanAmount
                    val decimalFormat = DecimalFormat("#,###.00")
                    val am = decimalFormat.format(java.lang.Double.parseDouble(max.toString()))
                    maxAmount!!.text = "₦ $am"

                }catch (e: Exception){
                    if (e is IllegalStateException)
                        Toast.makeText(activity, "Please check your network connection", Toast.LENGTH_LONG).show()

                    Log.i("ERROR MESSAGE", e.javaClass.simpleName)
                }
            }
        }

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
                    editAmount!!.setText(finalValue.reverse())
                    editAmount!!.setSelection(finalValue.length)
                } catch (e: Exception) {
                    // Do nothing since not a number
                }

            }
            override fun afterTextChanged(s: Editable?) {


                afterTextChanged.invoke(s.toString())
            }
        })
    }
    fun views(){
        editAmount!!.afterTextChanged {
            if (!editAmount!!.text.toString().isEmpty()) {
                val textV = editAmount!!.text.toString().replace(",", "")
                val amount = Integer.valueOf(textV)
                val deci = 9.5 / 100.0f
                val percent = deci * amount
                val total = percent + amount
                val decimalFormat = DecimalFormat("#,###.00")
                val am = decimalFormat.format(java.lang.Double.parseDouble(total.toString()))

                totalAmount!!.text = am.toString()
                val max = Integer.valueOf(maxAmount!!.text.toString().replace(",", "").replace(".00", "").replace("₦ ", ""))

                if (amount > max) {
                    val paramString = "Your loan plan is higher than your Maximum Loan Amount. Please request for a lower Amount "

                    val localBuilder = AlertDialog.Builder(activity!!)
                    localBuilder.setMessage(paramString)
                    localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                        clearAmt()

                    }
                    localBuilder.create().show()
                }
            }

        }
        buttonSubmit!!.setOnClickListener {
            submitForm()

        }
        val durationList = ArrayList<String>()
        val savedDuration = activity!!.getDurationToSharedPrefs(R.string.loan_duration)
        durationList.add("Loan Duration")

        if (savedDuration != null)
            if (!savedDuration.isEmpty() &&(savedDuration.size != 0)){
                for (duration in savedDuration){
                    if (duration.duration.length ==1)
                        durationList.add(duration.duration +" day")
                    else
                        durationList.add(duration.duration +" days")
                    //durationList.sort()
                }
            }

        val titleAdapter = ArrayAdapter(activity,android.R.layout.simple_spinner_dropdown_item, durationList)
        titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDuration!!.adapter = titleAdapter


        spinnerDuration!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                if (i == 0) {
                    Log.i("PersonalFragment", "Nothing selected")
                } else {
                    val durationSelected = adapterView.selectedItem.toString()
                    val ds: String
                    ds = if (durationSelected.contains("days")) {
                        durationSelected.replace(" days", "")
                    }else{
                        durationSelected.replace(" day", "")
                    }
                    if (savedDuration!= null)
                        if (!savedDuration.isEmpty() &&(savedDuration.size != 0)){
                            for (duration in savedDuration){
                                if(ds == duration.duration)
                                    idselected = duration.id
                            }
                        }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }
    }

    private fun clearAmt() {
        editAmount!!.text.clear()
        editAmount!!.requestFocus()

    }

    private fun submitForm(){

        val amount =editAmount!!.text.toString().replace(",","")
        if(!amount.isEmpty()) {
            val deci = 9.5 / 100.0f
            val percent = deci * Integer.valueOf(amount)
            val total = totalAmount!!.text.toString().replace(",", "")

            val max = Integer.valueOf(maxAmount!!.text.toString().replace(",", "").replace(".00", "").replace("₦ ", ""))

            if (idselected.isNullOrBlank()) {
                appUtils!!.showAlert("Please select your Loan Duration")
                return
            }
            if (amount.isEmpty()) {
                editAmount!!.error = "This field i required"
                return
            }
            if (Integer.valueOf(amount) > max) {
                appUtils!!.showAlert("Your loan plan is higher than your Maximum Loan Amount. Please request for a lower Amount ")
            } else {
                loanModel = FormModel()

                loanModel!!.duration = idselected
                loanModel!!.loanAmount = amount
                loanModel!!.interest = percent.toString()
                loanModel!!.totalPayback = total
                listener!!.onFormDetailSubmit(loanModel!!)
                listener!!.onFragmentNavigation(NavigationDirection.FORM_FORWARD)
            }
        }else {
            appUtils!!.showAlert("Please enter a valid plan")
            return
        }
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (parentFragment is FragmentListener) {
            listener = parentFragment as FragmentListener
        } else {
            throw RuntimeException(context.toString() + " must implement Loan")
        }
    }
    override fun onDetach() {
        super.onDetach()
        listener =null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String?, loanFormModel: FormModel?) =
                LoanFormFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putParcelable(LOAN_DETAILS, loanFormModel)
                    }
                }
    }
}
