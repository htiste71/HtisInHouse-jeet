package com.htistelecom.htisinhouse.activity.WFMS.marketing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.marketing.fragments.OfficeFragment
import com.htistelecom.htisinhouse.activity.WFMS.marketing.fragments.TaskFragment
import com.htistelecom.htisinhouse.activity.WFMS.marketing.fragments.TravelFragment

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_marketing)
//        tabLayout.addTab(tabLayout.newTab().setText("Office"));
//        tabLayout.addTab(tabLayout.newTab().setText("Travel"));
//        tabLayout.addTab(tabLayout.newTab().setText("Task"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        val tabsAdapter = TabsAdapter(supportFragmentManager, tabLayout.getTabCount())
//        viewPager.setAdapter(tabsAdapter)
//        viewPager.offscreenPageLimit = 0
//        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
//        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                viewPager.setCurrentItem(tab.position)
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })

//        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
//        val newFragment: MarketingFullScreenDialog? = MarketingFullScreenDialog.newInstance(tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
//        newFragment!!.show(ft, "dialog")

    }
    class TabsAdapter(fm: FragmentManager?, var mNumOfTabs: Int) : FragmentStatePagerAdapter(fm!!) {
        override fun getCount(): Int {
            return mNumOfTabs
        }

        override fun getItem(position: Int): Fragment {

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
                else -> TravelFragment()
            }
        }
    }
}