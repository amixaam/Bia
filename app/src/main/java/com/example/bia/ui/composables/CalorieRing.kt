package com.example.bia.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalorieRing(
    consumed: Int,
    goal: Int,
    modifier: Modifier = Modifier
) {
    val arcAngleOutset = 55f
    val arcAngleSweep = 180f + arcAngleOutset * 2

    // how many degrees the circle is filled
    val fillAngle = (consumed.toFloat() / goal.toFloat()).coerceAtMost(1f) * arcAngleSweep

    val caloriesLeft = (goal - consumed).coerceAtLeast(0)

    val arcStrokeWidth = 16.dp

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
        Canvas(modifier = Modifier.size(200.dp)) {
            // empty ring
            drawArc(
                color = Color.LightGray.copy(alpha = 0.45f),
                startAngle = 180f - arcAngleOutset,
                sweepAngle = arcAngleSweep,
                useCenter = false,
                style = Stroke(width = arcStrokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // filled ring
            drawArc(
                color = Color(0xFF4CAF50),
                startAngle = 180f - arcAngleOutset,
                sweepAngle = fillAngle,
                useCenter = false,
                style = Stroke(width = arcStrokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

//        TODO: Show different text if over calorie limit
        Text(
            text = "$caloriesLeft\bkcal left",
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalorieRingPreview() {
    CalorieRing(consumed = 1140, goal = 2000)
}