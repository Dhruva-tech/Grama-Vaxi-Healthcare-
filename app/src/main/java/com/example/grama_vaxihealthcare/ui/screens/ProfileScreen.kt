package com.example.grama_vaxihealthcare.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.example.grama_vaxihealthcare.R
import com.example.grama_vaxihealthcare.navigation.Screen
import com.example.grama_vaxihealthcare.ui.components.ReadableLightSurface
import com.example.grama_vaxihealthcare.ui.theme.ErrorRed
import com.example.grama_vaxihealthcare.ui.theme.RuralGreen
import com.example.grama_vaxihealthcare.ui.theme.RuralGreenDark
import com.example.grama_vaxihealthcare.ui.theme.TextPrimary
import com.example.grama_vaxihealthcare.ui.theme.TextSecondary
import com.example.grama_vaxihealthcare.viewmodel.GramaVaxiViewModel

@Composable
fun ProfileScreen(viewModel: GramaVaxiViewModel, navController: NavController) {
    val farmer by viewModel.farmer.collectAsState()
    val currentLanguage = AppCompatDelegate.getApplicationLocales().get(0)?.language ?: "en"

    Scaffold(
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader(
                name = farmer?.name ?: "N/A",
                phoneNumber = farmer?.phoneNumber ?: "N/A",
                villageName = farmer?.villageName ?: "N/A"
            )

            Spacer(modifier = Modifier.height(24.dp))

            LanguageSettingsCard(
                currentLanguage = currentLanguage,
                onLanguageSelected = { languageTag ->
                    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageTag)
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LogoutCard(
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.Register.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Grama-Vaxi v1.0.0",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.88f),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    name: String,
    phoneNumber: String,
    villageName: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
        color = RuralGreenDark.copy(alpha = 0.90f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.profile),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.size(104.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 10.dp
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(22.dp),
                    tint = RuralGreen
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = name,
                color = Color.White,
                fontSize = 32.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = phoneNumber,
                color = Color.White.copy(alpha = 0.95f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Text(
                text = villageName,
                color = Color.White.copy(alpha = 0.90f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LanguageSettingsCard(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ReadableLightSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = RuralGreenDark,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = stringResource(R.string.language_settings),
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LanguageOptionChip(
                    text = stringResource(R.string.english),
                    selected = currentLanguage != "kn",
                    modifier = Modifier.weight(1f),
                    onClick = { onLanguageSelected("en") }
                )
                LanguageOptionChip(
                    text = stringResource(R.string.kannada),
                    selected = currentLanguage == "kn",
                    modifier = Modifier.weight(1f),
                    onClick = { onLanguageSelected("kn") }
                )
            }
        }
    }
}

@Composable
private fun LanguageOptionChip(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) RuralGreenDark else LanguageChipUnselected,
        tonalElevation = 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (selected) Color.White else TextPrimary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LogoutCard(onLogout: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onLogout),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ReadableLightSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = null,
                tint = ErrorRed,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = stringResource(R.string.logout),
                color = ErrorRed,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private val LanguageChipUnselected = Color(0xFFE7ECE4)
