package com.example.fitquest.ui.screens.workout

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitquest.network.models.Exercise
import com.example.fitquest.ui.AppViewModelProvider
import com.example.fitquest.ui.navigation.NavDestinations
import com.example.fitquest.ui.navigation.NavigationDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WorkoutScreen(
    onBackClicked: () -> Unit,
    onWorkoutItemClicked: (workoutId: Long) -> Unit,
    workoutViewModel: WorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory)){

    val workoutsUiState by workoutViewModel.workoutsUiStates.collectAsState()
    val filteredExercises by workoutViewModel.filteredExercises.collectAsState()

    var userSearch by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by rememberSaveable { mutableStateOf(false) }



    Scaffold(
        topBar = {
            TopAppBar("Workout Screen")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showSheet = true
                    scope.launch {
                        sheetState.show()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary ,
            ) { Text(text = "Add Workout", fontWeight = Bold , modifier = Modifier.padding(15.dp)) }
        }
    ){ innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = userSearch,
                onValueChange = { userSearch = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                placeholder = {
                    Text(text = "Search for workouts")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .padding(horizontal = 9.dp)
                )
            Spacer(modifier = Modifier.height(16.dp))
            WorkoutBody(
                workoutList = workoutsUiState,
                onItemClick = { onWorkoutItemClicked(it.workoutId) }
            )
            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showSheet = false
                        scope.launch {
                            sheetState.hide()
                        }
                    },
                    sheetState = sheetState
                ) {
                    WorkoutCreationSheet(
                        filteredExercises = filteredExercises.toList(), // Or filtered
                        onSaveClick = { name, desc, selected ->
                            workoutViewModel.saveWorkoutWithExercises(
                                name, desc, selected,
                                userId = 0
                            )
                            showSheet = false
                            scope.launch { sheetState.hide() }
                        },
                        searchQuery = workoutViewModel.exerciseSearch,
                        onValueChange = {workoutViewModel.updateUserSearch(it)}
                    )
                }
            }
        }
    }
}


@Composable
private fun WorkoutBody(
    workoutList: List<WorkoutWithExercisesUiState>,
    onItemClick: (WorkoutWithExercisesUiState) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        modifier = modifier,
    ) {
        if (workoutList.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "No workouts available",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(contentPadding)
                )
            }
        } else {
            WorkoutList(
                workouts = workoutList,
                onWorkoutClick = { onItemClick( it ) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun WorkoutList(
    workouts: List<WorkoutWithExercisesUiState>,
    onWorkoutClick: (WorkoutWithExercisesUiState) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ){
        items(items= workouts, key = { it.workoutId }) { workout ->
            Card {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { onWorkoutClick(workout) } )
                        .padding(16.dp),
                    horizontalAlignment =  Alignment.Start
                ) {
                    Text(
                        text = workout.workoutName.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = workout.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}





@Composable
fun WorkoutCreationSheet(
    filteredExercises: List<Exercise>,
    searchQuery: String,
    onValueChange: (String) -> Unit,
    onSaveClick: (name: String, description: String, selectedExercises: List<ExerciseUiState>) -> Unit
){
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    val selectedExercises = remember { mutableStateListOf<ExerciseUiState>() }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
        .padding(16.dp)
        .fillMaxSize(1f)
    ) {
        Text(
        text = "Create a new workout",
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Workout Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
        TextField(
            value = searchQuery,
            onValueChange = { onValueChange(it)},
            label = { Text("Search Exercises") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            onSaveClick(name, description, selectedExercises)
        }) {
            Text("Save Workout")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(filteredExercises) { exercise ->
                val existing = selectedExercises.find { it.id == exercise.id }
                val isSelected = ( existing != null )

                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                        if (isSelected) {
                            selectedExercises.removeIf { it.id == exercise.id }
                        } else {
                            selectedExercises.add(
                                ExerciseUiState(
                                    id = exercise.id,
                                    name = exercise.name,
                                    description = exercise.description,
                                    category = exercise.category,
                                    equipment = exercise.equipment,
                                    muscles = exercise.muscles,
                                    sets = null,
                                    reps = null,
                                    duration = 0,
                                    isCompleted = false
                                )
                            )
                        }
                    }
                        .padding(vertical = 8.dp)
                ) {
                    Checkbox(checked = isSelected, onCheckedChange = null)
                    Text(exercise.name, modifier = Modifier.padding(start = 8.dp))
                }
                if (isSelected) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        var sets by rememberSaveable { mutableStateOf("") }
                        var reps by rememberSaveable { mutableStateOf("") }

                        OutlinedTextField(
                            value = sets,
                            onValueChange = {
                                sets = it
                                val updated = existing.copy(sets = sets.toIntOrNull())
                                selectedExercises[selectedExercises.indexOf(existing)] = updated
                            },
                            label = { Text("Sets") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = reps,
                            onValueChange = {
                                reps = it
                                val updated = existing.copy(reps = reps.toIntOrNull())
                                selectedExercises[selectedExercises.indexOf(existing)] = updated
                            },
                            label = { Text("Reps") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
