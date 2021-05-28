package market

import de.vandermeer.asciitable.AsciiTable

data class InvestmentComparatorResponse(val ticker: String, val totalInv: Double, val returnAfterXYears: Double, val extraEarning: Double, val growthPercentage: Double)

class InvestmentCalculatorRealDataTickerComparator(val initial: Double, val years: Int) {

    private fun analyse(ticker: String, from: String, to: String): InvestmentComparatorResponse {

        val yahooFinanceApiReport = YahooFinanceApi(ticker, dateToUnixTimestamp(from), dateToUnixTimestamp(to)).getMonthlyReturnsOverLastFiveYears()

        var _initial = initial
        for (month in (years * 12) downTo 1) {
            val percentageThisMonth = yahooFinanceApiReport[subtractMonthsFromCurrentDate(month)]
            val returnThisMonth = _initial * (if(percentageThisMonth != null) percentageThisMonth / 100 else 0.0)
            _initial += returnThisMonth
            _initial += initial
        }

        val totalStaticValueAdded = initial * (years * 12)
        val profitMade =  _initial - totalStaticValueAdded
        val percentageGrowth = ((_initial - totalStaticValueAdded) / totalStaticValueAdded) * 100

        return InvestmentComparatorResponse(
            ticker,
            totalStaticValueAdded,
            _initial,
            profitMade,
            percentageGrowth
        )
    }

    fun compare(tickers: List<String>, from: String, to: String) {
        val table = AsciiTable()

        table.addRow(
            "Ticker",
            "Total inv",
            "After $years years",
            "Extra Earnings",
            "Growth Percentage"
        )

        tickers.forEach {
            val report = analyse(it, from, to)
            table.addRow(
                report.ticker,
                report.totalInv,
                String.format("%.2f", report.returnAfterXYears),
                String.format("%.2f", report.extraEarning),
                "${String.format("%.2f", report.growthPercentage)}%",
            )
        }

        table.render().also(::println)
    }
}