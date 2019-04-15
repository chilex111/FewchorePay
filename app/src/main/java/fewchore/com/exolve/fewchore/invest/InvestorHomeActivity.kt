package fewchore.com.exolve.fewchore.invest

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.squareup.picasso.Picasso
import fewchore.com.exolve.fewchore.activity.LandingActivity
import fewchore.com.exolve.fewchore.fragment.ProfileFragment
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.SharedPreferenceUtil
import fewchore.com.exolve.fewchore.local_database.LocalDatabase
import fewchore.com.exolve.fewchore.views.CircleImageView
import kotlinx.android.synthetic.main.activity_investor_home.*
import kotlinx.android.synthetic.main.app_bar_investor_home.*
import kotlin.jvm.java
import org.json.JSONException
import android.os.StrictMode
import android.view.View
import android.widget.*
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.apis.StatusAsync
import fewchore.com.exolve.fewchore.listener.IdListener
import fewchore.com.exolve.fewchore.listener.StatusListener


class InvestorHomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, StatusListener,IdListener {


    private var title: TextView?= null
    private var localDB: LocalDatabase?= null
    private var home: LinearLayout ?= null
    private var invest: LinearLayout ?= null
    private var container : FrameLayout ?= null
    private var nav_container : FrameLayout ?= null
    private var relativeProgress : RelativeLayout ?= null
    private var appUtils: AppUtils ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        try {
            // JSON here
        } catch (e2: JSONException) {
            // TODO Auto-generated catch block
            e2.printStackTrace()
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_investor_home)
        setSupportActionBar(toolbar1)

        localDB = LocalDatabase(this)
        appUtils = AppUtils(this)
        StatusAsync.statusListener = this
        InvestHomeFragment.listner = this
        InvestNowFragment.listner = this

        home = findViewById(R.id.homeLin)
        invest = findViewById(R.id.investLin)
        title = findViewById(R.id.pageTitle)
        nav_container = findViewById(R.id.nav_container)
        container = findViewById(R.id.container)
        relativeProgress = findViewById(R.id.relativeProgress)

        if (appUtils!!.hasActiveInternetConnection(this)){
            relativeProgress!!.visibility = View.GONE
            StatusAsync(this).execute()
        }

        /*supportFragmentManager.beginTransaction()
                .add(R.id.container, InvestHomeFragment())
                .commit()
        home!!.alpha = 1f
        invest!!.alpha = 0.3F*/

        val toggle = ActionBarDrawerToggle(
                this, drawer__layout, toolbar1, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer__layout.addDrawerListener(toggle)
        toggle.setHomeAsUpIndicator(R.drawable.ic_nav_bar)
        toggle.syncState()
        toolbar1.setNavigationIcon(R.drawable.ic_nav_bar)

        nav__view.setNavigationItemSelectedListener(this)

        val headerview = layoutInflater.inflate(R.layout.nav_header_home, nav__view, false)
        nav__view.addHeaderView(headerview)

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

        findViewById<ImageView>(R.id.homeInvest).setOnClickListener {
            container!!.removeAllViews()

            home!!.alpha = 1f
            invest!!.alpha = 0.3F
            val statuss = AppUtils.getMyInvestStatus(this)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, InvestHomeFragment.newInstance(statuss!!, null))
                    .commit()
        }
        findViewById<ImageView>(R.id.investNow).setOnClickListener {
            container!!.removeAllViews()
            home!!.alpha = 0.3f
            invest!!.alpha = 1F
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, InvestNowFragment())
                    .commit()
        }

    }

    override fun idListener(id: String) {
        if (id == "Invest") {
            home!!.alpha = 0.3f
            invest!!.alpha = 1F
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, InvestNowFragment())
                    .commit()
        }
        else if (id == "home"){
            home!!.alpha = 1f
            invest!!.alpha = 0.3F
            val statuss = AppUtils.getMyInvestStatus(this)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, InvestHomeFragment.newInstance(statuss!!, null))
                    .commit()
        }
    }

    override fun statusListener(status_msg: String, status: Boolean?, user_status: String?) {
        relativeProgress!!.visibility = View.GONE
        home!!.alpha = 1f
        invest!!.alpha = 0.3F
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, InvestHomeFragment.newInstance(user_status!!, null))
                .commit()

    }

    override fun onBackPressed() {
        if (drawer__layout.isDrawerOpen(GravityCompat.START)) {
            drawer__layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }




    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_dashboard1 -> {
                nav_container!!.removeAllViews()
                title!!.text = resources.getText(R.string.my_investments)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, InvestHomeFragment())
                        .commit()
            }
            R.id.nav_invest -> {
                title!!.text = resources.getText(R.string.invest_now)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_container, InvestNowFragment.newInstance("1", null))
                        .commit()
            }

            R.id.nav_history -> {
                title!!.text = resources.getText(R.string.investment_history)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_container, InvestmentHistoryFragment.newInstance("1", null))
                        .commit()
            }
            R.id.nav_Profile -> {
                title!!.text = resources.getText(R.string.profile)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_container, ProfileFragment())
                        .commit()
            }
            R.id.nav_signout -> {
                signOut()

            }
        }

        drawer__layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun signOut() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please confirm sign out")
                .setPositiveButton("Sign Out") { _, _ ->
                    val sharedPreferenceUtils = SharedPreferenceUtil

                    sharedPreferenceUtils.clearSharedPreference(this@InvestorHomeActivity)
                    localDB!!.clearDB()

                    startActivity(Intent(this@InvestorHomeActivity, LandingActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .create().show()
    }

}
