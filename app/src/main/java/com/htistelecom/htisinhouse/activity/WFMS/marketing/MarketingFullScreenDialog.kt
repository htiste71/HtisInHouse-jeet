package com.htistelecom.htisinhouse.activity.WFMS.marketing;

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.activity.MainActivityNavigation

import com.htistelecom.htisinhouse.activity.WFMS.marketing.fragments.OfficeFragment
import com.htistelecom.htisinhouse.activity.WFMS.marketing.fragments.TaskFragment
import com.htistelecom.htisinhouse.activity.WFMS.marketing.fragments.TravelFragment
import kotlinx.android.synthetic.main.dialog_marketing.*
import kotlinx.android.synthetic.main.toolbar_marketing.*


class MarketingFullScreenDialog : DialogFragment() {
var empId:String=""
    var isFromHome:Boolean=false

    companion object {
        fun newInstance(userId: String,isHome:Boolean): MarketingFullScreenDialog? {
            var fragment=MarketingFullScreenDialog()
            val args = Bundle()
            args.putString("id", userId)
            args.putBoolean("fromHome", isHome)
            fragment.setArguments(args)
            return fragment
        }
    }


    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_marketing, null)
        empId= getArguments()!!.getString("id").toString();
        isFromHome=getArguments()!!.getBoolean("fromHome")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTitleDialog.text = "Leads"
        ivCloseDialog.setOnClickListener {
            dismiss()
            if(isFromHome)
            startActivity(Intent(activity, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Home"))
            else
                startActivity(Intent(activity, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Task"))


        }
        tabLayout.setBackground(ContextCompat.getDrawable(activity!!, R.drawable.tab_color_selector));

        tabLayout.addTab(tabLayout.newTab().setText("Office"));
        tabLayout.addTab(tabLayout.newTab().setText("Travel"));
        tabLayout.addTab(tabLayout.newTab().setText("Task"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        val tabsAdapter = TabsAdapter(childFragmentManager, tabLayout.getTabCount(),empId)
        viewPager.setAdapter(tabsAdapter)
        viewPager.offscreenPageLimit = 0
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    class TabsAdapter(fm: FragmentManager?, var mNumOfTabs: Int,var empId: String) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int {
            return mNumOfTabs
        }

        override fun getItem(position: Int): Fragment? {

            return when (position) {
                0 -> {
                    OfficeFragment()
                }
                1 -> {
                    TravelFragment()
                }
                2 -> {
                    TaskFragment(empId)
                }
                else -> null
            }
        }
    }
}


