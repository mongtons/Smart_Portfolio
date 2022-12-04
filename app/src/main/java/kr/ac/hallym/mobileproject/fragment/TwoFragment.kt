package kr.ac.hallym.mobileproject.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.hallym.mobileproject.R
import kr.ac.hallym.mobileproject.databinding.FragmentTwoBinding
import kr.ac.hallym.mobileproject.databinding.ItemFragmentTwoBinding
import kr.ac.hallym.mobileproject.datatype.License

class TwoFragment : Fragment() {
    val license= mutableListOf<License>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        license.add(License(img = R.drawable.adsp, title = "ADsP", subTitle = "데이터 분석 준전문가", num = "ADsP-032002701", date = "2022월 3월 25일 취득"))
        license.add(License(img = R.drawable.mos_excel, title = "MOS Excel 2016 Expert", subTitle = null, num = "wYKKR-22Xy", date = "2022년 6월 29일 취득"))
        license.add(License(img = R.drawable.mos_word, title = "MOS Word 2016 Expert", subTitle = null, num = "wYWHw-22XU", date = "2022년 7월 7일 취득"))
        license.add(License(img = R.drawable.sqld, title = "SQLD", subTitle = "SQL 개발자", num = "SQLD-046012817", date = "2022년 9월 30일 취득"))

        val binding= FragmentTwoBinding.inflate(inflater, container, false)
        val layoutManager=LinearLayoutManager(activity)
        binding.recyclerviewTwo.layoutManager=layoutManager
        val adapter=FragTwoAdapter(license)
        binding.recyclerviewTwo.adapter=adapter

        return binding.root
    }
}
class FragTwoViewHolder(val binding: ItemFragmentTwoBinding): RecyclerView.ViewHolder(binding.root)
class FragTwoAdapter(val contents: MutableList<License>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder=
        FragTwoViewHolder(ItemFragmentTwoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as FragTwoViewHolder).binding
        binding.itemImage.setImageResource(contents!![position].img)
        binding.itemTitle.text="${contents!![position].title}"
        binding.itemSubtitle.text="${(contents!![position].subTitle)?:""}"
        binding.itemNum.text="${contents!![position].num}"
        binding.itemDate.text="${contents!![position].date}"
    }

    override fun getItemCount(): Int {
        return contents?.size?:0
    }
}