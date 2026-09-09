package com.edtech.ranks.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Deprecated: Use standard Material 3 ElevatedCard instead
// This alias is temporarily kept to avoid compilation errors during refactoring
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    backgroundColor: Color = Color.Unspecified, // Ignored
    borderColor: Color = Color.Unspecified, // Ignored
    content: @Composable() (BoxScope.() -> Unit)
) {
    ElevatedCard(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        )
    ) {
        androidx.compose.foundation.layout.Box {
            content()
        }
    }
}

// Stub out glowEffect so we don't break compilation during transition
fun Modifier.glowEffect(
    color: Color = Color.Unspecified,
    radius: Dp = 0.dp
) = this
