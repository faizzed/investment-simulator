package market

import de.vandermeer.asciitable.AsciiTable

/**
 * S&P 500 from 2010 to 2019, the average stock market return for the last 10 years is 11.805%
 * But this class take actual monthly reports and run the investment simulation on it
 * */
class InvestmentCalculatorRealData(val initial: Double, val years: Int, val summary: Boolean = true) {

    fun analyse(ticker: String, from: String, to: String): Int {

        val yahooFinanceApiReport = YahooFinanceApi(ticker, dateToUnixTimestamp(from), dateToUnixTimestamp(to)).getMonthlyReturnsOverLastFiveYears()
        print("\n\nHypothetical Summary of investing $initial every month over $years years in $ticker index fund.\n\n")
        val table = AsciiTable()
        table.addRow(
            "Month",
            "Month In Report",
            "Return Report",
            "Return This Month",
            "Inv Amt",
            "After Return",
            "Increase Rate",
            "After Adding Extra",
            "Net Profit"
        )

        var _initial = initial
        var counterOfProfitPerMonth = 1
        for (month in (years * 12) downTo 1) {
            val monthInReport = subtractMonthsFromCurrentDate(month)
            val percentageThisMonth = yahooFinanceApiReport[subtractMonthsFromCurrentDate(month)]
            val returnThisMonth = _initial * (if(percentageThisMonth != null) percentageThisMonth / 100 else 0.0)
            _initial += returnThisMonth

            val profit = _initial - (initial * counterOfProfitPerMonth) // this var isnt necessary
            val beforeAddingInvestment = _initial // this var isnt necessary
            val staticIncrementalValue = initial * counterOfProfitPerMonth
            val increaseRate = ((beforeAddingInvestment - staticIncrementalValue) / staticIncrementalValue) * 100

            _initial += initial

            table.addRow(
                counterOfProfitPerMonth,
                monthInReport,
                "${String.format("%.3f", percentageThisMonth)}%",
                returnThisMonth.toInt(),
                staticIncrementalValue,
                beforeAddingInvestment.toInt(),
                "${String.format("%.1f", increaseRate)}%",
                _initial.toInt(),
                profit.toInt()
            )

            counterOfProfitPerMonth++
        }

        if (summary) {
            table.render().also(::println)
        }

        println("\n\nEnd analysis:")
        val totalStaticValueAdded = initial * (years * 12)
        val profitMade =  _initial - totalStaticValueAdded
        val percentageGrowth = ((_initial - totalStaticValueAdded) / totalStaticValueAdded) * 100

        val table2 = AsciiTable()
        table2.addRow(
            "Total inv",
            "After $years years",
            "Extra Earnings",
            "Growth Percentage"
        )
        table2.addRow(
            totalStaticValueAdded,
            _initial.toInt(),
            profitMade.toInt(),
            "${String.format("%.3f", percentageGrowth)}%"
        )
        table2.render().also(::println)

        return _initial.toInt()
    }
}