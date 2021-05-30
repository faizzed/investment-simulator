package market

/**
 * S&P 500 from 2010 to 2019, the average stock market return for the last 10 years is 11.805%
 * Given a static amount or added over the years we can see how it will look like after x years.
 *
 * This is static and it run over a static return each month.
 * */
class InvestmentCalculatorStaticAnalysis(val initial: Double, val returnPercentage: Double, val years: Int) {

    /*
    * One time investment and if the return wasn't reinvested
    */
    fun staticAmountNonRollingReturn(): Double {
        return initial + (initial * returnPercentage/100) * years
    }

    /*
    * If the returns were re-invested on a one time investment
    */
    fun staticAmountRollingReturn(): Double {
        var _initial = initial
        for (year in 1..years) {
            _initial += (_initial * (returnPercentage / 100))
        }
        return _initial
    }

    /*
    * If the returns were re-invested as well as the same amount is added each month
    */
    fun staticAmountAddedRollingReturnHypothetical(): Int {
        var _initial = initial
        for (month in 1..(years * 12)) {
            val returnThisMonth = (_initial * (returnPercentage / 100))
            _initial += returnThisMonth

            val profit = _initial - (initial * month) // this var isnt necessary
            val beforeAddingInvestment = _initial // this var isnt necessary

            _initial += initial


            println("Month $month; " +
                    "return: ${returnThisMonth.toInt()}; " +
                    "afterReturn: ${beforeAddingInvestment.toInt()}; " +
                    "afterAddingExtra: ${_initial.toInt()}; " +
                    "Profit: ${profit.toInt()}")
        }

        println("End analysis:")
        val totalStaticValueAdded = initial * (years * 12)
        println("Total static value added: $totalStaticValueAdded")
        println("Final value over $years years investment on $returnPercentage% return: ${_initial.toInt()}")
        val profitMade =  _initial - totalStaticValueAdded
        println("Profit made: ${profitMade.toInt()}")
        return _initial.toInt()
    }
}