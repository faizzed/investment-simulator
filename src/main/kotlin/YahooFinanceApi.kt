package market

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result;
import com.google.gson.Gson
import java.io.File
import kotlin.math.abs

data class Meta(val currency: String, val symbol: String, val exchangeName: String, val instrumentType: String)
data class AdjClose(val adjclose: Array<Double>)
data class Indicators(val quote: Array<Any>, val adjclose: Array<AdjClose>)
data class Result(val meta: Meta, val timestamp: Array<Long>, val indicators: Indicators)
data class Chart(val result: Array<market.Result>, val error: String)
data class YahooResponse(val chart: Chart)

class YahooFinanceApi(val ticker: String, val startDate: Long, val endDate: Long, val interval: String = "1mo") {

    /**
    * Since the response doesnt change -- keep the file locally..
    * */
    private fun pullReport(): YahooResponse? {
        val fileName = "./src/main/resources/$ticker-$startDate-$endDate-$interval.json"

        if(File(fileName).exists()) {
            println("[YAHOO API]: Using local file system.")
            return Gson().fromJson(File(fileName).bufferedReader(), YahooResponse::class.java)
        }

        println("[YAHOO API: Sending request to yahoo finance api.")
        val url = "https://query1.finance.yahoo.com/v8/finance/chart/$ticker?formatted=true&includeAdjustedClose=true&interval=$interval&period1=$startDate&period2=$endDate"
        val (request, response, result) = url
            .httpGet()
            .responseString()

        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                println(ex)
                return null
            }
            is Result.Success -> {
                File(fileName).writeText(result.get())
                return Gson().fromJson(result.get(), YahooResponse::class.java)
            }
        }

    }

    fun getMonthlyReturns(): MutableMap<String, Double> {
        val report = pullReport() as YahooResponse
        val monthlyReturnsMap = mutableMapOf<String, Double>()
        val timeStampsArr = report.chart.result.first().timestamp
        val adCloseArr = report.chart.result.first().indicators.adjclose.first().adjclose
        for (i in timeStampsArr.indices) {

            val lastMonth = try {
                adCloseArr[i-1]
            } catch (e: IndexOutOfBoundsException ) {
                0.0
            }

            val currentMonth = adCloseArr[i]
            val percentageIncrease = if (lastMonth == 0.0) 0.0 else ((currentMonth - lastMonth) / abs(lastMonth)) * 100
            monthlyReturnsMap[timeStampToDate(timeStampsArr[i])] = percentageIncrease
//            print("current Month: $currentMonth - lastMonth: $lastMonth data: ${timeStampToDate(timeStampsArr[i])}\n")
        }
        return monthlyReturnsMap
    }
}