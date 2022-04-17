
import java.io.File
import java.io.InputStreamReader
import java.io.BufferedReader
import java.util.ArrayList
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
fun printAccount(account: Account) {

    println(
        account.id.toString() + "\t \t \t"+
                account.creationDate.toString()+ "\t \t \t"+
                account.firstName+ "\t \t \t"+
                account.lastName+ "\t \t \t"+
                account.type+ "\t \t \t"+
                account.interestRate+ "\t \t \t"+
                account.balance
    )
}
fun printOperation(operation: Operation) {

    println(
        operation.clientId.toString() + "\t \t \t"+
                operation.type+ "\t \t \t"+
                operation.date.toString()+ "\t \t \t"+
                operation.amount+ "\t \t \t"+
                operation.balanceBeforeOp+ "\t \t \t"+
                operation.balanceAfterOp+ "\t \t \t"
    )
}
fun printAccountHead() {
    println("Id\t\t\tcreationDate\t\tFirstName\t\tLastName\t\ttype\t\tRate\t\tBalance")
}
fun printOperationHead() {
    println("Id\t\t\tType\t\tDate\t\tAmount\t\tbalanceBeforeOp\t\tbalanceAfterOp")
}
fun printAccounts(accounts: List<Account>) {
    printAccountHead()
    accounts.forEach {
        printAccount(it)
    }
}
fun printOperations(operations: List<Operation>) {
    printOperationHead()
    operations.forEach {
        printOperation(it)
    }
}
fun readAccounts(): ArrayList<Account> {
    var bankAccounts = arrayListOf<Account>()
    val path = System.getProperty("user.dir") +"\\src\\main\\kotlin\\Bank-Account.csv"
    val file = File(path)
    val text = file.readText()
    text.split("\n").forEach {
        val line = it.split(";")
        if(line.isNotEmpty()) {
            val id = line[0].toInt()
            val date = line[1]
            val creationDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
            val firstname = line[2]
            val lastname = line[3]
            val type = line[4]
            var indexType=0
            when (type) {
                "Basic" -> indexType = 0
                "Student" -> indexType = 1
                "Individual" -> indexType = 2
                "Joint" -> indexType = 3
            }
            val interestRate = line[5].toDouble()
            val balance = line[6].toDouble()
            val operations = arrayListOf<Operation>()
            val bankAccount = Account(id,creationDate, firstname,lastname, AccountTypes.values()[indexType], interestRate, balance,
                operations
            )
            bankAccounts.add(bankAccount)
        }
    }
    return bankAccounts
}
fun readOperations(): ArrayList<Operation> {
    var accountOperations = arrayListOf<Operation>()
    val path = System.getProperty("user.dir") + "\\src\\main\\kotlin\\Bank-Operation.csv"
    val file = File(path)
    val text = file.readText()
    text.split("\n").forEach {
        val line = it.split(";")
        if (line.isNotEmpty()) {
            val id = line[0].toInt()
            val type = line[1]
            var indexType = 0
            when (type) {
                "Deposit" -> indexType = 0
                "Withdrawal" -> indexType = 1
            }
            val date = line[2]
            val creationDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)

            val amount = line[3].toDouble()
            val balanceBeforeOp = line[4].toDouble()
            val balanceAfterOp = line[5].toDouble()

            val operation = Operation(
                id, OperationTypes.values()[indexType],creationDate, amount, balanceBeforeOp,balanceAfterOp)
            accountOperations.add(operation)
        }
    }
    return accountOperations
}
fun printBanner() {
    println("Bank Account Management Main Menu:")
    println()
    println("Please select an option from the following menu:")
    println("\t • 1 - Enter New Accounts")
    println("\t • 2 - Check Account Record")
    println("\t • 3 - Show Sorted Account List")
    println("\t • 4 - Show Operations List")
    println("\t • 5 - Exit")
}
fun newAccount(input: BufferedReader): Account {
    input.readLine()
    println("Enter New Account Data:")
    print("\t • Account ID :")
    // get the list of the existing ids
    var accounts = readAccounts()
    var listOfIds = mutableListOf<Int>()
    for (i in accounts) {
        listOfIds.add(i.id)
    }
    // the new id is the last one +1
    println(listOfIds.last() + 1)
    val id = listOfIds.last() + 1
    // Today's date is the date of creation (automatique)
    val creationDate = LocalDate.now()
    println("\t • Date of creation :$creationDate")

    // type the firstname
    print("\t • FirstName:")
    val firstname = input.readLine()

    // type the lasttname
    print("\t • LastName: ")
    val lastname = input.readLine()

    // type the account type
    print("\t • Account Type: (1)Basic  (2)Student  (3)Individual (4)Joint")
    var typeNumber = input.readLine()
    var type = AccountTypes.Basic
    while (typeNumber != "1" && typeNumber != "2" && typeNumber != "3" && typeNumber != "4") {
        println("type is out of choices please choose :")
        print("\t • Account Type: (1)Basic  (2)Student  (3)Individual (4)Joint")
        typeNumber = input.readLine()
    }
    when (typeNumber) {
        "1" -> {
            println("\t • type : Basic")
            type = AccountTypes.Basic
        }
        "2" -> {
            println("\t • type : Student")
            type = AccountTypes.Student
        }
        "3" -> {
            println("\t • type : Individual")
            type = AccountTypes.Individual
        }
        "4" -> {
            println("\t • type : Joint")
            type = AccountTypes.Joint
        }
    }
    // type the rate
    println("\t • Interest Rate: 0")
    var error = true
    var interestRate = 0.0
    while (error) {
        error = try {
            //interestRate = input.readLine().toDouble()
            interestRate = 0.0
            false
        } catch (ex: NumberFormatException) {
            print("\t • You have to choose a number for Interest Rate:")
            true
        }
    }
    // type the Balance
    println("\t • Balance: 0.0$")
    error = true
    var balance = 0.0

    //----------------------------------------------------------------------------------------------------------
    // Insert the new account in the database (the File)
    val path = System.getProperty("user.dir") + "\\src\\main\\kotlin\\Bank-Account.csv"
    val text = "\n$id;$creationDate;$firstname;$lastname;$type;$interestRate;$balance;"
    try {
        Files.write(Paths.get(path), text.toByteArray(), StandardOpenOption.APPEND)
    } catch (e: IOException) {
    }
    val operations = arrayListOf<Operation>()
    return Account(id, creationDate, firstname, lastname, type, interestRate, balance,operations)
}
fun newOperation(input: BufferedReader): Operation {
    input.readLine()
    println("Enter New Account Data:")
    print("\t • Account ID :")
    // get the list of the existing accounts
    var accounts = readAccounts()
    var id=1


    // type the account type
    print("\t • Account Type: (1)Basic  (2)Student  (3)Individual (4)Joint")
    var typeNumber = input.readLine()
    var type = OperationTypes.Deposit
    while (typeNumber != "1" && typeNumber != "2") {
        println("type is out of choices please choose :")
        print("\t • Account Type: (1)Deposit  (2)Withdrawal")
        typeNumber = input.readLine()
    }
    when (typeNumber) {
        "1" -> {
            println("\t • type : Deposit")
            type = OperationTypes.Deposit
        }
        "2" -> {
            println("\t • type : Withdrawal")
            type = OperationTypes.Withdrawal
        }
    }

    // Today's date is the date of creation (automatique)
    val creationDate = LocalDate.now()
    println("\t • Date of creation :$creationDate")

    // type the rate
    println("\t • Amount: ")
    var error = true
    var amount = 0.0
    while (error) {
        error = try {
            amount = input.readLine().toDouble()
            false
        } catch (ex: NumberFormatException) {
            print("\t • You have to choose a number for amount:")
            true
        }
    }

    //----------------------------------------------------------------------------------------------------------
    // Insert the new account in the database (the File)
    val path = System.getProperty("user.dir") + "\\src\\main\\kotlin\\Bank-Operation.csv"
    val text = "\n$id;$type;$creationDate;$amount;$amount;$amount;"
    try {
        Files.write(Paths.get(path), text.toByteArray(), StandardOpenOption.APPEND)
    } catch (e: IOException) {
    }
    val operations = arrayListOf<Operation>()
    return Operation(id, type,creationDate, amount, amount, amount)
}
fun main(args: Array<String>) {
    var accounts = readAccounts()
    var operations = readOperations()

    printBanner()
    while(true) {
        val input = BufferedReader(InputStreamReader(System.`in`))
        print("===> ")
        val choice = input.read()
        // ascii of "1" is 49
        if(choice == 49) {
            // Enter new accounts
            val account = newAccount(input)
            accounts.add(account)
        // ascii of "2" is 50
        } else if(choice == 50) {
            // Check the account
            input.readLine()
            print("Enter account_no: ")
            val number = input.readLine().toInt()
            val account = accounts.find { it.id == number }
            if(account != null) {
                printAccountHead()
                printAccount(account)
            }
        // ascii of "3" is 51
        } else if(choice == 51) {
            // Show sorted list by number
            accounts.sortBy { it.id }
            printAccounts(accounts)
        // ascii of "5" is 53
        } else if(choice == 52) {
            // Show sorted list by number
            operations.sortBy { it.clientId }
            printOperations(operations)
            // ascii of "5" is 53
        } else if(choice == 53) {
            // Exit the program
            break
        }
    }
}

