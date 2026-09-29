package com.example.skynet.ui.ejercicios

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.skynet.R

import com.example.skynet.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarEjercicioScreen(
    viewModel: AgregarEjercicioViewModel,
    onCancel: () -> Unit,
    onCreateNew: () -> Unit
) {
    val uiState by viewModel.uiState.observeAsState(AgregarEjercicioUiState.Loading())
    val searchQuery by viewModel.searchQuery.observeAsState("")

    GYMCrushTheme {
        MainBackground {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                "EJERCICIOS",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    letterSpacing = 2.sp
                                )
                            )
                        },
                        navigationIcon = {
                            TextButton(onClick = onCancel) {
                                Text("VOLVER", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = onCreateNew,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), CircleShape)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                },
                containerColor = Color.Transparent
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    GlassSearchBar(
                        query = searchQuery,
                        onQueryChange = { viewModel.onSearchQueryChange(it) },
                        placeholder = "Buscar movimiento..."
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "POPULARES",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    when (val state = uiState) {
                        is AgregarEjercicioUiState.Loading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            }
                        }
                        is AgregarEjercicioUiState.Success -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(bottom = 100.dp)
                            ) {
                                items(state.ejercicios) { ejercicio ->
                                    GlassWorkoutCard(
                                        title = ejercicio.nombre,
                                        subtitle = ejercicio.musculo,
                                        onClick = { /* Seleccionar */ },
                                        leadingIcon = {
                                            AsyncImage(
                                                model = ejercicio.imagenUrl ?: "file:///android_asset/login.jpg",
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(50.dp)
                                                    .clip(RoundedCornerShape(12.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        is AgregarEjercicioUiState.Error -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                GlassCard(modifier = Modifier.padding(24.dp)) {
                                    Text(
                                        state.message, 
                                        color = Color.White,
                                        textAlign = TextAlign.Center, 
                                        modifier = Modifier.padding(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = { Text("Buscar ejercicio", color = Color.Gray) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF4F6F9),
            unfocusedContainerColor = Color(0xFFF4F6F9),
            disabledContainerColor = Color(0xFFF4F6F9),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}

@Composable
fun FilterChipsRow(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(label = "Todo Equipamiento")
        }
        item {
            FilterChip(label = "Todos Músculos")
        }
    }
}

@Composable
fun FilterChip(label: String) {
    Surface(
        color = Color(0xFFF4F6F9),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(44.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF111111)
                )
            )
        }
    }
}

@Composable
fun EjercicioItem(ejercicio: EntrenamientoDto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Seleccionar ejercicio */ }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = if (ejercicio.imagenUrl.isNullOrEmpty()) "file:///android_asset/login.jpg" else ejercicio.imagenUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_workout),
            error = painterResource(R.drawable.ic_workout),
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFF4F6F9)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = ejercicio.nombre,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111111)
                )
            )
            Text(
                text = ejercicio.musculo,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                )
            )
        }

        IconButton(
            onClick = { /* Acción de selección */ },
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFFF4F6F9), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color(0xFF0088CC),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
