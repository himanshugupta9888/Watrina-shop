package com.watirna.shop.views.adapters

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

class  HistoryPageAdapter internal  constructor(activity:AppCompatActivity, fm:FragmentManager, fragList:Vector<Fragment>,lifecycle: Lifecycle):FragmentStateAdapter(fm,lifecycle){

    private  var fragmentList:Vector<Fragment>?=null
    private  var tabTitle:String?=""
    private  var context:Context?=null

    init {
        this.fragmentList=fragList
        this.context=context
    }

    override fun getItemCount(): Int {
        return  fragmentList!!.size
    }

    override fun createFragment(position: Int): Fragment {
        return  fragmentList!!.get(position)
    }


}