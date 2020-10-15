package com.htistelecom.htisinhouse.activity.WFMS.adapters

import android.content.Intent
import android.graphics.Typeface
import androidx.fragment.app.FragmentActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.htistelecom.htisinhouse.R

import com.htistelecom.htisinhouse.activity.WFMS.models.SalaryModel
import kotlinx.android.synthetic.main.row_salary_adapter_wfms.view.*

class SalaryAdapterWFMS(private val mContext: FragmentActivity?, val salaryList: ArrayList<SalaryModel>) : RecyclerView.Adapter<SalaryAdapterWFMS.MyHolder>() {
    var isDFirst = true
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyHolder {
        val view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_salary_adapter_wfms, viewGroup, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return salaryList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val model = salaryList.get(position)
        if (model.ctcType.equals("D", true)) {
            holder.bind(mContext, salaryList.get(position), position, isDFirst)

            isDFirst = false

        } else {
            holder.bind(mContext, salaryList.get(position), position, isDFirst)

        }
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mContext: FragmentActivity?, model: SalaryModel, pos: Int, isDFirst: Boolean) {
            if (model.ctcType.equals("A", true)) {
                if (pos == 0) {
                    itemView.tvHeadingSalaryAdapterWFMS.visibility = View.VISIBLE

                    itemView.tvHeadingSalaryAdapterWFMS.text = "Earnings"


                } else {
                    if (model.charges.equals("Allowances") || model.equals("Net Payable")) {
                        itemView.txtHeadingSalaryAdapterWFMS.setTypeface(itemView.txtHeadingSalaryAdapterWFMS.getTypeface(), Typeface.BOLD)
                        itemView.tvTextSalaryAdapterWFMS.setTypeface(itemView.txtHeadingSalaryAdapterWFMS.getTypeface(), Typeface.BOLD)

                    }
                    itemView.tvHeadingSalaryAdapterWFMS.visibility = View.GONE

                }
                itemView.txtHeadingSalaryAdapterWFMS.text = model.charges
                itemView.tvTextSalaryAdapterWFMS.text = model.chargeAmount.toString()

            } else {

                if (isDFirst) {


                    itemView.tvHeadingSalaryAdapterWFMS.visibility = View.VISIBLE

                    itemView.tvHeadingSalaryAdapterWFMS.text = "Deductions"
                } else {

                    itemView.tvHeadingSalaryAdapterWFMS.visibility = View.GONE
                }
                if (model.charges.equals("Deductions", true) || model.charges.equals("Net Payable")) {
                    itemView.txtHeadingSalaryAdapterWFMS.setTypeface(itemView.txtHeadingSalaryAdapterWFMS.getTypeface(), Typeface.BOLD)
                    itemView.tvTextSalaryAdapterWFMS.setTypeface(itemView.txtHeadingSalaryAdapterWFMS.getTypeface(), Typeface.BOLD)

                }



                itemView.txtHeadingSalaryAdapterWFMS.text = model.charges
                itemView.tvTextSalaryAdapterWFMS.text = model.chargeAmount.toString()


            }


        }
    }


}
