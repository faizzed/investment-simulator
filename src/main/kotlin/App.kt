package market

class App {
    fun runAnalysisHypothetical() {
        InvestmentCalculatorStaticAnalysis(500.0, 3.3, 10).apply {
            staticAmountNonRollingReturn().also(::println)
            staticAmountRollingReturn().also(::println)
            staticAmountAddedRollingReturnHypothetical()
        }
    }

    fun runAnalysisOnRealData() {
        InvestmentCalculatorRealData(500.0, true).apply {
            val startDate = "01-01-2010"
            val endDate = "01-12-2020"
            report("ARKK", startDate, endDate)
            report("VGT", startDate, endDate)
            report("TSLA", startDate, endDate)
        }
    }

    fun compareTickerWithRealMonthlyReturnReports() {
        InvestmentCalculatorRealData(500.0).apply {
            val startDate = "01-01-2018"
            val endDate = "01-05-2021"
            val highReturnStocks = listOf(
                "SPY",
                "SOXL",
                "TECL",
                "TQQQ",
//                "USD",
                "ROM",
//                "ARKW",
//                "QLD",
                "ARKK",
//                "PALL",
                "ARKG",
                "ARKW",
                "PALL",
                "GOOGL",
                "TSLA",
                "VOO",
                "VTI",
                "VUG",
                "VGT",
//                "IBUY",
//                "USRT",
//                "SCHH",
//                "IYR"
            )
            compare(highReturnStocks, startDate, endDate)
        }
    }
}

fun main() {
    App().apply {
//        runAnalysisHypothetical()
        runAnalysisOnRealData()
        compareTickerWithRealMonthlyReturnReports()
    }

    NasdaqApi("ARKK").getMeta().also(::println)
}