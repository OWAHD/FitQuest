package com.example.fitquest.ui.screens.workout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitquest.R
import com.example.fitquest.ui.AppViewModelProvider
import com.example.fitquest.ui.navigation.NavigationDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.collections.sumOf

object WorkoutTrackingDestination : NavigationDestination {
    override val route = "workout_tracking"
    const val workoutIdArg = "workoutId"
    val routeWithArgs = "$route/{$workoutIdArg}"
}


//@Composable
//fun WorkoutTrackingScreen(
//    onBackClicked: () -> Unit,
//    viewModel: WorkoutTrackingViewModel = viewModel(factory = AppViewModelProvider.Factory)
//) {
//
//    val workoutsUiState by viewModel.selectedWorkoutUiState.collectAsState()
//    val context = LocalContext.current
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        Row {
//            Button(onClick = onBackClicked) {
//                Text("Back")
//            }
//            Spacer(modifier = Modifier.weight(1f))
//        }
//        Text("Workout Progress",
//            style = MaterialTheme.typography.headlineLarge,
//            modifier = Modifier.padding(vertical = 16.dp).align(alignment = Alignment.CenterHorizontally))
//        Spacer(modifier = Modifier.height(8.dp))
//
//        workoutsUiState.exercises.forEach { exercise ->
//            var timeLeft by remember { mutableIntStateOf(exercise.duration) }
//            val isRunning = remember { mutableStateOf(false) }
////            val isDone by remember { mutableStateOf(exercise.isCompleted)}
//
//            LaunchedEffect(isRunning.value) {
//                if (isRunning.value) {
//                    while (timeLeft > 0 && isActive) {
//                        delay(1000L)
//                        timeLeft--
//                    }
//                }
//            }
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
////                Icon(
////                    imageVector = if (isDone){
////                        ImageVector.vectorResource(id = R.drawable.checkboxes_1)
////                    } else {
////                        ImageVector.vectorResource(id = R.drawable.checkboxes)
////                    },
////                    contentDescription = null,
////                    modifier = Modifier.clickable(onClick = { viewModel.completeExercise(exercise.id, workoutsUiState.workoutId) })
////                )
//                Checkbox(
//                    checked = exercise.isCompleted,
//                    onCheckedChange = {
//                        viewModel.completeExercise(exercise.id, workoutsUiState.workoutId)
//                    }
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Column {
//                    Text(exercise.name)
//                    Text("${exercise.sets} sets x ${exercise.reps} reps", style = MaterialTheme.typography.bodySmall)
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Button(onClick = { isRunning.value = !isRunning.value }) {
//                            Text(if (isRunning.value) "Pause" else "Start")
//                        }
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text("Time left: $timeLeft s")
//                    }
//                }
//
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = { viewModel.resetWorkout(workoutsUiState.workoutId) }) {
//            Text("Reset Workout")
//        }
//    }
//}




@Composable
fun WorkoutTrackingScreen(
    onBackClicked: () -> Unit,
    viewModel: WorkoutTrackingViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val workoutsUiState by viewModel.selectedWorkoutUiState.collectAsState()
    val context = LocalContext.current
    val summaryUiState by viewModel.summaryUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            Button(onClick = onBackClicked) {
                Text("Back")
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            "Workout Progress",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Track timers for each exercise
        workoutsUiState.exercises.forEach { exercise ->
            val elapsed = viewModel.elapsedTimes[exercise.id] ?: 0
            val isRunning = remember { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = exercise.isCompleted,
                    onCheckedChange = {
                        viewModel.completeExercise(exercise.id, workoutsUiState.workoutId)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(exercise.name)
                    Text("${exercise.sets ?: 0} sets x ${exercise.reps ?: 0} reps",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(onClick = {
                            if (isRunning.value) {
                                viewModel.stopTimer(exercise.id)
                            } else {
                                viewModel.startTimer(exercise.id)
                            }
                            isRunning.value = !isRunning.value
                        }) {
                            Text(if (isRunning.value) "Pause" else "Start")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Time: ${elapsed}s")
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Reset workout
        Button(onClick = { viewModel.resetWorkout(workoutsUiState.workoutId) }) {
            Text("Reset Workout")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Finish workout
        Button(
            onClick = { viewModel.finishWorkout(workoutsUiState.workoutId.toInt(), workoutsUiState.exercises) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finish Workout")
        }
    }

    if (summaryUiState.isVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissSummary() },
            confirmButton = {
                Button(onClick = { viewModel.dismissSummary() }) {
                    Text("OK")
                }
            },
            title = { Text("Workout Summary") },
            text = {
                Column {
                    Text("Total Duration: ${summaryUiState.totalDuration} seconds")
                    Text("Total Calories: ${summaryUiState.totalCalories}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Completed Exercises:")
                    summaryUiState.completedExercises.forEach {
                        Text("• $it")
                    }
                }
            }
        )
    }


}
