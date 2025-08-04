package practice.common.util

import org.springframework.stereotype.Component
import java.util.*

@Component
class RandomGenerator {
    
    fun nextInt(lowerBound: Int, upperBound: Int): Int {
        return lowerBound + random.nextInt(upperBound - lowerBound)
    }

    val uuid: String
        get() {
            val uuid = UUID.randomUUID().toString().replace("[^0-9]".toRegex(), "")

            val shortenedUUID = uuid.substring(0, 16)

            return String.format(
                "%s-%s-%s-%s",
                shortenedUUID.substring(0, 4), shortenedUUID.substring(4, 8),
                shortenedUUID.substring(8, 12), shortenedUUID.substring(12, 16)
            )
        }

    companion object {
        private val random = Random()
    }
}
