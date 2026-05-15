package com.example.grama_vaxihealthcare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grama_vaxihealthcare.R
import com.example.grama_vaxihealthcare.data.entity.CampAlert
import com.example.grama_vaxihealthcare.navigation.Screen
import com.example.grama_vaxihealthcare.ui.components.ReadableLightSurface
import com.example.grama_vaxihealthcare.ui.components.readableTopAppBarColors
import com.example.grama_vaxihealthcare.ui.theme.ErrorRed
import com.example.grama_vaxihealthcare.ui.theme.RuralGreen
import com.example.grama_vaxihealthcare.ui.theme.RuralGreenDark
import com.example.grama_vaxihealthcare.ui.theme.SuccessGreen
import com.example.grama_vaxihealthcare.ui.theme.WarningOrange
import com.example.grama_vaxihealthcare.viewmodel.GramaVaxiViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: GramaVaxiViewModel, navController: NavController) {
    val farmer by viewModel.farmer.collectAsState()
    val animals by viewModel.allAnimals.collectAsState()
    val alerts by viewModel.allCampAlerts.collectAsState()
    val upcomingShotsCount by viewModel.upcomingShotsCount.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            modifier = Modifier.size(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Agriculture,
                                    contentDescription = "Logo",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.namaste, farmer?.name ?: "Farmer"),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.88f)
                            )
                        }
                    }
                },
                colors = readableTopAppBarColors()
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        title = stringResource(R.string.total_animals),
                        count = animals.size.toString(),
                        icon = Icons.Default.Pets,
                        modifier = Modifier.weight(1f),
                        colors = listOf(SuccessGreen, RuralGreen)
                    )
                    StatCard(
                        title = stringResource(R.string.upcoming_shots),
                        count = upcomingShotsCount.toString(),
                        icon = Icons.Default.Vaccines,
                        modifier = Modifier.weight(1f),
                        colors = listOf(WarningOrange, Color(0xFFF57C00))
                    )
                }
            }

            item {
                Text(
                    text = stringResource(R.string.quick_actions),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            item {
                QuickActions(navController)
            }

            item {
                Text(
                    text = stringResource(R.string.government_camps),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            if (alerts.isEmpty()) {
                item {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = ReadableLightSurface)
                    ) {
                        Text(
                            text = stringResource(R.string.no_camps_yet),
                            modifier = Modifier.padding(24.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(alerts) { camp ->
                    CampAlertCard(camp)
                }
            }
            
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun CampAlertCard(camp: CampAlert) {
    val context = LocalContext.current
    val title = remember(camp.title) { 
        try { context.getString(context.resources.getIdentifier(camp.title, "string", context.packageName)) } 
        catch (e: Exception) { camp.title }
    }
    val location = remember(camp.location) { 
        try { context.getString(context.resources.getIdentifier(camp.location, "string", context.packageName)) }
        catch (e: Exception) { camp.location }
    }
    val doctor = remember(camp.doctorName) { 
        try { context.getString(context.resources.getIdentifier(camp.doctorName, "string", context.packageName)) }
        catch (e: Exception) { camp.doctorName }
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = ReadableLightSurface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationImportant,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp).size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = RuralGreenDark
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(icon = Icons.Default.Event, label = stringResource(R.string.date), value = camp.date)
            Spacer(modifier = Modifier.height(12.dp))
            InfoRow(icon = Icons.Default.Place, label = stringResource(R.string.location), value = location)
            Spacer(modifier = Modifier.height(12.dp))
            InfoRow(icon = Icons.Default.Person, label = stringResource(R.string.doctor), value = doctor)
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StatCard(title: String, count: String, icon: ImageVector, modifier: Modifier, colors: List<Color>) {
    ElevatedCard(
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors))
                .padding(20.dp)
        ) {
            Text(
                text = title.uppercase(Locale.getDefault()),
                modifier = Modifier.align(Alignment.TopStart),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.8f),
                lineHeight = 14.sp
            )

            Icon(
                icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.2f),
                modifier = Modifier.size(48.dp).align(Alignment.TopEnd)
            )
            
            Text(
                text = count,
                modifier = Modifier.align(Alignment.BottomStart),
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
    }
}

@Composable
fun QuickActions(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ActionButton(
            label = stringResource(R.string.add_animal), 
            icon = Icons.Default.Pets, 
            color = RuralGreen, 
            modifier = Modifier.weight(1f)
        ) {
            navController.navigate(Screen.AddAnimal.route)
        }
        ActionButton(
            label = stringResource(R.string.disease_report), 
            icon = Icons.Default.MedicalServices, 
            color = ErrorRed, 
            modifier = Modifier.weight(1f)
        ) {
            navController.navigate(Screen.DiseaseReport.route)
        }
    }
}

@Composable
fun ActionButton(label: String, icon: ImageVector, color: Color, modifier: Modifier, onClick: () -> Unit) {
    ElevatedCard(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = ReadableLightSurface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp).fillMaxWidth()
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(64.dp)
            ) {
                Icon(icon, contentDescription = label, tint = color, modifier = Modifier.padding(16.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primaryContainer,
                maxLines = 1
            )
        }
    }
}
