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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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

// ---------- CONTENT: màn chọn test (giống trước, rút gọn) ----------
@Composable
private fun TestSelectContent(
    onNext: (String) -> Unit
) {
    val tests = remember {
        listOf(
            TestItem("BECK", "Test 1: BECK"),
            TestItem("PHQ-9", "Test 2: PHQ-9"),
            TestItem("DASS 21", "Test 3: DASS 21"),
            TestItem("more1", "..."),
            TestItem("more2", "...")
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
                text = "Next",
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
            "Select the appropriate test",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold, color = Color(0xFF1E2330)
            )
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Please choose the mental health test that is right for you so we can better understand your current condition.",
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
                    Question("q1", "Little interest or pleasure in doing things", emptyList()),
                    Question("q2", "Feeling down, depressed, or hopeless", emptyList()),
                    Question(
                        "q3",
                        "Trouble falling or staying asleep, or sleeping too much",
                        emptyList()
                    ),
                    Question("q4", "Feeling tired or having little energy", emptyList()),
                    Question("q5", "Poor appetite or overeating", emptyList()),
                    Question(
                        "q6",
                        "Feeling bad about yourself—or that you are a failure or have let yourself or your family down",
                        emptyList()
                    ),
                    Question(
                        "q7",
                        "Trouble concentrating on things, such as reading the newspaper or watching television",
                        emptyList()
                    ),
                    Question(
                        "q8",
                        "Moving or speaking so slowly that other people could have noticed? Or the opposite—being so fidgety or restless that you have been moving around a lot more than usual",
                        emptyList()
                    ),
                    Question(
                        "q9",
                        "Thoughts that you would be better off dead or of hurting yourself in some way",
                        emptyList()
                    )
                ),
                listOf(
                    "Not at all",                 // 0 điểm
                    "Several days",               // 1 điểm
                    "More than half the days",    // 2 điểm
                    "Nearly every day"            // 3 điểm
                ),
                3 // max mỗi câu
            )

            "DASS 21" -> Triple(
                listOf(
                    Question("q1","I found it hard to wind down", emptyList()),
                    Question("q2","I was aware of dryness of my mouth", emptyList()),
                    Question("q3","I couldn't seem to experience any positive feeling at all", emptyList()),
                    Question("q4","I experienced breathing difficulty (e.g., excessively rapid breathing, breathlessness in the absence of physical exertion)", emptyList()),
                    Question("q5","I found it difficult to work up the initiative to do things", emptyList()),
                    Question("q6","I tended to over-react to situations", emptyList()),
                    Question("q7","I experienced trembling (e.g., in the hands)", emptyList()),
                    Question("q8","I felt that I was using a lot of nervous energy", emptyList()),
                    Question("q9","I was worried about situations in which I might panic and make a fool of myself", emptyList()),
                    Question("q10","I felt that I had nothing to look forward to", emptyList()),
                    Question("q11","I found myself getting agitated", emptyList()),
                    Question("q12","I found it difficult to relax", emptyList()),
                    Question("q13","I felt down-hearted and blue", emptyList()),
                    Question("q14","I was intolerant of anything that kept me from getting on with what I was doing", emptyList()),
                    Question("q15","I felt I was close to panic", emptyList()),
                    Question("q16","I was unable to become enthusiastic about anything", emptyList()),
                    Question("q17","I felt I wasn't worth much as a person", emptyList()),
                    Question("q18","I felt that I was rather touchy", emptyList()),
                    Question("q19","I was aware of the action of my heart in the absence of physical exertion (e.g., sense of heart rate increase, heart missing a beat)", emptyList()),
                    Question("q20","I felt scared without any good reason", emptyList()),
                    Question("q21","I felt that life was meaningless", emptyList())
                ),
                listOf(
                    "Did not apply to me at all",                                      // 0
                    "Applied to me to some degree, or some of the time",               // 1
                    "Applied to me to a considerable degree, or a good part of time",  // 2
                    "Applied to me very much, or most of the time"                     // 3
                ),
                3
            )

            "BECK" -> Triple(
                listOf(
                    Question("q1","Sadness", listOf(
                        "I do not feel sad.",
                        "I feel sad much of the time.",
                        "I am sad all the time.",
                        "I am so sad or unhappy that I can’t stand it."
                    )),
                    Question("q2","Pessimism", listOf(
                        "I am not discouraged about my future.",
                        "I feel more discouraged about my future than I used to.",
                        "I do not expect things to work out for me.",
                        "I feel my future is hopeless and will only get worse."
                    )),
                    Question("q3","Past Failure", listOf(
                        "I do not feel like a failure.",
                        "I have failed more than I should have.",
                        "As I look back, I see a lot of failures.",
                        "I feel I am a total failure."
                    )),
                    Question("q4","Loss of Pleasure", listOf(
                        "I get as much pleasure as I ever did from the things I enjoy.",
                        "I don’t enjoy things as much as I used to.",
                        "I get very little pleasure from the things I used to enjoy.",
                        "I can’t get any pleasure from the things I used to enjoy."
                    )),
                    Question("q5","Guilty Feelings", listOf(
                        "I don’t feel particularly guilty.",
                        "I feel guilty over many things I have done or should have done.",
                        "I feel quite guilty most of the time.",
                        "I feel guilty all of the time."
                    )),
                    Question("q6","Punishment Feelings", listOf(
                        "I don’t feel I am being punished.",
                        "I feel I may be punished.",
                        "I expect to be punished.",
                        "I feel I am being punished."
                    )),
                    Question("q7","Self-Dislike", listOf(
                        "I feel the same about myself as ever.",
                        "I have lost confidence in myself.",
                        "I am disappointed in myself.",
                        "I dislike myself."
                    )),
                    Question("q8","Self-Criticalness", listOf(
                        "I don’t criticize or blame myself more than usual.",
                        "I am more critical of myself than I used to be.",
                        "I criticize myself for all of my faults.",
                        "I blame myself for everything bad that happens."
                    )),
                    Question("q9","Suicidal Thoughts or Wishes", listOf(
                        "I don’t have any thoughts of killing myself.",
                        "I have thoughts of killing myself, but I would not carry them out.",
                        "I would like to kill myself.",
                        "I would kill myself if I had the chance."
                    )),
                    Question("q10","Crying", listOf(
                        "I don’t cry any more than I used to.",
                        "I cry more than I used to.",
                        "I cry over every little thing.",
                        "I feel like crying, but I can’t."
                    )),
                    Question("q11","Agitation", listOf(
                        "I am no more restless or wound up than usual.",
                        "I feel more restless or wound up than usual.",
                        "I am so restless or agitated that it’s hard to stay still.",
                        "I am so restless or agitated that I have to keep moving or doing something."
                    )),
                    Question("q12","Loss of Interest", listOf(
                        "I have not lost interest in other people or activities.",
                        "I am less interested in other people or things than before.",
                        "I have lost most of my interest in other people or things.",
                        "It’s hard to get interested in anything."
                    )),
                    Question("q13","Indecisiveness", listOf(
                        "I make decisions about as well as ever.",
                        "I find it more difficult to make decisions than usual.",
                        "I have much greater difficulty in making decisions than I used to.",
                        "I have trouble making any decisions."
                    )),
                    Question("q14","Worthlessness", listOf(
                        "I do not feel I am worthless.",
                        "I don’t consider myself as worthwhile and useful as I used to.",
                        "I feel more worthless as compared to other people.",
                        "I feel utterly worthless."
                    )),
                    Question("q15","Loss of Energy", listOf(
                        "I have as much energy as ever.",
                        "I have less energy than I used to have.",
                        "I don’t have enough energy to do very much.",
                        "I don’t have enough energy to do anything."
                    )),
                    Question("q16","Changes in Sleeping Pattern", listOf(
                        "I have not experienced any change in my sleeping pattern.",
                        "I sleep somewhat more OR somewhat less than usual.",
                        "I sleep a lot more OR a lot less than usual.",
                        "I sleep most of the day OR I wake up 1–2 hours early and can’t get back to sleep."
                    )),
                    Question("q17","Irritability", listOf(
                        "I am no more irritable than usual.",
                        "I am more irritable than usual.",
                        "I am much more irritable than usual.",
                        "I am irritable all the time."
                    )),
                    Question("q18","Changes in Appetite", listOf(
                        "I have not experienced any change in my appetite.",
                        "My appetite is somewhat less than usual OR somewhat greater than usual.",
                        "My appetite is much less than before OR much greater than before.",
                        "I have no appetite at all OR I want to eat all the time."
                    )),
                    Question("q19","Concentration Difficulty", listOf(
                        "I can concentrate as well as ever.",
                        "I can’t concentrate as well as usual.",
                        "It’s hard to keep my mind on anything for very long.",
                        "I find I can’t concentrate on anything."
                    )),
                    Question("q20","Tiredness or Fatigue", listOf(
                        "I am no more tired or fatigued than usual.",
                        "I get more tired or fatigued more easily than usual.",
                        "I am too tired or fatigued to do a lot of the things I used to do.",
                        "I am too tired or fatigued to do most of the things I used to do."
                    )),
                    Question("q21","Loss of Interest in Sex", listOf(
                        "I have not noticed any recent change in my interest in sex.",
                        "I am less interested in sex than I used to be.",
                        "I am much less interested in sex now.",
                        "I have lost interest in sex completely."
                    ))
                ),
                emptyList(), // không dùng option chung cho BDI-II
                3
            )

            else -> Triple(
                // fallback: 10 câu giả lập nếu testId chưa được map
                List(10) { i -> Question("q$i", "Question ${i + 1}: Lorem ipsum?", emptyList()) },
                listOf("Strongly disagree", "Disagree", "Agree", "Strongly agree"),
                3
            )
        }
    }
    val total = questions.size
    var index by remember { mutableIntStateOf(0) }
    var selections by remember { mutableStateOf(MutableList<Int?>(total) { null }) }
    val maxScore = total * maxPerItem

    // Header progress
    Column(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                "Question ${index + 1}/$total",
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
                text = if (index == total - 1) "Finish" else "Next",
                enabled = selections[index] != null,
                onClick = {
                    if (index < total - 1) index++
                    else {
                        // tính điểm: cộng chỉ số lựa chọn (0..3), null => 0
                        val raw = selections.sumOf { it ?: 0 }   // 0..(3*items)
                        val isDASS21 = testId.equals("DASS 21", true)

                        val score = if (isDASS21) raw * 2 else raw
                        val maxScore = questions.size * maxPerItem * if (isDASS21) 2 else 1

                        onFinish(score, maxScore)
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
        percent < 0.25f -> "Minimal"
        percent < 0.50f -> "Mild"
        percent < 0.75f -> "Moderate"
        else -> "Severe"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Your result",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.height(12.dp))

        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Test: $testId", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(6.dp))
                Text("Score: $score / $maxScore", style = MaterialTheme.typography.titleMedium)
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
                    "Level: $level",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "This is a quick screening result, not a diagnosis.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF8D93A1)),
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        Button(onClick = onRetake, modifier = Modifier.fillMaxWidth()) {
            Text("Retake")
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onBackToList, modifier = Modifier.fillMaxWidth()) {
            Text("Back to test list")
        }
    }
}