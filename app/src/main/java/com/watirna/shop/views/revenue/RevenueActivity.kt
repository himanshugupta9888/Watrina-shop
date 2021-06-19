package com.watirna.shop.views.revenue

import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.watirna.shop.R
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityRevenueBinding
import com.watirna.shop.datamodel.RevenueData
import kotlinx.android.synthetic.main.activity_revenue.*
import kotlinx.android.synthetic.main.toolbar_main.view.*


class RevenueActivity : BaseActivity<ActivityRevenueBinding>() {

    private lateinit var mViewModel: RevenueViewModel
    private lateinit var mBinding: ActivityRevenueBinding


    override fun getLayoutId() = R.layout.activity_revenue

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mViewModel = ViewModelProviders.of(this).get(RevenueViewModel::class.java)
        mBinding = mViewDataBinding as ActivityRevenueBinding
        mBinding.lifecycleOwner = this
        mBinding.mViewModel = mViewModel

        mViewDataBinding.revenueToolbar.toolbar.title =
            resources.getString(R.string.title_revenue)
        mViewDataBinding.revenueToolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        mViewDataBinding.tvMonthlyEarning.isSelected=true
        mViewDataBinding.tvTodayEarning.isSelected=true

        viewModelObserve()
    }

    private fun viewModelObserve() {
        mViewModel.getLoadState().observe(this, Observer {
            loadingObservable.value = it
        })
        mViewModel.getError().observe(this, Observer {
            showErrorToast(it)
        })
        mViewModel.getRevenue().observe(this, Observer {
            prepareBarChart(it)
        })

        mViewModel.getRevenueResponse()

    }

    private fun prepareBarChart(res: RevenueData) {

        val months: Array<String> = resources.getStringArray(R.array.months)

        val l: Legend = barChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(true)
        l.yOffset = 0f
        l.xOffset = 10f
        l.yEntrySpace = 0f
        l.textSize = 8f

        val xAxis: XAxis = barChart.xAxis
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.valueFormatter = IndexAxisValueFormatter(months)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = 12

        val leftAxis: YAxis = barChart.axisLeft
        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(false)
        leftAxis.spaceTop = 35f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


        barChart.axisRight.isEnabled = false


        barChart.setPinchZoom(true)
        barChart.description.text = getString(R.string.pinch_zoom_message)
        barChart.description.textColor = ContextCompat.getColor(this, R.color.colorBaseTheme)
        barChart.setDrawGridBackground(false)

        val entriesGroup1: MutableList<BarEntry> = ArrayList()
        val entriesGroup2: MutableList<BarEntry> = ArrayList()
        months.forEachIndexed { i, item ->
            entriesGroup1.add(BarEntry(i.toFloat(), res.completed_data[i].toFloat()))
            entriesGroup2.add(BarEntry(i.toFloat(), res.cancelled_data[i].toFloat()))
        }


        val set1 = BarDataSet(entriesGroup1, "Delivered")
        set1.color = ContextCompat.getColor(this, R.color.colorPrimary)
        val set2 = BarDataSet(entriesGroup2, "Cancelled")
        set2.color = ContextCompat.getColor(this, R.color.grey)

        val groupSpace = 0.06f
        val barSpace = 0.02f
        val barWidth = 0.45f

        val dataSet: ArrayList<IBarDataSet> = ArrayList()
        dataSet.add(set1)
        dataSet.add(set2)


        val data = BarData(dataSet)
        data.barWidth = barWidth
        data.setValueFormatter(LargeValueFormatter())


        barChart.data = data
        barChart.xAxis.axisMinimum = 0.0f
        barChart.xAxis.axisMaximum = 0 + barChart.barData.getGroupWidth(groupSpace, barSpace) * 12
        barChart.groupBars(0.0f, groupSpace, barSpace)

        barChart.invalidate()

    }


}
