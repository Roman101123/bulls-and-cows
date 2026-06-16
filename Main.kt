import java.util.Random

fun main() {
    println("Добро пожаловать в игру 'Быки и коровы'!")
    println("Выберите режим игры:")
    println("1. Играть против компьютера")
    println("2. Играть против другого игрока")

    val mode = readMode()
    if (mode == 1) playAgainstComputer() else playAgainstPlayer()
}

fun readMode(): Int {
    while (true) {
        print("Введите 1 или 2: ")
        val input = readLine()?.trim()
        if (input == "1") return 1
        if (input == "2") return 2
        println("Ошибка: Введите 1 или 2.")
    }
}

fun generateSecretNumber(): String {
    val digits = (0..9).toMutableList().shuffled()
    return digits.take(4).joinToString("")
}

fun readGuess(): String {
    while (true) {
        print("Введите 4-значное число: ")
        val input = readLine()?.trim() ?: continue
        if (input.length == 4 && input.all { it.isDigit() } && input.toSet().size == 4) return input
        println("Ошибка: введите 4 цифры без повторений.")
    }
}

fun readSecretNumber(): String {
    while (true) {
        print("Введите ваше тайное число: ")
        val input = readLine()?.trim() ?: continue
        if (input.length == 4 && input.all { it.isDigit() } && input.toSet().size == 4) return input
        println("Ошибка: введите 4 уникальные цифры.")
    }
}

fun clearConsole() {
    repeat(50) { println() }
}

fun calculateBullsAndCows(secret: String, guess: String): Pair<Int, Int> {
    var bulls = 0
    var cows = 0
    for (i in secret.indices) {
        when {
            secret[i] == guess[i] -> bulls++
            guess[i] in secret -> cows++
        }
    }
    return Pair(bulls, cows)
}

fun playAgainstComputer() {
    println("\nКомпьютер загадал число. Угадайте его!")
    val secret = generateSecretNumber()
    var attempts = 0

    while (true) {
        attempts++
        val guess = readGuess()
        val (bulls, cows) = calculateBullsAndCows(secret, guess)
        println("Быки: $bulls, Коровы: $cows")
        if (bulls == 4) {
            println("Поздравляем! Вы угадали за $attempts попыток.")
            break
        }
    }
}

fun playAgainstPlayer() {
    println("\nИгрок 1, введите ваше число:")
    val secret1 = readSecretNumber()
    clearConsole()

    println("Игрок 2, введите ваше число:")
    val secret2 = readSecretNumber()
    clearConsole()

    val firstPlayer = Random().nextBoolean()
    println(if (firstPlayer) "Игрок 1 ходит первым!" else "Игрок 2 ходит первым!")

    var attempts1 = 0
    var attempts2 = 0
    var winner: Int? = null
    var guess1: String? = null
    var guess2: String? = null

    while (winner == null) {
        if (firstPlayer || guess2 != null) {
            println("\nХод Игрока 1 (попытка #${attempts1 + 1}):")
            guess1 = readGuess()
            attempts1++
            val (bulls, cows) = calculateBullsAndCows(secret2, guess1)
            println("Быки: $bulls, Коровы: $cows")
            if (bulls == 4) { winner = 1; break }
        }

        if (!firstPlayer || guess1 != null) {
            println("\nХод Игрока 2 (попытка #${attempts2 + 1}):")
            guess2 = readGuess()
            attempts2++
            val (bulls, cows) = calculateBullsAndCows(secret1, guess2)
            println("Быки: $bulls, Коровы: $cows")
            if (bulls == 4) { winner = 2; break }
        }
    }

    println("\nПобедил Игрок $winner!")
    println("Число Игрока 1: $secret1, Число Игрока 2: $secret2")
}