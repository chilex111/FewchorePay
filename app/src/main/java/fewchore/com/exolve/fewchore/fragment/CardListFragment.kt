package fewchore.com.exolve.fewchore.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import co.paystack.android.Paystack
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.exceptions.ExpiredAccessCodeException
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.adapter.CardAdapter
import fewchore.com.exolve.fewchore.apis.AddCardAsync
import fewchore.com.exolve.fewchore.apis.CardListAsync
import fewchore.com.exolve.fewchore.enums.CardValidity
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.CreditCardFormatter
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.listener.*
import fewchore.com.exolve.fewchore.local_database.LocalDatabase
import fewchore.com.exolve.fewchore.model.*
import java.util.*
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val CARD_DETAILS = "param2"
private const val LOAN_AMOUNT = "loanAmt"


class CardListFragment : PayStackCardValidationListener,Fragment()  , StringListener,
        CardLoanListener, CardListListener{


    // TODO: Rename and change types of parameters
    private var pageValue: String? = null
    private var cardModel: CardModel? = null
    private var localDatabase: LocalDatabase? = null
    private var skip: Button? = null
    private var previous: Button? = null
    private var next: Button? = null
    private var cardNo: EditText? = null
    private var expiry: EditText? = null
    private var cvv: EditText? = null
    private var card: Card? = null
    private  var cardAdapter: CardAdapter? = null
    private var expiryDateIsValid = false
    private var cardIsValid = false
    private var cvvIsValid = false
    private var transactionValue : Transaction ?= null
    private var cardModelList: MutableList<CardModel>? = null
    private var cardContainer: ConstraintLayout?= null
    private var recyclerView: RecyclerView ?= null
    private var cardNoText : String ?= null
    private var cardExpiryText: String?= null
    private var cardIdText: String?= null
    private var appUtils: AppUtils? = null
    private var buttonFrame: FrameLayout?= null
    private var buttonSubmit: ImageButton?= null
    private var accessCodeValue : String?= null
    private  var dialog: Dialog ?= null
    private var dialogProgress : ProgressDialog ?= null
    private var textEmpty: TextView ?= null
    private var paystack_public_key: String?= null
    private var relativeProgress : RelativeLayout ?= null
    private var listener: LoanPayedListener? = null
    private var internetChecks :Int ?= null
    private var loanAmt : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pageValue = it.getString(ARG_PARAM1)
            cardModel = it.getParcelable(CARD_DETAILS)
            loanAmt = it.getString(LOAN_AMOUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_card_list, container, false) as View

        CardAdapter.stringlistener = this
        AddCardAsync.cardListener = this
        CardListAsync.cardListener = this

        localDatabase = LocalDatabase(activity)
        appUtils = AppUtils(activity!!)
        cardContainer = view.findViewById(R.id.cardContainer)
        buttonFrame = view.findViewById(R.id.framelogIn)
        relativeProgress = view.findViewById(R.id.relativeProgress)
        if (pageValue!= null){
            if(pageValue.equals("1") ||pageValue.equals("2")){
                cardContainer!!.setBackgroundColor(activity!!.resources.getColor(R.color.green_light))
                buttonFrame!!.visibility = View.GONE
            }
        }
        recyclerView= view.findViewById(R.id.recyclerView)
        textEmpty =view.findViewById(R.id.textEmpty) as TextView
        buttonSubmit = view.findViewById(R.id.buttonSubmit)
        textEmpty!!.visibility = View.GONE

        if ( localDatabase!!.allCard.size >=1) {
            dbData()
        }
        relativeProgress!!.visibility = View.VISIBLE
        CardListAsync(activity!!).execute()

        buttonSubmit!!.setOnClickListener{
            saveCard()
        }
        val addCard = view.findViewById(R.id.addCard) as FloatingActionButton
        addCard.setOnClickListener{
            initPayStack()
        }


        return view
    }

    private fun dbData() {
        if (localDatabase!!.allCard != null && !localDatabase!!.allCard.isEmpty()) {
            cardAdapter = CardAdapter(localDatabase!!.allCard, activity!!, pageValue)
            recyclerView!!.layoutManager = LinearLayoutManager(activity)

            recyclerView!!.adapter = cardAdapter
            recyclerView!!.setHasFixedSize(true)
            recyclerView!!.requestFocus()
           // cardAdapter!!.notifyDataSetChanged()

        }else{
            textEmpty!!.visibility = View.VISIBLE
        }
    }

    override fun cardDetailsListener(cardDetails: MutableList<CardDetails>?, msg: String) {
        relativeProgress!!.visibility = View.GONE
        if (msg == "success"){
            for (details in cardDetails!!) {
                textEmpty!!.visibility = View.GONE

                val cardNum = details.cardNumber
                val cardExpiry = details.cardExpiryMonth + "/" +
                        details.cardExpiryYear
                cardModel = CardModel()
                cardModel!!.cardNo = cardNum
                cardModel!!.cardExpiry = cardExpiry
                cardModel!!.cardId = details.cardId
                cardModel!!.authCode = details.cardAuthorizationCode
                cardModel!!.cardType = details.cardCardType

                cardModelList = ArrayList()
                cardModelList?.add(cardModel!!)
                localDatabase!!.clearCard()
                localDatabase!!.insertCard(cardModelList)
            }

      //      cardAdapter!!.notifyDataSetChanged()
        }else{
            localDatabase!!.clearCard()
            dbData()

        }
    }


    private fun saveLocally(details: CardDetails0?, cardId: String?){
        textEmpty!!.visibility = View.GONE


        val cardNum = details!!.cardNumber
        val cardExpiry = details.cardExpiryMonth + "/" +
                details.cardExpiryYear
        cardModel = CardModel()
        cardModel!!.cardNo = cardNum
        cardModel!!.cardExpiry = cardExpiry
        cardModel!!.cardId = cardId
        cardModel!!.authCode = details.cardAuthorizationCode
      //  cardModel!!.authCode = details.cardCardType
        cardModel!!.cardType = details.cardCardType


        cardModelList = ArrayList()
        cardModelList?.add(cardModel!!)
        localDatabase!!.insertCard(cardModelList)

        dbData()
    }
    private fun initPayStack() {
        paystack_public_key = resources.getString(R.string.public_key)
        PaystackSdk.setPublicKey(paystack_public_key)
        PaystackSdk.initialize(activity)
        addCard()
    }
    private fun saveCard() {
        PayNowAsync(cardIdText).execute()
    }

    override fun accessCodeListener(cardDetails: Msg?, status: Boolean) {
        if (status){
            accessCodeValue = cardDetails!!.accessCode
            try {
                startAFreshCharge(cardDetails.accessCode!!)
            } catch (e: Exception) {
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }else{
            appUtils!!.showAlert("Card cannot be tokenized, Try again !!!")
        }

    }

    override fun accountDetailsListener(valueName: String?, valueType: String?, valueNumber: String?, s: String?, auth_code: String?) {
       if (loanAmt =="0"){
           appUtils!!.showAlert("You have no Outstanding Loan to pay back")
           adapterListener!!.adapterListener(true)

       }else {
           if (buttonFrame!!.visibility == View.GONE) {
               if (!valueName!!.isEmpty()) {
                   cardNoText = valueName
                   cardExpiryText = valueType
                   cardIdText = valueNumber
                   val msg = "Are you sure you want to pay your loan with this card?"
                   val localBuilder = AlertDialog.Builder(activity!!)
                   localBuilder.setMessage(msg)
                   localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                       PayNowAsync(cardIdText).execute()
                   }
                   localBuilder.create().show()
               } else {
                   appUtils!!.showAlert("Invalid card Detail")
               }
           }
       }

    }

    @SuppressLint("StaticFieldLeak")
    inner class PayNowAsync(private val cardIdText: String?): AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            val url = Const.FEWCHORE_URL+"paynow"
            val userId = AppUtils.getMyUserId(activity)
            val map = HashMap<String, Any?>()
            map["card_id"] = cardIdText
            map["user_id"] = userId
            return HttpUtility.sendPostRequest(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {
                val gson = Gson()
                try {
                    val type = object : TypeToken<ResponseStringModel>() {}.type
                    val response = gson.fromJson<ResponseStringModel>(result, type)
                    if (response != null) {
                        if (response.status =="true") {

                            if (response.msg == "Payment Failed!") {
                                msgBox(response.error_msg!!)
                            } else {
                                msgBox(response.msg!!)
                            }
                            adapterListener!!.adapterListener(true)
                        }else{
                            msgBox(response.error_msg!!)
                            adapterListener!!.adapterListener(true)
                        }
                    }
                }catch (e:Exception){
                    appUtils!!.showAlert(e.message.toString())
                }
            }
        }

    }
    fun msgBox(msg: String){
        val localBuilder = AlertDialog.Builder(activity!!)

        localBuilder.setMessage(msg)
        localBuilder.setNeutralButton(R.string.ok) { _, _ ->
            listener!!.onSuccess(msg)

        }
        localBuilder.create().show()
    }

    fun addCard(){
        dialog = Dialog(activity, R.style.Dialog)
        dialog!!.setContentView(R.layout.fragment_card)
        dialog!!.setCanceledOnTouchOutside(false)
        skip = dialog!!.findViewById(R.id.buttonSkip)as Button
        previous = dialog!!.findViewById(R.id.buttonPrevious)as Button
        next = dialog!!.findViewById(R.id.buttonNext)as Button
        cardNo = dialog!!.findViewById(R.id.editCardNo) as EditText
        expiry = dialog!!.findViewById(R.id.editExpiryDate) as EditText
        cvv = dialog!!.findViewById(R.id.editCVV)as EditText
        next!!.isEnabled = false

        next!!.text =resources.getText(R.string.submit)
        skip!!.visibility = View.GONE
        previous!!.visibility = View.GONE


        cardNo!!.addTextChangedListener(CreditCardFormatter(CardValidity.CARD_NO, this, expiry!!, card, 16))
        next!!.setOnClickListener {
            validateCardForm()
            if (card != null && card!!.isValid) {
                dialogProgress = ProgressDialog(activity)
                dialogProgress!!.setMessage("Performing transaction... please wait")
                dialogProgress!!.setCancelable(true)
                dialogProgress!!.setCanceledOnTouchOutside(true)
                dialogProgress!!.show()

                AddCardAsync(this.activity!!).execute()

                /* val cardNum = card!!.number
                 val cardExpiryMonth =  (card!!.expiryMonth)

                 val cardExpiryYear = card!!.expiryYear
                 val cardCVV = card!!.cvc
                 dialog!!.dismiss()
                 AddCardAsync(cardCVV, cardExpiryMonth,cardNum, this.activity!!,cardExpiryYear).execute()*/

            }
        }

        dialog!!.show()
    }

    private fun startAFreshCharge(accessCode: String) {
        accessCodeValue = accessCode
        val charge = Charge()
        charge.accessCode = accessCodeValue
        charge.email = AppUtils.getMyEmail(activity)

        charge.card = card
        chargeCard(charge)
    }

    private fun chargeCard(charge: Charge) {
        PaystackSdk.chargeCard(activity, charge, C10894())
    }


    internal inner class C10894 : Paystack.TransactionCallback {

        override fun onSuccess(transaction: Transaction) {
            Log.i("ChargeCard_Success", transaction.reference + " Successful")
            transactionValue = transaction
            VerifyOnServer(transactionValue!!.reference).execute()


        }

        override fun beforeValidate(transaction: Transaction) {}

        override fun onError(error: Throwable, transaction: Transaction) {

            if (error is ExpiredAccessCodeException) {
                startAFreshCharge(accessCodeValue!!)
                return
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class VerifyOnServer(private var reference: String?):AsyncTask<Void, Int, String>() {

        override fun doInBackground(vararg p0: Void?): String {
            val map = HashMap<String, Any?>()
            map["reference"] = reference
            val url = Const.FEWCHORE_URL +"verifycode"
            return HttpUtility.sendPostRequest(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {
                val gson = Gson()
                val type = object : TypeToken<CardTokenModel>() {}.type
                val cardTokenModel = gson.fromJson<CardTokenModel>(result, type)

                if (cardTokenModel.status == "true"){
                    dialogProgress!!.dismiss()
                    saveLocally(cardTokenModel.cardDetails,cardTokenModel.cardId)
                    dialog!!.dismiss()
                }else{
                    appUtils!!.showAlert("Card cannot be tokenize, Try again !!!")
                }
            }
        }

    }

    private fun validateCardForm() {
        //validate fields
        val cardNum = cardNo!!.text.toString().trim().replace(" ", "")

        if (isEmpty(cardNum)) {
            cardNo!!.error = "Empty card number"
            return
        }

        //build card object with ONLY the number, update the other fields later
        val card = Card.Builder(cardNum, 0, 0, "").build()
        if (!card.validNumber()) {
            cardNo!!.error = "Invalid card number"
            return
        }

        //validate cvc
        val cvc = cvv!!.text.toString().trim()
        if (isEmpty(cvc)) {
            cvv!!.error = "Empty cvc"
            return
        }
        //update the cvc field of the card
        card.cvc = cvc

        //check that it's valid
        if (!card.validCVC()) {
            cvv!!.error = "Invalid cvc"
            return
        }

        val date = expiry!!.text.toString()
        if (date.contains("/")) {
            val rawYear = Calendar.getInstance().get(Calendar.YEAR).toString()
            val yearPrefix = rawYear.substring(0, 2)
            val monthYear = date.split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val monthStr = monthYear[0]
            val yearStr = yearPrefix + monthYear[1]

            var month = -1
            try {
                month = Integer.parseInt(monthStr)
            } catch (ignored: Exception) {
            }

            if (month in 1..12) {
                card.expiryMonth = month
            } else {
                return
            }

            var year = -1
            try {
                year = Integer.parseInt(yearStr)
            } catch (ignored: Exception) {
            }

            if (year > 0) {
                card.expiryYear = year
            } else {
                return
            }

            if (!card.validExpiryDate()) {
                expiry!!.error = "Invalid expiry"
            }
        }
    }

    override fun afterChange(cardValidity: CardValidity, editable: Editable) {
        when (cardValidity) {
            CardValidity.EXPIRY_DATE -> {
                if (editable.length == 2) {
                    var month = -1
                    try {
                        month = Integer.parseInt(editable.toString().trim({ it <= ' ' }))
                    } catch (e: Exception) {
                    }

                    if (month < 1 || month > 12) {
                        expiry!!.error = "Invalid month"
                    }
                    if (editable.length == 5 && !editable.toString().contains("/")) {
                        expiry!!.error = "Invalid date"
                        return
                    }
                    return
                }
                return
            }
            else -> {
            }
        }

    }

    override fun paramIsValid(z: Boolean, cardValidity: CardValidity) {
        when (cardValidity) {
            CardValidity.CARD_NO -> {
                if (z) {
                    card = Card.Builder(this.cardNo!!.text.toString().trim().replace(" ", ""), Integer.valueOf(0), Integer.valueOf(0), "").build()
                    expiry!!.addTextChangedListener(CreditCardFormatter(CardValidity.EXPIRY_DATE, this, this.cvv!!, card, 5))
                    cvv!!.addTextChangedListener(CreditCardFormatter(CardValidity.CVV, this, cardNo!!, card, 3))
                    expiry!!.visibility = View.VISIBLE
                    cvv!!.visibility = View.VISIBLE
                    next!!.visibility = View.VISIBLE
                    cardIsValid = true
                    checkCardValidity()
                    return
                }
                cardNo!!.error = "Invalid card number"
                return
            }
            CardValidity.EXPIRY_DATE -> {
                if (z) {
                    expiryDateIsValid = true
                    checkCardValidity()
                    return
                }
                expiry!!.error = "Invalid expiry date"
                return
            }
            CardValidity.CVV -> {
                if (z) {
                    cvvIsValid = true
                    checkCardValidity()
                    return
                }
                cvv!!.error = "Invalid cvc"
                return
            }
            else -> return
        }
    }


    private fun checkCardValidity() {
        if (this.cardIsValid && expiryDateIsValid && cvvIsValid) {
            next!!.isEnabled = true
        }
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoanPayedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    companion object {
        var adapterListener : AdapterListener ?= null
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?, cardModel: CardModel?, loanAmt: String) =
                CardListFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putParcelable(CARD_DETAILS, cardModel)
                        putString(LOAN_AMOUNT, loanAmt)
                    }
                }
    }
}
