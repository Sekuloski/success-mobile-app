package mk.sekuloski.success.fragments.workouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import mk.sekuloski.success.data.remote.dto.workouts.Exercise
import mk.sekuloski.success.data.remote.dto.workouts.Workout
import mk.sekuloski.success.data.remote.dto.workouts.WorkoutExecution
import mk.sekuloski.success.data.remote.services.workouts.WorkoutsService
import mk.sekuloski.success.fragments.destinations.WorkoutScreenDestination
import mk.sekuloski.success.ui.theme.AppTheme
import mk.sekuloski.success.utils.normalizeWorkouts

@Destination
@Composable
fun FitnessScreen(
    navigator: DestinationsNavigator
) {
    val client = WorkoutsService.create()
    var dailyWorkout by remember {
        mutableStateOf(Workout(0, "Demo", emptyList(), "-1"))
    }
    var workouts by remember {
        mutableStateOf(listOf(dailyWorkout))
    }
    var workoutHistory by remember {
        mutableStateOf(emptyList<WorkoutExecution>())
    }
    LaunchedEffect(key1 = true) {
        workouts = client.getWorkouts()
        normalizeWorkouts(workouts)
        dailyWorkout = workouts[0]
        workoutHistory = client.getWorkoutHistory()
    }
    AppTheme {
        Column(modifier = Modifier) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Today's Workout",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp
            )
            WorkoutCard(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(1f),
                workouts[0],
                navigator
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Workout History",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp
            )
            LazyColumn (modifier = Modifier.weight(1f)) {
                itemsIndexed(workoutHistory) { index, workoutExecution ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = workoutExecution.workout_id,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 28.sp
                        )
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = workoutExecution.date.dayOfMonth.toString()
                                .padStart(2, '0') +
                                    ".${
                                        workoutExecution.date.monthValue.toString()
                                            .padStart(2, '0')
                                    }",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 28.sp
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Other Workouts",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp
            )
            LazyRow {
                itemsIndexed(workouts) { index, workout ->
                    WorkoutCard(
                        Modifier.align(Alignment.CenterHorizontally),
                        workout,
                        navigator
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutCard(
    modifier: Modifier,
    workout: Workout,
    navigator: DestinationsNavigator
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable {
                    navigator.navigate(
                        WorkoutScreenDestination(workout)
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 10.dp, top = 10.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    val fontSize = 26.sp
                    ShadowText(fontSize, workout.name)
                    Text(
                        text = workout.name,
                        fontSize = fontSize,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                for (exercise: Exercise in workout.exercises) {
                    val fontSize = 24.sp
                    Box {
                        ShadowText(fontSize, exercise.name)
                        Text(
                            text = exercise.name,
                            fontSize = fontSize,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShadowText(
    fontSize: TextUnit,
    text: String
) {
    Text(
        modifier = Modifier
            .alpha(alpha = 0.8f)
            .offset(x = 4.dp, y = 3.dp)
            .blur(radius = 2.dp),
        color = Color.Black,
        fontSize = fontSize,
        text = text
    )
}