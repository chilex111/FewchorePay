package fewchore.com.exolve.fewchore.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.adapter.LookUpCardAdapter
import fewchore.com.exolve.fewchore.apis.EncryptCard
import fewchore.com.exolve.fewchore.apis.OTPAsync
import fewchore.com.exolve.fewchore.apis.SaveLookUpAsync
import fewchore.com.exolve.fewchore.apis.TransactionAsync
import fewchore.com.exolve.fewchore.enums.NavigationDirection
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.listener.*
import fewchore.com.exolve.fewchore.model.KeyModel
import fewchore.com.exolve.fewchore.model.LookupCardsModel
import kotlinx.android.synthetic.main.fragment_look_up.*
import kotlinx.android.synthetic.main.fragment_look_up.view.*
import kotlinx.android.synthetic.main.template_progress.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@SuppressLint("StaticFieldLeak")
class LookUpFragment : Fragment(), CardBooleanListener, StatusListener, EncryptionListener,SaveEncryptionListener {



    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: FragmentListener? = null
    private var recyclerView: RecyclerView ?= null
    //private var relativeLayout: RelativeLayout ?= null
    private var appUtils: AppUtils ?= null
    private var secretKey: String ?= null
    private var api_key : String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_look_up, container, false)

        LookUpCardAdapter.listener = this
        EncryptCard.encryptListener = this
        TransactionAsync.saveEncryptionListener = this
        SaveLookUpAsync.listener = this

        recyclerView = v.findViewById(R.id.recyclerView)
       // relativeLayout = v.findViewById(R.id.relativeProgress)
        appUtils = AppUtils(activity!!)

        if (appUtils!!.hasActiveInternetConnection(activity!!)) {
            v.progressLk.visibility = View.VISIBLE

            KeysAsync().execute()
        }else{
            Toast.makeText(activity,"Check your Internet Connection", Toast.LENGTH_LONG).show()
        }
        return v
    }
    override fun statusListener(status_msg: String, status: Boolean?, user_status: String?) {
        activity?.progressLk?.visibility = View.GONE
        if (status!!){
            Toast.makeText(activity,status_msg,Toast.LENGTH_LONG).show()
            proceed()

        }
        else{
            if (status_msg == "Card previously added")
                proceed()
            else
                appUtils!!.showAlert(status_msg)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun lookUpListener(Id: String, cvv: Boolean, exp: Boolean, pin: Boolean) {
        if (!Id.isEmpty()){
            val dialog = Dialog(activity)
            dialog.setContentView(R.layout.look_card)
            val longNo = dialog.findViewById<EditText>(R.id.editCardNo)
            val expiry =  dialog.findViewById<EditText>(R.id.editExpiryDate)
            val CVV =  dialog.findViewById<EditText>(R.id.editCVV)
            val PIN =  dialog.findViewById<EditText>(R.id.editPin)

            dialog.findViewById<Button>(R.id.buttonNext).setOnClickListener {
                val noText = longNo.text.toString()
                val cvvText = CVV.text.toString()
                val expText = expiry.text.toString()
                val pinText = PIN.text.toString()
                dialog.dismiss()
                activity?.progressLk?.visibility = View.VISIBLE
                activity?.progressLk?.where?.text ="Please wait while your card is been encrypted"
                EncryptCard(noText,cvvText,expText,pinText, Id,activity!!).execute()


            }
            dialog.show()
        }

    }
    override fun encryptListener(encryptValue: String, user_status: String?, id: String) {
        if (user_status == "true"){

            val phone = AppUtils.getMyPhoneNumber(activity)
            val phoneCon = takeLastTen("234", phone!!)
            val email = AppUtils.getMyEmail(activity)

            TransactionAsync(encryptValue,id,secretKey!!,api_key!!,phone,phoneCon,email, activity!!).execute()
        }
    }
    override fun saveEncryptListener(encryptValue: String, chargeToken: String?, card_id: String, transaction_ref: String, description: String) {
        if(!description.isEmpty()) {
            if (description.contains("Approved by Financial", true)) {
                SaveLookUpAsync(encryptValue, card_id, chargeToken, secretKey!!, activity!!).execute()
            }

            if (description.contains("Kindly enter the OTP", true)) {

                val dialog = Dialog(context)
                dialog.setContentView(R.layout.dialog_otp)
                val otp = dialog.findViewById<EditText>(R.id.editOtp)

                dialog.findViewById<Button>(R.id.buttonSubmit).setOnClickListener {
                    val otpText = otp.text.toString()
                    if (otpText.isEmpty())
                        otp.error = " This field is required"
                    else {
                        dialog.dismiss()
                        activity?.progressLk?.visibility = View.VISIBLE
                        activity?.progressLk?.where?.text ="Please wait while I validate the OTP you inserted "

                        OTPAsync(otpText,transaction_ref, api_key, secretKey!!, encryptValue, card_id, context!!).execute()

                    }
                }
                dialog.show()
            }
        }else
            SaveLookUpAsync(encryptValue, card_id, chargeToken, secretKey!!, context!!).execute()
    }

    fun proceed(){
        listener!!.onLookUp()
        listener!!.onFragmentNavigation(NavigationDirection.LOOK_UP_FORWARD)
    }

    inner class KeysAsync: AsyncTask<Void, Int,String>(){
        override fun doInBackground(vararg p0: Void?): String {
            val url = Const.FEWCHORE_URL+"getonepipekeys"
            return HttpUtility.getRequest(url)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                try {
                    val gson = Gson()
                    val type = object : TypeToken<KeyModel>() {}.type
                    val keys = gson.fromJson<KeyModel>(result, type)
                    secretKey = keys.secretKey
                    api_key= keys.apiKey

                    LookUpCardAsync().execute()


                }catch (e: Exception){
                    Log.i("TAG", e.message.toString())
                }
            }
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

    fun takeLastTen(codeNo: String, phNo: String): String {
        return if (phNo.length >= 10) {
            val startIndex = phNo.length - 10
            codeNo + phNo.substring(startIndex, phNo.length)
        } else {
            codeNo + phNo
        }
    }

    inner class LookUpCardAsync: AsyncTask<Void, Int, String>(){

        override fun doInBackground(vararg p0: Void?): String? {
            @SuppressLint("SimpleDateFormat")
            val nowAsString = SimpleDateFormat("yy").format(Date())
            @SuppressLint("SimpleDateFormat")
            val transactionRef = "TRAN" + nowAsString + SimpleDateFormat("yyyyMMddHHmmss").format(Date())

            val map = JSONObject()
            val auth = JSONObject()
            val email = AppUtils.getMyEmail(activity!!)
            val mobile = AppUtils.getMyPhoneNumber(activity)
            val id  = takeLastTen("234", mobile!!)
            auth.put("email", email)
            auth.put("mobile_no", id)
            auth.put("id", id)

            map.put("request_ref",transactionRef)
            map.put("customer",auth)

            val url = Const.ONE_PIPE_CUSTOMER+"lookup/cards"
            try {
                return HttpUtility.postJsonAuth(url, map, api_key!!)
            }catch (e: Exception){
                Log.i("LOOK", e.message)
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {

                activity?.progressLk?.visibility = View.GONE
                val gson = Gson()
                try {
                    val type = object : TypeToken<LookupCardsModel>() {}.type
                    val cardsModel = gson.fromJson<LookupCardsModel>(result, type)
                    if (cardsModel.cards.isNotEmpty()&&(cardsModel.cards!= null)) {

                        val cardAdapter = LookUpCardAdapter(cardsModel.cards, activity!!)
                        recyclerView!!.layoutManager = LinearLayoutManager(activity)

                        recyclerView!!.adapter = cardAdapter
                        recyclerView!!.setHasFixedSize(true)
                        recyclerView!!.requestFocus()
                    }else{
                        proceed()
                    }
                } catch (e: Exception) {
                }
            }else
                proceed()
        }

    }
    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                LookUpFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

}
