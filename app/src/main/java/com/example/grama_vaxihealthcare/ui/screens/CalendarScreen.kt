package com.example.grama_vaxihealthcare.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grama_vaxihealthcare.R
import com.example.grama_vaxihealthcare.data.entity.Animal
import com.example.grama_vaxihealthcare.data.entity.VaccinationSchedule
import com.example.grama_vaxihealthcare.ui.components.ReadableLightSurface
import com.example.grama_vaxihealthcare.ui.components.readableTextFieldColors
import com.example.grama_vaxihealthcare.ui.components.readableTopAppBarColors
import com.example.grama_vaxihealthcare.ui.theme.RuralGreen
import com.example.grama_vaxihealthcare.ui.theme.RuralGreenDark
import com.example.grama_vaxihealthcare.viewmodel.GramaVaxiViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: GramaVaxiViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val animals by viewModel.filteredAnimals.collectAsState()
    val allSchedules by viewModel.allSchedules.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.calendar),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                colors = readableTopAppBarColors()
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.setSearchQuery(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (animals.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (searchQuery.isBlank()) stringResource(R.string.no_animals_yet) else "No matching animals found",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    items(animals) { animal ->
                        val animalSchedules = allSchedules.filter { it.animalId == animal.id }
                        AnimalVaccinationCard(
                            animal = animal,
                            schedules = animalSchedules,
                            onUpdateDate = { schedule, newDate ->
                                viewModel.updateVaccineDate(schedule, newDate)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp)),
        placeholder = { Text(stringResource(R.string.search_animals)) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        colors = readableTextFieldColors(),
        singleLine = true
    )
}

@Composable
fun AnimalVaccinationCard(
    animal: Animal,
    schedules: List<VaccinationSchedule>,
    onUpdateDate: (VaccinationSchedule, Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.elevatedCardColors(
            containerColor = ReadableLightSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = animal.type,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = animal.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RuralGreenDark
                    )
                    Text(
                        text = stringResource(R.string.shots_scheduled, schedules.size),
                        fontSize = 12.sp,
                        color = RuralGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    val nextDue = schedules.minByOrNull { it.date }
                    nextDue?.let {
                        Text(
                            text = stringResource(R.string.days_to_go, it.daysRemaining),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier
                            .rotate(rotationState)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    schedules.sortedBy { it.date }.forEach { schedule ->
                        VaccineItem(
                            schedule = schedule,
                            onDateChange = { newDate -> onUpdateDate(schedule, newDate) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccineItem(schedule: VaccinationSchedule, onDateChange: (Long) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = schedule.date
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onDateChange(it) }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = getLocalizedVaccineName(schedule.vaccineName),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = RuralGreenDark
            )
            Text(
                text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(schedule.date)),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.days_to_go, schedule.daysRemaining),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = RuralGreen
            )
        }

        IconButton(onClick = { showDatePicker = true }) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Edit Date",
                tint = RuralGreenDark,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun getLocalizedVaccineName(name: String): String {
    return when (name.lowercase()) {
        "nobivac" -> stringResource(R.string.nobivac)
        "recombitek" -> stringResource(R.string.recombitek)
        "canigen" -> stringResource(R.string.canigen)
        "vanguard" -> stringResource(R.string.vanguard)
        "hs" -> stringResource(R.string.hs)
        else -> name
    }
}
