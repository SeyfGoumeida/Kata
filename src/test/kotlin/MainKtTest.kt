import org.junit.FixMethodOrder
import org.junit.jupiter.api.Test
import org.junit.runners.MethodSorters
import java.time.LocalDate
import kotlin.test.assertEquals
@FixMethodOrder(MethodSorters.DEFAULT)

    internal class MainKtTest {
    // these functions are just for visual purposes ( prints)
    @Test
    fun printAccountHead() {
    }
    @Test
    fun printAccounts() {
    }
    @Test
    fun printAccount() {
    }
    @Test
    fun printOperationHead() {
    }
    @Test
    fun printOperation() {
    }

    @Test
    fun printOperations() {
    }

    //------------------------------------------------------------------------------------

    @Test
    fun newAccountTest() {
        // type the firstname
        val firstname = "testUser"
        // type the lastname
        val lastname = "testUser"
        // type the account type
        val typeNumber = "Basic"
        val account = newAccount(firstname,lastname,typeNumber)

        //check if the created account is equal to the entered data
        assertEquals(0.0,account.balance)
        assertEquals("testUser",account.firstName)
        assertEquals("testUser",account.lastName)
        assertEquals(AccountTypes.Basic,account.type)
        assertEquals(LocalDate.now(),account.creationDate)
        val bankAccounts = readAccounts()

        // check if the last account is the same that we insert in the test NewAccount
        val nbAccounts=bankAccounts.size
        for (bankAccount in bankAccounts){
            if (bankAccount.id==nbAccounts){
                assertEquals(bankAccount.balance,account.balance)
                assertEquals(bankAccount.firstName,account.firstName)
                assertEquals(bankAccount.lastName,account.lastName)
                assertEquals(bankAccount.type,account.type)
                assertEquals(bankAccount.creationDate,account.creationDate)
            }
        }
    }
    @Test
    // check if the reading lines from the Data Base file gives arraylist of accounts
    // And each element is of type Account
    fun readAccountsTest() {
        val accounts = readAccounts()
        assertEquals(accounts::class.simpleName,"ArrayList")
        for (account in accounts){
            assertEquals(account::class.simpleName,"Account")
        }
    }

    @Test
    fun newOperationTest() {
        // Enter new operation
        val idClient="1"
        // type the account type
        val typeNumber = "1"
        // type the amount
        val amount = 5.0
        val operation = newOperation(idClient,typeNumber,amount.toString())
        //check if the created account is equal to the entered data
        assertEquals(5.0,operation.amount)
        assertEquals(OperationTypes.Deposit,operation.type)
        assertEquals(LocalDate.now(),operation.date)

        // check if the last account is the same that we insert in the test NewAccount
        val bankOperations = readOperations()
        val nbOperations=bankOperations.size
        for (bankOperation in bankOperations){
            if (bankOperation.id==nbOperations){
                assertEquals(bankOperation.clientId,operation.clientId)
                assertEquals(bankOperation.amount,operation.amount)
                assertEquals(bankOperation.type,operation.type)
                assertEquals(bankOperation.date,operation.date)
            }
        }
    }

    @Test
    fun readOperationsTest() {
        val operations = readOperations()
        assertEquals(operations::class.simpleName,"ArrayList")
        for (operation in operations){
            assertEquals(operation::class.simpleName,"Operation")
        }
    }

    @Test
    fun printBanner() {
    }

    @Test
    fun printAccountHistory() {
    }
}