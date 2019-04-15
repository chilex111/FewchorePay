package fewchore.com.exolve.fewchore.invest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.CardView
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.activity.HomeActivity

class WhoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_das)
        findViewById<CardView>(R.id.invest).setOnClickListener {
            startActivity(Intent(this@WhoActivity, InvestorHomeActivity::class.java))

        }
        findViewById<CardView>(R.id.loan).setOnClickListener {
            startActivity(Intent(this@WhoActivity, HomeActivity::class.java))

        }
    }
}
