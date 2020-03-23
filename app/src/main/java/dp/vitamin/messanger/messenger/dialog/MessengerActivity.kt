package dp.vitamin.messanger.messenger.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dp.vitamin.messanger.R
import dp.vitamin.messanger.utils.Message
import java.text.SimpleDateFormat
import java.util.*

class MessengerActivity : AppCompatActivity() {

    private lateinit var listMessage: ListView
    private lateinit var adapter: FirebaseListAdapter<Message>
    private lateinit var sendButton: FloatingActionButton
    private lateinit var arrayAdapter: ArrayAdapter<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        listMessage = findViewById(R.id.messagesList)
        sendButton = findViewById(R.id.sendButton)

        sendButton.setOnClickListener {
            val messageEditText: EditText = findViewById(R.id.messageEditText)

            if (messageEditText.text.isEmpty()) return@setOnClickListener

            FirebaseDatabase.getInstance().reference.push().setValue(
                Message(
                    FirebaseAuth.getInstance().currentUser?.email.toString(),
                    messageEditText.text.toString()
                ))
            messageEditText.text.clear()
            adapter.notifyDataSetChanged()
        }

        adapter = object : FirebaseListAdapter<Message>(
            this,
            Message::class.java,
            R.layout.recycler_view_item_message,
            FirebaseDatabase.getInstance().reference
        ) {
            @SuppressLint("SimpleDateFormat")
            override fun populateView(v: View, model: Message, position: Int) {
                try {
                    val textMessage: TextView = v.findViewById(R.id.textMessage)
                    val userName: TextView = v.findViewById(R.id.userNameMessage)
                    val timeMessage: TextView = v.findViewById(R.id.timeMessage)

                    textMessage.text = model.userName
                    userName.text = model.messageText
                    timeMessage.text = SimpleDateFormat("dd/MM/yyy").format(Date())
                    return
                } catch (e: Exception) {
                    Log.d("ERROR", e.localizedMessage)
                    return
                }

            }
        }

        listMessage.adapter = adapter




        //InitializationRecyclerView(listMessageRecyclerView)
    }

    private fun InitializationRecyclerView(listMessageRecyclerView: RecyclerView) {
        //this.listMessageRecyclerView = findViewById(R.id.messagesListRecyclerView)
        listMessageRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        listMessageRecyclerView.adapter = MessagesRecyclerViewAdapter(generateFake())
    }

    private fun generateFake(): List<Message> {
        val list = mutableListOf<Message>()

        for (i in 0..9) {
            var m = Message()

            m.userName = "Avatar $i"
            m.messageText = "Message https://www.google.ru/url?sa=i&url=https%3A%2F%2Fwww"
            /*var month = Date().month.toLong()
            if (month < 10) {
                m.messageTime = "${Date().date.toLong()}.0${month}.${Date().year} ${Date().hours}:${Date().minutes}"
            } else {
                m.messageTime = "${Date().date.toLong()}.${month}.${Date().year} ${Date().hours}:${Date().minutes}"
            }*/


            list.add(m)
        }
        return list.reversed()
    }
}
