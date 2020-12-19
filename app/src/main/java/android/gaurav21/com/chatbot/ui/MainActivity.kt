package android.gaurav21.com.chatbot.ui

import android.content.Intent
import android.gaurav21.com.chatbot.R
import android.gaurav21.com.chatbot.data.Message
import android.gaurav21.com.chatbot.utils.BotResponse
import android.gaurav21.com.chatbot.utils.Constants.OPEN_GOOGLE
import android.gaurav21.com.chatbot.utils.Constants.OPEN_SEARCH
import android.gaurav21.com.chatbot.utils.Constants.RECEIVE_ID
import android.gaurav21.com.chatbot.utils.Constants.SEND_ID
import android.gaurav21.com.chatbot.utils.Time
import android.icu.util.Measure
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var adapter : MessagingAdaptor
    private val botList = listOf("Gaurav", "Mohit")
    var messagesList = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycleView()
        clickEvents()
        val random = (0..3).random()
        customMessage("Hello! Today you are speaking with ${botList[random]}, how may I help?")
    }

    private fun clickEvents(){
        btn_send.setOnClickListener{
            sendMessage()
        }
        et_message.setOnClickListener{
            GlobalScope.launch {
                delay(1000)
                withContext(Dispatchers.Main){
                    rv_messages.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    private fun recycleView(){
        adapter = MessagingAdaptor()
        rv_messages.adapter = adapter
        rv_messages.layoutManager = LinearLayoutManager(applicationContext)
    }

    private  fun sendMessage(){
        val message = et_message.text.toString()
        val timestamp = Time.timestamp()
        if(message.isNotEmpty()){
            messagesList.add(Message(message, SEND_ID, timestamp))
            et_message.setText("")
            adapter.insertMessage(Message(message, SEND_ID, timestamp))
            rv_messages.scrollToPosition(adapter.itemCount - 1)
            botResponse(message)
        }
    }

    private fun botResponse(message: String){
        val timestamp = Time.timestamp()
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                val response = BotResponse.basicResponse(message)
                messagesList.add(Message(response, RECEIVE_ID, timestamp))
                adapter.insertMessage(Message(response, RECEIVE_ID, timestamp))
                rv_messages.scrollToPosition(adapter.itemCount - 1)
                when(response){
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfter("search")
                        site.data = Uri.parse("https://www.google.com/search?q=$searchTerm")
                        startActivity(site)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun customMessage(message: String){
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                val timestamp = Time.timestamp()
                messagesList.add(Message(message, RECEIVE_ID, timestamp))
                adapter.insertMessage(Message(message, RECEIVE_ID, timestamp))
                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}