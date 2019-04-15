package fewchore.com.exolve.fewchore.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import fewchore.com.exolve.fewchore.R

class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        val buttonCreate = findViewById<Button>(R.id.buttonCreate)
        val buttonLogin = this.findViewById<Button>(R.id.buttonLogin)

        buttonCreate.setOnClickListener { startActivity(Intent(this@LandingActivity, SignUpActivity::class.java)) }

        buttonLogin.setOnClickListener { startActivity(Intent(this@LandingActivity, LoginActivity::class.java)) }


    }
}
