package fewchore.com.exolve.fewchore.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.StrictMode
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.apis.*
import fewchore.com.exolve.fewchore.invest.WhoActivity
import fewchore.com.exolve.fewchore.child_fragment.DashLoanFragment
import fewchore.com.exolve.fewchore.fragment.CardListFragment
import fewchore.com.exolve.fewchore.fragment.DashboardFragment
import fewchore.com.exolve.fewchore.fragment.HistoryFragment
import fewchore.com.exolve.fewchore.fragment.ProfileFragment
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.helper.SharedPreferenceUtil
import fewchore.com.exolve.fewchore.listener.*
import fewchore.com.exolve.fewchore.local_database.LocalDatabase
import fewchore.com.exolve.fewchore.model.Activeloan
import fewchore.com.exolve.fewchore.model.ResponseStringModel
import fewchore.com.exolve.fewchore.views.CircleImageView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.dialog_withdraw_investment.*
import java.lang.IllegalStateException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.net.ssl.SSLContext

class HomeActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener, StatusListener, ActiveLoanListener, LoanPayedListener {


    private var payNow : TextView? = null
    private var loanAmount: TextView ?= null
    private var title: TextView ?= null
    private var appUtils: AppUtils ?= null
    private var relativeProgress : RelativeLayout ?= null
    private var linearDate : LinearLayout ?= null
    private var localDB: LocalDatabase?= null
    private var textDate : TextView?=  null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        setSupportActionBar(toolbar)

        appUtils = AppUtils(this)
        localDB = LocalDatabase(this)
        AwaitApprovalAsync.statusListener = this
        ActiveLoanAsync.activeLoanListener = this

        try {
            ProviderInstaller.installIfNeeded(applicationContext)
            val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        payNow = findViewById(R.id.payNow)
        payNow!!.visibility = View.VISIBLE
        loanAmount = findViewById(R.id.textLoanAmount)
        title = findViewById(R.id.pageTitle)
        relativeProgress = findViewById(R.id.relativeProgress)
        linearDate = findViewById(R.id.linearDate)
        textDate  = findViewById(R.id.textDueDate)
        loadAPI()

        payNow!!.setOnClickListener {
            val loanAmt =loanAmount!!.text.toString()
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, CardListFragment.newInstance("1", null, loanAmt))
                    .commit()
        }
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.setHomeAsUpIndicator(R.drawable.ic_nav_bar)
        toggle.syncState()
        toolbar.setNavigationIcon(R.drawable.ic_nav_bar)

        nav_view.setNavigationItemSelectedListener(this)

        val headerview = layoutInflater.inflate(R.layout.nav_header_home, nav_view, false)
        nav_view.addHeaderView(headerview)

        val header = headerview.findViewById<LinearLayout>(R.id.linearHead)
        val headerName = header.findViewById<TextView>(R.id.textName)
        val headerEmail = header.findViewById<TextView>(R.id.textEmail)
        val profileHead = header.findViewById<CircleImageView>(R.id.imageHeader)
        headerEmail.text = AppUtils.getMyEmail(this)
        val profile = AppUtils.getMyProfile(this)
        Picasso.with(this).load(profile).into(profileHead)

