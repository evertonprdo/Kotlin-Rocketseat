package challenges

abstract class Fuel {
    abstract var price: Double
    abstract fun requestPrice(): Double

    fun requestAmount(): Double {
        return try {
            print("$ ")
            readln().toDouble()
        } catch (error: NumberFormatException) {
            println("Invalid input: Please enter a valid Double 10.0")
            requestAmount()
        } catch (error: Exception) {
            println("Something got wrong try again")
            requestAmount()
        }
    }
}

class Gasoline() : Fuel() {
    override var price: Double

    init {
        price = requestPrice()
    }

    override fun requestPrice(): Double {
        println("Enter the price of gasoline in liters: ")
        return super.requestAmount()
    }
}

class Ethanol() : Fuel() {
    override var price: Double

    init {
        price = requestPrice()
    }

    override fun requestPrice(): Double {
        println("Enter the price of ethanol in liters: ")
        return super.requestAmount()
    }
}

fun main() {
    val gasoline = Gasoline()
    val ethanol = Ethanol()

    println(ethanol.price)
    println(gasoline.price)

    val reason = ethanol.price / gasoline.price
    val isLessThen = reason < 0.7

    println(
        "Ethanol is ${"%.1f".format(reason * 100)}% of the price of Gasoline, which means it ${
            if (isLessThen) "is below" else "exceeds"
        } the optimal threshold of 70%. Therefore, the app indicates that the most economical fuel is ${
            if (isLessThen) "Ethanol" else "Gasoline"
        }"
    ) // when equal, Gasoline is still a better option
}
