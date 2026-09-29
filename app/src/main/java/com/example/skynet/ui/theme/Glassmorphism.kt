package com.example.skynet.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb

/**
 * Modificador para aplicar un efecto de Glassmorphism mejorado.
 */
fun Modifier.glassmorphism(
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    backgroundColor: Color = Color(0xFF1A1625).copy(alpha = 0.8f),
    borderColor: Color = Color(0xFF8B5CF6).copy(alpha = 0.15f),
    borderWidth: Dp = 1.dp,
    blurRadius: Dp = 0.dp
): Modifier = this
    .then(if (blurRadius > 0.dp) Modifier.blur(blurRadius) else Modifier)
    .clip(shape)
    .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                backgroundColor.copy(alpha = backgroundColor.alpha * 1.2f),
                backgroundColor.copy(alpha = backgroundColor.alpha * 0.4f)
            )
        )
    )
    .border(borderWidth, borderColor, shape)

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .glassmorphism()
    ) {
        content()
    }
}
