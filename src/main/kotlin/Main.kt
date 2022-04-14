
import java.io.File
import java.io.InputStreamReader
import java.io.BufferedReader
import java.util.ArrayList


fun printAccount(bankAccount: Account) {
    println("""
			${bankAccount.id}\t
			${bankAccount.firstName}\t\t
			${bankAccount.lastName}\t\t
			${bankAccount.type}\t\t
			${bankAccount.interestRate}\t\t\t
			${bankAccount.balance}
	""")
}

fun printAccountHead() {
    println("Number\tName\t\t\tType\t\tInterest Rate\t\tBalance")
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
            val firstname = line[1].removeSurrounding("\"") + ", " + line[2].removeSurrounding("\"")
            val lastname = line[1].removeSurrounding("\"") + ", " + line[2].removeSurrounding("\"")
            val type = line[3].removeSurrounding("\"")
            val interestRate = line[4].toDouble()
            val balance = line[4].substring(0, line[4].length - 1).toInt()
            val bankAccount = Account(id, firstname,lastname, type, interestRate, balance)

            bankAccounts.add(bankAccount)
        }
    }

    return bankAccounts
}

fun printBanner() {
    println("Bank Account Management Main Menu:")
    println()
    println("Please select an option from the following menu:")
    println("\t • (E)nter New Accounts")
    println("\t • (C)heck Account Record")
    println("\t • (S)how Sorted Account List")
    println("\t •  E(x)it")
}

fun newAccount(input: BufferedReader): Account {
    input.readLine()
    println("Enter New Account Data:")
    print("\t • Account No.:")
    val number = input.readLine().toInt()
    print("\t • Name:")
    val firstname = input.readLine()
    print("\t • Name:")
    val lastname = input.readLine()
    print("\t • Account Type:")
    val type = input.readLine()
    print("\t • Interest Rate:")
    val interestRate = input.readLine().toDouble()
    print("\t • Balance:")
    val balance = input.readLine().toInt()

    return Account(number, firstname,lastname, type, interestRate, balance)
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

