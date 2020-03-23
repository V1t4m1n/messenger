package dp.vitamin.messanger.utils

class Message() {
    lateinit var messageText: String
    lateinit var userName: String

    constructor(message: String, name: String) : this() {
        this.messageText = message
        this.userName = name
    }


}