package com.example.uetontop.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uetontop.navigation.Screen
import com.example.uetontop.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen2(navController: NavController) {
    var selectedGender by remember { mutableStateOf("Male") }
    var height by remember { mutableStateOf(00f) }
    var weight by remember { mutableStateOf(00f) }

    // palette
    val bg = Color(0xFFF2F4F7)          // nền tổng
    val ink = Color(0xFF0F172A)         // chữ/icon đậm
    val cardStroke = Color(0xFFE5E7EB)  // viền nhạt
    val primary = Color(0xFF111827)

    Scaffold(
        containerColor = bg,
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 44.dp, bottom = 40.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.Home.route) },
                    shape = CircleShape,
                    containerColor = Color(0xFF0F172A),
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(12.dp, CircleShape, clip = false)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.White
                    )
                }
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(horizontal = 25.dp, vertical = 40.dp)
        ) {
            // Tiêu đề 2 dòng
            Text(
                text = "Hãy cung cấp 1 số\nthông tin cơ bản",
                color = primary,
                fontSize = 25.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(16.dp))

            // Panel giữa theo mockup
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF2F4F7)
            ) {
                Column {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        GenderOption(
                            label = "Nam",
                            icon = Icons.Outlined.Male,
                            selected = selectedGender == "Male",
                            onClick = { selectedGender = "Male" },
                            ink = ink,
                            stroke = cardStroke,
                            modifier = Modifier.weight(1f)
                        )
                        GenderOption(
                            label = "Nữ",
                            icon = Icons.Outlined.Female,
                            selected = selectedGender == "Female",
                            onClick = { selectedGender = "Female" },
                            ink = ink,
                            stroke = cardStroke,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Text("Chiều cao", color = primary, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(20.dp))
                    LabeledSlider(
                        value = height,
                        onValueChange = { height = it },
                        valueLabel = "${height.toInt()}cm",
                        minLabel = "0cm",
                        maxLabel = "300cm",
                        startIcon = {
                            Image(
                                painter = painterResource(R.drawable.people300cm), // ảnh của bạn
                                contentDescription = null,
                                modifier = Modifier.size(25.dp)
                            )
                        },
                        endIcon = {
                            Image(
                                painter = painterResource(R.drawable.people300cm),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        valueRange = 00f..300f
                    )

                    Spacer(Modifier.height(25.dp))

                    Text("Cân nặng", color = primary, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(20.dp))
                    LabeledSlider(
                        value = weight,
                        onValueChange = { weight = it },
                        valueLabel = "${weight.toInt()}kg",
                        minLabel = "0kg",
                        maxLabel = "300kg",
                        startIcon = {
                            Image(
                                painter = painterResource(R.drawable.people300cm), // ảnh của bạn
                                contentDescription = null,
                                modifier = Modifier.size(25.dp)
                            )
                        },
                        endIcon = {
                            Image(
                                painter = painterResource(R.drawable.people300kg), // ảnh của bạn
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        valueRange = 00f..300f
                    )
                }
            }
        }
    }
}

@Composable
private fun GenderOption(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    ink: Color,
    stroke: Color,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        if (selected) ink.copy(alpha = .35f) else stroke, label = ""
    )
    val textColor by animateColorAsState(
        if (selected) ink else ink.copy(alpha = .55f), label = ""
    )
    val iconTint by animateColorAsState(
        if (selected) ink else ink.copy(alpha = .55f), label = ""
    )
    val shadow = if (selected) 6.dp else 0.dp

    Surface(
        modifier = modifier
            .height(112.dp)
            .shadow(shadow, RoundedCornerShape(16.dp), clip = false)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = 0.dp
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(44.dp)
                )
                if (selected) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = ink,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 40.dp, y = (-10).dp) // đẩy xa khỏi icon
                            .size(18.dp)
                    )
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(label, color = textColor, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun LabeledSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueLabel: String,
    minLabel: String,
    maxLabel: String,
    startIcon: @Composable () -> Unit,
    endIcon: @Composable () -> Unit,
    valueRange: ClosedFloatingPointRange<Float>
) {
    val track = Color(0xFFE5E7EB)
    val active = Color(0xFF0F172A)

    // padding ngang mà phần Slider đang dùng (phải khớp với Row bên dưới)
    val sliderHPadding = 12.dp

    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val totalWidth = this.maxWidth
        val available = totalWidth - sliderHPadding * 2
        val frac = ((value - valueRange.start) /
                (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)

        // đo width của nhãn để canh giữa trên thumb
        var labelWidth by remember { mutableStateOf(0) }
        val labelWidthDp = with(LocalDensity.current) { labelWidth.toDp() }

        // X của tâm nhãn: startPadding + frac * available
        val centerX = sliderHPadding + available * frac
        // offset trái của nhãn = tâm - 1/2 nhãn (clamp trong [0, totalWidth - labelWidth])
        val leftX = remember(centerX, labelWidthDp, totalWidth) {
            val minX = 0.dp
            val maxX = (totalWidth - labelWidthDp).coerceAtLeast(0.dp)
            (centerX - labelWidthDp / 2f)
                .coerceIn(minX, maxX)
        }

        // Nhãn động bám theo thumb
        Text(
            valueLabel,
            color = active,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .offset(x = leftX, y = 0.dp)
                .padding(bottom = 4.dp) // khoảng cách so với track
                .onGloballyPositioned { labelWidth = it.size.width }
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        startIcon()
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = sliderHPadding),
            colors = SliderDefaults.colors(
                activeTrackColor = active,
                inactiveTrackColor = track,
                thumbColor = Color.White
            )
        )
        endIcon()
    }

    Row(Modifier.fillMaxWidth()) {
        Text(minLabel, color = Color(0xFF6B7280))
        Spacer(Modifier.weight(1f))
        Text(maxLabel, color = Color(0xFF6B7280))
    }
}


/* ------------ Preview ------------- */

@Preview(showBackground = true)
@Composable
private fun PreviewScreen2() {
    val navController = rememberNavController()
    MaterialTheme {
        OnboardingScreen2(navController)
    }
}
