package kr.ac.hallym.mobileproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.hallym.mobileproject.R
import kr.ac.hallym.mobileproject.databinding.FragmentThreeBinding
import kr.ac.hallym.mobileproject.databinding.ItemFragmentThreeBinding
import kr.ac.hallym.mobileproject.datatype.Grade

class ThreeFragment : Fragment() {
    val grade= mutableListOf<Grade>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        grade.add(Grade(semester = "2018학년도 1학기", title = "융합SW개론1", credit = 1, grade = "P"))
        grade.add(Grade(semester = "2018학년도 1학기", title = "Modern IT", credit = 1, grade = "P"))
        grade.add(Grade(semester = "2018학년도 1학기", title = "기초프로그래밍", credit = 3, grade = "A0"))
        grade.add(Grade(semester = "2018학년도 2학기", title = "창의적공학설계입문", credit = 3, grade = "A0"))
        grade.add(Grade(semester = "2018학년도 2학기", title = "창의코딩웹", credit = 3, grade = "B0"))
        grade.add(Grade(semester = "2021학년도 1학기", title = "인공지능개론", credit = 3, grade = "A0"))
        grade.add(Grade(semester = "2021학년도 1학기", title = "이산구조론", credit = 3, grade = "A0"))
        grade.add(Grade(semester = "2021학년도 1학기", title = "자바프로그래밍1", credit = 3, grade = "A+"))
        grade.add(Grade(semester = "2021학년도 1학기", title = "파이썬과학기초프로그래밍", credit = 3, grade = "A0"))
        grade.add(Grade(semester = "2021학년도 1학기", title = "소프트웨어개론", credit = 3, grade = "A0"))
        grade.add(Grade(semester = "2021학년도 1학기", title = "데이터사이언스기초", credit = 3, grade = "A+"))
        grade.add(Grade(semester = "2021학년도 2학기", title = "자바프로그래밍2", credit = 3, grade = "A+"))
        grade.add(Grade(semester = "2021학년도 2학기", title = "C프로그래밍", credit = 3, grade = "A0"))
        grade.add(Grade(semester = "2021학년도 2학기", title = "오픈소스SW의이해", credit = 3, grade = "A0"))
        grade.add(Grade(semester = "2021학년도 2학기", title = "데이터베이스기초", credit = 3, grade = "A0"))
        grade.add(Grade(semester = "2021학년도 2학기", title = "데이터분석과시각화", credit = 3, grade = "A+"))
        grade.add(Grade(semester = "2021학년도 2학기", title = "AI학습용데이터활용", credit = 3, grade = "A0"))
        grade.add(Grade(semester = "2022학년도 1학기", title = "운영체제", credit = 3, grade = "A+"))
        grade.add(Grade(semester = "2022학년도 1학기", title = "프로그래밍어론", credit = 3, grade = "A+"))
        grade.add(Grade(semester = "2022학년도 1학기", title = "영상처리프로그래밍", credit = 3, grade = "A+"))
        grade.add(Grade(semester = "2022학년도 1학기", title = "자료구조", credit = 3, grade = "A+"))
        grade.add(Grade(semester = "2022학년도 1학기", title = "데이터베이스시스템", credit = 3, grade = "A0"))
        grade.add(Grade(semester = "2022학년도 1학기", title = "소프트웨어특강1", credit = 3, grade = "A+"))

        val binding=FragmentThreeBinding.inflate(inflater, container, false)
        val layoutManager= LinearLayoutManager(activity)
        binding.recyclerview.layoutManager=layoutManager
        val adapter= FragThreeAdapter(grade)
        binding.recyclerview.adapter=adapter

        return binding.root
    }
}
class FragThreeViewHolder(val binding: ItemFragmentThreeBinding):RecyclerView.ViewHolder(binding.root)
class FragThreeAdapter(val contents:MutableList<Grade>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        FragThreeViewHolder(ItemFragmentThreeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as FragThreeViewHolder).binding
        binding.semester.text=contents[position].semester
        binding.title.text=contents[position].title
        binding.credit.text="${contents[position].credit}학점"
        binding.grade.text="성적: ${contents[position].grade}"
    }

    override fun getItemCount(): Int {
        return contents?.size?:0
    }
}