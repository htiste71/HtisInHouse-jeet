//package com.htistelecom.htisinhouse.activity.WFMS.add_details.tabs
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.htistelecom.htisinhouse.R
//import com.htistelecom.htisinhouse.activity.WFMS.add_details.DetailViewModel
//
//class ProjectFragment :Fragment{
//    constructor() : super()
//
//companion object
//{
//    fun newInstance(): ProjectFragment? {
//        return ProjectFragment()
//    }
//}
//
//
//    lateinit var vm: DetailViewModel
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_project, null)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        vm = ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)
//    }
//
//    fun hitAPINow() {
//        TODO("Not yet implemented")
//    }
//}