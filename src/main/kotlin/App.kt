package market

import de.vandermeer.asciitable.AsciiTable

class App {
    fun runAnalysisHypothetical() {
        InvestmentCalculator(500.0, 3.3, 10).apply {
            staticAmountNonRollingReturn().also(::println)
            staticAmountRollingReturn().also(::println)
            staticAmountAddedRollingReturnHypothetical()
        }
    }

    fun runAnalysisOnRealData() {
        InvestmentCalculatorRealData(500.0, 2, false).apply {
            val startDate = "01-01-2019"
            val endDate = "01-05-2021"
            val leveraged = arrayOf("SOXL", "TECL", "TQQQ")
            val reits = arrayOf("USRT", "SCHH", "IYR")

            reits.forEach {
                analyse(it, startDate, endDate)
            }
        }
    }

    fun compareTickerWithRealMonthlyReturnReports() {
        InvestmentCalculatorRealDataTickerComparator(500.0, 2).apply {
            val startDate = "01-01-2019"
            val endDate = "01-05-2021"
            val leveraged = arrayOf("SOXL", "TECL", "TQQQ")
            val reits = listOf("USRT", "SCHH", "IYR")
            compare(reits, startDate, endDate)
        }
    }
}

fun main() {
    App().apply {
//        runAnalysisHypothetical()
//        runAnalysisOnRealData()
        compareTickerWithRealMonthlyReturnReports()
    }
}