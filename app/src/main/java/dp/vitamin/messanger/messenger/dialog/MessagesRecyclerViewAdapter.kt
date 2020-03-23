package dp.vitamin.messanger.messenger.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dp.vitamin.messanger.R
import dp.vitamin.messanger.utils.Message

class MessagesRecyclerViewAdapter (private val listMessage: List<Message>) : RecyclerView.Adapter<MessagesRecyclerViewAdapter.MessagesViewHolder>() {

    override fun getItemCount(): Int {
        return listMessage.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_message, parent, false)
        return MessagesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {


        holder.textMessage.text = listMessage[position].messageText
        //holder.messageTime.text = listMessage[position].messageTime
        holder.userName.text = listMessage[position].userName
    }

    inner class MessagesViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textMessage: TextView = itemView.findViewById(R.id.textMessage)
        var userName: TextView = itemView.findViewById(R.id.userNameMessage)
        //var messageTime: TextView

        init {
            //messageTime = itemView.findViewById(R.id.timeMessage)

        }
    }
}