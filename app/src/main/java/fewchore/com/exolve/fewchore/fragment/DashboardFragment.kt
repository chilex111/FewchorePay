package fewchore.com.exolve.fewchore.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.child_fragment.DashHomeFragment
import fewchore.com.exolve.fewchore.child_fragment.DashLoanFragment
import fewchore.com.exolve.fewchore.listener.IdListener
import fewchore.com.exolve.fewchore.listener.LoanListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DashboardFragment : Fragment(), LoanListener,IdListener {



    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var payIt: String? = null
    private var home : ImageView ?= null
    private var loan : ImageView ?= null
    private var pay : ImageView ?= null

    /*
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var currentTab = 0*/

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param payIt Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?, payIt: String) =
                DashboardFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, payIt)
                    }
                }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            payIt = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false) as View
       DashHomeFragment.loanListener = this
        DashLoanFragment.listener = this

        home = view.findViewById(R.id.homeLoan)
        loan = view.findViewById(R.id.takeLoan)
        pay = view.findViewById(R.id.payLoan)

        childFragmentManager
                .beginTransaction()
                .replace(R.id.container, DashHomeFragment())
                .addToBackStack(null)
                .commit()
        loan!!.alpha = 0.3F
        pay!!.alpha = 0.3F
        home!!.alpha = 1.0F

        view.findViewById<ImageView>(R.id.homeLoan).setOnClickListener {
            childFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, DashHomeFragment())
                    .addToBackStack(null)
                    .commit()

            loan!!.alpha = 0.3F
            pay!!.alpha = 0.3F
            home!!.alpha = 1.0F
        }

        view.findViewById<ImageView>(R.id.takeLoan).setOnClickListener {
            childFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, DashLoanFragment())
                    .addToBackStack(null)
                    .commit()

            loan!!.alpha = 1.0F
            pay!!.alpha = 0.3F
            home!!.alpha = 0.3F
        }
        view.findViewById<ImageView>(R.id.payLoan).setOnClickListener {
            childFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, CardListFragment())
                    .addToBackStack(null)
                    .commit()

            loan!!.alpha = 0.3F
            pay!!.alpha = 1.0F
            home!!.alpha = 0.3F
        }

   /*     viewPager = view.findViewById(R.id.viewPager) as ViewPager
        tabLayout = view.findViewById(R.id.tabLayout) as TabLayout


        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)

        tabLayout!!.setupWithViewPager(viewPager)
        viewPager!!.adapter = mSectionsPagerAdapter
        tabLayout!!.getTabAt(0)!!.setIcon(R.drawable.ic_home)
        tabLayout!!.getTabAt(1)!!.setIcon(R.drawable.pay)
        tabLayout!!.getTabAt(2)!!.setIcon(R.drawable.card)


        tabLayout!!.getTabAt(0)!!.text = "Home"
        tabLayout!!.getTabAt(1)!!.text = "Loan"
        tabLayout!!.getTabAt(2)!!.text = "Pay"
        tabLayout!!.setSelectedTabIndicatorColor(Color.WHITE)
        viewPager!!.setCurrentItem(currentTab, true)
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })*/

      /*  if (payIt.equals("PAY_NOW")) {
            viewPager!!.currentItem = 3
            CardListFragment()
        }*/


        return view
    }
    override fun loanListener(value: Boolean?) {
        if (value!!){
            childFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, DashLoanFragment())
                    .addToBackStack(null)
                    .commit()

        }
    }
    override fun idListener(id: String) {
        childFragmentManager
                .beginTransaction()
                .replace(R.id.container, DashHomeFragment())
                .addToBackStack(null)
                .commit()
        loan!!.alpha = 0.3F
        pay!!.alpha = 0.3F
        home!!.alpha = 1.0F
    }
  /*  inner class SectionsPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            // getItem is called to newInstance the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return when (position) {
                0 -> {
                    currentTab = 0
                    DashHomeFragment()
                }
                1 -> {
                    currentTab = 1
                    DashLoanFragment()
                }
                2 -> {
                    currentTab = 2
                    CardListFragment()
                }
                else -> null
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }*/
}


