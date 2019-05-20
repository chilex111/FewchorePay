package fewchore.com.exolve.fewchore.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

import fewchore.com.exolve.fewchore.R


class SliderActivity : AppCompatActivity() {
    private var buttonSignUp: Button? = null
    private var buttonLogin: Button? = null
    private var dotsLayout: LinearLayout? = null
    private var layouts: IntArray? = null
    private var viewPagerListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrollStateChanged(paramAnonymousInt: Int) {}

        override fun onPageScrolled(paramAnonymousInt1: Int, paramAnonymousFloat: Float, paramAnonymousInt2: Int) {}

        override fun onPageSelected(position: Int) {
            addBottomDots(position)
            if (position == layouts!!.size - 1) {
                buttonSignUp!!.setText(R.string.register)
                buttonLogin!!.setText(R.string.log_in)
            }

            dotsLayout!!.visibility = View.VISIBLE
            buttonLogin!!.visibility = View.VISIBLE
            buttonSignUp!!.visibility = View.VISIBLE
        }
    }

    private fun addBottomDots(currentPage: Int) {
        val dots = arrayOfNulls<TextView>(layouts!!.size)

        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        dotsLayout!!.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(colorsInactive[currentPage])
            dotsLayout!!.addView(dots[i])
        }
        if (dots.isNotEmpty()) {
            dots[currentPage]!!.setTextColor(colorsActive[currentPage])
        }
    }

    override fun onCreate(paramBundle: Bundle?) {
        super.onCreate(paramBundle)
        setContentView(R.layout.activity_slider)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        dotsLayout = findViewById(R.id.layoutDots)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        buttonLogin = findViewById(R.id.buttonLogin)
        layouts = intArrayOf(R.layout.slider1, R.layout.slider2)

        addBottomDots(0)
        buttonLogin!!.setOnClickListener { startActivity(Intent(this@SliderActivity, LoginActivity::class.java)) }
        val myViewPagerAdapter = MyViewPagerAdapter()
        viewPager.adapter = myViewPagerAdapter
        viewPager.addOnPageChangeListener(this.viewPagerListener)
        viewPager.offscreenPageLimit = 2
        buttonSignUp!!.setOnClickListener { startActivity(Intent(this@SliderActivity, SignUpActivity::class.java)) }
    }

     inner class MyViewPagerAdapter : PagerAdapter() {

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater.inflate(layouts!![position], container, false)
            container.addView(view)
            return view
        }


        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }
    }
}
