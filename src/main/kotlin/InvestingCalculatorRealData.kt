package market

import de.vandermeer.asciitable.AsciiTable

/**
 * S&P 500 from 2010 to 2019, the average stock market return for the last 10 years is 11.805%
 * But this class take actual monthly reports and run the investment simulation on it
 * */
open class InvestmentCalculatorRealData(open val initial: Double, val summary: Boolean = true) {

    fun analyse(ticker: String, from: String, to: String): Report {
        val years = differenceInYears(from, to)
        val monthlyReport = mutableListOf<MonthlyReport>()
        val yahooFinanceApiReport = YahooFinanceApi(ticker, dateToUnixTimestamp(from), dateToUnixTimestamp(to)).getMonthlyReturns()

        var _initial = initial
        var counterOfProfitPerMonth = 1
        var monthCounter = 1

        loop@for (month in (years * 12) downTo 1) {
            monthCounter++
            val monthInReport = subtractMonthsFromCurrentDate(month)
            // skip when there is no return in reports
            val percentageThisMonth = yahooFinanceApiReport[monthInReport] ?: continue@loop

            val returnThisMonth = _initial * (percentageThisMonth / 100)

            _initial += returnThisMonth

            val profit = _initial - (initial * counterOfProfitPerMonth) // this var isnt necessary
            val beforeAddingInvestment = _initial // this var isnt necessary
            val staticIncrementalValue = initial * counterOfProfitPerMonth
            val increaseRate = ((beforeAddingInvestment - staticIncrementalValue) / staticIncrementalValue) * 100

            _initial += initial
            counterOfProfitPerMonth++

            monthlyReport.add(
                MonthlyReport(
                    ticker,
                    monthCounter,
                    monthInReport,
                    percentageThisMonth,
                    returnThisMonth,
                    staticIncrementalValue,
                    beforeAddingInvestment,
                    increaseRate,
                    _initial,
                    profit
                )
            )
        }

        val totalStaticValueAdded = initial * (years * 12)
        val profitMade =  _initial - totalStaticValueAdded
        val percentageGrowth = ((_initial - totalStaticValueAdded) / totalStaticValueAdded) * 100

        return Report(
            monthlyReport,
            FinalAnalysisReport(
                ticker,
                totalStaticValueAdded,
                _initial,
                profitMade,
                percentageGrowth
            )
        )

    }

    fun report(ticker: String, from: String, to: String) {
        val years = differenceInYears(from, to)
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

        val report = analyse(ticker, from, to)

        report.monthlyReport.forEach {
            table.addRow(
                it.month,
                it.monthInReport,
                "${String.format("%.3f", it.returnInReport)}%",
                it.returnThisMonth.toInt(),
                it.investedAmount,
                it.afterReturn.toInt(),
                "${String.format("%.1f", it.increaseRate)}%",
                it.afterReturn.toInt(),
                it.netProfit.toInt()
            )
        }

        if (summary) {
            table.render().also(::println)
        }

        println("\n\nEnd analysis:")
        val table2 = AsciiTable()
        table2.addRow(
            "Total inv",
            "After $years years",
            "Extra Earnings",
            "Growth Percentage"
        )
        table2.addRow(
            report.finalAnalysisReport.totalInvestment,
            String.format("%.3f", report.finalAnalysisReport.AfterXYears),
            String.format("%.3f", report.finalAnalysisReport.extraEarnings),
            "${String.format("%.3f", report.finalAnalysisReport.growthPercentage)}%"
        )

        table2.render().also(::println)
    }

    fun compare(tickers: List<String>, from: String, to: String) {
        val table = AsciiTable()
        val years = differenceInYears(from, to)

        table.addRow(
            "Ticker",
            "Total inv",
            "After $years years",
            "Extra Earnings",
            "Growth Percentage"
        )

        val reportsList = mutableListOf<FinalAnalysisReport>()

        tickers.forEach {
            reportsList.add(analyse(it, from, to).finalAnalysisReport)
        }

        reportsList.sortByDescending { it.growthPercentage }
        reportsList.forEach { report ->
            table.addRow(
                report.ticker,
                report.totalInvestment,
                String.format("%.2f", report.AfterXYears),
                String.format("%.2f", report.extraEarnings),
                "${String.format("%.2f", report.growthPercentage)}%",
            )
        }

        table.render().also(::println)
    }
}