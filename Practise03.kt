import java.util.Currency

fun main() {
    val eur = Currency.getInstance("EUR")
    val usd = Currency.getInstance("USD")

    val eurToUsdRate = 1.05
    val usdToEurRate = 0.95

    // eur to usd
    convertCurrency(
        amount = 12.3,
        initialCurrency = eur,
        targetCurrency = usd,
        conversionFormula = { amount -> amount * eurToUsdRate }
    )

    // usd to eur
    convertCurrency(
        amount = 12.3,
        initialCurrency = usd,
        targetCurrency = eur,
        conversionFormula = { amount -> amount * usdToEurRate }
    )
}

fun convertCurrency(
    amount: Double,
    initialCurrency: java.util.Currency,
    targetCurrency: java.util.Currency,
    conversionFormula: (Double) -> Double
) {
    val convertedAmount = String.format("%.2f", conversionFormula(amount)) // round the result to 2 decimal places
    println("$amount $initialCurrency can be changed in $convertedAmount $targetCurrency.")
}