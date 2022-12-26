package kr.ac.hallym.mobileproject

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import kr.ac.hallym.mobileproject.databinding.ActivityMainPageBinding
import kr.ac.hallym.mobileproject.datatype.Project

class MainPageActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityMainPageBinding.inflate(layoutInflater)
    }
    val db=DBHelper(this)
    lateinit var languageMap: MutableMap<String, Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        languageMap= mutableMapOf()
        val data=db.readableDatabase
        val cursor=data.rawQuery("SELECT * FROM Project;", null)
        while(cursor.moveToNext()){
            if(languageMap.containsKey(cursor.getString(3))){
                var num:Int=languageMap.get(cursor.getString(3))!!
                languageMap[cursor.getString(3)] = ++num
            }else{
                languageMap.put(cursor.getString(3), 1)
            }
        }
        val languageList= mutableListOf<String>()

        for((key, value) in languageMap){
            languageList.add(key)
        }
        var languageRank:Array<String> = Array(3, { "" })
        var languageNum:Array<Int> = Array(3, { 0 })
        for(j in 0 until languageList.size){
            if(languageNum[0]<languageMap[languageList[j]]!!) {
                languageNum[0] = languageMap[languageList[j]]!!
                languageRank[0]=languageList[j]
            }
        }
        for(j in 0 until languageList.size){
            if(languageNum[1]<languageMap[languageList[j]]!! && languageNum[0]>languageMap[languageList[j]]!!) {
                languageNum[1] = languageMap[languageList[j]]!!
                languageRank[1]=languageList[j]
            }
        }
        for(j in 0 until languageList.size){
            if(languageNum[2]<languageMap[languageList[j]]!! && languageNum[1]>languageMap[languageList[j]]!!) {
                languageNum[2] = languageMap[languageList[j]]!!
                languageRank[2]=languageList[j]
            }
        }
        if (languageRank[0].equals("스프링부트")) {
            languageRank[0] = "Spring Boot"
            binding.firstLangImg.setImageResource(R.drawable.springboot)
        } else if (languageRank[0].equals("파이썬")) {
            languageRank[0] = "Python"
            binding.firstLangImg.setImageResource(R.drawable.python)
        } else if (languageRank[0].equals("데이터베이스")){
            languageRank[0] = "MySQL"
            binding.firstLangImg.setImageResource(R.drawable.mysql_icon)
        } else if (languageRank[0].equals("자바")){
            languageRank[0]="Java"
            binding.firstLangImg.setImageResource(R.drawable.java_icon)
        } else
            binding.firstLangImg.setImageResource(R.drawable.code)
        binding.firstLang.text=languageRank[0]

        if (languageRank[1].equals("스프링부트")) {
            languageRank[1] = "Spring Boot"
            binding.secondLangImg.setImageResource(R.drawable.springboot)
        } else if (languageRank[1].equals("파이썬")) {
            languageRank[1] = "Python"
            binding.secondLangImg.setImageResource(R.drawable.python)
        } else if (languageRank[1].equals("데이터베이스")){
            languageRank[1] = "MySQL"
            binding.secondLangImg.setImageResource(R.drawable.mysql_icon)
        } else if (languageRank[1].equals("자바")){
            languageRank[1]="Java"
            binding.secondLangImg.setImageResource(R.drawable.java_icon)
        } else
            binding.secondLangImg.setImageResource(R.drawable.code)
        binding.secondLang.text=languageRank[1]

        if (languageRank[2].equals("스프링부트")) {
            languageRank[2] = "Spring Boot"
            binding.thirdLangImg.setImageResource(R.drawable.springboot)
        } else if (languageRank[2].equals("파이썬")) {
            languageRank[2] = "Python"
            binding.thirdLangImg.setImageResource(R.drawable.python)
        } else if (languageRank[2].equals("데이터베이스")){
            languageRank[2] = "MySQL"
            binding.thirdLangImg.setImageResource(R.drawable.mysql_icon)
        } else if (languageRank[2].equals("자바")){
            languageRank[2]="Java"
            binding.thirdLangImg.setImageResource(R.drawable.java_icon)
        } else
            binding.thirdLangImg.setImageResource(R.drawable.code)
        binding.thirdLang.text=languageRank[2]

        binding.mainDrawer.setNavigationItemSelectedListener {
            val intent:Intent
            when(it.title){
                "HOME" ->{

                }
                "ABOUT" ->{
                    intent=Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                }
                "PROJECT" ->{
                    intent=Intent(this, ProjectActivity::class.java)
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