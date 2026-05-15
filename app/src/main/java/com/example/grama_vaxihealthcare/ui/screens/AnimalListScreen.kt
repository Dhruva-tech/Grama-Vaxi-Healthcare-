package com.example.grama_vaxihealthcare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.grama_vaxihealthcare.R
import com.example.grama_vaxihealthcare.data.entity.Animal
import com.example.grama_vaxihealthcare.navigation.Screen
import com.example.grama_vaxihealthcare.ui.components.ReadableLightSurface
import com.example.grama_vaxihealthcare.ui.components.readableTopAppBarColors
import com.example.grama_vaxihealthcare.ui.theme.ErrorRed
import com.example.grama_vaxihealthcare.ui.theme.RuralGreenDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalListScreen(viewModel: com.example.grama_vaxihealthcare.viewmodel.GramaVaxiViewModel, navController: NavController) {
    val animals by viewModel.allAnimals.collectAsState()
    var animalPendingDelete by remember { mutableStateOf<Animal?>(null) }

    animalPendingDelete?.let { animal ->
        DeleteAnimalDialog(
            onDismiss = { animalPendingDelete = null },
            onConfirm = {
                viewModel.deleteAnimal(animal)
                animalPendingDelete = null
            }
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.animals),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                colors = readableTopAppBarColors()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddAnimal.route) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_animal))
            }
        }
    ) { padding ->
        if (animals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding), 
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.no_animals_yet),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(animals) { animal ->
                    AnimalItem(
                        animal = animal, 
                        onDelete = { animalPendingDelete = animal },
                        onClick = {
                            navController.navigate(Screen.AnimalDetail.createRoute(animal.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DeleteAnimalDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.delete_animal),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = stringResource(R.string.delete_warning),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
            ) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun AnimalItem(animal: Animal, onDelete: () -> Unit, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = ReadableLightSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (animal.imageUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(animal.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ) {
                    Icon(
                        Icons.Default.Pets, 
                        contentDescription = null, 
                        modifier = Modifier.padding(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = animal.name, 
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = RuralGreenDark
                )
                Text(
                    text = "${animal.type} • ${animal.breed}", 
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ErrorRed)
            }
        }
    }
}
