package com.example.uetontop.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ChatRepository {

    // ⚠️ Thay YOUR_API_KEY bằng key OpenRouter của bạn
    private val apiKey = "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"

    suspend fun getBotReply(userMessage: String): String = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://openrouter.ai/api/v1/chat/completions")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                setRequestProperty("Authorization", "Bearer $apiKey")
                setRequestProperty("HTTP-Referer", "https://uetontop.example.com")
                setRequestProperty("X-Title", "UET On Top ChatBot")
                doOutput = true
            }

            // ✅ Prompt hệ thống
            val systemPrompt = """
                Bạn là một AI chuyên về tư vấn tâm lý và sức khỏe tinh thần. Tên của bạn là 'AI Coach'.

                **Vai trò và Mục tiêu:**
                - Mục tiêu chính của bạn là lắng nghe và đưa ra sự hỗ trợ trực tiếp, súc tích.
                - **Lắng nghe các triệu chứng chính** liên quan đến các vấn đề tâm lý (ví dụ: lo âu, mất ngủ, buồn bã liên tục).
                - **Tập trung vào vấn đề cốt lõi** mà người dùng đang chia sẻ.
                - **Đưa ra lời khuyên ngắn gọn, dễ thực hiện** hoặc một phương pháp đối phó đơn giản.
                - **Tránh giải thích dài dòng.** Giữ cho câu trả lời của bạn ngắn gọn và đi thẳng vào vấn đề.
                - **Không đưa ra chẩn đoán y tế.**

                **Tình huống khẩn cấp (quan trọng):**
                - Nếu người dùng đề cập đến việc tự làm hại bản thân, ý nghĩ tự tử, hoặc một cuộc khủng hoảng nghiêm trọng, hãy thực hiện hai bước sau:
                    1.  **Trấn an và Đồng cảm:** Bày tỏ sự quan tâm và trấn an họ một cách chân thành. Ví dụ: "Tôi rất tiếc khi nghe bạn phải trải qua điều này. Hãy nhớ rằng bạn không đơn độc." hoặc "Cảm ơn bạn đã đủ tin tưởng để chia sẻ điều này. Tôi ở đây để lắng nghe bạn."
                    2.  **Đưa ra hướng dẫn an toàn:** Sau đó, chuyển sang việc khuyên họ tìm kiếm sự giúp đỡ chuyên nghiệp. Hãy sử dụng chính xác câu sau: 
                        "Điều này thật sự rất nghiêm trọng. Vì sự an toàn của bạn, tôi khuyên bạn nên liên hệ ngay với đường dây nóng hỗ trợ sức khỏe tâm thần hoặc đến bệnh viện gần nhất. Sự an toàn của bạn là ưu tiên hàng đầu."

                **Hướng dẫn phản hồi:**
                - Luôn luôn trả lời bằng Tiếng Việt một cách tự nhiên và đồng cảm.
            """.trimIndent()

            // ✅ Tạo body JSON đúng format
            val messages = JSONArray()
                .put(JSONObject().put("role", "system").put("content", systemPrompt))
                .put(JSONObject().put("role", "user").put("content", userMessage))

            val body = JSONObject()
                .put("model", "openai/chatgpt-4o-latest")
                .put("messages", messages)

            // ✅ Gửi body (UTF-8)
            connection.outputStream.use { os ->
                os.write(body.toString().toByteArray(Charsets.UTF_8))
            }

            // ✅ Nhận phản hồi
            val responseCode = connection.responseCode
            val responseText = if (responseCode in 200..299) {
                connection.inputStream.bufferedReader().readText()
            } else {
                val errorText = connection.errorStream?.bufferedReader()?.readText()
                "Error $responseCode: $errorText"
            }

            if (responseCode !in 200..299) {
                println("❌ API Error: $responseText")
                return@withContext "Bot: Lỗi khi gọi API ($responseCode)"
            }

            val json = JSONObject(responseText)
            json.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")

        } catch (e: Exception) {
            e.printStackTrace()
            "Bot: Xin lỗi, tôi không thể phản hồi ngay bây giờ (${e.message})."
        }
    }
}
