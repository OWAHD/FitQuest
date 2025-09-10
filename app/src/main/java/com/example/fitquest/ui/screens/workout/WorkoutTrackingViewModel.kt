package com.example.fitquest.ui.screens.workout

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitquest.data.repository.ExerciseRepository
import com.example.fitquest.data.repository.WorkoutRepository
import com.example.fitquest.data.room.models.RoomExercise
import com.example.fitquest.data.room.models.WorkoutHistory
import com.example.fitquest.data.room.models.WorkoutWithExercises
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.collections.map

class WorkoutTrackingViewModel(
    private val savedStateHandle: SavedStateHandle, // For ViewModel to receive nav args
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository
): ViewModel() {

    private val _exerciseTimers = mutableStateMapOf<Int, Job>() // exerciseId -> timer job
    private val _elapsedTimes = mutableStateMapOf<Int, Int>()   // exerciseId -> elapsed seconds

    val elapsedTimes: Map<Int, Int> get() = _elapsedTimes


    private val _summaryUiState = MutableStateFlow(WorkoutSummaryUiState())
    val summaryUiState: StateFlow<WorkoutSummaryUiState> = _summaryUiState

    fun startTimer(exerciseId: Int) {
        if (_exerciseTimers[exerciseId] != null) return // already running
        _exerciseTimers[exerciseId] = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                _elapsedTimes[exerciseId] = (_elapsedTimes[exerciseId] ?: 0) + 1
            }
        }
    }

    fun stopTimer(exerciseId: Int) {
        _exerciseTimers[exerciseId]?.cancel()
        _exerciseTimers.remove(exerciseId)
    }


    private val selectedWorkout: Long = checkNotNull(savedStateHandle[WorkoutTrackingDestination.workoutIdArg])

    val selectedWorkoutUiState: StateFlow<WorkoutWithExercisesUiState> =
        workoutRepository.getWorkoutWithExercises(selectedWorkout)
            .filterNotNull()
            .map{
                it.toUiState()
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                WorkoutWithExercisesUiState()
            )


    fun completeExercise(exerciseId: Int, workoutId: Long) {
        viewModelScope.launch {
            exerciseRepository.completeExercise(exerciseId, workoutId)
        }
    }

    fun resetWorkout(workoutId: Long) {
        viewModelScope.launch {
            workoutRepository.resetWorkout(workoutId)
        }
    }

    fun finishWorkout(workoutId: Int, exercises: List<ExerciseUiState>) {
        viewModelScope.launch {
            val totalDuration = _elapsedTimes.values.sum()
            val totalCalories = exercises.sumOf { (it.sets ?: 0) * (it.reps ?: 0) * 5 }
            val completedExercises = exercises.joinToString(";") {
                "${it.name}:${it.sets ?: 0}x${it.reps ?: 0} (${_elapsedTimes[it.id] ?: 0}s)"
            }

            val history = WorkoutHistory(
                workoutId = workoutId,
                date = System.currentTimeMillis(),
                totalDuration = totalDuration,
                totalCalories = totalCalories,
                completedExercises = completedExercises
            )
            workoutRepository.saveWorkoutHistory(history)

            _summaryUiState.value = WorkoutSummaryUiState(
                totalDuration = totalDuration,
                totalCalories = totalCalories,
                completedExercises = completedExercises.split(";"),
                isVisible = true
            )

            // cleanup timers
            _exerciseTimers.values.forEach { it.cancel() }
            _exerciseTimers.clear()
            _elapsedTimes.clear()
        }
    }

    fun dismissSummary() {
        _summaryUiState.value = _summaryUiState.value.copy(isVisible = false)
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

}




data class WorkoutSummaryUiState(
    val totalDuration: Int = 0,
    val totalCalories: Int = 0,
    val completedExercises: List<String> = emptyList(),
    val isVisible: Boolean = false
)












//    private val _selectedWorkoutId = MutableStateFlow<Long>(0)
//    val selectedWorkoutId: StateFlow<Long> = _selectedWorkoutId
//
//        fun selectWorkout(id: Long) {
//        _selectedWorkoutId.value = id
//    }

//    fun getExercises(workoutId: Long) = exerciseRepository.getExercisesForWorkout(workoutId).stateIn(
//        viewModelScope,
//        SharingStarted.WhileSubscribed(5000),
//        emptyList()
//    )


//    fun getExercises(workoutIdFlow: StateFlow<Long>): Flow<WorkoutWithExercisesUiState> { // Adjusted parameter
//        return workoutIdFlow.flatMapLatest { workoutId ->
//            if (workoutId != 0L) {
//                // Assuming workoutRepository.getWorkoutWithExercises returns Flow<WorkoutWithExercises?>
//                workoutRepository.getWorkoutWithExercises(workoutId).map { it.toUiState() }
//            } else {
//                flowOf(WorkoutWithExercisesUiState())
//            }
//        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WorkoutWithExercisesUiState())
//    }