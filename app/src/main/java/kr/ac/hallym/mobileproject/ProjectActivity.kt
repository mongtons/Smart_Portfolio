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
    val itemBinding by lazy {
        ItemMainBinding.inflate(layoutInflater)
    }
    val dialogBinding by lazy {
        InputDbBinding.inflate(layoutInflater)
    }
    lateinit var holder: RecyclerView.ViewHolder
    val db=DBHelper(this)
    var contents= mutableListOf<Project>()
    val requestGalleryLauncher by lazy{
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            try{
                val calRatio=calculateInSampleSize(
                    it.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )
                holder=MyViewHolder(itemBinding)

                val option= BitmapFactory.Options()
                option.inSampleSize=calRatio

                var inputStream=contentResolver.openInputStream(it.data!!.data!!)
                val bitmap=BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()
                inputStream=null

                bitmap?.let {
                    var binding=(holder as MyViewHolder).binding
                    binding.itemImage.setImageBitmap(bitmap)
                }?:let {
                    Log.d("kkang", "bitmap null")
                }
            }catch (e:Exception){
                Log.d("kkang", "bitmap null")
            }
        }
    }
    lateinit var languageMap: MutableMap<String, Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        languageMap= mutableMapOf()
        val data=db.readableDatabase
        val cursor=data.rawQuery("SELECT * FROM Project;", null)
        var i=0
        while(cursor.moveToNext()){
            contents.add(Project(++i, cursor.getString(1).toString(), cursor.getString(2).toString(),
                cursor.getString(3)))
            if(languageMap.containsKey(cursor.getString(3))){
                var num:Int=languageMap.get(cursor.getString(3))!!
                languageMap[cursor.getString(3)] = ++num
            }else{
                languageMap.put(cursor.getString(3), 1)
            }
        }

        val languageList= mutableListOf<String>()
        var first:String=""
        var firstNum:Int=0
        var second:String=""
        var secondNum:Int=0
        var third:String=""
        var thirdNum:Int=0

        for((key, value) in languageMap){
            languageList.add(key)
        }
        for(j in 0 until languageList.size){
            if(firstNum<languageMap[languageList[j]]!!) {
                firstNum = languageMap[languageList[j]]!!
                first=languageList[j]
            }
        }
        for(j in 0 until languageList.size){
            if(secondNum<languageMap[languageList[j]]!! && firstNum>languageMap[languageList[j]]!!) {
                secondNum = languageMap[languageList[j]]!!
                second=languageList[j]
            }
        }
        for(j in 0 until languageList.size){
            if(thirdNum<languageMap[languageList[j]]!! && secondNum>languageMap[languageList[j]]!!) {
                thirdNum = languageMap[languageList[j]]!!
                third=languageList[j]
            }
        }
        Log.d("kkang", "first: $first, second: $second, third: $third")

        val layoutManager= LinearLayoutManager(this)
        binding.recyclerview.layoutManager=layoutManager
        val adapter=MyAdapter(contents, this, dialogBinding, requestGalleryLauncher)
        binding.recyclerview.adapter=adapter

        binding.mainDrawer.setNavigationItemSelectedListener {
            val intent: Intent
            when(it.title){
                "HOME" ->{
                    intent= Intent(this, MainPageActivity::class.java)
                    intent.putExtra("firstLanguage", first)
                    intent.putExtra("secondLanguage", second)
                    intent.putExtra("thirdLanguage", third)

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
                        setPositiveButton("확인",DialogInterface.OnClickListener { dialogInterface, id ->
                            db.writableDatabase.execSQL("INSERT INTO Project (title, summary, category) VALUES (?, ?, ?);",
                                arrayOf(dialogBinding.addTitle.text.toString(),
                                    dialogBinding.addSummary.text.toString(),
                                    dialogBinding.category.text.toString()))
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
                    "category text"+
                    ");")
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
    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int):Int{
        val options=BitmapFactory.Options()
        options.inJustDecodeBounds=true
        try {
            var inputStream=contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream=null
        }catch (e: Exception){
            e.printStackTrace()
        }
        val (height: Int, width: Int)=options.run {
            outHeight to outWidth
        }
        var inSampleSize=1
        if(height>reqHeight || width>reqWidth){
            val halfHeight:Int=height/2
            val halfWidth:Int=width/2
            while(halfHeight/inSampleSize >= reqHeight &&
                    halfWidth/inSampleSize >= reqWidth){
                inSampleSize*=2
            }
        }
        return inSampleSize
    }
}
class MyViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)
class MyAdapter(var contents: MutableList<Project>, var context: Context,
                var dialogBinding: InputDbBinding, var requestGalleryLauncher:ActivityResultLauncher<Intent>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var db=DBHelper(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder=
        MyViewHolder((ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder).binding
        binding.itemNo.text=contents[position]!!.id.toString()
        binding.itemTitle.text=contents[position]!!.title.toString()
        binding.itemSummary.text=contents[position]!!.summary.toString()
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
                    db.writableDatabase.execSQL("UPDATE Project SET title=(?), summary=(?), category=(?) WHERE title=(?);",
                        arrayOf(dialogBinding.addTitle.text.toString(),
                            dialogBinding.addSummary.text.toString(),
                            dialogBinding.category.text.toString(),
                            contents[position].title))
                    db.close()
                    val intent=Intent(context, ProjectActivity::class.java)
                    context.startActivity(intent)
                })
                setNegativeButton("취소", null)
                show()
            }
        }
        binding.updatePicture.setOnClickListener {
            val intent=Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type="image/*"
            requestGalleryLauncher.launch(intent)
        }
    }

    override fun getItemCount(): Int {
        return contents?.size?:0
    }
}