package com.watirna.shop.utils

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.MainThread
import com.google.gson.JsonSyntaxException
import com.watirna.shop.BuildConfig
import com.watirna.shop.application.AppController
import es.dmoral.toasty.Toasty
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class  CommanMethods {

    @Inject
    lateinit var dateTimeUtlity:DateTimeUtility

    @Inject
    lateinit var  application:Application

    @Inject
    lateinit var  sessionManager: SessionManager

    init {
        AppController.appComponent?.inject(this)
    }

    fun getNumberFormat(): NumberFormat? {
        val currency: String = sessionManager.get(PreferenceKey.CURRENCY_SYMBOL,"$").toString()
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        val decimalFormatSymbols = (numberFormat as DecimalFormat).decimalFormatSymbols
        decimalFormatSymbols.currencySymbol = currency
        numberFormat.decimalFormatSymbols = decimalFormatSymbols
        numberFormat.setMinimumFractionDigits(2)
        return numberFormat
    }

    fun getErrorMessage(e: Throwable): String {
        return when (e) {
            is JsonSyntaxException -> {
                if (BuildConfig.DEBUG) e.message.toString()
                else NetworkError.DATA_EXCEPTION
            }
            is HttpException -> { if(e.code()==401) getErrorMessage(e.response()!!.errorBody()!!) else ""}
            is SocketTimeoutException -> NetworkError.TIME_OUT
            is IOException -> NetworkError.IO_EXCEPTION
            else -> NetworkError.SERVER_EXCEPTION
        }
    }

    fun isMinLength(input: String, minLength: Int): Boolean {
        return minLength >= input.length
    }



    private fun getErrorMessage(responseBody: ResponseBody): String {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            jsonObject.getString("message")
        } catch (e: Exception) {
            e.message!!
        }
    }


    @MainThread
    fun showToast(context: Context, message: String, isSuccess: Boolean) {
        if (isSuccess) Toasty.success(context, message, Toast.LENGTH_SHORT).show()
        else Toasty.error(context, message, Toast.LENGTH_SHORT).show()
    }

    @MainThread
    fun showNormalToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getLocalTimeStamp(dateStr: String, request: String): String {
        val df = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
//        df.timeZone = TimeZone.getTimeZone("UTC")
        var date: Date? = null
        var calendar: Calendar? = null
        var strDate = ""
        try {
            date = df.parse(dateStr)
            calendar = Calendar.getInstance()
            calendar!!.time = date!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val dayOfTheWeek = SimpleDateFormat("EEEE").format(date) // Thursday
        val day = SimpleDateFormat("dd").format(date) // 20
        val monthString = SimpleDateFormat("MMM").format(date) // Jun
        val monthNumber = SimpleDateFormat("MM").format(date) // 06
        val hours = SimpleDateFormat("hh").format(date) // 12
        val min = SimpleDateFormat("mm").format(date) // 60
        val amPm = SimpleDateFormat("a").format(date) // 60
        val year = calendar!!.get(Calendar.YEAR)

        Log.d("Date", day + "-" + dayOfTheWeek + "-" + monthString + "-" + monthNumber +
                "-" + year + "/" + hours + ":" + min + ":" + amPm)

        if (request == "Req_Date_Month") return "$day $monthString"
        if (request == "Req_time") return "$hours:$min $amPm"

        val dayMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val strMonth = SimpleDateFormat("MMM").format(calendar.time)

        if (strMonth != null) strDate = Integer.toString(dayMonth) + "-" +
                strMonth + "-" + Integer.toString(year)

        return strDate

    }

    fun AlertButton(builder: AlertDialog){
        val negbuttonbackground: Button = builder!!.getButton(DialogInterface.BUTTON_NEGATIVE)
        negbuttonbackground.setBackgroundColor(Color.TRANSPARENT)
        negbuttonbackground.setTextColor(Color.BLACK)

        val posbuttonbackground: Button = builder!!.getButton(DialogInterface.BUTTON_POSITIVE)
        posbuttonbackground.setBackgroundColor(Color.TRANSPARENT)
        posbuttonbackground.setTextColor(Color.BLACK)
    }
}