package fewchore.com.exolve.fewchore.invest

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.Toast.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.child_fragment.CardLoanListFragment
import fewchore.com.exolve.fewchore.child_fragment.PasswordFragment
import fewchore.com.exolve.fewchore.enums.NavigationDirection
import fewchore.com.exolve.fewchore.fragment.BankListFragment
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.listener.FragmentListener
import fewchore.com.exolve.fewchore.listener.IdListener
import fewchore.com.exolve.fewchore.model.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class InvestNowFragment : Fragment(),FragmentListener {
    override fun onLookUp() {

    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var investForm: FormModel ?= null
    private var bankModel: BankModel ?= null
    private var cardModel: CardModel ?= null
    private var passwordModel: PasswordModel ?= null
    private var appUtils: AppUtils ?= null
    private var relativeProgress: RelativeLayout?= null
    private var frameLayout: FrameLayout ?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        appUtils= AppUtils(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_invest_now, container, false)
        frameLayout = v.findViewById(R.id.container)
        investForm = FormModel()
        cardModel = CardModel()
        bankModel = BankModel()
        passwordModel = PasswordModel()

        relativeProgress = v.findViewById(R.id.relativeProgress)
        if (param1=="1"){
            frameLayout!!.setBackgroundColor(ContextCompat.getColor(activity!!,R.color.light_green))

        }
        childFragmentManager
                .beginTransaction()
                .replace(R.id.container, InvestFormFragment.newInstance(null, investForm))
                .commit()
        return v
    }

    override fun onFragmentNavigation(navigationDirection: NavigationDirection) {
        when(navigationDirection){
            NavigationDirection.FORM_FORWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, BankListFragment.newInstance(null, bankModel))
                        .commit()
                return
            }
            NavigationDirection.BANK_DETAILS_BACKWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, InvestFormFragment.newInstance(null, investForm))
                        .commit()
                return
            }
            NavigationDirection.BANK_DETAILS_FORWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, CardLoanListFragment.newInstance(null, cardModel))
                        .commit()
                return
            }
            NavigationDirection.CARD_DETAILS_BACKWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, BankListFragment.newInstance(null, bankModel))
                        .commit()
                return
            }
            NavigationDirection.CARD_DETAILS_FORWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, PasswordFragment.newInstance("invest", passwordModel))
                        .commit()
                return
            }
            NavigationDirection.PASSWORD_BACKWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, CardLoanListFragment.newInstance(null, cardModel))
                        .commit()
                return
            }

            NavigationDirection.PASSWORD_FORWARD ->{


                if (appUtils!!.hasActiveInternetConnection(activity!!)){
                    relativeProgress!!.visibility =View.VISIBLE

                    PayInvestmentAsync().execute()
                }else{
                    appUtils!!.showAlert("Please check your Internet connection")
                }
            }
            else -> {
            }
        }    }

    override fun onFormDetailSubmit(formModel: FormModel) {

        investForm= formModel
        childFragmentManager
                .beginTransaction()
                .replace(R.id.container, BankListFragment.newInstance(null, bankModel))
                .commit()

    }

    override fun onBankDetailSubmit(bankModel: BankModel) {
        this.bankModel = bankModel
        childFragmentManager
                .beginTransaction()
                .replace(R.id.container, CardLoanListFragment.newInstance(null, cardModel))
                .commit()
    }

    override fun onCardDetailSubmit(cardModel: CardModel) {
        this.cardModel = cardModel
        childFragmentManager
                .beginTransaction()
                .replace(R.id.container, PasswordFragment.newInstance("invest", passwordModel))
                .commit()
    }

    override fun onPasswordDetailSubmit(passwordModel: PasswordModel) {
        this.passwordModel = passwordModel

        //submit this investment
        val accNo = bankModel!!.acctNo
        val acctype = bankModel!!.acctType
        val bcName= bankModel!!.bankName
        val dur= investForm!!.duration
        val amt = investForm!!.totalInvest
        val pass = passwordModel.password
        val cardNo = cardModel!!.cardNo
        val expr= cardModel!!.cardExpiry
        val cvv = cardModel!!.cardId

        val i = "$accNo $acctype $bcName $dur $amt $pass $cardNo $expr $cvv"
        Log.i("VALUES", i)
    }

    @SuppressLint("StaticFieldLeak")
    inner class PayInvestmentAsync: AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
            val url = "https://api.paystack.co/transaction/charge_authorization"
            val map = HashMap<String, Any?>()
            val auth_code = cardModel!!.authCode

            val amt  = Integer.valueOf(investForm!!.investAmt!!.replace(",", ""))
            val email = AppUtils.getMyEmail(activity!!)

            map["authorization_code"] = auth_code
            map["email"] = email
            map["plan"]= amt * 100
            map["amount"]= amt * 100

            Log.i("authorization_code", auth_code)
            try {
                return HttpUtility.sendPaystackPostRequest(url, map, activity!!)
            }catch (e:Exception){}
            return null
        }

        @SuppressLint("ShowToast")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                val gson = Gson()
                try {
                    val type = object :TypeToken<DeductModel>(){}.type
                    val response = gson.fromJson<DeductModel>(result, type)
                    if(response.status!!){
                        if (response.data!!.status =="success"){
                            if (appUtils!!.hasActiveInternetConnection(activity!!)){
                                InvestmentAsync(response.data!!.reference).execute()
                            }else{
                                appUtils!!.showAlert(response.message.toString())
                            }
                        }else{
                            relativeProgress!!.visibility = View.GONE
                            appUtils!!.showAlert("Your Investment plan Payment could not be completed on this card."+response.data!!.gatewayResponse)
                        }
                    }else{
                        relativeProgress!!.visibility = View.GONE

                        appUtils!!.showAlert(response.message!!)
                    }
                }catch (e: Exception){
                    makeText(activity, "Please check your Internet connection", LENGTH_LONG)
                }


            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class InvestmentAsync(private var reference: String?) : AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {

            val url = Const.FEWCHORE_URL + "add_investment"
            val map = HashMap<String, Any?>()
            val descript = investForm!!.shortDescription
            val duration = investForm!!.duration
            val userId = AppUtils.getMyUserId(activity)
            val investedAmt = investForm!!.investAmt
            val investName= investForm!!.planName
            val total = investForm!!.totalInvest
            val interest = investForm!!.interest
            val card_id  = cardModel!!.cardId
            val bankName =  bankModel!!.bankName
            val accNo = bankModel!!.acctNo
            val acctype = bankModel!!.acctType

            map["investment_name"] = investName
            map["investment_duration"] = duration
            map["investment_amount"] = investedAmt
            map["investment_interest"] =interest
            map["investment_description"] = descript
            map["investment_total"] = total
            map["investment_userid"] = userId
            map["card_id"] = card_id
            map["bank_name"] = bankName
            map["account_type"] = acctype
            map["account_no"] = accNo
            map["investment_reference"]= reference
            try {
                return HttpUtility.sendPostRequest(url, map)
            }catch (e: Exception){}
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                relativeProgress!!.visibility = View.GONE
                val gson = Gson()
                val type = object : TypeToken<ResponseStringModel>(){}.type
                val response = gson.fromJson<ResponseStringModel>(result, type)
                if (response.status =="true"){
                    val localBuilder = AlertDialog.Builder(activity!!)
                    localBuilder.setMessage(response.msg!!)
                    localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                        listner!!.idListener("home")
                    }
                    localBuilder.create().show()

                }else{
                    appUtils!!.showAlert(response.msg!!)
                }
            }
        }
    }

    companion object {
        var listner: IdListener ?= null
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InvestNowFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String?) =
                InvestNowFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
