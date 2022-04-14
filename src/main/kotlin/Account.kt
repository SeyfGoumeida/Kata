import java.time.LocalDate
import java.util.Date

data class Account(
    val id: Int,
    val creationDate : LocalDate,
    val firstName: String,
    val lastName: String,
    val type: String,
    val interestRate: Double,
    val balance: Int
)