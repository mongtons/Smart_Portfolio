package kr.ac.hallym.mobileproject

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.hallym.mobileproject.databinding.ActivityProjectBinding
import kr.ac.hallym.mobileproject.databinding.InputDbBinding
import kr.ac.hallym.mobileproject.databinding.ItemMainBinding
import kr.ac.hallym.mobileproject.datatype.Project


class ProjectActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityProjectBinding.inflate(layoutInflater)
    }
    val dialogBinding by lazy {
        InputDbBinding.inflate(layoutInflater)
    }
    val db=DBHelper(this)
    var contents= mutableListOf<Project>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val data=db.readableDatabase
        val cursor=data.rawQuery("SELECT * FROM Project;", null)
        var i=0
        while(cursor.moveToNext()){
            contents.add(Project(++i, cursor.getString(1).toString(), cursor.getString(2).toString(),
                cursor.getString(3), cursor.getString(4)))
        }

        val layoutManager= LinearLayoutManager(this)
        binding.recyclerview.layoutManager=layoutManager
        val adapter=MyAdapter(contents, this, dialogBinding)
        binding.recyclerview.adapter=adapter

        binding.mainDrawer.setNavigationItemSelectedListener {
            val intent: Intent
            when(it.title){
                "HOME" ->{
                    intent= Intent(this, MainPageActivity::class.java)
                    startActivity(intent)
                }
                "ABOUT" ->{
                    intent= Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                }
                "PROJECT" ->{
                    binding.recyclerview.smoothScrollToPosition(0)
                }
            }
            true
        }
        binding.addButton.shrink()
        binding.addButton.setOnClickListener {
            when(binding.addButton.isExtended){
                true -> {
                    binding.addButton.shrink()
                    AlertDialog.Builder(this).run {
                        setTitle("Insert Project")
                        setView(dialogBinding.root)
                        setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, id ->
                            db.writableDatabase.execSQL("INSERT INTO Project (title, summary, category, link) " +
                                    "VALUES (?, ?, ?, ?);",
                                arrayOf(dialogBinding.addTitle.text.toString(),
                                    dialogBinding.addSummary.text.toString(),
                                    dialogBinding.category.text.toString(),
                                    dialogBinding.link.text.toString()))
                            db.close()
                            finish()
                            overridePendingTransition(0, 0)
                            val intent:Intent=getIntent()
                            startActivity(intent)
                            overridePendingTransition(0,0)
                        })
                        setNegativeButton("취소", null)
                        show()
                    }
                }
                false -> binding.addButton.extend()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        return super.onSupportNavigateUp()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_projectbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =when(item.title){
        "REFRESH" -> {
            finish()
            overridePendingTransition(0, 0)
            val intent:Intent=getIntent()
            startActivity(intent)
            overridePendingTransition(0,0)
            true
        }
        "DELETE ALL" -> {
            db.writableDatabase.execSQL("DROP TABLE Project;")
            db.writableDatabase.execSQL("CREATE TABLE if not exists Project ("+
                    "id integer primary key autoincrement not null,"+
                    "title text not null,"+
                    "summary text not null,"+
                    "category text,"+
                    "link text"+
                    ");"
            )
            db.close()
            finish()
            overridePendingTransition(0, 0)
            val intent:Intent=getIntent()
            startActivity(intent)
            overridePendingTransition(0,0)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
class MyViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)
class MyAdapter(var contents: MutableList<Project>, var context: Context, var dialogBinding: InputDbBinding):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var db=DBHelper(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder=
        MyViewHolder((ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder).binding
        binding.itemNo.text=contents[position]!!.id.toString()
        binding.itemTitle.text= contents[position]!!.title
        binding.itemSummary.text= contents[position]!!.summary
        binding.category.text="카테고리: ${contents[position]!!.category.toString()}"
        when(contents[position]!!.category){
            "데이터베이스" ->{
                binding.itemImage.setImageResource(R.drawable.mysql_icon)
            }
            "스프링부트" ->{
                binding.itemImage.setImageResource(R.drawable.springboot)
            }
            "자바" ->{
                binding.itemImage.setImageResource(R.drawable.java_icon)
            }
            "파이썬" ->{
                binding.itemImage.setImageResource(R.drawable.python)
            }
            else ->{
                binding.itemImage.setImageResource(R.drawable.code)
            }
        }
        binding.deleteProject.setOnClickListener {
            db.writableDatabase.execSQL("DELETE FROM Project WHERE title=(?)",
                arrayOf(contents[position].title))
            db.close()
            contents.removeAt(position)
            notifyDataSetChanged()
            ProjectActivity().contents=contents
        }
        binding.updateProject.setOnClickListener {
            AlertDialog.Builder(context).run {
                setTitle("Update Project")
                setView(dialogBinding.root)
                setPositiveButton("확인",DialogInterface.OnClickListener { dialogInterface, id ->
                    db.writableDatabase.execSQL("UPDATE Project SET " +
                            "title=(?), summary=(?), category=(?), link=(?)" +
                            "WHERE title=(?);",
                        arrayOf(dialogBinding.addTitle.text.toString(),
                            dialogBinding.addSummary.text.toString(),
                            dialogBinding.category.text.toString(),
                            dialogBinding.link.text.toString(),
                            contents[position].title
                        )
                    )
                    db.close()
                    val intent=Intent(context, ProjectActivity::class.java)
                    context.startActivity(intent)
                })
                setNegativeButton("취소", null)
                show()
            }
        }
        binding.itemRoot.setOnClickListener {
            Log.d("kkang", "click item: $position")
            if (contents[position].link!=null){
                val intent=Intent(android.content.Intent.ACTION_VIEW)
                intent.data=Uri.parse(contents[position].link)
                context.startActivity(intent)
            }else
                Toast.makeText(context, "해당 프로젝트의 링크가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return contents?.size
    }
}