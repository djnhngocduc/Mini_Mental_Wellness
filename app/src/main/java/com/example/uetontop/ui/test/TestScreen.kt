package com.example.uetontop.ui.test

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uetontop.navigation.Screen
import com.example.uetontop.ui.home.BottomBar
import com.example.uetontop.ui.home.HomeHeader


// ---------- TestScreen ----------
@Composable
fun TestScreen(navController: NavController) {
    // UI state: chọn test -> câu hỏi
    var uiState by remember { mutableStateOf<TestUiState>(TestUiState.Select) }

    Scaffold(
        bottomBar = { BottomBar(navController) },
        containerColor = Color(0xFFF5F5F7),
        contentWindowInsets = WindowInsets(0.dp)
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(Color(0xFFF5F5F7))
        ) {
            HomeHeader(
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onChatClick = { /* ... */ },
                onBellClick = { /* ... */ },
            )
            Column(modifier = Modifier.padding(16.dp)) {
                // ---- chỉ thay phần giữa từ đây ----
                when (val s = uiState) {
                    is TestUiState.Select -> TestSelectContent(
                        onNext = { selectedId ->
                            uiState = TestUiState.Questions(testId = selectedId)
                        }
                    )

                    is TestUiState.Questions -> QuestionFlowContent(
                        testId = s.testId,
                        onFinish = { score, maxScore ->
                            uiState = TestUiState.Result(
                                testId = s.testId,
                                score = score,
                                maxScore = maxScore
                            )
                        }
                    )

                    is TestUiState.Result -> ResultContent(
                        testId = s.testId,
                        score = s.score,
                        maxScore = s.maxScore,
                        onRetake = { uiState = TestUiState.Questions(testId = s.testId) },
                        onBackToList = { uiState = TestUiState.Select }
                    )

                }
            }
            // ---- hết phần giữa ----
        }
    }
}

// ---------- UI State ----------
private sealed class TestUiState {
    data object Select : TestUiState()
    data class Questions(val testId: String) : TestUiState()
    data class Result(val testId: String, val score: Int, val maxScore: Int) : TestUiState()
}

@Composable
private fun TestSelectContent(
    onNext: (String) -> Unit
) {
    val tests = remember {
        listOf(
            TestItem("BECK", "BECK"),
            TestItem("PHQ-9", "PHQ-9"),
            TestItem("DASS 21", "DASS 21"),
            TestItem("ZUNG SAS", "ZUNG SAS"),
            TestItem("DISC", "DISC"),
            TestItem("EPDS", "EPDS")
        )
    }
    var selectedId by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        TextsSelectHeader()

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(items = tests, key = { it.id }) { item ->
                TestCard(
                    text = item.title,
                    selected = selectedId == item.id,
                    onClick = { selectedId = item.id }
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F7))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            GradientButton(
                text = "Tiếp tục",
                enabled = selectedId != null,
                onClick = { selectedId?.let(onNext) }
            )
        }
    }
}

private data class TestItem(val id: String, val title: String)

@Composable
private fun TextsSelectHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp) // << thêm dòng này
    ) {
        Text(
            "Chọn bài kiểm tra phù hợp",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold, color = Color(0xFF1E2330)
            )
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Vui lòng chọn bài kiểm tra phù hợp để chúng tôi hiểu rõ hơn tình trạng hiện tại của bạn.",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF8D93A1))
        )
        Spacer(Modifier.height(12.dp))
    }
}

