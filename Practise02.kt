fun main() {
    val child = 5
    val adult = 28
    val senior = 87
    val oops = -1

    println("The movie ticket price for a person aged $child is $${ticketPrice(child)}.")
    println("The movie ticket price for a person aged $adult is $${ticketPrice(adult)}.")
    println("The movie ticket price for a person aged $senior is $${ticketPrice(senior)}.")
    println("The movie ticket price for a person aged oops is $${ticketPrice(oops)}.")
}

fun ticketPrice(age: Int): Int? {
    val priceChild = 15
    val priceAdult = 25
    val priceSenior = 20
    val priceOops = null

    if (age <= 0 ) {
        return priceOops
    }
    else if (age in 1..12) {
        return priceChild
    }
    else if (age in 13..64) {
        return priceAdult
    }
    else if (age in 65..100) {
        return priceSenior
    }
    return 0
}