package kr.ac.hallym.mobileproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import kr.ac.hallym.mobileproject.databinding.ActivityMainPageBinding

class MainPageActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityMainPageBinding.inflate(layoutInflater)
    }
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){}
        binding.mainDrawer.setNavigationItemSelectedListener {
            val intent:Intent
            when(it.title){
                "HOME" ->{

                }
                "ABOUT" ->{
                    intent=Intent(this, AboutActivity::class.java)
                    requestLauncher.launch(intent)
                }
                "PROJECT" ->{
                    intent=Intent(this, ProjectActivity::class.java)
                    requestLauncher.launch(intent)
                }
            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent=Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }
}