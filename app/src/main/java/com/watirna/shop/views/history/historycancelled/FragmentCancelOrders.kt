package com.watirna.shop.views.history.historycancelled

import android.util.Log
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseFragment
import com.watirna.shop.databinding.FragmentCancelledOrderBinding
import com.watirna.shop.datamodel.HistoryDataModel
import com.watirna.shop.utils.Constants
import com.watirna.shop.utils.EndlessRecyclerViewScrollListener
import com.watirna.shop.views.adapters.HistoryCommonAdapter

class FragmentCancelOrders : BaseFragment<FragmentCancelledOrderBinding>() {
    private lateinit var fragmentCancelledOrderBinding: FragmentCancelledOrderBinding
    private lateinit var fragmentCancelViewModel: FragmentCancelViewModel
    private lateinit var linearLayouManager: LinearLayoutManager
    private lateinit var historyCancelAdapter: HistoryCommonAdapter
    private var totalPages: Int? = 0


    init {
        AppController.appComponent.inject(this)
    }

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        fragmentCancelledOrderBinding = mViewDataBinding as FragmentCancelledOrderBinding
        fragmentCancelViewModel =
            ViewModelProviders.of(this).get(FragmentCancelViewModel::class.java)
        Log.e("LifeCycle", "-------" + "oncreateView")
        //Set RecylerView
        setRecylerView()
        //Add ScrollListener
        addScrollListner()
        getObservarValues()
    }

    fun setRecylerView() {
        linearLayouManager = LinearLayoutManager(activity)
        fragmentCancelledOrderBinding.rvCancelOrders.layoutManager = linearLayouManager
    }

    fun addScrollListner() {
        fragmentCancelledOrderBinding.rvCancelOrders.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(linearLayouManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                fragmentCancelViewModel.pageNumber.value = page + 1
                if (fragmentCancelViewModel.cancelList.value!!.size < fragmentCancelViewModel.totalItem.value!!)
                    fragmentCancelViewModel.getCancelledOrderList()
            }

        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_cancelled_order
    }

    fun getObservarValues() {
        fragmentCancelViewModel.cancelList.value!!.addOnListChangedCallback(ListChangeListener())

        fragmentCancelViewModel.loadingProgress.observe(this, Observer {
            baseLiveDataLoading!!.postValue(it)
        })
    }

    override fun onResume() {
        super.onResume()
        if (fragmentCancelViewModel.pageNumber.value!! < 2) {
            fragmentCancelViewModel.loadingProgress.value = true
            fragmentCancelViewModel.getCancelledOrderList()
        }
    }

    override fun onPause() {
        super.onPause()
    }


//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        if (isVisibleToUser) {
//            if (fragmentCancelViewModel.pageNumber.value!! == 1)
//                fragmentCancelViewModel.loadingProgress.postValue(true)
//            fragmentCancelViewModel.getCancelledOrderList()
//        }
//    }

    inner class ListChangeListener :
        ObservableList.OnListChangedCallback<ObservableArrayList<HistoryDataModel.ResponseData.Data>>() {
        override fun onChanged(sender: ObservableArrayList<HistoryDataModel.ResponseData.Data>?) {
        }

        override fun onItemRangeRemoved(
            sender: ObservableArrayList<HistoryDataModel.ResponseData.Data>?,
            positionStart: Int,
            itemCount: Int
        ) {
            Log.e("list", "------------" + "Removed")
        }

        override fun onItemRangeMoved(
            sender: ObservableArrayList<HistoryDataModel.ResponseData.Data>?,
            fromPosition: Int,
            toPosition: Int,
            itemCount: Int
        ) {
            Log.e("list", "------------" + "Moved" + fromPosition + toPosition + itemCount)

        }

        override fun onItemRangeInserted(
            sender: ObservableArrayList<HistoryDataModel.ResponseData.Data>?,
            positionStart: Int,
            itemCount: Int
        ) {
            Log.e("list", "------------" + "Inserted" + positionStart + itemCount)
            if (fragmentCancelViewModel.pageNumber.value == 1) {
                historyCancelAdapter =
                    HistoryCommonAdapter(
                        activity!!,
                        fragmentCancelViewModel.cancelList.value!!,
                        Constants.WebConstants.CANCELLED
                    )
                fragmentCancelledOrderBinding.rvCancelOrders.adapter = historyCancelAdapter
                fragmentCancelledOrderBinding.rvCancelOrders.adapter!!.notifyDataSetChanged()

            } else {
                fragmentCancelledOrderBinding.rvCancelOrders.adapter!!.notifyItemRangeChanged(
                    fragmentCancelledOrderBinding.rvCancelOrders.adapter!!.itemCount,
                    fragmentCancelViewModel.cancelList.value!!.size - 1
                )
            }

        }

        override fun onItemRangeChanged(
            sender: ObservableArrayList<HistoryDataModel.ResponseData.Data>?,
            positionStart: Int,
            itemCount: Int
        ) {

        }

    }

}