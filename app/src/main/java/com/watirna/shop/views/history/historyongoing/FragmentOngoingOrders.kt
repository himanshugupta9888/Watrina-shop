package com.watirna.shop.views.history.historyongoing

import android.util.Log
import android.view.View
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.watirna.shop.R
import com.watirna.shop.base.BaseFragment
import com.watirna.shop.databinding.FragmentOngoingOrderBinding
import com.watirna.shop.datamodel.HistoryDataModel
import com.watirna.shop.utils.Constants
import com.watirna.shop.utils.EndlessRecyclerViewScrollListener
import com.watirna.shop.views.adapters.HistoryCommonAdapter

class FragmentOngoingOrders:BaseFragment<FragmentOngoingOrderBinding>(){
    private lateinit var fragmentOngoingOrderBinding: FragmentOngoingOrderBinding
    private  lateinit var fragmentOngoinViewModel:FragmentOngoingViewModel
    private  lateinit var  linearLayoutManager: LinearLayoutManager
    private  lateinit var  historyCommonAdapter: HistoryCommonAdapter

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        fragmentOngoingOrderBinding=mViewDataBinding as FragmentOngoingOrderBinding
        fragmentOngoinViewModel=ViewModelProviders.of(this).get(FragmentOngoingViewModel::class.java)

        //SetRecylerView
        setRecylerView()

        //SetScrollListener
        setScrollListener()

        //GetObserVerValues
        getObserverValues()
    }

    fun  setRecylerView (){
        linearLayoutManager= LinearLayoutManager(activity)
        fragmentOngoingOrderBinding.rvOnGoingOrders.layoutManager=linearLayoutManager
    }

    fun setScrollListener(){
        fragmentOngoingOrderBinding.rvOnGoingOrders.addOnScrollListener(object:EndlessRecyclerViewScrollListener(linearLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if(fragmentOngoinViewModel.ongoingList.value!!.size < fragmentOngoinViewModel.totalItem.value!!) {
                    fragmentOngoinViewModel.pageNumber.value = page + 1
                    fragmentOngoinViewModel.limit.value = fragmentOngoinViewModel.limit.value?.plus(50)
                    Log.e("OnGoing","----- page "+fragmentOngoinViewModel.limit.value)
                    fragmentOngoinViewModel.getOngoingOrder()
                }
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_ongoing_order
    }

    fun getObserverValues (){
        fragmentOngoinViewModel.ongoingList.value!!.addOnListChangedCallback(ListObserVerListener())

        fragmentOngoinViewModel.mLoadingPrgress.observe(this, Observer {
            baseLiveDataLoading!!.postValue(it)
        })
    }

    override fun onResume() {
        super.onResume()
//        if(fragmentOngoinViewModel.pageNumber.value!! < 2) {
            Log.e("OnGoing","-----")
            fragmentOngoinViewModel.ongoingList.value!!.clear()
            fragmentOngoinViewModel.mLoadingPrgress.value=true
            fragmentOngoinViewModel.getOngoingOrder()
            fragmentOngoinViewModel.ongoingList.value!!.clear()
//        }
    }

    override fun onPause() {
        super.onPause()
    }

    inner class  ListObserVerListener:ObservableList.OnListChangedCallback<ObservableList<HistoryDataModel.ResponseData.Data>>(){
        override fun onChanged(sender: ObservableList<HistoryDataModel.ResponseData.Data>?) {
            fragmentOngoingOrderBinding.rvOnGoingOrders.adapter!!.notifyItemChanged(fragmentOngoingOrderBinding.rvOnGoingOrders.adapter!!.itemCount)
        }

        override fun onItemRangeRemoved(
            sender: ObservableList<HistoryDataModel.ResponseData.Data>?,
            positionStart: Int,
            itemCount: Int
        ) {
            fragmentOngoingOrderBinding.rvOnGoingOrders.adapter!!.notifyDataSetChanged()
        }



        override fun onItemRangeMoved(
            sender: ObservableList<HistoryDataModel.ResponseData.Data>?,
            fromPosition: Int,
            toPosition: Int,
            itemCount: Int
        ) {
        }

        override fun onItemRangeInserted(
            sender: ObservableList<HistoryDataModel.ResponseData.Data>?,
            positionStart: Int,
            itemCount: Int
        ) {
            if(fragmentOngoinViewModel.pageNumber.value==1){
                historyCommonAdapter= HistoryCommonAdapter(activity!!,fragmentOngoinViewModel.ongoingList.value!!,Constants.WebConstants.ONGOING)
                fragmentOngoingOrderBinding.rvOnGoingOrders.adapter=historyCommonAdapter
            }else{
                fragmentOngoingOrderBinding.rvOnGoingOrders.adapter!!.notifyItemRangeInserted(fragmentOngoingOrderBinding.rvOnGoingOrders.adapter!!.itemCount,fragmentOngoinViewModel.ongoingList.value!!.size)
            }
        }

        override fun onItemRangeChanged(
            sender: ObservableList<HistoryDataModel.ResponseData.Data>?,
            positionStart: Int,
            itemCount: Int
        ) {
            fragmentOngoingOrderBinding.rvOnGoingOrders.adapter!!.notifyItemRangeChanged(fragmentOngoingOrderBinding.rvOnGoingOrders.adapter!!.itemCount,fragmentOngoinViewModel.ongoingList.value!!.size)
        }

    }

}