package fewchore.com.exolve.fewchore.child_fragment

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.adapter.CardAdapter
import fewchore.com.exolve.fewchore.apis.ActiveLoanAsync
import fewchore.com.exolve.fewchore.apis.AwaitApprovalAsync
import fewchore.com.exolve.fewchore.enums.NavigationDirection
import fewchore.com.exolve.fewchore.fragment.BankListFragment
import fewchore.com.exolve.fewchore.fragment.LookUpFragment
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.listener.*
import fewchore.com.exolve.fewchore.local_database.LocalDatabase
import fewchore.com.exolve.fewchore.model.*
import java.lang.IllegalStateException
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DashLoanFragment : Fragment(), FragmentListener , ActiveLoanListener, StatusListener {


    // TODO: Rename and change types of parameters
    private var page_from: String? = null
    private var param2: String? = null
    private var titleAdapter: ArrayAdapter<String>? = null
    private var localDB : LocalDatabase? = null
    private var cardAdapter : CardAdapter? = null
    private var loanForm: FormModel ? = null
    private var bankModel: BankModel ? = null
    private var passwordModel: PasswordModel ? = null
    private var cardList : List<CardModel>? = null
    private var acctTypeSelected: String?= null
    private var banKName: String?= null
    private var acctTypeId: Int? = null
    private var appUtils: AppUtils? = null
    private var progressBar: ProgressBar? = null
    private var accType: String?= null
    private var cardModel:CardModel ? = null
    private var amountTaken: TextView ?= null
    private var interest: TextView ?= null
    private var dueDate: TextView ? = null
    private var extraCharge: TextView ?= null
    private var total: TextView ?= null
    private var loanCard: CardView ?= null
    private var approvalCard: CardView ?= null
    private var textAmount : TextView ?= null
    private var textInterest: TextView ?= null
    private var numberCheck : Int?= null
    private var textDue: TextView ?= null
    private var textTotal: TextView ?= null
    private var textApproval: TextView ?= null
    private var fragContainer: FrameLayout?= null
    private var relativeProgress: RelativeLayout ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page_from = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_dash_loan, container, false)

        localDB = LocalDatabase(activity) // initialise local DB
        appUtils = AppUtils(this.activity!!)

        AwaitApprovalAsync.statusListener = this
        ActiveLoanAsync.activeLoanListener = this

        textAmount = view.findViewById(R.id.textAmount)
        textInterest = view.findViewById(R.id.textInterest)
        textDue = view.findViewById(R.id.textDueDate)
        textTotal = view.findViewById(R.id.textTotal)
        textApproval = view.findViewById(R.id.textApproval)
        approvalCard = view.findViewById(R.id.cardApproval)
        approvalCard!!.visibility = View.GONE
        fragContainer = view.findViewById(R.id.container)
        relativeProgress = view.findViewById(R.id.relativeProgress)

        loanForm = FormModel()
        bankModel = BankModel()
        passwordModel = PasswordModel()
        cardModel =  CardModel()

        val containerLayout = view.findViewById(R.id.loanContainer)as ConstraintLayout
        if (page_from.equals("1"))
            containerLayout.setBackgroundColor(ContextCompat.getColor(this.activity!!, R.color.green_light))

        amountTaken = view.findViewById(R.id.textAmount)
        interest = view.findViewById(R.id.textInterest)
        dueDate = view.findViewById(R.id.textDueDate)
        extraCharge = view.findViewById(R.id.extra)
        total = view.findViewById(R.id.textTotal)
        loanCard =view.findViewById(R.id.cardTakeLoan)
        loanCard!!.visibility = View.GONE
        // add an if statement so if there is loan display loan and balance else display loan form


        cardList = localDB!!.allCard //gets all the cards that is in local DB
        /*  if (true){
              Log.i("TAG","Active loan")
          }else{*/
        /*progressBar!!.visibility = View.VISIBLE
        BankAsync(progressBar!!).execute()*/
        if (appUtils!!.hasActiveInternetConnection(activity!!))
            AwaitApprovalAsync(activity!!).execute()

        return view
    }


    override fun activeLoanListener(activeLoan: Activeloan?, msg: String?, status: Boolean) {
        //do a check to see if the user have an active loan if yes display the form else open a form that allow user to request for a form
        if (activeLoan != null) {
            loanCard!!.visibility = View.VISIBLE
            if (activeLoan.loanPaybackdate != null)
                textDue!!.text = activeLoan.loanPaybackdate.toString()

            val decimalFormat = DecimalFormat("#,###.00")
            val  loanTotal = decimalFormat.format(java.lang.Double.parseDouble(activeLoan.loanTotalpayback))
            val  loanInt = decimalFormat.format(java.lang.Double.parseDouble(activeLoan.loanInterest))
            val  loanAmt = decimalFormat.format(java.lang.Double.parseDouble(activeLoan.loanAmount))

            textTotal!!.text = loanTotal
            textInterest!!.text = loanInt
            textAmount!!.text = loanAmt
        }else{
            if (isAdded)
                childFragmentManager
                        .beginTransaction()
                        .add(R.id.container, LoanFormFragment())
                        .addToBackStack(null)
                        .commit()
        }
    }

    override fun statusListener(status_msg: String, status: Boolean?, user_status: String?) {
        try {
            if (status!!) {
                if (user_status == "User has made 2 loans already this month!") {
                    approvalCard!!.visibility = View.VISIBLE
                    fragContainer!!.visibility = View.GONE
                    textApproval!!.text = "You have used up your monthly Loan Allowance."

                }else {
                    approvalCard!!.visibility = View.VISIBLE
                    fragContainer!!.visibility = View.GONE
                    textApproval!!.text = status_msg
                }
            } else {
                if (user_status == "User has made 2 loans already this month!") {
                    approvalCard!!.visibility = View.VISIBLE
                    fragContainer!!.visibility = View.GONE
                    textApproval!!.text = "You have used up your monthly Loan Allowance."

                }
                ActiveLoanAsync(activity!!).execute()
            }
        }catch (e: Exception){
            Log.i("ERRRROR", e.message)
        }


    }



    override fun onFragmentNavigation(navigationDirection: NavigationDirection) {
        when(navigationDirection){
            NavigationDirection.FORM_FORWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, BankListFragment.newInstance(null, bankModel))
                        .addToBackStack(null)
                        .commit()
                return
            }
            NavigationDirection.BANK_DETAILS_BACKWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, LoanFormFragment.newInstance(null, loanForm))
                        .addToBackStack(null)
                        .commit()
                return
            }
            NavigationDirection.BANK_DETAILS_FORWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, CardLoanListFragment.newInstance(null, cardModel))
                        .addToBackStack(null)
                        .commit()
                return
            }
            NavigationDirection.CARD_DETAILS_BACKWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, BankListFragment.newInstance(null, bankModel))
                        .addToBackStack(null)
                        .commit()
                return
            }
            NavigationDirection.CARD_DETAILS_FORWARD ->{

                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, LookUpFragment())
                        .addToBackStack(null)
                        .commit()
                return
            }
            NavigationDirection.LOOK_UP_FORWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, PasswordFragment.newInstance(null, passwordModel))
                        .addToBackStack(null)
                        .commit()
                return
            }
            NavigationDirection.PASSWORD_BACKWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, LookUpFragment())
                        .addToBackStack(null)
                        .commit()
                return
            }

            NavigationDirection.PASSWORD_FORWARD ->{
                val msg1 = getString(R.string.msg)
                val localBuilder = AlertDialog.Builder(this.activity!!)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    localBuilder.setMessage(Html.fromHtml(msg1,Html.FROM_HTML_MODE_LEGACY))
                }
                else{
                    localBuilder.setMessage(Html.fromHtml(msg1))
                }
                localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                    relativeProgress!!.visibility = View.VISIBLE
                    Pay100Async().execute()
                    // LoanRequestAsync().execute()
                }
                localBuilder.create().show()
            }
        }
    }

    override fun onFormDetailSubmit(formModel: FormModel) {
        this.loanForm = formModel
        childFragmentManager
                .beginTransaction()
                .replace(R.id.container, BankListFragment.newInstance(null, bankModel))
                .addToBackStack(null)
                .commit()
    }

    override fun onBankDetailSubmit(bankModel: BankModel) {
        this.bankModel = bankModel
        childFragmentManager
                .beginTransaction()
                .replace(R.id.container, CardLoanListFragment.newInstance(null, cardModel))
                .addToBackStack(null)
                .commit()
    }

    override fun onCardDetailSubmit(cardModel: CardModel) {
        this.cardModel = cardModel

        childFragmentManager
                .beginTransaction()
                .replace(R.id.container, LookUpFragment())
                .addToBackStack(null)
                .commit()
    }

    override fun onLookUp() {
        childFragmentManager
                .beginTransaction()
                .replace(R.id.container, PasswordFragment.newInstance(null, passwordModel))
                .addToBackStack(null)
                .commit()
    }

    override fun onPasswordDetailSubmit(passwordModel: PasswordModel) {
        this.passwordModel = passwordModel
        val accNo = bankModel!!.acctNo
        val acctype = bankModel!!.acctType
        val bcName= bankModel!!.bankName
        val dur= loanForm!!.duration
        val amt = loanForm!!.loanAmount
        val pass = passwordModel.password
        val cardNo = cardModel!!.cardNo
        val expr= cardModel!!.cardExpiry
        val cvv = cardModel!!.cardId

        val i = "$accNo $acctype $bcName $dur $amt $pass $cardNo $expr $cvv"
        Log.i("VALUES", i)

    }

    @SuppressLint("StaticFieldLeak")
    inner class Pay100Async: AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
            val url = "https://api.paystack.co/transaction/charge_authorization"
            val map = HashMap<String, Any?>()
            val auth_code = cardModel!!.authCode
            val email = AppUtils.getMyEmail(activity!!)

            map["authorization_code"] = auth_code
            map["email"] = email
            map["plan"]= 10000
            map["amount"]= 10000
            try {
                return HttpUtility.sendPaystackPostRequest(url, map,activity!!)
            }catch (e: Exception){}
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                val gson = Gson()
                try{
                    val type = object :TypeToken<DeductModel>(){}.type
                    val response = gson.fromJson<DeductModel>(result, type)
                    if(response.status!!){
                        if (response.data!!.status =="success"){
                            if (appUtils!!.hasActiveInternetConnection(activity!!))
                                LoanRequestAsync().execute()
                        }else{
                            relativeProgress!!.visibility = View.GONE
                            appUtils!!.showAlert("The ₦100 charge could not be completed on this card."+response.data!!.gatewayResponse)

                        }
                    }else{
                        relativeProgress!!.visibility = View.GONE

                        appUtils!!.showAlert(response.message!!)
                    }

                }catch (e: Exception){
                    Log.i("ERROR MESSAGE", e.javaClass.simpleName)
                }
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    inner class LoanRequestAsync: AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {

            val url = Const.FEWCHORE_URL + "loanrequest"
            val map = HashMap<String, Any?>()
            val amt = loanForm!!.loanAmount
            val duration = loanForm!!.duration
            val userId = AppUtils.getMyUserId(activity)
            val interest = loanForm!!.interest
            val total = loanForm!!.totalPayback
            val card_id  = cardModel!!.cardId
            val bankName =  bankModel!!.bankName
            val accNo = bankModel!!.acctNo
            val acctype = bankModel!!.acctType

            map["loan_amount"] = amt
            map["frequency_id"] = duration
            map["loan_interest"] = interest
            map["loan_totalpayback"] =total
            map["loan_userid"] = userId
            map["card_id"] = card_id
            map["bank_name"] = bankName
            map["account_type"] = acctype
            map["account_no"] = accNo
            try {
                return HttpUtility.sendPostRequest(url, map)
            }
            catch (e: Exception){}
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                relativeProgress!!.visibility = View.GONE
                val gson = Gson()
                try {
                    val type = object :TypeToken<ResponseStringModel>(){}.type
                    val response = gson.fromJson<ResponseStringModel>(result, type)
                    if (response.status =="true"){
                        appUtils!!.showAlert(response.msg!!)
                        appUtils!!.showAlert("Your request has been sent and is been reviewed for approval.")
                        if (appUtils!!.hasActiveInternetConnection(activity!!)) {
                            SecondScoreAsync(response.loan_id).execute()
                            AwaitApprovalAsync(activity!!).execute()
                        }
                    }else{
                        appUtils!!.showAlert(response.msg!!)
                    }
                }catch (e: Exception){
                    if (e is IllegalStateException)
                        appUtils!!.showAlert("Please check your network connection")}
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner  class SecondScoreAsync(private var loan_id: String?):AsyncTask<Void, Int, String>() {

        override fun doInBackground(vararg p0: Void?): String? {
            val userId = AppUtils.getMyUserId(activity)
            val url = Const.FEWCHORE_URL +"secondscore/"+userId
            val map = HashMap<String, Any?>()
            map["loan_id"] = loan_id
            try {
                return HttpUtility.sendPostRequest(url , map)
            }catch (e:Exception){}
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null){
                val gson = Gson()
                val type = object :TypeToken<ResponseStringModel>(){}.type
                val response = gson.fromJson<ResponseStringModel>(result, type)
                if (response.status =="true") {
                    listener!!.idListener("home")
                }else{
                    appUtils!!.showAlert(response.msg!!)
                }
            }
        }

    }

    companion object {
        var listener : IdListener ?= null

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String?) =
                DashLoanFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }



}
