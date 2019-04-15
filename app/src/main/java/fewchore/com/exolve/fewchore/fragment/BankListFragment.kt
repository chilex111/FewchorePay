package fewchore.com.exolve.fewchore.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.adapter.BankAdapter
import fewchore.com.exolve.fewchore.apis.AcctListAsync
import fewchore.com.exolve.fewchore.apis.BankListAsync
import fewchore.com.exolve.fewchore.enums.NavigationDirection
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.AppUtils.Companion.PREF_BVN
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.helper.SharedPreferenceUtil
import fewchore.com.exolve.fewchore.listener.AcctListListener
import fewchore.com.exolve.fewchore.listener.InternetListener
import fewchore.com.exolve.fewchore.listener.FragmentListener
import fewchore.com.exolve.fewchore.listener.StringListener
import fewchore.com.exolve.fewchore.local_database.LocalDatabase
import fewchore.com.exolve.fewchore.model.BankDetail
import fewchore.com.exolve.fewchore.model.BankModel
import fewchore.com.exolve.fewchore.model.ResponseStringModel
import java.lang.*
import kotlin.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PAGE_VALUE = "param1"
private const val BANK_DETAILS = "param2"


class BankListFragment : Fragment() , StringListener,AcctListListener, InternetListener {


    // TODO: Rename and change types of parameters
    private var pageValue: String? = null
    private var bankModel: BankModel? = null
    private var bankList : ArrayList<BankModel>? = null
    private var localDB : LocalDatabase? = null
    private var spinnerAcctType: Spinner? = null
    private var spinnerBank: Spinner?= null
    private var bankForm: RecyclerView?=null
    private var textEmpty: TextView? = null
    private var buttonSubmit : ImageButton ?= null
    private var acctTypeSelected: String?= null
    private var bankSelected: String?= null
    private var acctTypeId: Int? = null
    private var appUtils: AppUtils ?= null
    private var banKName: String?= null
    private var bankName: String?= null
    private var acctType: String?= null
    private var acctNumberText: String?= null
    private var acctName : EditText?= null
    private var editAcctNo : EditText?= null
    private var relativeProgress: RelativeLayout ?= null
    private var progress: RelativeLayout ?= null

    private var bvnStatus: Boolean ? = null
    private var frameSubmit : FrameLayout ?= null
    private var internetCheck : Int ?= null
    private var enteredBVN : String ?= null
    private var dialogBank : Dialog?= null
    private var bvn: EditText ?= null
    private var progressBVN: ProgressBar ?= null
    private var sharedPrefernce: SharedPreferenceUtil ?= null


    private var listener: FragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        arguments?.let {

            pageValue = it.getString(PAGE_VALUE)
            bankModel = it.getParcelable(BANK_DETAILS)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bank_list, container, false)

        localDB = LocalDatabase(activity) // initialise local DB
        BankAdapter.stringlistener = this
        AcctListAsync.listener = this


        sharedPrefernce = SharedPreferenceUtil
        appUtils = AppUtils(activity!!)

        relativeProgress = view.findViewById(R.id.relativeProgress)
        val frameContainer = view.findViewById<ConstraintLayout>(R.id.bankContainer)
        frameSubmit = view.findViewById(R.id.framelogIn)
        frameSubmit!!.visibility = View.GONE
        if (pageValue!= null) {
            if ( pageValue.equals("2")) {
                frameContainer!!.setBackgroundColor(ContextCompat.getColor(activity!!,R.color.white))
            }
        }
        bankList = localDB!!.allAcct //gets all the banks that is in local DB
        bankForm = view.findViewById(R.id.recyclerView)
        textEmpty =view.findViewById(R.id.textEmpty)

        buttonSubmit = view.findViewById(R.id.buttonSubmit)
        if ( localDB!!.allAcct.size >=1) {
            localDB()
            frameSubmit!!.visibility= View.GONE
            //AcctListAsync(activity!!).execute()
        }
        /*internetCheck = 1
        InternetCheckAsync().execute()*/

        if (appUtils!!.hasActiveInternetConnection(activity!!)){
            relativeProgress!!.visibility = View.VISIBLE
            AcctListAsync(activity!!).execute()
        }else{
            appUtils!!.showAlert("Please check your Internet connection")
        }


