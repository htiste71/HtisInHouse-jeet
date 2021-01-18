//package com.htistelecom.htisinhouse.activity.WFMS.marketing.activity
//
//import android.os.Bundle
//import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import butterknife.BindView
//import butterknife.ButterKnife
//import butterknife.OnClick
//import com.htistelecom.htisinhouse.R
//
//class MarketingTabActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_marketing_tab)
//        inItData()
//    }
//
//    private fun inItData() {
//        tvtitle!!.text = "Your Status"
//        ivDrawer!!.visibility = View.GONE
//        tabLayout.addTab(tabLayout.newTab().setText("Office"))
//        tabLayout.addTab(tabLayout.newTab().setText("Travel"))
//        tabLayout.addTab(tabLayout.newTab().setText("Task"))
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL)
//        val adapter = MarketingTabAdapter(this, supportFragmentManager,
//                tabLayout.getTabCount())
//        viewPager.setOffscreenPageLimit(0)
//        viewPager.setAdapter(adapter)
//        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
//        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener() {
//            fun onTabSelected(tab: TabLayout.Tab) {
//                viewPager.setCurrentItem(tab.getPosition())
//            }
//
//            fun onTabUnselected(tab: TabLayout.Tab?) {}
//            fun onTabReselected(tab: TabLayout.Tab?) {}
//        })
//    }
//
//    @OnClick(R.id.ivBack)
//    fun onClick(view: View?) {
//        finish()
//    }
//}