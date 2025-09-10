package com.example.fitquest.ui.screens.workout


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitquest.R
import com.example.fitquest.ui.AppViewModelProvider
import com.example.fitquest.ui.screens.authentication.AuthenticationViewModel



@Composable
fun HomeScreen(onViewAllWorkoutClick: () -> Unit,
               onViewWorkoutClick: (workoutId: Long) -> Unit,
               onLogoutClick: () -> Unit,
               onWaterTrackerClick: () -> Unit,
               workoutViewModel: WorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory),
               authenticationViewModel: AuthenticationViewModel = viewModel(factory = AppViewModelProvider.Factory)
){

    Log.d("LifecycleStart", "HomeScreen launched with AuthenticationViewModel instance: ${authenticationViewModel.hashCode()}")

    val workouts by workoutViewModel.workoutsUiStates.collectAsState()
    val user by authenticationViewModel.loggedInUserDetails.collectAsState()

    Log.d("HomeScreenUser", "User in HomeScreen: Name='${user.name}', Weight='${user.weight}', WaterGoal='${user.waterGoal}'")


    val waterProgress = 0.4f // This will later come from tracked data
    val stepsProgress = 0.9f // This will later come from tracked data

    Scaffold(
        topBar = {TopAppBar( text = stringResource(R.string.appHeader) )}
    ){ innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState(0), true)
        ){
            Header(
                username = user.name,
                onLogoutClick = {
                authenticationViewModel.logout()
                onLogoutClick()
                })
            Spacer(modifier = Modifier.padding(13.dp))
            Text(
                text = "Your Goals",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = Bold,
                modifier = Modifier.padding(5.dp)

            )
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                GoalProgressItem(
                    image = R.drawable.waterbottle,
                    title = "Water Intake",
                    goalValue = "${ user.waterGoal} L",
                    progress = waterProgress
                )
                GoalProgressItem(
                    image = R.drawable.footstep,
                    title = "Steps",
                    goalValue = "${user.stepsGoal} steps",
                    progress = stepsProgress
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                ActionCard("Workouts", "Start a new session", R.drawable.stairs_playstore, onViewAllWorkoutClick)
                ActionCard("Water Tracker", "Log your water intake", R.drawable.waterbottle, onWaterTrackerClick)
            }
            Spacer(Modifier.size(15.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WORKOUT PLANS",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "View All",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clickable(onClick = onViewAllWorkoutClick),
                    textAlign = TextAlign.End
                )
            }
            HomeWorkoutList(workouts, onWorkoutClick = {
                onViewWorkoutClick(it.workoutId)
            })
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}


@Composable
private fun HomeWorkoutList(
    workouts: List<WorkoutWithExercisesUiState>,
    onWorkoutClick: (WorkoutWithExercisesUiState) -> Unit
){
     workouts.forEach{ workout ->
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .clickable { onWorkoutClick(workout) },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_playstore),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(text = workout.workoutName.uppercase(),
                            style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(start = 10.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(workout.description, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

}


@Composable
fun GoalProgressItem(image: Int, title: String, goalValue: String, progress: Float) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp)
                )
                Column {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),horizontalArrangement = Arrangement.SpaceEvenly) {
                        Text(text = title, fontWeight = FontWeight.Medium)
                        Text(text = goalValue, color = MaterialTheme.colorScheme.primary)
                    }
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = ProgressIndicatorDefaults.linearTrackColor,
                        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    )
                }
            }
        }
    }
}


@Composable
fun Header(username:String, onLogoutClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    )
    {
        Image(
            painter = painterResource(R.drawable.ic_launcher_playstore),
            contentDescription = null,
            modifier = Modifier
                .padding(10.dp)
                .size(80.dp)
                .clip(shape = RoundedCornerShape(50.dp))
        )
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Hello $username",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Let's smash your goals today!",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .clickable { onLogoutClick }
        )
    }
}

@Composable
fun ActionCard(title: String, description: String, iconRes: Int, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = Bold)
            Text(description, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    text: String
){
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primary)
            ){
                Text(
                    text = text,
                    fontSize = 30.sp,
                    fontWeight = Bold,
                    style = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
                    textAlign = TextAlign.Center
                )
            }
        },
        windowInsets = WindowInsets(top = 4.dp),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    )
}
