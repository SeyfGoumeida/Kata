
import java.io.File
import java.io.InputStreamReader
import java.io.BufferedReader
import java.util.ArrayList
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun printAccount(bankAccount: Account) {

    println(
            bankAccount.id.toString() + "\t \t \t"+
                    bankAccount.creationDate.toString()+ "\t \t \t"+
                    bankAccount.firstName+ "\t \t \t"+
                    bankAccount.lastName+ "\t \t \t"+
                    bankAccount.type+ "\t \t \t"+
                    bankAccount.interestRate+ "\t \t \t"+
                    bankAccount.balance
    )
}

fun printAccountHead() {
    println("Id\t\t\tcreationDate\t\tFirstName\t\tLastName\t\ttype\t\tRate\t\tBalance")

}

fun printAccounts(bankAccounts: List<Account>) {
    printAccountHead()
    bankAccounts.forEach {
        printAccount(it)
    }
}

fun readAccounts(): ArrayList<Account> {
    var bankAccounts = arrayListOf<Account>()
    val file = File("C:\\Users\\Seyf_GOUMEIDA\\IdeaProjects\\kata\\src\\main\\kotlin\\Bank-Account.txt")
    val text = file.readText()
    text.split("\n").forEach {
        val line = it.split(", ")
        if(line.size > 1) {
            val id = line[0].removeSurrounding("\"").toInt()
            val date = line[1].removeSurrounding("\"")
            val creationDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
            val firstname = line[2].removeSurrounding("\"")
            val lastname = line[3].removeSurrounding("\"")
            val type = line[4].removeSurrounding("\"")
            val interestRate = line[5].toDouble()
            val balance = line[6].substring(0, line[5].length - 1).toInt()
            val bankAccount = Account(id,creationDate, firstname,lastname, type, interestRate, balance)

            bankAccounts.add(bankAccount)
        }
    }

    return bankAccounts
}

fun printBanner() {
    println("Bank Account Management Main Menu:")
    println()
    println("Please select an option from the following menu:")
    println("\t • 1 - Enter New Accounts")
    println("\t • 2 - Check Account Record")
    println("\t • 3 - Show Sorted Account List")
    println("\t • 4 - Exit")
}

fun newAccount(input: BufferedReader): Account {
    input.readLine()
    println("Enter New Account Data:")
    print("\t • Account No.:")
    val id = input.readLine().toInt()

    val creationDate = LocalDate.now()
    println("\t • Date of creation :$creationDate")

    print("\t • FirstName:")
    val firstname = input.readLine()

    print("\t • LastName: ")
    val lastname = input.readLine()


    print("\t • Account Type: (1)Basic  (2)Student  (3)Individual (4)Joint")
    var type = input.readLine()

    while (type!="1" && type!="2"&& type!="3"&& type!="4"){
            println("type is out of choices please choose :")
            print("\t • Account Type: (1)Basic  (2)Student  (3)Individual (4)Joint")
        type = input.readLine()
    }

    when (type) {
        "1" -> {
            println("\t • type : Basic")
            type ="Basic"
        }
        "2" -> {
            println("\t • type : Student")
            type ="Student"
        }
        "3" -> {
            println("\t • type : Individual")
            type ="Individual"
        }
        "4" -> {
            println("\t • type : Joint")
            type ="Joint"
        }
    }

    print("\t • Interest Rate:")
    val interestRate = input.readLine().toDouble()
    print("\t • Balance:")
    val balance = input.readLine().toInt()

    return Account(id, creationDate,firstname,lastname, type, interestRate, balance)
}

fun main(args: Array<String>) {

    var account = readAccounts()

    printBanner()

    while(true) {
        val input = BufferedReader(InputStreamReader(System.`in`))

        print("==> ")
        val choice = input.read()

        if(choice == 69) {
            // Enter new accounts
            val bankAccount = newAccount(input)
            account.add(bankAccount)
        } else if(choice == 67) {
            // Check the account
            input.readLine()
            print("Enter account_no: ")
            val number = input.readLine().toInt()
            val bankAccount = account.find { it.id == number }
            if(bankAccount != null) {
                printAccountHead()
                printAccount(bankAccount)
            }

        } else if(choice == 83) {
            // Show sorted list by number
            account.sortBy { it.id }
            printAccounts(account)
        } else if(choice == 120) {
            // Exit the program
            break
        }

    }
}

