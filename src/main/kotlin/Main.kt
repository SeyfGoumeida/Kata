@file:Suppress("NAME_SHADOWING")

import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

//#####################  Functions #############################
//#####################   Account  #############################
fun printAccountHead() {
    println("Id\t\t\tcreationDate\t\tFirstName\t\tLastName\t\t\ttype\t\t\tBalance")
}
fun printAccount(account: Account) {
    println(
        account.id.toString() + "\t \t \t"+
                account.creationDate.toString()+ "\t \t \t"+
                account.firstName+ "\t \t \t"+
                account.lastName+ "\t \t \t"+
                account.type+ "\t \t \t"+
                account.balance
    )
}
fun printAccounts(accounts: List<Account>) {
    printAccountHead()
    accounts.forEach {
        printAccount(it)
    }
}
fun newAccount(firstname:String,lastname:String,typeNumber:String): Account {
    print("\t • Account ID :")
    // get the list of the existing ids
    val accounts = readAccounts()
    val listOfIds = mutableListOf<Int>()
    for (i in accounts) {
        listOfIds.add(i.id)
    }
    // the new id is the last one +1
    println(listOfIds.last() + 1)
    val id = listOfIds.last() + 1
    // Today's date is the date of creation (automatic)
    val creationDate = LocalDate.now()
    println("\t • Date of creation :$creationDate")

    var type = AccountTypes.Basic
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

    // type the Balance
    println("\t • Balance: 0.0$")
    val balance = 0.0

    //----------------------------------------------------------------------------------------------------------
    // Insert the new account in the database (the File)
    val path = System.getProperty("user.dir") + "\\src\\main\\kotlin\\Bank-Account.csv"
    val text = "\n$id;$creationDate;$firstname;$lastname;$type;$balance;"
    try {
        Files.write(Paths.get(path), text.toByteArray(), StandardOpenOption.APPEND)
    } catch (e: IOException) {
        print("error")
    }
    val operations = arrayListOf<Operation>()
    return Account(id, creationDate, firstname, lastname, type, balance,operations)
}
fun readAccounts(): ArrayList<Account> {
    val bankAccounts = arrayListOf<Account>()
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
            val balance = line[5].toDouble()
            val operations = arrayListOf<Operation>()
            val bankAccount = Account(id,creationDate, firstname,lastname, AccountTypes.values()[indexType], balance,
                operations
            )
            bankAccounts.add(bankAccount)
        }
    }
    return bankAccounts
}
//#####################  Operations  #############################
fun printOperationHead() {
    println("Id\t\t\tType\t\t\tDate\t\t\t\tAmount\t\tbalanceBeforeOp\t\tbalanceAfterOp")
    println("-------------------------------------------------------------------------------------------")
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
fun printOperations(operations: List<Operation>) {
    printOperationHead()
    operations.forEach {
        printOperation(it)
    }
}
fun newOperation(idClient:String,typeNumber:String,amount:String): Operation {

    print("\t • Operation ID :")
    // get the list of the existing operations
    val operation = readOperations()
    var listOfIds = mutableListOf<Int>()
    for (i in operation) {
        listOfIds.add(i.id)
    }
    // the new id is the last one +1
    println(listOfIds.last() + 1)
    var id = listOfIds.last() + 1

    // type the account type
    var type = OperationTypes.Deposit
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

    // Today's date is the date of creation (automatic)
    var date = LocalDate.now()
    println("\t • Date of creation :$date")

    // get the list of the existing accounts
    val accounts = readAccounts()
    listOfIds = mutableListOf()
    for (i in accounts) {
        listOfIds.add(i.id)
    }
    val indexOfAccount = listOfIds.indexOf(idClient.toInt())
    val account = accounts[indexOfAccount]
    val balanceBeforeOp = account.balance
    val balanceAfterOp: Double

    if (typeNumber=="1"){
        balanceAfterOp = balanceBeforeOp + amount.toDouble()
        accounts[indexOfAccount].balance = balanceAfterOp
    }else {
        balanceAfterOp = balanceBeforeOp - amount.toDouble()
        accounts[indexOfAccount].balance = balanceAfterOp
    }
    //----------------------------------------------------------------------------------------------------------
    // Insert the new operation in the database (the File)
    var path = System.getProperty("user.dir") + "\\src\\main\\kotlin\\Bank-Operation.csv"
    var text = "\n$id;$idClient;$type;$date;$amount;$balanceBeforeOp;$balanceAfterOp;"
    try {
        Files.write(Paths.get(path), text.toByteArray(), StandardOpenOption.APPEND)
    } catch (e: IOException) {
        print("error")
    }
    // Update the account balance after the operation
    //----------------------------------------------------------------------------------------------------------
    // Insert the new account in the database (the File)
    path = System.getProperty("user.dir") + "\\src\\main\\kotlin\\Bank-Account.csv"
    val pw = PrintWriter(path)
    pw.close()
    for (account in accounts) {
        id = account.id
        val creationDate = account.creationDate
        val firstname = account.firstName
        val lastname = account.lastName
        val types = account.type
        val balance = account.balance
        text = "\n$id;$creationDate;$firstname;$lastname;$types;$balance;"
        try {
            Files.write(Paths.get(path), text.toByteArray(),StandardOpenOption.APPEND)
        } catch (e: IOException) {
            print("error")
        }
    }
    //file cleaning ( delete the first line )
    removeFirstLineFromFile(path)
    return Operation(id, idClient.toInt(), type,date, amount.toDouble(), amount.toDouble(), amount.toDouble())
}
fun readOperations(): ArrayList<Operation> {
    val accountOperations = arrayListOf<Operation>()
    val path = System.getProperty("user.dir") + "\\src\\main\\kotlin\\Bank-Operation.csv"
    val file = File(path)
    val text = file.readText()
    text.split("\n").forEach {
        val line = it.split(";")
        if (line.isNotEmpty()) {
            val id =  line[0].toInt()
            val idClient = line[1].toInt()
            val type = line[2]
            var indexType = 0
            when (type) {
                "Deposit" -> indexType = 0
                "Withdrawal" -> indexType = 1
            }
            val date = line[3]
            val creationDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)

            val amount = line[4].toDouble()
            val balanceBeforeOp = line[5].toDouble()
            val balanceAfterOp = line[6].toDouble()

            val operation = Operation(
                id,idClient, OperationTypes.values()[indexType],creationDate, amount, balanceBeforeOp,balanceAfterOp)
            accountOperations.add(operation)
        }
    }
    return accountOperations
}
fun printBanner() {
    println("################################################")
    println("                  Main Menu:                    ")
    println("################################################")
    println("Please select an option from the following menu:")
    println("\t • 1 - Enter New Accounts")
    println("\t • 2 - Enter New Operation")
    println("\t • 3 - Check Account Record")
    println("\t • 4 - Show Sorted Account List")
    println("\t • 5 - Show Operations List")
    println("\t • 6 - Show Account Statement")
    println("\t • 7 - Show Account History")
    println("\t • 8 - Exit")
}
//this function is just to remove the first line from the file
//we use it when we update the Balance
@Throws(IOException::class)
fun removeFirstLineFromFile(filePath: String?) {
    val raf = RandomAccessFile(filePath, "rw")
    //Initial write position
    var writePosition = raf.filePointer
    raf.readLine()
    // Shift the next lines upwards.
    var readPosition = raf.filePointer
    val buff = ByteArray(1024)
    var n: Int
    while (-1 != raf.read(buff).also { n = it }) {
        raf.seek(writePosition)
        raf.write(buff, 0, n)
        readPosition += n.toLong()
        writePosition += n.toLong()
        raf.seek(readPosition)
    }
    raf.setLength(writePosition)
    raf.close()
}
//################# Account statement & history ######################
fun printAccountHistory(id: Int):ArrayList<Operation> {
    val operations = readOperations()
    val accountHistory =ArrayList<Operation>()
    printOperationHead()
    for (operation in operations){
        if(operation.clientId==id){
            printOperation(operation)
            accountHistory.add(operation)
        }
    }
    return accountHistory
}
fun printStatement(id: Int):Account{
    val accounts = readAccounts()
    printAccount(accounts[id])
    return accounts[id]
}
//######################  MAIN  ###############################
fun main() {
    while(true) {
        printBanner()
        val input = BufferedReader(InputStreamReader(System.`in`))
        print("===> ")
        val choice = input.read()
        // ascii of "1" is 49
        if(choice == 49) {
            // Enter new accounts
            println("Enter New Account Data:")
            //newAccount(input)
            input.readLine()

            // type the firstname
            print("\t • FirstName:")
            val firstname = input.readLine()

            // type the lastname
            print("\t • LastName: ")
            val lastname = input.readLine()

            // type the account type
            print("\t • Account Type: (1)Basic  (2)Student  (3)Individual (4)Joint")
            var typeNumber = input.readLine()
            while (typeNumber != "1" && typeNumber != "2" && typeNumber != "3" && typeNumber != "4") {
                println("type is out of choices please choose :")
                print("\t • Account Type: (1)Basic  (2)Student  (3)Individual (4)Joint")
                typeNumber = input.readLine()
            }
            newAccount(firstname,lastname,typeNumber)
            // ascii of "2" is 50
        } else if(choice == 50) {
            input.readLine()
            // Enter new operation
            print("\t • Account ID concerned by the operation:")
            val idClient=input.readLine().toInt()

            // type the account type
            print("\t • Account Type: (1)Deposit  (2)Withdrawal")
            var typeNumber = input.readLine()
            while (typeNumber != "1" && typeNumber != "2") {
                println("type is out of choices please choose :")
                print("\t • Account Type: (1)Deposit  (2)Withdrawal")
                typeNumber = input.readLine()
            }

            // type the amount
            print("\t • Amount: ")
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

            newOperation(idClient.toString(),typeNumber,amount.toString())
            // ascii of "3" is 51
        }else if(choice == 51) {
            // Check the account
            val accounts = readAccounts()
            input.readLine()
            print("Enter account_no: ")
            val number = input.readLine().toInt()
            val account = accounts.find { it.id == number }
            if(account != null) {
                printAccountHead()
                printAccount(account)
            }
        // ascii of "4" is 52
        } else if(choice == 52) {
            val accounts = readAccounts()
            // Show sorted list by number
            accounts.sortBy { it.id }
            printAccounts(accounts)
        // ascii of "5" is 53
        } else if(choice == 53) {
            // Show sorted list by number
            val operations = readOperations()
            operations.sortBy { it.clientId }
            printOperations(operations)
            // ascii of "6" is 54
        } else if(choice == 54) {
            val reader = Scanner(System.`in`)
            var id: Int
            try{
                println("Enter account ID: ")
                id = reader.nextInt()
                println("############################################################################################################")
                printStatement(id-1)
                println("############################################################################################################")


            }catch (ex:Exception){
                println("############################")
                println("####   Invalid Index !#####")
                println("############################")
            }
            // ascii of "7" is 55
        }else if(choice == 55) {

            val reader = Scanner(System.`in`)
            var id: Int
            try{
                println("Enter account ID: ")
                id = reader.nextInt()
                println("Enter Your password : ")
                println("############################################################################################################")
                printAccountHistory(id)
                println("############################################################################################################")
            }catch (ex:Exception){
                println("############################")
                println("####   Invalid Index !#####")
                println("############################")
            }
            // ascii of "8" is 56
        }else if(choice == 56) {
            // Exit the program
            break
        }
    }
}