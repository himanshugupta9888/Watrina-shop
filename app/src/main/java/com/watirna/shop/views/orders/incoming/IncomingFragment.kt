package com.watirna.shop.views.orders.incoming

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.base.BaseFragment
import com.watirna.shop.databinding.FragmentIncomingOrdersBinding
import com.watirna.shop.datamodel.NewOrderModel
import com.watirna.shop.socket.SocketManager
import com.watirna.shop.utils.Constants
import com.watirna.shop.utils.SessionManager
import com.watirna.shop.views.adapters.IncomingOrderAdapter
import com.watirna.shop.views.dashboard.DashboardNavigator
import com.watirna.shop.views.login.LoginActivity
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.*
import javax.inject.Inject

class IncomingFragment : BaseFragment<FragmentIncomingOrdersBinding>(), IncomingNavigator {
    private lateinit var fragmentIncomingOrdersBinding: FragmentIncomingOrdersBinding
    private lateinit var incomingViewModel: IncomingViewModel
    private lateinit var dashboardNavigator: DashboardNavigator
    private lateinit var ctx: Context

    @Inject
    lateinit var sessionManager: SessionManager

    override fun getLayoutId(): Int {
        return R.layout.fragment_incoming_orders
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashboardNavigator = context as DashboardNavigator
    }

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        fragmentIncomingOrdersBinding = mViewDataBinding as FragmentIncomingOrdersBinding
        incomingViewModel = ViewModelProviders.of(this).get(IncomingViewModel::class.java)
        fragmentIncomingOrdersBinding.incomingViewModel = incomingViewModel
        fragmentIncomingOrdersBinding.setLifecycleOwner(this)
        incomingViewModel.navigator = this
        if (dashboardNavigator != null)
            dashboardNavigator.setTitle(resources.getString(R.string.title_home))
        dashboardNavigator.hideLeftIcon(true)
        dashboardNavigator.hideRightIcon(true)
        ctx = activity!!
        getApiResponse()
        incomingViewModel.getIncomingOrders()
        SocketManager.emit(Constants.RoomName.JOINSHOPROOM, Constants.RoomID.TRANSPORT_ROOM)
        SocketManager.onEvent(Constants.RoomName.NEW_REQ, Emitter.Listener {
            Log.e("SOCKET", "SOCKET_SK transport STATUS ")
            incomingViewModel.getIncomingOrders()
        })
    }

    override fun onResume() {
        super.onResume()
        incomingViewModel.getIncomingOrders()
    }

    fun getApiResponse() {
        incomingViewModel.newOrderLiveData.observe(this, Observer<NewOrderModel> {

            if (it != null)

               /* //Store type set
                sessionManager.put(PreferenceKey.SHOP_TYPE,""+it.responseData?.get(0).)
                sessionManager.sharedPreferences.edit().commit()*/
                when (it.statusCode!!.toInt()) {
                    Constants.StatusCode.SUCCESS -> {
                        if (!it.responseData.isNullOrEmpty()) {
                            fragmentIncomingOrdersBinding.rvIncomingOrders.visibility = View.VISIBLE
                            val incomingOrderAdapter =
                                IncomingOrderAdapter(incomingViewModel, ctx!!, it)
                            fragmentIncomingOrdersBinding.rvIncomingOrders.setAdapter(
                                incomingOrderAdapter
                            )
                            fragmentIncomingOrdersBinding.rvIncomingOrders.adapter!!.notifyDataSetChanged()
                            fragmentIncomingOrdersBinding.emptyViewLayout.visibility = View.GONE

                        } else {
                            fragmentIncomingOrdersBinding.rvIncomingOrders.visibility = View.GONE
                            fragmentIncomingOrdersBinding.emptyViewLayout.visibility = View.VISIBLE
                        }
                    }
                }
        })

    }

    override fun showError(message: String, statusCode: Int) {
        when (statusCode) {
            Constants.StatusCode.UNAUTHORIZED -> {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity!!.finish()
            }
        }
    }

}