// ---------- CONTENT: flow câu hỏi ----------
@Composable
private fun QuestionFlowContent(
    testId: String,
    onFinish: (score: Int, maxScore: Int) -> Unit
) {
    val (questions, optionLabels, maxPerItem) = remember(testId) {
        when (testId) {
            "PHQ-9" -> Triple(
                listOf(
                    Question("q1", "Ít hứng thú hoặc ít vui thích khi làm việc gì", emptyList()),
                    Question("q2", "Cảm thấy buồn bã, chán nản hoặc tuyệt vọng", emptyList()),
                    Question("q3", "Khó đi vào giấc ngủ, khó duy trì giấc ngủ hoặc ngủ quá nhiều", emptyList()),
                    Question("q4", "Cảm thấy mệt mỏi hoặc thiếu năng lượng", emptyList()),
                    Question("q5", "Chán ăn hoặc ăn quá nhiều", emptyList()),
                    Question("q6", "Cảm thấy tệ về bản thân — như thể bạn là kẻ thất bại hoặc làm gia đình thất vọng", emptyList()),
                    Question("q7", "Khó tập trung (ví dụ: đọc báo, xem TV)", emptyList()),
                    Question("q8", "Cử động hoặc nói chậm đến mức người khác có thể nhận ra? Hoặc ngược lại — bồn chồn, đứng ngồi không yên hơn bình thường", emptyList()),
                    Question("q9", "Có ý nghĩ rằng chết đi sẽ tốt hơn hoặc làm tổn hại bản thân theo một cách nào đó", emptyList())
                ),
                listOf(
                    "Không hề",          // 0
                    "Vài ngày",               // 1
                    "Hơn một nửa số ngày",    // 2
                    "Gần như mỗi ngày"        // 3
                ),
                3 // max mỗi câu
            )

            "DASS 21" -> Triple(
                listOf(
                    Question("q1","Tôi thấy khó thư giãn", emptyList()),
                    Question("q2","Tôi nhận thấy miệng bị khô", emptyList()),
                    Question("q3","Tôi dường như không thể cảm nhận bất kỳ cảm xúc tích cực nào", emptyList()),
                    Question("q4","Tôi có khó thở (ví dụ: thở nhanh, hụt hơi dù không gắng sức)", emptyList()),
                    Question("q5","Tôi thấy khó khởi động để làm việc gì đó", emptyList()),
                    Question("q6","Tôi có xu hướng phản ứng thái quá trước các tình huống", emptyList()),
                    Question("q7","Tôi bị run (ví dụ: run tay)", emptyList()),
                    Question("q8","Tôi cảm giác đang dùng rất nhiều năng lượng thần kinh", emptyList()),
                    Question("q9","Tôi lo lắng về những tình huống có thể khiến mình hoảng sợ và trở nên ngớ ngẩn", emptyList()),
                    Question("q10","Tôi cảm thấy chẳng còn điều gì để mong đợi", emptyList()),
                    Question("q11","Tôi thấy mình trở nên bứt rứt, kích động", emptyList()),
                    Question("q12","Tôi thấy khó thư giãn", emptyList()),
                    Question("q13","Tôi cảm thấy chán nản, buồn bã", emptyList()),
                    Question("q14","Tôi thiếu bao dung với bất cứ điều gì cản trở công việc của mình", emptyList()),
                    Question("q15","Tôi cảm thấy gần như hoảng loạn", emptyList()),
                    Question("q16","Tôi không thể hứng thú với bất cứ điều gì", emptyList()),
                    Question("q17","Tôi cảm thấy mình không có nhiều giá trị", emptyList()),
                    Question("q18","Tôi thấy mình khá dễ chạnh lòng/dễ bị kích thích", emptyList()),
                    Question("q19","Tôi nhận thấy nhịp tim của mình (ví dụ: tim đập nhanh, hẫng nhịp) dù không gắng sức", emptyList()),
                    Question("q20","Tôi thấy sợ hãi mà không có lý do rõ ràng", emptyList()),
                    Question("q21","Tôi cảm thấy cuộc sống vô nghĩa", emptyList())
                ),
                listOf(
                    "Hoàn toàn không đúng với tôi",
                    "Đúng với tôi ở mức độ nhất định, đôi lúc",
                    "Đúng với tôi khá nhiều, trong thời gian đáng kể",
                    "Rất đúng với tôi, trong phần lớn thời gian"
                ),

                3
            )

            "BECK" -> Triple(
                listOf(
                    Question("q1","Buồn bã", listOf(
                        "Tôi không cảm thấy buồn.",
                        "Tôi buồn phần lớn thời gian.",
                        "Tôi buồn suốt cả ngày.",
                        "Tôi buồn/khổ sở đến mức không chịu nổi."
                    )),
                    Question("q2","Bi quan", listOf(
                        "Tôi không nản lòng về tương lai.",
                        "Tôi nản lòng về tương lai hơn trước.",
                        "Tôi không mong mọi việc sẽ suôn sẻ với mình.",
                        "Tôi thấy tương lai vô vọng và sẽ còn tệ hơn."
                    )),
                    Question("q3","Thất bại trong quá khứ", listOf(
                        "Tôi không thấy mình là kẻ thất bại.",
                        "Tôi đã thất bại nhiều hơn mức nên có.",
                        "Nhìn lại, tôi thấy rất nhiều thất bại.",
                        "Tôi thấy mình là kẻ thất bại hoàn toàn."
                    )),
                    Question("q4","Mất hứng thú/vui thích", listOf(
                        "Tôi vẫn vui với những điều mình thích như trước.",
                        "Tôi không còn vui như trước nữa.",
                        "Tôi rất ít khi thấy vui với những điều trước đây từng thích.",
                        "Tôi không còn thấy vui với những điều trước đây từng thích."
                    )),
                    Question("q5","Cảm giác tội lỗi", listOf(
                        "Tôi không thấy đặc biệt có lỗi.",
                        "Tôi thấy có lỗi về nhiều điều mình đã/đáng lẽ phải làm.",
                        "Hầu hết thời gian tôi thấy khá có lỗi.",
                        "Tôi luôn thấy có lỗi."
                    )),
                    Question("q6","Cảm giác bị trừng phạt", listOf(
                        "Tôi không thấy mình bị trừng phạt.",
                        "Tôi thấy có thể mình sẽ bị trừng phạt.",
                        "Tôi mong đợi sẽ bị trừng phạt.",
                        "Tôi thấy mình đang bị trừng phạt."
                    )),
                    Question("q7","Tự ghét/bất mãn bản thân", listOf(
                        "Tôi cảm thấy về bản thân như trước đây.",
                        "Tôi đã mất tự tin vào bản thân.",
                        "Tôi thất vọng về bản thân.",
                        "Tôi ghét bản thân mình."
                    )),
                    Question("q8","Tự chỉ trích", listOf(
                        "Tôi không tự chỉ trích/đổ lỗi cho mình nhiều hơn bình thường.",
                        "Tôi tự chỉ trích bản thân nhiều hơn trước.",
                        "Tôi tự chỉ trích mình vì mọi sai sót.",
                        "Tôi đổ lỗi cho mình về mọi chuyện tồi tệ xảy ra."
                    )),
                    Question("q9","Ý nghĩ tự sát", listOf(
                        "Tôi không có ý nghĩ tự làm hại mình.",
                        "Tôi có ý nghĩ như vậy nhưng sẽ không làm.",
                        "Tôi muốn làm hại bản thân.",
                        "Nếu có cơ hội tôi sẽ làm."
                    )),
                    Question("q10","Khóc", listOf(
                        "Tôi không khóc nhiều hơn trước.",
                        "Tôi khóc nhiều hơn trước.",
                        "Tôi khóc vì những chuyện rất nhỏ.",
                        "Tôi muốn khóc mà không thể khóc được."
                    )),
                    Question("q11","Kích động/bồn chồn", listOf(
                        "Tôi không bồn chồn hơn bình thường.",
                        "Tôi bồn chồn hơn bình thường.",
                        "Tôi bứt rứt đến mức khó ngồi yên.",
                        "Tôi bồn chồn đến mức phải luôn cử động/làm gì đó."
                    )),
                    Question("q12","Mất hứng thú với người/việc", listOf(
                        "Tôi không mất hứng thú với người khác hay hoạt động.",
                        "Tôi ít hứng thú với người/việc hơn trước.",
                        "Tôi hầu như mất hứng thú với người/việc.",
                        "Rất khó để tôi thấy hứng thú với bất cứ điều gì."
                    )),
                    Question("q13","Khó quyết định", listOf(
                        "Tôi quyết định cũng tốt như trước.",
                        "Tôi thấy khó quyết định hơn bình thường.",
                        "Tôi khó quyết định hơn trước rất nhiều.",
                        "Tôi gần như không thể quyết định."
                    )),
                    Question("q14","Cảm giác vô giá trị", listOf(
                        "Tôi không thấy mình vô giá trị.",
                        "Tôi không thấy mình còn có ích như trước.",
                        "Tôi thấy mình kém giá trị hơn người khác.",
                        "Tôi thấy mình hoàn toàn vô dụng."
                    )),
                    Question("q15","Mất năng lượng", listOf(
                        "Tôi có năng lượng như mọi khi.",
                        "Tôi ít năng lượng hơn trước.",
                        "Tôi không đủ năng lượng để làm nhiều việc.",
                        "Tôi hầu như không có năng lượng để làm gì."
                    )),
                    Question("q16","Thay đổi giấc ngủ", listOf(
                        "Tôi không thấy thay đổi nào về giấc ngủ.",
                        "Tôi ngủ nhiều hơn HOẶC ít hơn bình thường một chút.",
                        "Tôi ngủ nhiều hơn HOẶC ít hơn rất nhiều.",
                        "Tôi ngủ gần như cả ngày HOẶC dậy sớm 1–2 giờ và không ngủ lại được."
                    )),
                    Question("q17","Dễ cáu", listOf(
                        "Tôi không cáu kỉnh hơn bình thường.",
                        "Tôi cáu kỉnh hơn bình thường.",
                        "Tôi rất cáu kỉnh.",
                        "Tôi luôn ở trạng thái cáu kỉnh."
                    )),
                    Question("q18","Thay đổi khẩu vị", listOf(
                        "Tôi không thấy thay đổi khẩu vị.",
                        "Khẩu vị của tôi ít hơn HOẶC nhiều hơn bình thường một chút.",
                        "Khẩu vị ít đi HOẶC tăng lên nhiều so với trước.",
                        "Tôi không muốn ăn gì cả HOẶC muốn ăn suốt."
                    )),
                    Question("q19","Khó tập trung", listOf(
                        "Tôi tập trung tốt như trước.",
                        "Tôi tập trung kém hơn bình thường.",
                        "Khó giữ sự chú ý lâu vào việc gì.",
                        "Tôi thấy không thể tập trung vào bất cứ điều gì."
                    )),
                    Question("q20","Mệt mỏi kiệt sức", listOf(
                        "Tôi không mệt hơn bình thường.",
                        "Tôi mệt/kiệt sức dễ hơn bình thường.",
                        "Tôi quá mệt không làm được nhiều việc như trước.",
                        "Tôi quá mệt không làm được hầu hết việc."
                    )),
                    Question("q21","Giảm hứng thú tình dục", listOf(
                        "Tôi không thấy thay đổi gần đây về hứng thú tình dục.",
                        "Tôi ít hứng thú hơn trước.",
                        "Hiện tôi rất ít hứng thú.",
                        "Tôi hoàn toàn mất hứng thú."
                    ))
                ),
                emptyList(),
                3
            )

            "ZUNG SAS" -> Triple(
                listOf(
                    Question("q1","Tôi thấy lo âu hoặc hoảng sợ mà không có lý do rõ ràng.", emptyList()),
                    Question("q2","Tôi dễ bị kích động hoặc bực bội.", emptyList()),
                    Question("q3","Tôi bị run tay/chân hoặc run người.", emptyList()),
                    Question("q4","Tôi đổ mồ hôi dù không vận động.", emptyList()),
                    Question("q5","Tôi thấy khó thư giãn.", emptyList()),
                    Question("q6","Tim tôi đập nhanh hoặc hồi hộp.", emptyList()),
                    Question("q7","Tôi thấy chóng mặt hoặc lâng lâng.", emptyList()),
                    Question("q8","Tôi sợ rằng điều gì đó tồi tệ sắp xảy ra.", emptyList()),
                    Question("q9","Tôi dễ giật mình.", emptyList()),
                    Question("q10","Tôi khó ngủ hoặc giấc ngủ không sâu.", emptyList()),
                    Question("q11","Tôi thấy tê bì hay kiến bò ở ngón tay/ngón chân.", emptyList()),
                    Question("q12","Tôi đau nhức cơ bắp mà không rõ nguyên nhân.", emptyList()),
                    Question("q13","Tôi thấy khó thở hoặc không đủ hơi.", emptyList()),
                    Question("q14","Tôi có cảm giác nghẹn ở cổ hay khó nuốt.", emptyList()),
                    Question("q15","Tôi khó chịu/đau bụng.", emptyList()),
                    Question("q16","Tôi đi vệ sinh thường xuyên hơn bình thường.", emptyList()),
                    Question("q17","Tay tôi lạnh và ướt.", emptyList()),
                    Question("q18","Mặt tôi bừng nóng hoặc bỗng tái nhợt.", emptyList()),
                    Question("q19","Tôi đổ mồ hôi lạnh.", emptyList()),
                    Question("q20","Tôi dễ mệt.", emptyList())
                ),
                listOf(
                    "Không bao giờ hoặc rất ít khi",
                    "Đôi khi",
                    "Phần lớn thời gian",
                    "Hầu như hoặc toàn bộ thời gian"
                ),
                4
            )

            "DISC" -> Triple(
                listOf(
                    // D – Thống trị
                    Question("q1","Tôi quyết đoán và sẵn sàng dẫn dắt khi cần.", emptyList()),
                    Question("q2","Tôi thoải mái chấp nhận rủi ro để đạt mục tiêu.", emptyList()),
                    Question("q3","Tôi ưu tiên hành động nhanh hơn là bàn luận kéo dài.", emptyList()),
                    // I – Ảnh hưởng
                    Question("q4","Tôi dễ bắt chuyện và xây dựng mối quan hệ mới.", emptyList()),
                    Question("q5","Tôi nói chuyện nhiệt huyết và tạo động lực cho người khác.", emptyList()),
                    Question("q6","Tôi thích môi trường sôi động, giàu năng lượng.", emptyList()),
                    // S – Kiên định
                    Question("q7","Tôi điềm tĩnh và kiên nhẫn với mọi người.", emptyList()),
                    Question("q8","Tôi trung thành và đáng tin cậy khi làm việc nhóm.", emptyList()),
                    Question("q9","Tôi thích nhịp độ ổn định, ít thay đổi đột ngột.", emptyList()),
                    // C – Tận tâm/Chuẩn mực
                    Question("q10","Tôi chú ý đến chi tiết và tiêu chuẩn chất lượng.", emptyList()),
                    Question("q11","Tôi cẩn trọng và thích quy trình rõ ràng.", emptyList()),
                    Question("q12","Tôi suy nghĩ logic trước khi quyết định.", emptyList())
                ),
                listOf(
                    "Hoàn toàn không đồng ý",
                    "Không đồng ý",
                    "Đồng ý",
                    "Hoàn toàn đồng ý"
                ),
                3
            )

            "EPDS" -> Triple(
                listOf(
                    Question("q1","Tôi vẫn có thể cười và thấy được điều hài hước.", emptyList()),
                    Question("q2","Tôi mong chờ và thấy vui với các hoạt động.", emptyList()),
                    Question("q3","Khi có chuyện không ổn, tôi tự trách mình một cách không cần thiết.", emptyList()),
                    Question("q4","Tôi lo lắng hoặc bồn chồn mà không có lý do rõ ràng.", emptyList()),
                    Question("q5","Tôi cảm thấy sợ hãi hoặc hoảng sợ mà không có lý do xác đáng.", emptyList()),
                    Question("q6","Mọi việc dồn dập khiến tôi bị quá tải.", emptyList()),
                    Question("q7","Tôi buồn đến mức khó ngủ.", emptyList()),
                    Question("q8","Tôi cảm thấy buồn bã hoặc khổ sở.", emptyList()),
                    Question("q9","Tôi buồn đến mức bật khóc.", emptyList()),
                    Question("q10","Từng có ý nghĩ làm hại bản thân.", emptyList())
                ),
                listOf(
                    "Không hề",
                    "Thỉnh thoảng",
                    "Thường xuyên",
                    "Gần như mọi lúc"
                ),
                3
            )

            else -> Triple(
                List(10) { i -> Question("q$i", "Câu hỏi ${i + 1}: (mẫu)", emptyList()) },
                listOf("Hoàn toàn không đồng ý", "Không đồng ý", "Đồng ý", "Hoàn toàn đồng ý"),
                3
            )
        }
    }
    val total = questions.size
    var index by remember { mutableIntStateOf(0) }
    var selections by remember { mutableStateOf(MutableList<Int?>(total) { null }) }

    val zungReverseIdx = setOf(4, 8, 12, 16, 18)
    val epdsReverseIdx = (2..9).toSet()

    fun scoreFor(testId: String, qIndex: Int, choiceIndex: Int): Int {
        return when (testId) {
            "ZUNG SAS" -> {
                // Zung: 1..4; đảo điểm cho các câu reverse
                val base = if (zungReverseIdx.contains(qIndex)) 4 - choiceIndex else choiceIndex + 1
                base // 1..4
            }
            "EPDS" -> {
                // EPDS: câu 1-2 chấm thuận (0..3); câu 3-10 đảo (3..0)
                if (epdsReverseIdx.contains(qIndex)) 3 - choiceIndex else choiceIndex
            }
            else -> {
                // PHQ-9, DASS-21, BECK, DISC (Likert đơn giản)
                choiceIndex
            }
        }
    }

    // Header progress
    Column(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                "Câu ${index + 1}/$total",
                style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF8D93A1))
            )
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { (index + 1) / total.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = Color(0xFF9D7BFF),
                trackColor = Color(0xFFE2E2E8),
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
        }

        // Nội dung câu hỏi + đáp án
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                questions[index].text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium, color = Color(0xFF1E2330)
                )
            )
            Spacer(Modifier.height(16.dp))

            val isBeck = testId.equals("BECK", true)

            val currentOptions: List<String> =
                if (isBeck) questions[index].options
                else optionLabels

            currentOptions.forEachIndexed { optIdx, label ->
                AnswerOption(
                    text = label,
                    selected = selections[index] == optIdx,  // 0..3
                    onClick = {
                        selections = selections.toMutableList().also { it[index] = optIdx }
                    }
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        // Nút Next/Finish
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F7))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            GradientButton(
                text = if (index == total - 1) "Hoàn tất" else "Tiếp theo",
                enabled = selections[index] != null,
                onClick = {
                    if (index < total - 1) index++
                    else {
                        // tính điểm: cộng chỉ số lựa chọn (0..3), null => 0
                        val raw = selections.withIndex().sumOf { (i, sel) ->
                            val ci = sel ?: 0
                            scoreFor(testId, i ,ci)
                        }

                        val finalScore = when (testId) {
                            "DASS 21" -> raw * 2
                            else -> raw
                        }

                        val realMax = when (testId) {
                            "ZUNG SAS" -> 20 * 4      // 20 câu, mỗi câu 1..4 => 80
                            "EPDS"     -> 10 * 3      // 10 câu, mỗi câu 0..3 => 30
                            "PHQ-9"    -> 9  * 3      // 27
                            "DASS 21"  -> (21 * 3) * 2// 21 câu 0..3, rồi ×2 => 126
                            "BECK"     -> 21 * 3      // 63
                            else       -> questions.size * maxPerItem
                        }

                        onFinish(finalScore, realMax)
                    }
                }
            )
        }
    }
}

