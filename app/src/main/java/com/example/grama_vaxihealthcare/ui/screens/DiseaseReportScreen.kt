package com.example.grama_vaxihealthcare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.grama_vaxihealthcare.navigation.Screen
import com.example.grama_vaxihealthcare.ui.components.ReadableLightSurface
import com.example.grama_vaxihealthcare.ui.components.readableTopAppBarColors
import com.example.grama_vaxihealthcare.ui.theme.RuralGreen
import com.example.grama_vaxihealthcare.viewmodel.GramaVaxiViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiseaseReportScreen(viewModel: GramaVaxiViewModel, navController: NavController) {
    var symptoms by remember { mutableStateOf("") }
    val animals by viewModel.allAnimals.collectAsState()
    var selectedAnimalId by remember { mutableStateOf(-1L) }
    var expanded by remember { mutableStateOf(false) }
    val selectAnimalText = stringResource(R.string.select_animal)
    var selectedAnimalName by remember { mutableStateOf(selectAnimalText) }
    val isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val reportSubmittedMessage = stringResource(R.string.report_submitted)

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.disease_report),
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.report_health_issue), 
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = ReadableLightSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = if (selectedAnimalId == -1L) selectAnimalText else selectedAnimalName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(selectAnimalText) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF2E7D32),
                                focusedLabelColor = Color(0xFF2E7D32)
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            animals.forEach { animal ->
                                DropdownMenuItem(
                                    text = { Text(animal.name) },
                                    onClick = {
                                        selectedAnimalId = animal.id
                                        selectedAnimalName = animal.name
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = symptoms,
                        onValueChange = { symptoms = it },
                        label = { Text(stringResource(R.string.symptoms)) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2E7D32),
                            focusedLabelColor = Color(0xFF2E7D32)
                        )
                    )
                }
            }

            Button(
                onClick = {
                    viewModel.submitDiseaseReport(selectedAnimalId, symptoms, null)
                    symptoms = ""
                    selectedAnimalId = -1L
                    selectedAnimalName = selectAnimalText
                    scope.launch {
                        snackbarHostState.showSnackbar(reportSubmittedMessage)
                        navController.navigate(Screen.Inbox.route)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = selectedAnimalId != -1L && symptoms.isNotBlank() && !isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RuralGreen)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(stringResource(R.string.submit_report), style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
