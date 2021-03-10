package com.htistelecom.htisinhouse.activity.WFMS.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.activity.FilterActivity
import com.htistelecom.htisinhouse.activity.WFMS.models.BranchListModel
import kotlinx.android.synthetic.main.layout_address_territory_filter_adapter.view.*
import kotlinx.android.synthetic.main.row_branch_filter_adapter.view.*


class BranchFilterAdapter(private val mContext: FilterActivity) : RecyclerView.Adapter<BranchFilterAdapter.MyHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyHolder {
        val view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_branch_filter_adapter, viewGroup, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return FilterActivity.branchList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(mContext, FilterActivity.branchList.get(position), position, FilterActivity.branchList)
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: FilterActivity, model: BranchListModel, position: Int, branchList: ArrayList<BranchListModel>) {
            if (position == 0) {
                itemView.rlOuterRowBranchFilterAdapter.visibility = View.VISIBLE
            } else {
                itemView.rlOuterRowBranchFilterAdapter.visibility = View.GONE

            }

            itemView.cbBranchRowBranchFilterAdapter.setOnCheckedChangeListener { buttonView, isChecked ->

                if (isChecked) {
                    for (i in 0 until branchList.size) {
                        branchList.get(i).isChecked = true
                        branchList.get(i).isAllChecked = true
                    }

                } else {
                    for (i in 0 until branchList.size) {
                        branchList.get(i).isChecked = false
                        branchList.get(i).isAllChecked = false

                    }
                }
                mContext.notifyMethodBranch()

            }
            itemView.cbAddressBranchFilterAdapter.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    branchList.get(position).isChecked = true
                } else {
                    branchList.get(position).isChecked = false
                }
            }
//            if (position==0 && model.isAllChecked) {
//                itemView.cbBranchRowBranchFilterAdapter.isChecked = true
//            }
//            else{
//                itemView.cbBranchRowBranchFilterAdapter.isChecked = false
//
//            }

            if (model.isChecked) {
                itemView.cbAddressBranchFilterAdapter.isChecked = true
            } else {
                itemView.cbAddressBranchFilterAdapter.isChecked = false

            }
            itemView.cbAddressBranchFilterAdapter.text = model.branchAddress
            //  addView(mContext, itemView.llAddressRowBranchFilterAdapter)

        }

        fun addView(mContext: Context, llAddressBranchFilterAdapter: LinearLayout) {
            val inflater = LayoutInflater.from(mContext);

            val view = inflater.inflate(R.layout.layout_address_territory_filter_adapter, null);

            llAddressBranchFilterAdapter.addView(view);
        }


    }

    fun getBranchId(): String {
        var id = ""
        for (i in 0 until FilterActivity.branchList.size) {

            if (FilterActivity.branchList.get(i).isChecked) {
                id = id + "," + FilterActivity.branchList.get(i).branchId
            }
        }
        id = id.drop(1)
        return id
    }

}