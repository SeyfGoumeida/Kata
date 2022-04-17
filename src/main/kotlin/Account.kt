import java.time.LocalDate
import java.util.ArrayList
data class Account(
    val id: Int,
    val creationDate : LocalDate,
    val firstName: String,
    val lastName: String,
    val type: AccountTypes,
    val interestRate: Double,
    var balance: Double,
    val operatins : ArrayList<Operation>
)
