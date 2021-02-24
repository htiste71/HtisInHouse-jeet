package com.htistelecom.htisinhouse.activity.WFMS.add_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.htistelecom.htisinhouse.R
import kotlinx.android.synthetic.main.dialog_marketing.*
import kotlinx.android.synthetic.main.fragment_add_detail.*
import kotlinx.android.synthetic.main.fragment_add_detail.tabLayout


class AddDetailsFragment : Fragment() {
    private val titles = arrayOf("Project", "Site", "Activity")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_detail, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout.setBackground(ContextCompat.getDrawable(activity!!, R.drawable.tab_color_selector));

        tabLayout.addTab(tabLayout.newTab().setText("Project"));
        tabLayout.addTab(tabLayout.newTab().setText("Site"));
        tabLayout.addTab(tabLayout.newTab().setText("Activity"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        val tabsAdapter =TabsAdapter(childFragmentManager, tabLayout.getTabCount())
        view_pager.setAdapter(tabsAdapter)
        view_pager.offscreenPageLimit = 0
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                view_pager.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })









    }


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


