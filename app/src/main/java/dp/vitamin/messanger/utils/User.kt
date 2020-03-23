package dp.vitamin.messanger.utils

class User() {
    lateinit var login: String
    lateinit var password: String
    lateinit var name: String

    constructor(login: String, password: String, name: String) : this() {
        this.login = login
        this.password = password
        this.name = name
    }
}