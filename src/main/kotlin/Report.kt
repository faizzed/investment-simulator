package market

data class Report(val monthlyReport: List<MonthlyReport>, val finalAnalysisReport: FinalAnalysisReport)

data class MonthlyReport(
    val ticker: String,
    val month: Int,
    val monthInReport: String,
    val returnInReport: Double?,
    val returnThisMonth: Double,
    val investedAmount: Double,
    val afterReturn: Double,
    val increaseRate: Double,
    val afterAddingExtra: Double,
    val netProfit: Double,
)

data class FinalAnalysisReport(
    val ticker: String,
    val totalInvestment: Double,
    val AfterXYears: Double,
    val extraEarnings: Double?,
    val growthPercentage: Double,
)