// ---------- models & small UI pieces ----------
private data class Question(val id: String, val text: String, val options: List<String>)

@Composable
private fun TestCard(text: String, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    val borderColor = if (selected) Color(0xFF9D7BFF) else Color(0xFFE2E2E8)
    val interaction = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (selected) 6.dp else 3.dp, shape, clip = false)
            .clip(shape)
            .border(1.dp, borderColor, shape)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        color = Color(0xFFFFFFFF)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium, color = Color(0xFF1E2330)
                )
            )
        }
    }
}

@Composable
private fun AnswerOption(text: String, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    val interaction = remember { MutableInteractionSource() }
    val stroke = if (selected) Color(0xFF9D7BFF) else Color(0xFFE2E2E8)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color(0xFFFFFFFF), shape)
            .border(1.dp, stroke, shape)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium, color = Color(0xFF1E2330)
            )
        )
    }
}

@Composable
private fun GradientButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    val brush = Brush.linearGradient(listOf(Color(0xFFB38BFF), Color(0xFF9D7BFF)))
    val disabledBrush = Brush.linearGradient(listOf(Color(0xFFE8E8ED), Color(0xFFE0E0E6)))

    Surface(
        onClick = onClick,
        enabled = enabled,
        color = Color.Transparent,
        shadowElevation = 2.dp,
        shape = shape
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(if (enabled) brush else disabledBrush, shape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (enabled) Color.White else Color(0xFF9A9AA5)
                )
            )
        }
    }
}

@Composable
private fun ResultContent(
    testId: String,
    score: Int,
    maxScore: Int,
    onRetake: () -> Unit,
    onBackToList: () -> Unit
) {
    val percent = (score.toFloat() / maxScore.toFloat()).coerceIn(0f, 1f)
    val level = when {
        percent < 0.25f -> "Rất nhẹ"
        percent < 0.50f -> "Nhẹ"
        percent < 0.75f -> "Vừa"
        else -> "Nặng"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Kết quả của bạn",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.height(12.dp))

        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Bài kiểm tra: $testId", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(6.dp))
                Text("Điểm: $score / $maxScore", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { percent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = Color(0xFF9D7BFF),
                    trackColor = Color(0xFFE2E2E8),
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Mức độ: $level",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Đây là kết quả sàng lọc nhanh, không phải chẩn đoán y khoa.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF8D93A1)),
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        Button(onClick = onRetake, modifier = Modifier.fillMaxWidth()) {
            Text("Làm lại")
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onBackToList, modifier = Modifier.fillMaxWidth()) {
            Text("Quay về danh sách")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewProfile() {
    val navController = rememberNavController()
    MaterialTheme {
        TestScreen(navController)
    }
}