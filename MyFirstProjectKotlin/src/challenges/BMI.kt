package challenges

import kotlin.math.pow

enum class BmiClassification(val description: String) {
    UNDERWEIGHT("Underweight"),
    NORMAL_WEIGHT("Normal weight"),
    OVERWEIGHT("Overweight"),
    OBESITY_I("Obesity I"),
    OBESITY_II("Obesity II"),
    OBESITY_III("Obesity III");

    companion object {
        fun classify(bmi: Double): BmiClassification = when {
            bmi < 18.5 -> UNDERWEIGHT
            bmi in 18.5..24.9 -> NORMAL_WEIGHT
            bmi in 25.0..29.9 -> OVERWEIGHT
            bmi in 25.0..29.9 -> OBESITY_I
            bmi in 35.0..39.9 -> OBESITY_II
            else -> OBESITY_III
        }
    }
}

fun requestWeight(): Double {
    println("Enter your weight in kilograms:")
    val weight = readlnOrNull()?.toDoubleOrNull()

    if (weight == null || weight < 0 || weight > 300) {
        println("Invalid input. Please enter a valid weight between 0 and 300 kg.")
        return requestWeight()
    }

    return weight
}

fun requestHeight(): Double {
    println("Enter your height in meters:")
    val height = readlnOrNull()?.toDoubleOrNull()

    if (height == null || height < 0 || height > 3) {
        println("Invalid height, please enter a valid height between 0 and 3 meters")
        return requestHeight()
    }

    return height
}

fun calcBMI(weight: Double, height: Double): Double =
    weight / height.pow(2)

fun main() {
    val weight = requestWeight()
    val height = requestHeight()

    val bmi = calcBMI(weight, height)
    val classification = BmiClassification.classify(bmi)

    println("Your BMI is ${"%.1f".format(bmi)} and your classification is ${classification.description}")
}