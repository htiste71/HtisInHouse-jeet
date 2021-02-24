package com.htistelecom.htisinhouse.activity.WFMS.marketing.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.htistelecom.htisinhouse.activity.WFMS.marketing.fragments.OfficeFragment
import com.htistelecom.htisinhouse.activity.WFMS.marketing.fragments.TravelFragment
import com.htistelecom.htisinhouse.activity.WFMS.marketing.fragments.TaskFragment

class MarketingTabAdapter(var context: Context, fm: FragmentManager?, var totalTabs: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> {
                OfficeFragment()
            }
            1 -> {
                TravelFragment()
            }
            2 -> {
                TaskFragment("12")
            }
            else -> null
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}