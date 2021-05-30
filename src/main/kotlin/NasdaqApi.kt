package market

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result;
import com.google.gson.Gson
import java.io.File

data class Volume(val label: String, val value: Double)
data class ExpenseRatio(val label: String, val value: Double)
data class AssetsUnderManagement(val label: String, val value: Double)
data class AverageSpread(val label: String, val value: Double)

data class NasdaqEtfKeyStats(val Volume: Volume, val ExpenseRatio: ExpenseRatio, val AUM: AssetsUnderManagement, val AverageSpread: AverageSpread)
data class NasdaqEtfMetaData(val symbol: String, val companyName: String, val stockType: String, val keyStats: NasdaqEtfKeyStats)
data class NasdaqEtfMetaResponse(val data: NasdaqEtfMetaData)

class NasdaqApi(val ticker: String) {

    /**
     * Since the response doesnt change -- keep the file locally..
     * */
    fun getMeta(): NasdaqEtfMetaResponse? {
        val fileName = "./src/main/resources/nasdaq-$ticker-info.json"

        if(File(fileName).exists()) {
            println("[NASDAQ API]: Using local file system.")
            return Gson().fromJson(File(fileName).bufferedReader(), NasdaqEtfMetaResponse::class.java)
        }

        println("[NASDAQ API: Sending request to nasdaq api.")
        val url = "https://api.nasdaq.com/api/quote/$ticker/info?assetclass=etf"
        val (request, response, result) = url
            .httpGet()
            .header(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36"
            )
            .responseString()

        println(request.headers)

        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                println(ex)
                return null
            }
            is Result.Success -> {
                File(fileName).writeText(result.get())
                return Gson().fromJson(result.get(), NasdaqEtfMetaResponse::class.java)
            }
        }

    }
}