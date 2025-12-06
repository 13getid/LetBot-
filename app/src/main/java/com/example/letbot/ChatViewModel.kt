package com.example.letbot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.math.log

class ChatViewModel : ViewModel () {

    // initializing the generative model
    val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.5-flash")

    // start a persistent chat session
    private  val  chatSession = model.startChat()
    //define a tag for your log messages
    private val tag = "GeminiChatbot"
    fun sendMessage(question : String) {
        viewModelScope.launch {
            try {
                Log.d(tag,"Sending message: $question")// log that message is being sent
                val response = chatSession.sendMessage(question)
                // log the response text specifically to logcat
                Log.d(tag,"Received response:${response.text}")
            }catch (e: Exception){
                Log.e(tag,"Error getting response",e)// Log any errors
            }
        }
    }
    }
