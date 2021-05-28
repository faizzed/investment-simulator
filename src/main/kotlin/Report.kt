package market

data class Report(
    val month: Int,
    val monthInReport: String,
    val returnInReport: Double?,
    val returnAppliedToAmountUptoLastMonth: Double,
    val afterReturn: Double,
    val afterAddingExtra: Double,
    val netProfit: Double,
)