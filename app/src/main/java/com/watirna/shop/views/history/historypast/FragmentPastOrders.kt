package com.watirna.shop.views.history.historypast

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
import com.watirna.shop.databinding.FragmentPastOrderBinding
import com.watirna.shop.datamodel.HistoryDataModel
import com.watirna.shop.utils.Constants
import com.watirna.shop.utils.EndlessRecyclerViewScrollListener
import com.watirna.shop.views.adapters.HistoryCommonAdapter

class FragmentPastOrders : BaseFragment<FragmentPastOrderBinding>() {
    private lateinit var fragmentPastOrderBinding: FragmentPastOrderBinding
    private lateinit var fragmentPastViewModel: FragmentPastViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var historyCommonAdapter: HistoryCommonAdapter


    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        fragmentPastOrderBinding = mViewDataBinding as FragmentPastOrderBinding
        fragmentPastViewModel = ViewModelProviders.of(this).get(FragmentPastViewModel::class.java)
        fragmentPastOrderBinding.fragmentPastViewModel = fragmentPastViewModel
        fragmentPastOrderBinding.setLifecycleOwner(this)

        //SetRecylerView
        setRecylerView()

        //SetScrollListener
        setScrollListener()

        //getObserverValues
        //getObserVarableValues()


        //GetObserVerValues
        getObserverValues()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_past_order
    }

    fun setRecylerView() {
        linearLayoutManager = LinearLayoutManager(activity)
        fragmentPastOrderBinding.rvPastOrders.layoutManager = linearLayoutManager
    }

    fun getObserverValues() {
        fragmentPastViewModel.pastOrdersList.value!!.addOnListChangedCallback(ListObserversList())

        fragmentPastViewModel.mlProgress.observe(this, Observer {
            baseLiveDataLoading!!.value = it
        })
    }

    fun setScrollListener() {
        fragmentPastOrderBinding.rvPastOrders.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                fragmentPastViewModel.pageNumber.value = page + 1
                if (fragmentPastViewModel.pastOrdersList.value!!.size < fragmentPastViewModel.totalItem.value!!) {
                    Log.e("OnGoing", "-----")
                    fragmentPastViewModel.getPastOrderList()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (fragmentPastViewModel.pageNumber.value!! < 2) {
            fragmentPastViewModel.mlProgress.value = true
            fragmentPastViewModel.getPastOrderList()
        }
    }


    inner class ListObserversList :
        ObservableList.OnListChangedCallback<ObservableList<HistoryDataModel.ResponseData.Data>>() {
        override fun onChanged(sender: ObservableList<HistoryDataModel.ResponseData.Data>?) {
        }

        override fun onItemRangeRemoved(
            sender: ObservableList<HistoryDataModel.ResponseData.Data>?,
            positionStart: Int,
            itemCount: Int
        ) {
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
            if (fragmentPastViewModel.pageNumber.value == 1) {
                historyCommonAdapter = HistoryCommonAdapter(
                    activity!!, fragmentPastViewModel.pastOrdersList.value!!,
                    Constants.WebConstants.COMPLETED
                )
                fragmentPastOrderBinding.rvPastOrders.adapter = historyCommonAdapter
            } else {
                fragmentPastOrderBinding.rvPastOrders.adapter!!.notifyItemRangeInserted(
                    fragmentPastOrderBinding.rvPastOrders.adapter!!.itemCount,
                    fragmentPastViewModel.pastOrdersList.value!!.size
                )
            }
        }

        override fun onItemRangeChanged(
            sender: ObservableList<HistoryDataModel.ResponseData.Data>?,
            positionStart: Int,
            itemCount: Int
        ) {
        }

    }

}