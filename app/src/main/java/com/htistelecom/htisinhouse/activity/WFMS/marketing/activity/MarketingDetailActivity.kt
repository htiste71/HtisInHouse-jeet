package com.htistelecom.htisinhouse.activity.WFMS.marketing.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.adapter.MarketingTaskAdapter
import kotlinx.android.synthetic.main.activity_marketing_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class MarketingDetailActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketing_detail)
        tv_title.setText("Task Detail")
        ivDrawer.setVisibility(View.GONE)
        ivBack.setOnClickListener { finish() }



        setData()
    }

    private fun setData() {
        tvCompanyName.text =" : "+ MarketingTaskAdapter.data.company_name
        tvPersonName.text =" : "+ MarketingTaskAdapter.data.employee_name
        tvPosition.text =" : "+ MarketingTaskAdapter.data.position_name
        tvWebsiteLink.text =" : "+ MarketingTaskAdapter.data.web_link
        tvReachTime.text = " : "+MarketingTaskAdapter.data.cust_reach_time
        tvOutTime.text =" : "+ MarketingTaskAdapter.data.cust_out_time
        tvEntityType.text =" : "+ MarketingTaskAdapter.data.entity_name
        tvNatureOfBusiness.text =" : "+ MarketingTaskAdapter.data.business_nature
        tvCompanyOwnerName.text =" : "+ MarketingTaskAdapter.data.company_owner
        tvCompanyOwnerContact.text =" : "+ MarketingTaskAdapter.data.owner_contact
        tvTurnover.text = " : "+MarketingTaskAdapter.data.annual_turnover
        tvLeadGenerated.text =" : "+MarketingTaskAdapter.data.lead_generated
        tvEmail.text = " : "+MarketingTaskAdapter.data.email_id
        tvMobileNumber.text = " : "+MarketingTaskAdapter.data.phone
        tvAddress.text =" : "+ MarketingTaskAdapter.data.address
        tvState.text = " : "+MarketingTaskAdapter.data.state_name
        tvCity.text = " : "+MarketingTaskAdapter.data.city_name
        tvZipCode.text =" : "+ MarketingTaskAdapter.data.zip_code


    }
}