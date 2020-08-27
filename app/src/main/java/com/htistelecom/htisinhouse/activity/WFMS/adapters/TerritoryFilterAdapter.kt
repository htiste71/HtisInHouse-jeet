package com.htistelecom.htisinhouse.activity.WFMS.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.activity.FilterActivity
import kotlinx.android.synthetic.main.row_territory_filter_adapter.view.*


class TerritoryFilterAdapter(private val mContext: FilterActivity) : RecyclerView.Adapter<TerritoryFilterAdapter.MyHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyHolder {
        val view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_territory_filter_adapter, viewGroup, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
          holder.bind(mContext)
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: Context) {
            addView(mContext, itemView.llAddressRowTerritoryFilterAdapter)

        }

        fun addView(mContext: Context, llAddressTerritoryFilterAdapter: LinearLayout) {
            val inflater = LayoutInflater.from(mContext);

            val view = inflater.inflate(R.layout.layout_address_territory_filter_adapter, null);

            llAddressTerritoryFilterAdapter.addView(view);
        }


    }


}