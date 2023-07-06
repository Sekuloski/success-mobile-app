package mk.sekuloski.success.fragments.workouts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mk.sekuloski.success.R
import mk.sekuloski.success.data.remote.dto.workouts.Exercise
import mk.sekuloski.success.data.remote.dto.workouts.Workout
import mk.sekuloski.success.data.remote.services.workouts.WorkoutsService
import mk.sekuloski.success.databinding.FragmentWorkoutBinding
import mk.sekuloski.success.ui.theme.AppTheme
import java.lang.Math.PI


class WorkoutFragment(
        private val workout: Workout,
        private val client: WorkoutsService // DaggerHilt
    ) : Fragment(R.layout.fragment_workouts),
    CoroutineScope by MainScope() {
    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutBinding.inflate(
            inflater, container, false
        ).apply {
            composeView.setContent {
                Main()
            }
        }
        return binding.root
    }

    @Composable
    fun Main() {
        val scope = rememberCoroutineScope()
        var currentExerciseIndex by remember {
            mutableStateOf(0)
        }
        var currentExercise by remember {
            mutableStateOf(workout.exercises[currentExerciseIndex])
        }
        var currentReps by remember {
            mutableStateOf(emptyList<Int>())
        }
        var currentSet by remember {
            mutableStateOf(0)
        }
        var exercisesSkipped by remember {
            mutableStateOf(0)
        }

        AppTheme {
            Column(modifier = Modifier.fillMaxSize()) {
                WorkoutImage(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(16.dp)
                        .weight(1f),
                    exercise = currentExercise
                )
                Text(
                    text = currentExercise.name,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(10.dp),
                )
                Text(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(10.dp),
                    text = "Next Exercise: ${workout.exercises.elementAtOrNull(currentExerciseIndex + 1)?.name ?: "None"}",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(10.dp),
                    text = currentReps.toString().replace("[", "").replace("]", ""),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(10.dp),
                    text = "Best Set: ${currentExercise.best_set.replace(",", ", ")}",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(10.dp),
                    text = "Last Set: ${currentExercise.last_set.replace(",", ", ")}",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(10.dp)
                ) {
                    TextButton(
                        onClick = {
                            if (currentSet != 0) currentSet--
                        },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text(text = "-", fontSize = 28.sp)
                    }

                    Text(
                        text = currentSet.toString(),
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 28.sp
                    )

                    TextButton(
                        onClick = { currentSet++ },
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Text(text = "+", fontSize = 28.sp)
                    }
                }
                Timer(
                    totalTime = currentExercise.rest * 1000L,
                    backgroundColor = MaterialTheme.colorScheme.background,
                    primaryColor = MaterialTheme.colorScheme.primary,
                    onBackgroundColor = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .size(180.dp),
                    onSkip = {
                        exercisesSkipped++
                        // We were on final exercise
                        if (currentExerciseIndex == workout.exercises.size - 1) {
                            scope.launch {
                                if (exercisesSkipped != workout.exercises.size) {
                                    val response = client.updateWorkout(workout.id)
                                    val toast = Toast(requireContext())
                                    toast.setText(response)
                                    toast.show()
                                    Log.v("Workout", "Workout Finished!")
                                }
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .apply {
                                        replace(R.id.flFragment, WorkoutsFragment(client))
                                        addToBackStack(null)
                                        commit()
                                    }
                            }
                        }
                        else {
                            currentExerciseIndex++
                            currentExercise = workout.exercises[currentExerciseIndex]
                            currentSet = 0
                            currentReps = emptyList()
                        }
                    },
                    onNext = {
                        val newReps = currentReps.toMutableList()
                        newReps.add(currentSet)
                        currentReps = newReps
                    },
                    onDone = {
                        val newReps = currentReps.toMutableList()
                        newReps.add(currentSet)
                        currentReps = newReps
                        var newSet = "0"
                        if (currentReps.isNotEmpty()){
                            newSet = currentReps.joinToString(separator = ",")
                        }
                        val exerciseId = currentExercise.id

                        scope.launch {
                            val response = client.updateExercise(exerciseId, newSet)
                            val toast = Toast(requireContext())
                            toast.setText(response)
                            toast.show()
                        }
                        // We were on final exercise
                        if (currentExerciseIndex == workout.exercises.size - 1) {
                            scope.launch {
                                val response = client.updateWorkout(workout.id)
                                val toast = Toast(requireContext())
                                toast.setText(response)
                                toast.show()
                            }

                            Log.v("Workout", "Workout Finished!")
                            requireActivity().supportFragmentManager.beginTransaction().apply {
                                replace(R.id.flFragment, WorkoutsFragment(client))
                                addToBackStack(null)
                                commit()
                            }
                            return@Timer
                        }
                        currentExerciseIndex++
                        currentExercise = workout.exercises[currentExerciseIndex]
                        currentSet = 0
                        currentReps = emptyList()
                    }
                )
            }
        }
    }

    @Composable
    fun WorkoutImage(modifier: Modifier, exercise: Exercise) {
        Box(modifier = modifier) {
            AsyncImage(
                modifier = Modifier,
                model = exercise.image_url,
                contentDescription = exercise.name,
                contentScale = ContentScale.Fit,
            )
        }
    }


    @Composable
    fun Timer(
        totalTime: Long,
        modifier: Modifier = Modifier,
        backgroundColor: Color,
        primaryColor: Color,
        onBackgroundColor: Color,
        onSkip: () -> Unit,
//        onRest: () -> Unit,
        onNext: () -> Unit,
        onDone: () -> Unit
    ) {
        var size by remember {
            mutableStateOf(IntSize.Zero)
        }
        var currentTime by remember {
            mutableStateOf(totalTime)
        }
        var value by remember {
            mutableStateOf(currentTime / totalTime.toFloat())
        }
        var isRunning by remember {
            mutableStateOf(false)
        }
        
        LaunchedEffect(key1 = currentTime, key2 = isRunning) {
            if (currentTime > 0 && isRunning) {
                delay(100L)
                currentTime -= 100L
                value = currentTime / totalTime.toFloat()
            }
        }
        Column(Modifier.fillMaxWidth()) {
            Box(
                modifier = modifier
                    .onSizeChanged {
                        size = it
                    },
                contentAlignment = Center
            ) {
                Canvas(
                    modifier = modifier
                ) {
                    drawArc(
                        color = backgroundColor,
                        startAngle = -215f,
                        sweepAngle = 250f,
                        useCenter = false,
                        size = Size(size.width.toFloat(), size.height.toFloat()),
                        style = Stroke(5.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = primaryColor,
                        startAngle = -215f,
                        sweepAngle = 250f * value,
                        useCenter = false,
                        size = Size(size.width.toFloat(), size.height.toFloat()),
                        style = Stroke(5.dp.toPx(), cap = StrokeCap.Round)
                    )
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val beta = (250f * value + 145f) * (PI /180f).toFloat()
                    val r = size.width / 2f
                    val a = kotlin.math.cos(beta) * r
                    val b = kotlin.math.sin(beta) * r

                    drawPoints(
                        listOf(Offset(center.x + a, center.y + b)),
                        pointMode = PointMode.Points,
                        color = primaryColor,
                        strokeWidth = (5.dp * 3).toPx(),
                        cap = StrokeCap.Round
                    )
                }
                Text(
                    text = "${(currentTime / 1000L)/60}:${((currentTime / 1000L)%60).toString().padStart(2, '0')}",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    color = onBackgroundColor
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .align(CenterHorizontally)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    onSkip()
                }) {
                    Text(text = "Skip")
                }
                if (isRunning) {
                    Button(onClick = {
                        currentTime = totalTime + 100
                        value = currentTime / totalTime.toFloat()
                        isRunning = false
                        onNext()
                    }) {
                        Text(text = "Next")
                    }
                }
                else {
                    Button(onClick = {
                        isRunning = true
//                        onRest()
                    }) {
                        Text(text = "Rest")
                    }
                }
                Button(onClick = {
                    if (!isRunning) {
                        currentTime = totalTime
                        isRunning = true
                    }
                    onDone()
                }) {
                    Text(text = "Done")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}