package com.example.skynet.ui.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skynet.ui.theme.GlassCategorySelector
import com.example.skynet.ui.theme.glassmorphism

data class WeeklyPerformanceData(
    val activityPercentage: Float = 0f,
    val reservationsCount: Int = 0,
    val maxReservations: Int = 5,
    val dailyCompletion: List<Boolean> = List(7) { false }
)

@Composable
fun WeeklyPerformanceWidget(
    data: WeeklyPerformanceData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "Rendimiento Semanal",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111111),
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max), // Obliga a ambos a medir lo mismo
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Actividad Card
            GlassStatCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                title = "ACTIVIDAD"
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
                    val animatedProgress by animateFloatAsState(
                        targetValue = data.activityPercentage / 100f,
                        animationSpec = tween(durationMillis = 1000), label = ""
                    )
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFF6200FF),
                        strokeWidth = 6.dp,
                        trackColor = Color(0xFFE9E7FF),
                        strokeCap = StrokeCap.Round
                    )
                    Text(
                        text = "${data.activityPercentage.toInt()}%",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111111),
                            fontSize = 16.sp
                        )
                    )
                }
            }

            // Reservas Card
            GlassStatCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                title = "RESERVAS"
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${data.reservationsCount}",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF111111),
                            fontSize = 40.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val resProgress = (data.reservationsCount.toFloat() / data.maxReservations).coerceIn(0f, 1f)
                    val animatedResProgress by animateFloatAsState(
                        targetValue = resProgress,
                        animationSpec = tween(durationMillis = 1000), label = ""
                    )
                    LinearProgressIndicator(
                        progress = { animatedResProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = Color(0xFF6200FF),
                        trackColor = Color(0xFFE9E7FF),
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weekly Tracker Dots
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .glassmorphism(
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = Color.White,
                    borderColor = Color(0xFF6200FF).copy(alpha = 0.1f)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val days = listOf("L", "M", "M", "J", "V", "S", "D")
                days.forEachIndexed { index, day ->
                    DayDot(
                        day = day,
                        isCompleted = data.dailyCompletion.getOrElse(index) { false }
                    )
                }
            }
        }
    }
}

@Composable
fun DayDot(
    day: String,
    isCompleted: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    if (isCompleted) Color(0xFF6200FF) else Color(0xFFF0F0F4)
                )
                .then(
                    if (!isCompleted) Modifier.border(1.dp, Color(0xFF6200FF).copy(alpha = 0.1f), CircleShape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
        Text(
            text = day,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isCompleted) Color(0xFF6200FF) else Color(0xFF6B6B7B),
                fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}

@Composable
fun GlassStatCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .glassmorphism(
                backgroundColor = Color.White,
                borderColor = Color(0xFF6200FF).copy(alpha = 0.1f)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFF6B6B7B),
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

fun setupWeeklyPerformanceCompose(view: ComposeView, data: WeeklyPerformanceData) {
    view.apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            WeeklyPerformanceWidget(data = data)
        }
    }
}

fun setupGlassCategorySelector(
    view: ComposeView,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (category: String) -> Unit
) {
    view.apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            GlassCategorySelector(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected
            )
        }
    }
}
