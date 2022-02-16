package parking

import javax.print.attribute.standard.QueuedJobCount

fun main() {
    var parkingModel: ParkingModelBehavior? = null

    while (true) {
        val input = userInput()

        try {
            when (input[0]) {
                "exit" -> return
                "create" -> parkingModel = createParkingModel(input[1].toInt())
                "status" -> outputMessages(parkingModel!!.printStatusOfParking())
                "leave" -> outputMessages(parkingModel!!.leaveSlot(input[1]))
                "park" -> outputMessages(parkingModel!!.parkCar(listOf(input[1], input[2])))
                "reg_by_color" -> outputMessages(parkingModel!!.regByColor(input[1]))
                "spot_by_color" -> outputMessages(parkingModel!!.spotByColor(input[1]))
                "spot_by_reg" -> outputMessages(parkingModel!!.spotByReg(input[1]))
                else -> outputMessages("Please, input correct command and parameters")
            }
        } catch (e: NullPointerException) {
            outputMessages("Sorry, a parking lot has not been created.")
        }
    }
}

fun createParkingModel(parkingSize: Int): ParkingModelBehavior {
    outputMessages("Created a parking lot with $parkingSize spots.")
    return ParkingModelBehavior(parkingSize)
}

fun userInput() = readln().split(" ").map { it }

fun outputMessages(string: String) {
    println(string)
}

class ParkingModelBehavior(
    private var parkingSize: Int = 0,
    private val parking: MutableMap<String, List<String>> = mutableMapOf()
) {
    fun printStatusOfParking(): String {
        val string = mutableListOf<String>()
        if (parking.isEmpty()) {
            string.add("Parking lot is empty.")
        } else {
            for ((key,value) in parking) {
                string.add("$key ${value.joinToString(" ")}")
            }
        }
        return string.joinToString("\n")
    }

    fun leaveSlot(slotNumber: String): String {
        parking.remove(slotNumber)
        return "Spot $slotNumber is free."
    }

    fun parkCar(newCar: List<String>): String {
        val string = mutableListOf<String>()
        if (parking.size == parkingSize) {
            string.add("Sorry, the parking lot is full.")
        } else {
            for (i in 1..parkingSize) {
                if (!parking.containsKey(i.toString())) {
                    parking[i.toString()] = newCar
                    string.add("${newCar[1]} car parked in spot $i.")
                    break
                }
            }
        }
        return string.joinToString("\n")
    }

    fun regByColor(color: String): String {
        val string = mutableListOf<String>()
        for ((_, value) in parking) {
            if (value[1].lowercase() == color.lowercase()) string.add(value[0])
        }

        return if (string.size > 0) string.joinToString(", ") else "No cars with color $color were found."
    }

    fun spotByColor(color: String): String {
        val string = mutableListOf<String>()
        for ((key, value) in parking) {
            if (value[1].lowercase() == color.lowercase()) string.add(key)
        }

        return if (string.size > 0) string.joinToString(", ") else "No cars with color $color were found."
    }

    fun spotByReg(regNum: String): String {
        var string = ""
        for ((key, value) in parking) {
            if (value[0].lowercase() == regNum.lowercase()) {
                string = key
                break
            }
        }
        return string.ifEmpty { "No cars with registration number $regNum were found." }
    }
}
