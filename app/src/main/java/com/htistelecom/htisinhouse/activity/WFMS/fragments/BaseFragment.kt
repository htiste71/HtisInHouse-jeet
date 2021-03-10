package com.htistelecom.htisinhouse.fragment

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment


open class BaseFragment : Fragment() {


    protected open fun resetTitles() {
        try {
            val info: ActivityInfo = activity!!.getPackageManager().getActivityInfo(activity!!.getComponentName(), PackageManager.GET_META_DATA)
            if (info.labelRes != 0) {
                activity!!.setTitle(info.labelRes)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

}