        buttonSubmit!!.setOnClickListener{
            saveBank()
        }
        val empty = "No Bank,\n Click on Add Button to add Bank"
        /*   if (bankList != null && (!bankList!!.isEmpty())) {
              *//* if (bankList!!.size ==1) {
                if (pageValue.equals("2"))
                    frameSubmit!!.visibility = View.GONE
                else
                    frameSubmit!!.visibility = View.VISIBLE
            }else{
                frameSubmit!!.visibility = View.GONE
            }*//*
            localDB()

        } else {
            textEmpty!!.text = empty
            textEmpty!!.visibility = View.VISIBLE
            frameSubmit!!.visibility = View.GONE
        }*/

        val  buttonAdd = view.findViewById<FloatingActionButton>(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            addBank(null, null, null, null)
        }
        return view
    }

    override fun networkListener(status: Boolean) {
        if (status){
            when(internetCheck){
                1 ->{
                    relativeProgress!!.visibility = View.VISIBLE
                    AcctListAsync(activity!!).execute()
                }
                /*2 ->{
                    progressBVN!!.visibility = View.VISIBLE
                        VerifyBVNAsync(enteredBVN!!, progressBVN!!, bvn!!).execute()
                }*/
            }
        }
    }

    override fun acctDetailsListener(bankDetail: List<BankDetail>?, msg: String) {
        relativeProgress!!.visibility = View.GONE
        //  progress!!.visibility = View.GONE
        if (bankDetail != null){

            /*if( bankDetail.size >1){
                frameSubmit!!.visibility = View.GONE
            }*/
            val  bankModel = BankModel()
            val bankList = ArrayList<BankModel>()
            for (details in bankDetail) {
                bankModel.acctNo = details.userbankAccno
                bankModel.acctType = details.userbankAccttype
                bankModel.bankName = details.bankName
                //  bankModel!!.acctName = details.a
                bankList.add(bankModel)
                localDB!!.insertAccts(bankList)
            }
            localDB()
        }else{
            val empty = "No Bank,\n Click on Add Button to add Bank"
            textEmpty!!.text = empty
            textEmpty!!.visibility = View.VISIBLE
        }
    }
    private fun localDB(){
        val allBanks = localDB!!.allAcct
        if (!allBanks.isEmpty() &&(allBanks != null)) {
            textEmpty!!.visibility = View.GONE

            val bankAdapter = BankAdapter(allBanks, this.activity!!, pageValue)
            bankForm!!.layoutManager = LinearLayoutManager(activity)
            bankForm!!.adapter = bankAdapter
            bankForm!!.setHasFixedSize(true)
            bankForm!!.requestFocus()
        }else{
            textEmpty!!.visibility = View.VISIBLE

        }
    }
    private fun saveBank() {
        bankModel = BankModel()
        bankModel!!.bankName = bankName
        bankModel!!.acctType = acctType
        bankModel!!.acctNo = acctNumberText
        listener!!.onBankDetailSubmit(bankModel!!)
        listener!!.onFragmentNavigation(NavigationDirection.BANK_DETAILS_FORWARD)

    }

    @Suppress("NAME_SHADOWING")
    fun addBank(s: String?, bankName: String?, acctType: String?, acctNumber: String?) {
        textEmpty!!.visibility = View.GONE
        dialogBank = Dialog(activity, R.style.Dialog)
        dialogBank!!.setContentView(R.layout.fragment_bank)
        dialogBank!!.setCanceledOnTouchOutside(false)
        val skip = dialogBank!!.findViewById(R.id.buttonSkip)as Button
        val next = dialogBank!!.findViewById(R.id.buttonNext)as Button
        val prgressName = dialogBank!!.findViewById<ProgressBar>(R.id.progressName)
        progressBVN = dialogBank!!.findViewById(R.id.progressBVN)

        bvn = dialogBank!!.findViewById(R.id.editBVN)as EditText
        prgressName.visibility = View.GONE
        progressBVN!!.visibility = View.GONE
        acctName = dialogBank!!.findViewById(R.id.editAccountName)
        progress = dialogBank!!.findViewById(R.id.relativeProgress)as RelativeLayout
        editAcctNo = dialogBank!!.findViewById(R.id.editAccountNo) as EditText
        if (AppUtils.getMyBVN(activity).equals("verified"))
            bvn!!.visibility = View.GONE

        acctName!!.setTextIsSelectable(false)
        bvn!!.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length ==11){
                    enteredBVN = p0.toString()
                    /*
                    internetCheck = 2
                    InternetCheckAsync().execute()*/

                    if (appUtils!!.hasActiveInternetConnection(activity!!)){
                        progressBVN!!.visibility = View.VISIBLE
                        VerifyBVNAsync(enteredBVN!!, progressBVN!!, bvn!!).execute()
                    }else{
                        appUtils!!.showAlert("Please check your Internet connection")
                    }

                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })


        /* val bvnText = bvn.text.toString()
         if (bvnText.length ==11)
             VerifyBVNAsync(bvnText, progressBVN, bvn).execute() */

        editAcctNo!!.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length ==10) {
                    prgressName.visibility = View.VISIBLE
                    GetNameAsync(p0.toString(), prgressName).execute()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }


        })
        spinnerAcctType = dialogBank!!.findViewById(R.id.spinnerAcctType)
        spinnerBank = dialogBank!!.findViewById(R.id.spinnerBankName)
        if (s.equals("EDIT")){
            editAcctNo!!.setText(acctNumber)
            acctTypeSelected = acctType
            bankSelected = bankName

        }
        if (localDB!!.allBank != null &&(!localDB!!.allBank.isEmpty())){
            val bankList  = localDB!!.allBank
            val lists = ArrayList<String>()
            lists.add("Select Bank")
            for (s in bankList) {
                lists.add(s.bankName!!)
            }
            val titleAdapter = ArrayAdapter(activity,android.R.layout.simple_spinner_dropdown_item, lists)
            titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinnerBank!!.adapter = titleAdapter
            if (bankSelected != null) {
                val selectedPosition = lists.indexOf(bankSelected!!)
                spinnerBank!!.setSelection(selectedPosition)

            }

            spinnerBank!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                    if (i == 0) {
                        Log.i("PersonalFragment", "Nothing selected")
                    } else {
                        bankSelected = adapterView.selectedItem.toString()
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {
                }
            }
        }else{

            // progress!!.visibility = View.VISIBLE
            BankListAsync(activity!!).execute()
        }

        skip.setOnClickListener{
            dialogBank!!.dismiss()
        }

        val acctTypeList = ArrayList<String>()
        acctTypeList.add("Account Type")
        acctTypeList.add("Credit")
        acctTypeList.add("Saving")
        acctTypeList.add("Current")
        val acctAdapter = ArrayAdapter(activity,android.R.layout.simple_spinner_dropdown_item, acctTypeList)
        acctAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAcctType!!.adapter = acctAdapter

        if (acctTypeSelected != null) {
            val selectedPosition = acctTypeList.indexOf(acctTypeSelected!!)
            spinnerAcctType!!.setSelection(selectedPosition)

        }
        spinnerAcctType!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                if (i == 0) {
                    Log.i("PersonalFragment", "Nothing selected")
                } else {
                    acctTypeSelected = adapterView.selectedItem.toString()
                }
                acctTypeId= i
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }
        AppUtils.getMyBVN(activity!!)
        next.setOnClickListener {
            val acctNoType = editAcctNo!!.text.toString()
            val bvnText = bvn!!.text.toString()
            val name = acctName!!.text.toString()
            if(AppUtils.getMyBVN(activity!!).equals("verified")){
                bvnStatus = true
            }
            if (bvnStatus!!){
                bvn!!.error = "Please enter a valid BVN"
            }
            if (acctNoType.isEmpty())
                editAcctNo!!.error = "This field is required"
            if (acctTypeId == 0)
                appUtils!!.showAlert("Select a valid Account Type")

            else{
                val  bankModel = BankModel()
                val bankList = ArrayList<BankModel>()
                if(!bvnText.isEmpty()) {
                    val bvnscamb = "**** **** **${bvnText.substring(bvnText.length - 1)}"
                    bankModel.bvn = bvnscamb
                }
                bankModel.acctNo = acctNoType
                bankModel.acctType = acctTypeSelected
                bankModel.bankName = bankSelected
                bankModel.acctName = name
                bankList.add(bankModel)
                localDB!!.insertAccts(bankList)

                localDB() // call the recyclerview to display bank
                //bankAdapter!!.notifyDataSetChanged()
                dialogBank!!.dismiss()
            }

        }
        dialogBank!!.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if ( dialogBank!=null && dialogBank!!.isShowing){
            dialogBank!!.cancel()
        }

    }

    @SuppressLint("StaticFieldLeak")
    inner class VerifyBVNAsync(private var bvnText: String, private var progress: ProgressBar, private var bvn: EditText):AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            try {

                val url = Const.FEWCHORE_URL +"bvn"
                val userId = AppUtils.getMyUserId(activity)
                val map = HashMap<String, Any?>()
                map["bvn"] = bvnText
                map["user_id"] = userId
                return HttpUtility.sendPostRequest(url, map)
            }catch (e: Exception){
                appUtils!!.showAlert("Something happened during run time try again later")
            }
            return ""
        }

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                progress.visibility = View.GONE
                val gson = Gson()
                try{
                    val type = object : TypeToken<ResponseStringModel>() {}.type
                    val userModel = gson.fromJson<ResponseStringModel>(result, type)

                    if (userModel != null) {
                        if (userModel.status =="true") {
                            bvn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0)
                            bvnStatus = true
                            sharedPrefernce!!.save(activity!!, "verified", PREF_BVN)
                        } else {
                            //bvn.setCompoundDrawables(null,null,activity!!.resources.getDrawable(R.drawable.ic_close),null)

                            bvn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_close, 0)
                            bvn.error = "Your BVN could not be verified. Please try again"
                            bvnStatus = false
                        }
                    }
                }catch (e: Exception){
                    appUtils!!.showAlert("Please check your internet connection")
                }catch (e: java.lang.IllegalStateException){
                    appUtils!!.showAlert("Please check your internet connection")
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class GetNameAsync(private var acctNo: String, private var progressName: ProgressBar):AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
            val url = Const.FEWCHORE_URL + "accountnumber"
            val map = HashMap<String, Any?>()

            map["account_number"] = acctNo
            map["bank_name"] = bankSelected
            try {
                return HttpUtility.sendPostRequest(url, map)
            } catch (e: Exception){}
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                val gson = Gson()
                val type = object : TypeToken<ResponseStringModel>() {}.type
                val userModel = gson.fromJson<ResponseStringModel>(result, type)
                if (userModel.status =="true"){
                    acctName!!.setText(userModel.name)
                    progressName.visibility = View.GONE
                }else{
                    acctName!!.setText(userModel.name)
                }
            }
        }
    }

    override fun accountDetailsListener(bankName: String?, acctType: String?, acctNumber: String?, s: String?, bvn: String?) {
        if (s.equals("Details")){
            val dialog = Dialog(activity, R.style.Dialog)
            dialog.setContentView(R.layout.fragment_bank_detail)
            dialog.setCanceledOnTouchOutside(false)
            val bank = dialog.findViewById(R.id.textBank)as TextView
            val actNo = dialog.findViewById(R.id.textAcctNo)as TextView
            val textBvn = dialog.findViewById(R.id.textBVN)as TextView
            val actType = dialog.findViewById(R.id.textAcctType)as TextView

            bank.text = bankName
            actNo.text = acctNumber
            textBvn.text = bvn
            actType.text = acctType
            dialog.show()
        }else {
            if (s.equals("EDIT")) {
                addBank(s, bankName, acctType, acctNumber)
            } else if (!acctType!!.isNullOrEmpty()) {
                this.bankName = bankName
                this.acctType = acctType
                acctNumberText = acctNumber

                bankModel = BankModel()
                bankModel!!.bankName = bankName
                bankModel!!.acctType = acctType
                bankModel!!.acctNo = acctNumberText
                listener!!.onBankDetailSubmit(bankModel!!)
                listener!!.onFragmentNavigation(NavigationDirection.BANK_DETAILS_FORWARD)
            } else {
                appUtils!!.showAlert("Invalid Bank Detail")
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
         * @param page Parameter 1.
         * @param bankModel Parameter 2.
         * @return A new instance of fragment BankListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(page: String?, bankModel: BankModel?) =
                BankListFragment().apply {
                    arguments = Bundle().apply {
                        putString(PAGE_VALUE, page)
                        putParcelable(BANK_DETAILS, bankModel)
                    }
                }
    }
}
