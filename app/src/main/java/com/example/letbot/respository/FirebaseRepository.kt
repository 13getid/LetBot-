package com.example.letbot.respository

import com.google.firebase.firestore.SetOptions

class FirebaseRepository @Inject constructor() {
        private val auth = FirebaseAuth.getInstance()
        private val db = FirebaseFirestore.getInstance()

        // Save chat to Firebase
        suspend fun saveChatMessage(
            sessionId: String,
            userMessage: String,
            aiResponse: String,
            timestamp: Long = System.currentTimeMillis()
        ) {
            val userId = auth.currentUser?.uid ?: "anonymous"

            val messageData = hashMapOf(
                "userId" to userId,
                "sessionId" to sessionId,
                "userMessage" to userMessage,
                "aiResponse" to aiResponse,
                "timestamp" to timestamp,
                "aiProvider" to "DeepSeek" // Mark that response came from DeepSeek
            )

            db.collection("chats")
                .document(sessionId)
                .collection("messages")
                .add(messageData)
                .await()
        }

        // Load chat history from Firebase
        suspend fun loadChatHistory(sessionId: String): List<ChatSession> {
            val userId = auth.currentUser?.uid ?: return emptyList()

            val snapshot = db.collection("chats")
                .whereEqualTo("userId", userId)
                .whereEqualTo("sessionId", sessionId)
                .get()
                .await()

            return snapshot.documents.map { doc ->
                ChatSession(
                    id = doc.id,
                    userId = doc.getString("userId") ?: "",
                    title = doc.getString("title") ?: "Chat Session",
                    timestamp = doc.getLong("timestamp") ?: 0L
                )
            }
        }

        // Create new chat session
        suspend fun createChatSession(title: String = "New Chat"): String {
            val userId = auth.currentUser?.uid ?: "anonymous"
            val sessionId = System.currentTimeMillis().toString()

            val sessionData = hashMapOf(
                "userId" to userId,
                "sessionId" to sessionId,
                "title" to title,
                "createdAt" to System.currentTimeMillis(),
                "aiProvider" to "DeepSeek"
            )

            db.collection("chat_sessions")
                .document(sessionId)
                .set(sessionData, SetOptions.merge())
                .await()

            return sessionId
        }
}