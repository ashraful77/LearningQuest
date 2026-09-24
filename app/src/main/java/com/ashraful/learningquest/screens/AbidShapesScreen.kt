package com.ashraful.learningquest.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class ShapeItem(
    val name: String,
    val category: String,
    val description: String
)

@Composable
fun AbidShapesScreen(onBack: () -> Unit) {
    var index by remember { mutableIntStateOf(0) }

    val shapes = listOf(
        ShapeItem("Circle", "2D Shape", "A circle is round."),
        ShapeItem("Square", "2D Shape", "A square has 4 equal sides."),
        ShapeItem("Rectangle", "2D Shape", "A rectangle has 4 sides."),
        ShapeItem("Triangle", "2D Shape", "A triangle has 3 sides."),
        ShapeItem("Oval", "2D Shape", "An oval is round and stretched."),
        ShapeItem("Pentagon", "2D Shape", "A pentagon has 5 sides."),
        ShapeItem("Hexagon", "2D Shape", "A hexagon has 6 sides."),
        ShapeItem("Star", "2D Shape", "A star has points."),
        ShapeItem("Sphere", "3D Shape", "A sphere is round like a ball."),
        ShapeItem("Cube", "3D Shape", "A cube has 6 square faces."),
        ShapeItem("Cylinder", "3D Shape", "A cylinder has 2 circular ends."),
        ShapeItem("Cone", "3D Shape", "A cone has a circular base and a point.")
    )

    val current = shapes[index]

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFEAF7FF), Color.White, Color(0xFFFFF4DE))))
            .padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.fillMaxWidth().padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text("‹ Home", fontSize = 19.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
            Text(
                "🔷 Shapes",
                fontSize = 30.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                color = Color(0xFF1769AA)
            )
        }

        Text("Learn the names of shapes!", fontSize = 20.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(14.dp))

        Text(
            (index + 1).toString() + " / " + shapes.size,
            fontSize = 18.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = Color(0xFF65738A)
        )

        Spacer(Modifier.height(12.dp))

        Card(
            Modifier.fillMaxWidth().weight(1f),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Column(
                Modifier.fillMaxSize().padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    current.category,
                    fontSize = 17.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = if (current.category.startsWith("3D")) Color(0xFF8A5200) else Color(0xFF1769AA)
                )

                ShapeDrawing(index, Modifier.weight(1f).fillMaxWidth())

                Text(
                    current.name,
                    fontSize = 38.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                    color = Color(0xFF1769AA)
                )

                Spacer(Modifier.height(5.dp))
                Text(current.description, fontSize = 17.sp, color = Color(0xFF60758A))
                Spacer(Modifier.height(12.dp))
            }
        }

        Spacer(Modifier.height(14.dp))

        Button(
            onClick = { index = (index + 1) % shapes.size },
            modifier = Modifier.fillMaxWidth().height(62.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                if (index == shapes.lastIndex) "🔄 Start Again" else "Next Shape →",
                fontSize = 21.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }

        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun ShapeDrawing(index: Int, modifier: Modifier) {
    Canvas(modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val s = minOf(size.width, size.height) * 0.27f

        when (index) {
            0 -> drawCircle(Color(0xFF5DADE2), s, Offset(cx, cy))
            1 -> drawRoundRect(Color(0xFFFFC857), Offset(cx - s, cy - s), androidx.compose.ui.geometry.Size(s * 2, s * 2), CornerRadius(10f, 10f))
            2 -> drawRoundRect(Color(0xFF62C370), Offset(cx - s * 1.35f, cy - s * 0.8f), androidx.compose.ui.geometry.Size(s * 2.7f, s * 1.6f), CornerRadius(12f, 12f))
            3 -> drawPath(trianglePath(cx, cy, s), Color(0xFFFF6B6B))
            4 -> drawOval(Color(0xFFB084CC), Offset(cx - s * 1.35f, cy - s * 0.85f), androidx.compose.ui.geometry.Size(s * 2.7f, s * 1.7f))
            5 -> drawPath(polygonPath(cx, cy, s, 5), Color(0xFFFF9F43))
            6 -> drawPath(polygonPath(cx, cy, s, 6), Color(0xFF48C9B0))
            7 -> drawPath(starPath(cx, cy, s, 5), Color(0xFFFFC857))
            8 -> drawCircle(Brush.radialGradient(listOf(Color.White, Color(0xFF5DADE2), Color(0xFF1769AA)), Offset(cx - s * 0.35f, cy - s * 0.35f), s * 1.5f), s, Offset(cx, cy))
            9 -> drawCube(cx, cy, s)
            10 -> drawCylinder(cx, cy, s)
            11 -> drawCone(cx, cy, s)
        }
    }
}

private fun trianglePath(cx: Float, cy: Float, s: Float) = Path().apply {
    moveTo(cx, cy - s)
    lineTo(cx - s, cy + s)
    lineTo(cx + s, cy + s)
    close()
}

private fun polygonPath(cx: Float, cy: Float, s: Float, sides: Int) = Path().apply {
    for (i in 0 until sides) {
        val angle = -Math.PI / 2 + i * 2 * Math.PI / sides
        val x = cx + (kotlin.math.cos(angle) * s).toFloat()
        val y = cy + (kotlin.math.sin(angle) * s).toFloat()
        if (i == 0) moveTo(x, y) else lineTo(x, y)
    }
    close()
}

private fun starPath(cx: Float, cy: Float, s: Float, points: Int) = Path().apply {
    for (i in 0 until points * 2) {
        val angle = -Math.PI / 2 + i * Math.PI / points
        val radius = if (i % 2 == 0) s else s * 0.45f
        val x = cx + (kotlin.math.cos(angle) * radius).toFloat()
        val y = cy + (kotlin.math.sin(angle) * radius).toFloat()
        if (i == 0) moveTo(x, y) else lineTo(x, y)
    }
    close()
}

private fun DrawScope.drawCube(cx: Float, cy: Float, s: Float) {
    val top = Path().apply {
        moveTo(cx, cy - s); lineTo(cx + s, cy - s * 0.55f); lineTo(cx, cy - s * 0.1f); lineTo(cx - s, cy - s * 0.55f); close()
    }
    val left = Path().apply {
        moveTo(cx - s, cy - s * 0.55f); lineTo(cx, cy - s * 0.1f); lineTo(cx, cy + s); lineTo(cx - s, cy + s * 0.55f); close()
    }
    val right = Path().apply {
        moveTo(cx + s, cy - s * 0.55f); lineTo(cx, cy - s * 0.1f); lineTo(cx, cy + s); lineTo(cx + s, cy + s * 0.55f); close()
    }
    drawPath(top, Color(0xFF76D7C4))
    drawPath(left, Color(0xFF48C9B0))
    drawPath(right, Color(0xFF239B8A))
}

private fun DrawScope.drawCylinder(cx: Float, cy: Float, s: Float) {
    val width = s * 1.45f
    val height = s * 2f
    drawRoundRect(Color(0xFF5DADE2), Offset(cx - width, cy - height * 0.35f), androidx.compose.ui.geometry.Size(width * 2, height * 0.7f), CornerRadius(width, width))
    drawOval(Color(0xFF85C1E9), Offset(cx - width, cy - height * 0.55f), androidx.compose.ui.geometry.Size(width * 2, height * 0.42f))
    drawOval(Color(0xFF2E86C1), Offset(cx - width, cy + height * 0.2f), androidx.compose.ui.geometry.Size(width * 2, height * 0.42f))
}

private fun DrawScope.drawCone(cx: Float, cy: Float, s: Float) {
    val path = Path().apply {
        moveTo(cx, cy - s * 1.2f); lineTo(cx - s, cy + s * 0.65f); lineTo(cx + s, cy + s * 0.65f); close()
    }
    drawPath(path, Color(0xFFFF9F43))
    drawOval(Color(0xFFFFC477), Offset(cx - s, cy + s * 0.4f), androidx.compose.ui.geometry.Size(s * 2, s * 0.55f))
}
