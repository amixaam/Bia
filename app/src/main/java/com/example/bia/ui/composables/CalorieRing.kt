package com.example.bia.ui.composables

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue

@Composable
fun CalorieRing(
    consumed: Int,
    goal: Int,
    modifier: Modifier = Modifier
) {
    val arcAngleOutset = 55f
    val arcAngleSweep = 180f + arcAngleOutset * 2

    val sweepAnimationEasing = CubicBezierEasing(0.16f, 1f, 0.3f, 1f)

    // how many degrees the circle is filled
    val fillAngle = (consumed.toFloat() / goal.toFloat()).coerceAtMost(1f) * arcAngleSweep
    val animatedFillAngle by animateFloatAsState(
        targetValue = fillAngle,
        animationSpec = tween(durationMillis = 750, easing = sweepAnimationEasing),
        label = "CalorieRingAnimation"
    )

    val caloriesLeft = (goal - consumed).coerceAtLeast(0)
    val caloriesExcess = if (goal - consumed < 0) (goal - consumed).absoluteValue else 0

    val excessFillAngle = (caloriesExcess.toFloat() / goal.toFloat()).coerceAtMost(1f) * arcAngleSweep
    val animatedExcessFillAngle by animateFloatAsState(
        targetValue = excessFillAngle,
        animationSpec = tween(durationMillis = 750, easing = sweepAnimationEasing),
        label = "ExcessCalorieRingAnimation"

    )

    val arcStrokeWidth = 16.dp
    val spacing = (arcStrokeWidth / 2)

    val colorScheme = MaterialTheme.colorScheme

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
                .size(200.dp)
//                .border(width = 1.dp, color = colorScheme.error)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // empty ring
            drawArc(
                color = colorScheme.surfaceVariant,
                startAngle = 180f - arcAngleOutset,
                sweepAngle = arcAngleSweep,
                useCenter = false,
                topLeft = Offset(spacing.toPx(), spacing.toPx()),
                size = Size(
                    width = size.width - arcStrokeWidth.toPx(),
                    height = size.height - arcStrokeWidth.toPx()
                ),
                style = Stroke(width = arcStrokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // filled ring
            drawArc(
                color = colorScheme.primary,
                startAngle = 180f - arcAngleOutset,
                sweepAngle = animatedFillAngle,
                useCenter = false,
                topLeft = Offset(spacing.toPx(), spacing.toPx()),
                size = Size(
                    width = size.width - arcStrokeWidth.toPx(),
                    height = size.height - arcStrokeWidth.toPx()
                ),
                style = Stroke(width = arcStrokeWidth.toPx(), cap = StrokeCap.Round)
            )

            // overfilled ring
            drawArc(
                color = colorScheme.error,
                startAngle = 180f - arcAngleOutset,
                sweepAngle = animatedExcessFillAngle,
                useCenter = false,
                topLeft = Offset(spacing.toPx(), spacing.toPx()),
                size = Size(
                    width = size.width - arcStrokeWidth.toPx(),
                    height = size.height - arcStrokeWidth.toPx()
                ),
                style = Stroke(width = arcStrokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

//        TODO: Animate the text into bigger size, red (once) and a little shake every so often if over limit
        val leftOrOver = if (excessFillAngle == 0f) "left" else "over"
        val calorieDisplay = if (excessFillAngle == 0f) caloriesLeft else caloriesExcess
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$calorieDisplay",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface
            )
            Text(
                text = "kcal $leftOrOver",
                style = MaterialTheme.typography.labelLarge,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalorieRingPreview() {
    CalorieRing(consumed = 1140, goal = 2000)
}