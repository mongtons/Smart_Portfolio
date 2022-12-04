package kr.ac.hallym.mobileproject.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.hallym.mobileproject.databinding.FragmentOneBinding
import kr.ac.hallym.mobileproject.databinding.ItemFragmentOneBinding
import kr.ac.hallym.mobileproject.datatype.Career

class OneFragment : Fragment() {
    val career=mutableListOf<Career>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        career.add(Career(year = "2018년 2월", activity = "별내고등학교 졸업"))
        career.add(Career(year="2018년 3월", activity = "한림대학교 입학"))
        career.add(Career(year="2018학년도", activity = "전자공학과 1학년 과대표"))
        career.add(Career(year="2018학년도", activity = "제 11대 전자공학과 학생회 [Mate]\n복지부원 활동"))
        career.add(Career(year="2018년 3월", activity = "전자공학과 축구동아리 [Defy] 선수 활동"))
        career.add(Career(year="2018년 3월", activity = "전자공학과 학술동아리 [Multiplex] 부원 활동"))
        career.add(Career(year = "2020년 8월", activity = "육군 제 25보병사단 병장 만기 전역"))
        career.add(Career(year="2021학년도", activity = "제 3대 빅데이터전공 학생회 [Plus]\n대외국장 활동"))
        career.add(Career(year="2021년 1학기", activity = "2학년 1학기 학기우등"))
        career.add(Career(year="2021년 1학기", activity = "SW 인재장학금 1"))
        career.add(Career(year="2021년 8월", activity = "[Codeit] 대학생 코딩 캠프 7기\n프로그래밍 기초 in Python 수료"))
        career.add(Career(year="2021년 8월", activity = "[Codeit] 대학생 코딩 캠프 7기\n개발자를 위한 SQL 데이터베이스 수료"))
        career.add(Career(year="2021년 2학기", activity = "2학년 2학기 학기우등"))
        career.add(Career(year="2021년 2학기", activity = "SW 인재장학금 1"))
        career.add(Career(year="2022학년도", activity = "제 4대 빅데이터전공 학생회 [Ra-On]\n부학회장 활동"))
        career.add(Career(year="2022년 1학기", activity = "3학년 1학기 학기우등"))
        career.add(Career(year="2022년 1학기", activity = "SW 인재장학금 1"))
        career.add(Career(year="2022년 8월", activity = "학술동아리 [Multiplex] 자바프로그래밍\n멘토링 멘토 활동"))
        career.add(Career(year="2022년 하계계절학기", activity = "SW 멘토링 장학금"))
        career.add(Career(year="2022년 10월", activity = "교내 공모전 [SW WEEK] 웹개발 해커톤 참가"))
        career.add(Career(year="2022년 11월", activity = "교내 학술제 [서공제] 본선 진출"))

        val binding = FragmentOneBinding.inflate(inflater, container, false)
        val layoutManager=LinearLayoutManager(activity)
        binding.recyclerviewOne.layoutManager=layoutManager
        val adapter= FragOneAdapter(career)
        binding.recyclerviewOne.adapter=adapter

        return binding.root
    }
}
class FragOneViewHolder(val binding: ItemFragmentOneBinding):RecyclerView.ViewHolder(binding.root)
class FragOneAdapter(val contents: MutableList<Career>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder=
        FragOneViewHolder(ItemFragmentOneBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as FragOneViewHolder).binding
        binding.itemYear.text="${contents[position].year}"
        binding.itemCareer.text="${contents[position].activity}"
    }

    override fun getItemCount(): Int {
        return contents.size
    }
}