package com.htistelecom.htisinhouse.activity.WFMS.add_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.htistelecom.htisinhouse.R
import kotlinx.android.synthetic.main.fragment_add_detail.*


class AddDetailsFragment : Fragment() {
    private val titles = arrayOf("Project", "Site", "Activity")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_detail, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tab_layout.addTab(tab_layout.newTab().setText("Project"));
        tab_layout.addTab(tab_layout.newTab().setText("Site"));
        tab_layout.addTab(tab_layout.newTab().setText("Activity"));
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
        val tabsAdapter = TabsAdapter(activity!!.supportFragmentManager, tab_layout.getTabCount())
        view_pager.setAdapter(tabsAdapter)
        view_pager.offscreenPageLimit=0
        view_pager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                view_pager.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    val pFrag = AddProjectFragment()
    val sFrag = AddSiteFragment()
    val aFrag = AddActivityFragment()
    class TabsAdapter(fm: FragmentManager?, var mNumOfTabs: Int) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int {
            return mNumOfTabs
        }

        override fun getItem(position: Int): Fragment? {

            return when (position) {
                0 -> {
                    AddProjectFragment()
                }
                1 -> {
                    AddSiteFragment()
                }
                2 -> {
                    AddActivityFragment()
                }
                else -> null
            }
        }
    }

}


