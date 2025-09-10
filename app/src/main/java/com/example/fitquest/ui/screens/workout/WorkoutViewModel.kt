package com.example.fitquest.ui.screens.workout

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fitquest.data.repository.WorkoutRepository
import com.example.fitquest.network.models.Exercise
import com.example.fitquest.data.room.models.WorkoutWithExercises
import com.example.fitquest.FitQuestApplication
import com.example.fitquest.data.preferencesDatastore.UserPreferencesDataStore
import com.example.fitquest.data.repository.ExerciseRepository
import com.example.fitquest.data.room.models.RoomExercise
import com.example.fitquest.data.room.models.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException



class WorkoutViewModel(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository
): ViewModel() {


        init {
            fetchExercisesFromApi()
        }

    var allExercises : List<Exercise> = emptyList()
        private set

    private val _filteredExercises = MutableStateFlow<List<Exercise>>(emptyList())
    val filteredExercises: StateFlow<List<Exercise>> = _filteredExercises

    var exerciseSearch by mutableStateOf("")
        private set


    fun updateUserSearch(query: String){
        exerciseSearch = query
        searchExercises(exerciseSearch)
    }



    fun getWorkoutHistory() = workoutRepository.getWorkoutHistory().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )


    fun fetchExercisesFromApi() {
        viewModelScope.launch {
            try {
                val apiResponse = exerciseRepository.getExercisesFromApi()
                allExercises = apiResponse
                Log.d("FULL EXERCISES LIST", "Loaded: ${allExercises.size} exercises")
            } catch (e: IOException) {
                allExercises = emptyList()
                Log.d("$e", "Error fetching exercises from API")
            } catch (e: HttpException) {
                allExercises = emptyList()
                Log.d("$e", "Error fetching exercises from API")
            }
        }
    }

    fun searchExercises(query: String) {
        val lowercaseQuery = query.trim().lowercase()

        _filteredExercises.value = allExercises.filter { exercise ->
            val nameMatches = exercise.name.contains(lowercaseQuery, ignoreCase = true)
            val categoryMatches = exercise.category.contains(lowercaseQuery, ignoreCase = true)
            val equipmentMatches = exercise.equipment.contains(lowercaseQuery, ignoreCase = true)
            val muscleMatches = exercise.muscles.any { it.contains(lowercaseQuery, ignoreCase = true) }
            val descriptionMatches = exercise.description.contains(lowercaseQuery, ignoreCase = true)

            nameMatches || categoryMatches || equipmentMatches || muscleMatches || descriptionMatches
        }
    }


    fun WorkoutWithExercises.toUiState(): WorkoutWithExercisesUiState {
        return WorkoutWithExercisesUiState(
            workoutId = workout.workoutId,
            workoutName = workout.workoutName,
            description = workout.description,
            exercises = exercises.map {
                ExerciseUiState(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    category = it.category,
                    equipment = it.equipment,
                    muscles = it.muscles,
                    sets = it.sets,
                    reps = it.reps,
                    duration = it.duration,
                    isCompleted = it.isCompleted
                )
            }
        )
    }


    val workoutsUiStates: StateFlow<List<WorkoutWithExercisesUiState>> =
        workoutRepository.getAllWorkoutsWithExercises()
            .map { list -> list.map { it.toUiState() } }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )


    fun saveWorkoutWithExercises(
        name: String,
        description: String,
        selectedExercises: List<ExerciseUiState>,
        userId: Long
    ) {
        viewModelScope.launch {
                val workout = Workout(
                    workoutName = name,
                    description = description,
                    userId =  userId
                )
            val exercisesToInsert = selectedExercises.map {
                RoomExercise(
                    name = it.name,
                    description = it.description,
                    sets = it.sets?: 0,
                    reps = it.reps?: 0,
                    workoutId = 0,
                    category = it.category,
                    equipment = it.equipment,
                    muscles = it.muscles,
                    id = it.id
                )
            }
            workoutRepository.insertWorkoutWithExercises( workout, exercisesToInsert)
        }
    }


}



data class ExerciseUiState(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val equipment: String,
    val muscles: List<String>,
    val duration: Int,
    val sets: Int?,
    val reps: Int?,
    val isCompleted: Boolean,
    val elapsedTime: Int = 0
)


data class WorkoutWithExercisesUiState(
    val workoutId: Long = 0,
    val workoutName: String = "",
    val description: String = "",
    val exercises: List<ExerciseUiState> = listOf()
)