package kr.ac.hallym.mobileproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kr.ac.hallym.mobileproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    var flag:Boolean=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val intent=Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onBackPressed() {
        if(flag){
            val toast= Toast.makeText(this, "종료하려면 한 번 더 누르세요.", Toast.LENGTH_SHORT).show()
            flag=!flag
        }else {
            super.onBackPressed()
            ActivityCompat.finishAffinity(this)
            finish()
        }
    }
}