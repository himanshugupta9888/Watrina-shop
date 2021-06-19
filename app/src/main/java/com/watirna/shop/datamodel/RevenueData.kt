package com.watirna.shop.datamodel

data class RevenueData(
    var cancelled_data: List<Int> = listOf(),
    var completed_data: List<Int> = listOf(),
    var currency: String = "",
    var delivered_data: Int = 0,
    var max: Int = 0,
    var month_earnings: MonthEarnings = MonthEarnings(),
    var received_data: Int = 0,
    var today_earnings: TodayEarnings = TodayEarnings(),
    var total_earnings: TotalEarnings = TotalEarnings()
)