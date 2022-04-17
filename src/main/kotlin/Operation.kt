import java.time.LocalDate

data class Operation(
    val id :Int,
    val clientId: Int,
    val type : OperationTypes,
    val date : LocalDate,
    val amount: Double,
    val balanceBeforeOp:Double,
    val balanceAfterOp:Double
    )
