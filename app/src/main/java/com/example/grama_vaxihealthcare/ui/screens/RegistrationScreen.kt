package com.example.grama_vaxihealthcare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.grama_vaxihealthcare.R
import com.example.grama_vaxihealthcare.ui.components.ReadableLightSurface
import com.example.grama_vaxihealthcare.ui.components.readableOutlinedTextFieldColors
import com.example.grama_vaxihealthcare.viewmodel.GramaVaxiViewModel

@Composable
fun RegistrationScreen(viewModel: GramaVaxiViewModel, onRegisterSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    val allFarmers by viewModel.allFarmers.collectAsState()
    var showCreateProfile by remember { mutableStateOf(false) }

    LaunchedEffect(allFarmers) {
        if (allFarmers.isEmpty()) {
            showCreateProfile = true
        }
    }

    Scaffold(
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = if (showCreateProfile) stringResource(id = R.string.farmer_registration) else "Select Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(32.dp))

            if (showCreateProfile) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = ReadableLightSurface)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text(stringResource(id = R.string.name)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = readableOutlinedTextFieldColors()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text(stringResource(id = R.string.phone_number)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = readableOutlinedTextFieldColors()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = village,
                            onValueChange = { village = it },
                            label = { Text(stringResource(id = R.string.village_name)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = readableOutlinedTextFieldColors()
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank() && phone.isNotBlank() && village.isNotBlank()) {
                            viewModel.registerFarmer(name, phone, village)
                            onRegisterSuccess()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(id = R.string.register), style = MaterialTheme.typography.titleMedium)
                }
                
                if (allFarmers.isNotEmpty()) {
                    TextButton(onClick = { showCreateProfile = false }) {
                        Text("Back to Profile Selection", color = MaterialTheme.colorScheme.primary)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(allFarmers) { farmer ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.loginFarmer(farmer.id)
                                    onRegisterSuccess()
                                },
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = ReadableLightSurface)
                        ) {
                            ListItem(
                                headlineContent = { 
                                    Text(
                                        farmer.name, 
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ) 
                                },
                                supportingContent = { 
                                    Text(
                                        "${farmer.villageName} | ${farmer.phoneNumber}",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    ) 
                                },
                                leadingContent = { 
                                    Icon(
                                        Icons.Default.AccountCircle, 
                                        contentDescription = null, 
                                        modifier = Modifier.size(40.dp), 
                                        tint = MaterialTheme.colorScheme.primary
                                    ) 
                                },
                                trailingContent = {
                                    IconButton(onClick = { viewModel.deleteFarmer(farmer) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Profile",
                                            tint = Color(0xFFC62828)
                                        )
                                    }
                                },
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedButton(
                    onClick = { showCreateProfile = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create New Profile")
                }
            }
        }
    }
}
