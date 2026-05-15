package com.example.grama_vaxihealthcare.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.grama_vaxihealthcare.R

val ReadableLightSurface = Color.White.copy(alpha = 0.90f)
val ReadableNavSurface = Color.White.copy(alpha = 0.94f)
val ReadableTopBarSurface = Color.Black.copy(alpha = 0.48f)
val ReadableDarkOverlay = Color.Black.copy(alpha = 0.45f)

@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.grama_vaxi_global_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(8.dp),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ReadableDarkOverlay)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Black.copy(alpha = 0.50f),
                            0.5f to Color.Black.copy(alpha = 0.35f),
                            1.0f to Color.Black.copy(alpha = 0.60f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun readableTopAppBarColors() = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
    containerColor = ReadableTopBarSurface,
    titleContentColor = Color.White,
    navigationIconContentColor = Color.White,
    actionIconContentColor = Color.White
)

@Composable
fun readableTextFieldColors() = androidx.compose.material3.TextFieldDefaults.colors(
    focusedContainerColor = Color.White.copy(alpha = 0.94f),
    unfocusedContainerColor = Color.White.copy(alpha = 0.94f),
    disabledContainerColor = Color.White.copy(alpha = 0.94f),
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent
)

@Composable
fun readableOutlinedTextFieldColors() = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White.copy(alpha = 0.94f),
    unfocusedContainerColor = Color.White.copy(alpha = 0.94f),
    disabledContainerColor = Color.White.copy(alpha = 0.94f)
)
