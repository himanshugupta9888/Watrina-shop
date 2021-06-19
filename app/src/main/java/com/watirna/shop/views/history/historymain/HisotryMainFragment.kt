package com.watirna.shop.views.history.historymain

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TabWidget
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayoutMediator
import com.watirna.shop.R
import com.watirna.shop.base.BaseFragment
import com.watirna.shop.databinding.FragmentHisotryBinding
import com.watirna.shop.views.adapters.HistoryPageAdapter
import com.watirna.shop.views.dashboard.DashboardNavigator
import com.watirna.shop.views.history.historycancelled.FragmentCancelOrders
import com.watirna.shop.views.history.historyongoing.FragmentOngoingOrders
import com.watirna.shop.views.history.historypast.FragmentPastOrders
import java.util.*

class  HisotryMainFragment:BaseFragment<FragmentHisotryBinding>(){
    private  lateinit var  fragmentHisotryBinding: FragmentHisotryBinding
    private  lateinit var  historyMainViewModel: HistoryMainViewModel
    private  var tabWidget:TabWidget?=null
    private lateinit var dashBoardNavigator: DashboardNavigator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardNavigator = context as DashboardNavigator
        dashBoardNavigator.hideRightIcon(true)
    }

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        fragmentHisotryBinding=mViewDataBinding as FragmentHisotryBinding
        historyMainViewModel=ViewModelProviders.of(this).get(HistoryMainViewModel::class.java)
        fragmentHisotryBinding.historyMainModel=historyMainViewModel
        fragmentHisotryBinding.setLifecycleOwner(this)
        fragmentHisotryBinding.tbHistory.setTabTextColors(ContextCompat.getColor(activity!!,R.color.black),ContextCompat.getColor(activity!!,R.color.tab_history_selected_text))

        //Setup Pager
        setupViewPager()

        //Set tab item margin
        setTabMargin()

        dashBoardNavigator.setTitle(resources.getString(R.string.title_history))
    }

    fun setTabMargin(){
        for(i in 0 until  fragmentHisotryBinding.tbHistory.tabCount){
            val tabViewGroup=fragmentHisotryBinding.tbHistory.getChildAt(0) as ViewGroup
            val tabItem=tabViewGroup.getChildAt(i)
            val layoutParam:ViewGroup.MarginLayoutParams=tabItem.layoutParams as ViewGroup.MarginLayoutParams
            if(i==fragmentHisotryBinding.tbHistory.tabCount-1)
            layoutParam.setMargins(15,0,0,0)
            else
             layoutParam.setMargins(15,0,0,0)
            fragmentHisotryBinding.tbHistory.requestLayout()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_hisotry
    }

    fun setupViewPager(){
        val hisotryList=Vector<Fragment>()
        hisotryList.add(FragmentOngoingOrders())
        hisotryList.add(FragmentPastOrders())
        hisotryList.add(FragmentCancelOrders())

        //Set Pager Adapter
        val historyAdapter=HistoryPageAdapter(activity!! as AppCompatActivity,childFragmentManager,hisotryList,viewLifecycleOwner.lifecycle)
        fragmentHisotryBinding.vpHisotry.adapter=historyAdapter

        TabLayoutMediator(fragmentHisotryBinding.tbHistory,fragmentHisotryBinding.vpHisotry){tab, position ->
            when (position){
                0 -> {tab.text=context!!.resources.getString(R.string.tab_title_ongoing)}
                1 -> {tab.text= context!!.resources.getString(R.string.tab_title_past)}
                2 -> {tab.text= context!!.resources.getString(R.string.tab_title_cancel)}
            }
        }.attach()

    }

}