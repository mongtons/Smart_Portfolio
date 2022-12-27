package kr.ac.hallym.mobileproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kr.ac.hallym.mobileproject.databinding.ActivityAboutBinding
import kr.ac.hallym.mobileproject.fragment.OneFragment
import kr.ac.hallym.mobileproject.fragment.ThreeFragment
import kr.ac.hallym.mobileproject.fragment.TwoFragment

class AboutActivity : AppCompatActivity() {
    lateinit var aboutBinding: ActivityAboutBinding
    var arr:Array<String> = arrayOf("Career & Activty", "License", "Grade & Credit")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aboutBinding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(aboutBinding.root)

        setSupportActionBar(aboutBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        aboutBinding.viewpager.adapter=MyFragmentPagerViewAdapter(this)
        TabLayoutMediator(aboutBinding.tabs, aboutBinding.viewpager){ tab, position ->
            tab.text=arr[position]
        }.attach()

        aboutBinding.mainDrawer.setNavigationItemSelectedListener {
            val intent: Intent
            when (it.title) {
                "HOME" -> {
                    intent = Intent(this, MainPageActivity::class.java)
                    startActivity(intent)
                }
                "ABOUT" -> { }
                "PROJECT" -> {
                    intent = Intent(this, ProjectActivity::class.java)
                    startActivity(intent)
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
class MyFragmentPagerViewAdapter(activity: FragmentActivity): FragmentStateAdapter(activity){
    val fragments: List<Fragment>
    init {
        fragments= listOf(OneFragment(), TwoFragment(), ThreeFragment())
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment =fragments[position]
}