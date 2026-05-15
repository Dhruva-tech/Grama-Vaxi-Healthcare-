package com.example.grama_vaxihealthcare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grama_vaxihealthcare.R
import com.example.grama_vaxihealthcare.data.entity.DiseaseReport
import com.example.grama_vaxihealthcare.data.entity.Vaccination
import com.example.grama_vaxihealthcare.ui.components.ReadableLightSurface
import com.example.grama_vaxihealthcare.ui.components.readableOutlinedTextFieldColors
import com.example.grama_vaxihealthcare.ui.components.readableTopAppBarColors
import com.example.grama_vaxihealthcare.ui.theme.ErrorRed
import com.example.grama_vaxihealthcare.ui.theme.RuralGreenDark
import com.example.grama_vaxihealthcare.viewmodel.GramaVaxiViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDetailScreen(animalId: Long, viewModel: GramaVaxiViewModel, navController: NavController) {
    val animals by viewModel.allAnimals.collectAsState()
    val animal = animals.find { it.id == animalId }
    
    LaunchedEffect(animalId) {
        viewModel.setSelectedAnimal(animalId)
    }
    
    val vaccinations by viewModel.selectedAnimalVaccinations.collectAsState()
    val reports by viewModel.selectedAnimalReports.collectAsState()
    
    var showAddVaccinationDialog by remember { mutableStateOf(false) }
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    if (showAddVaccinationDialog) {
        AddVaccinationDialog(
            onDismiss = { showAddVaccinationDialog = false },
            onConfirm = { name, date, nextDate ->
                viewModel.recordVaccination(animalId, name, date, nextDate)
                showAddVaccinationDialog = false
            }
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        animal?.name ?: "Animal Details", 
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = readableTopAppBarColors()
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddVaccinationDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Medical History", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        animal?.let { animalData ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Text(
                        text = "Basic Information", 
                        style = MaterialTheme.typography.titleLarge, 
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = ReadableLightSurface)
                    ) {
                        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            DetailRow(label = stringResource(R.string.animal_type), value = animalData.type)
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            DetailRow(label = stringResource(R.string.breed), value = animalData.breed)
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            DetailRow(label = stringResource(R.string.age), value = "${animalData.age} years")
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            DetailRow(label = stringResource(R.string.gender), value = animalData.gender)
                        }
                    }
                }

                item {
                    Text(
                        text = "Vaccination History", 
                        style = MaterialTheme.typography.titleLarge, 
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                if (vaccinations.isEmpty()) {
                    item {
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = ReadableLightSurface)
                        ) {
                            Text(
                                "No medical records available.", 
                                modifier = Modifier.padding(24.dp),
                                style = MaterialTheme.typography.bodyMedium, 
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(vaccinations) { vaccination ->
                        VaccinationItem(vaccination, sdf)
                    }
                }

                item {
                    Text(
                        text = "Disease Reports", 
                        style = MaterialTheme.typography.titleLarge, 
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                if (reports.isEmpty()) {
                    item {
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = ReadableLightSurface)
                        ) {
                            Text(
                                "No previous disease reports found.", 
                                modifier = Modifier.padding(24.dp),
                                style = MaterialTheme.typography.bodyMedium, 
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(reports) { report ->
                        ReportItem(report, sdf)
                    }
                }

                item {
                    Text(
                        text = stringResource(R.string.health_notes), 
                        style = MaterialTheme.typography.titleLarge, 
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = ReadableLightSurface)
                    ) {
                        Text(
                            text = animalData.healthNotes.ifBlank { "No additional notes available." },
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun VaccinationItem(vaccination: Vaccination, sdf: SimpleDateFormat) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = ReadableLightSurface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = vaccination.vaccineName, 
                fontWeight = FontWeight.Bold, 
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Administered: ", 
                    style = MaterialTheme.typography.bodySmall, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = sdf.format(Date(vaccination.dateAdministered)), 
                    style = MaterialTheme.typography.bodySmall, 
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Next Due: ", 
                    style = MaterialTheme.typography.bodySmall, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = sdf.format(Date(vaccination.nextDueDate)), 
                    color = MaterialTheme.colorScheme.primary, 
                    fontWeight = FontWeight.Bold, 
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun ReportItem(report: DiseaseReport, sdf: SimpleDateFormat) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = ReadableLightSurface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.disease_report),
                    fontWeight = FontWeight.Bold,
                    color = ErrorRed,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = sdf.format(Date(report.reportDate)),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${stringResource(R.string.symptoms)}:",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = report.symptoms,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value, 
            style = MaterialTheme.typography.bodyMedium, 
            fontWeight = FontWeight.Bold,
            color = RuralGreenDark
        )
    }
}

@Composable
fun AddVaccinationDialog(onDismiss: () -> Unit, onConfirm: (String, Long, Long) -> Unit) {
    var name by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record Vaccination", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Vaccine Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = readableOutlinedTextFieldColors()
                )
                Text(
                    "Administered Today: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())}", 
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val date = System.currentTimeMillis()
                    val nextDate = date + (90L * 24 * 60 * 60 * 1000)
                    onConfirm(name, date, nextDate)
                },
                enabled = name.isNotBlank(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Record")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
