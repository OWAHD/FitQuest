package com.example.fitquest.ui.screens.profile//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.fitquest.ui.AppViewModelProvider
//import com.example.fitquest.ui.screens.authentication.AuthenticationViewModel
//import com.example.fitquest.ui.screens.workout.TopAppBar
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileScreen(
//    viewModel: AuthenticationViewModel = viewModel(factory = AppViewModelProvider.Factory),
//    onEditClick: () -> Unit = {},
//    onLogoutClick: () -> Unit = { viewModel.logout() }
//) {
//    val user by viewModel.loggedInUserDetails.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("Profile") })
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            // User Info
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation()
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Person,
//                        contentDescription = "User Avatar",
//                        modifier = Modifier.size(80.dp)
//                    )
//                    Spacer(Modifier.height(8.dp))
//                    Text(user.name.ifEmpty { "Guest" }, style = MaterialTheme.typography.titleMedium)
//                    Text("User ID: ${user.userId}", style = MaterialTheme.typography.bodySmall)
//                }
//            }
//
//            // Goals Section
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text("Your Goals", style = MaterialTheme.typography.titleMedium)
//                    Spacer(Modifier.height(8.dp))
//                    Text("Height: ${user.height} cm")
//                    Text("Weight: ${user.weight} kg")
//                    Text("Water Goal: ${user.waterGoal} ml")
//                    Text("Step Goal: ${user.stepsGoal} steps")
//                }
//            }
//
//            // Actions
//            Button(
//                onClick = onEditClick,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Edit Profile")
//            }
//
//            OutlinedButton(
//                onClick = onLogoutClick,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Log Out")
//            }
//        }
//    }
//}