        val name = AppUtils.getMyFirstName(this)+" "+ AppUtils.getMyLastName(this)
        headerName.text = name
        header.findViewById<Button>(R.id.nav_return).setOnClickListener {
            startActivity(Intent(this, WhoActivity::class.java))
        }
        if(loanAmount!!.text == "0") {
            payNow!!.visibility = View.GONE
            linearDate!!.visibility= View.GONE

        }
    }

    private fun loadAPI() {
        if (appUtils!!.hasActiveInternetConnection(this)) {

            AwaitApprovalAsync(this@HomeActivity).execute()
            BankListAsync(this@HomeActivity).execute()
            LoanDurationAsync(this).execute()
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    // this is executed when a pay now is successful from the CardListFragment class
    override fun onSuccess(msg: String) {
        loanAmount!!.text ="0"
        payNow!!.visibility = View.GONE
        ActiveLoanAsync(this).execute()
    }

    // this method is executed when AwaitingApproval Class has been executed. if true then active loan will be executed
    override fun statusListener(status_msg: String, status: Boolean?, user_status: String?) {
        if (status!!){
            loanAmount!!.text ="0"
            payNow!!.visibility = View.GONE
            linearDate!!.visibility= View.GONE

            ActiveLoanAsync(this).execute()

        }else{
            if(status_msg =="Please check your network connection")
                Toast.makeText(this@HomeActivity, status_msg, Toast.LENGTH_LONG).show()

            else
                ActiveLoanAsync(this).execute()
        }


    }
    //This method returns the active loan plan of the user.
    override fun activeLoanListener(activeLoan: Activeloan?, msg: String?, status: Boolean) {

        if (activeLoan != null){
            if (activeLoan.statusTitle =="approved") {
                linearDate!!.visibility= View.GONE
                loanAmount!!.text = "0"
                payNow!!.visibility = View.GONE
                textDate!!.text = activeLoan.loanPaybackdate
                approveLoan(activeLoan.loanAmount!!, activeLoan.loanTotalpayback!!, activeLoan.loanId)
            }else {
                loanAmount!!.text = activeLoan.loanTotalpayback
                linearDate!!.visibility= View.VISIBLE

            }
        }else{
            loanAmount!!.text = "0"
            payNow!!.visibility = View.GONE
            linearDate!!.visibility= View.GONE


        }
        supportFragmentManager.beginTransaction()
                .add(R.id.container, DashboardFragment())
                .commit()
    }
    private fun termsBox() {
        val dialog = Dialog(this, R.style.Dialog)
        dialog.setContentView(R.layout.custom_terms)
        dialog.show()
        dialog.findViewById<ImageButton>(R.id.buttonClose).setOnClickListener {
            dialog.close
        }
    }
    private fun approveLoan(loanAmount: String, loanTotalpayback1: String, loanId: Any?) {
        val name = AppUtils.getMyFirstName(this)
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_loan_offer)
        dialog.setCanceledOnTouchOutside(false)

        dialog.findViewById<TextView>(R.id.terms).setOnClickListener {
            termsBox()
        }

        val msg = dialog.findViewById<TextView>(R.id.textMsg)
        val txt = "Congratulations !!!$name Your Loan request of ₦$loanAmount has been approved by our Agents. Your total Pay back is ₦$loanTotalpayback1. If you wish to Accept this loan please click on the Accept Button. But if you have changed your mind You can click on the Reject Button to Cancel."
        msg.text = txt
        dialog.findViewById<Button>(R.id.buttonAccept).setOnClickListener {
            relativeProgress!!.visibility = View.VISIBLE
            LoanOfferAsync("accept", loanId,dialog).execute()
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.buttonReject).setOnClickListener {
            relativeProgress!!.visibility = View.VISIBLE
            LoanOfferAsync("reject", loanId, dialog).execute()
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("StaticFieldLeak")
    inner class LoanOfferAsync(private var requestStatus: String, private var loanId: Any?, private var dialog: Dialog):AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
            val url = Const.FEWCHORE_URL+"loanoffer"
            val map = HashMap<String, Any?> ()
            map["loan_id"]= loanId
            map["offer_status"]= requestStatus
            try {
                return HttpUtility.sendPostRequest(url, map)
            }catch (e: Exception){}
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null) {
                val gson = Gson()
                try{
                val type = object : TypeToken<ResponseStringModel>() {}.type
                val activeLoan = gson.fromJson<ResponseStringModel>(result, type)
                if (activeLoan != null) {
                    if (activeLoan.status =="true") {
                        appUtils!!.showAlert("Your Loan status will be updated soon")
                        dialog.dismiss()
                        relativeProgress!!.visibility = View.GONE
                    }
                }
            }catch (e: Exception){
                if (e is IllegalStateException)
                {
                    appUtils!!.showAlert("Please check your network connection")

                }}
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_dashboard -> {
                title!!.text = resources.getText(R.string.dashboard)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, DashboardFragment())
                        .commit()
                if(loanAmount!!.text == "0") {
                    payNow!!.visibility = View.GONE
                }
                else
                    payNow!!.visibility = View.VISIBLE

            }
            R.id.nav_borrow -> {
                payNow!!.visibility = View.GONE
                title!!.text = resources.getText(R.string.borrow_money)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, DashLoanFragment.newInstance("1",
                                null))
                        .commit()
            }
            R.id.nav_pay -> {
                payNow!!.visibility = View.GONE
                title!!.text = resources.getText(R.string.pay_now)
                val loanAmt = loanAmount!!.text.toString()
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, CardListFragment.newInstance("1",
                                null,loanAmt))
                        .commit()
            }
            R.id.nav_history -> {
                payNow!!.visibility = View.GONE
                title!!.text = resources.getText(R.string.loan_history)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HistoryFragment())
                        .commit()
            }
            R.id.nav_Profile -> {
                payNow!!.visibility = View.GONE
                title!!.text = resources.getText(R.string.profile)
                supportFragmentManager.beginTransaction()
                        .add(R.id.container, ProfileFragment())
                        .commit()
            }
            R.id.nav_signout -> {
                signOut()

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun signOut() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please confirm sign out")
                .setPositiveButton("Sign Out") { _, _ ->
                    val sharedPreferenceUtils = SharedPreferenceUtil

                    sharedPreferenceUtils.clearSharedPreference(this@HomeActivity)
                    localDB!!.clearDB()

                    startActivity(Intent(this@HomeActivity,LandingActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .create().show()
    }

}
