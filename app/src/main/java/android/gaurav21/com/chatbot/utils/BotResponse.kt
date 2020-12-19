package android.gaurav21.com.chatbot.utils

import android.gaurav21.com.chatbot.utils.Constants.OPEN_GOOGLE
import android.gaurav21.com.chatbot.utils.Constants.OPEN_SEARCH
import android.util.Log

object BotResponse {
    private const val TAG = "BotResponse"
    fun basicResponse(message : String): String {
        val random = (0..1).random()
        val message = message.toLowerCase()

        return when {
            //Hello
            message.contains("hello") -> {
                when (random) {
                    0 -> "Hello there!"
                    1 -> "sup"
                    else -> "error"
                }
            }
            message.contains("how are you") -> {
                when (random) {
                    0 -> "I'm good"
                    1 -> "Pretty good! How about you?"
                    else -> "error"
                }
            }

            message.contains("flip") &&  message.contains("coin") -> {
                var r = (0..1).random()
                var result = if(r==0) "heads" else "tail"
                "I flipped a coin and it landed on $result"
            }

            message.contains("solve") -> {
                val equation: String? = message.substringAfter("solve")
                return try {
                    val answer = SolveMath.solveMath(equation?: "0")
                    answer.toString()
                }
                catch (e : Exception){
                    "Sorry, I can't solve that!"
                }
            }
            //Get the current time
            message.contains("time") && message.contains("?")-> {
                Time.timestamp()
            }
            message.contains("open") && message.contains("google") -> {
                OPEN_GOOGLE
            }
            message.contains("search") -> {
                OPEN_SEARCH
            }
            else -> {
                "Sorry, I'm not yet trained for this"
            }
        }
    }
}