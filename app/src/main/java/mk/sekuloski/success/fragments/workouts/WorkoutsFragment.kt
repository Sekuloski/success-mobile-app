package mk.sekuloski.success.fragments.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import mk.sekuloski.success.R
import mk.sekuloski.success.data.remote.dto.workouts.Exercise
import mk.sekuloski.success.data.remote.dto.workouts.Workout
import mk.sekuloski.success.data.remote.dto.workouts.WorkoutExecution
import mk.sekuloski.success.data.remote.services.workouts.WorkoutsService
import mk.sekuloski.success.databinding.FragmentWorkoutsBinding
import mk.sekuloski.success.ui.theme.AppTheme
import mk.sekuloski.success.utils.normalizeWorkouts

//const val TAG = "Workouts Fragment"

class WorkoutsFragment(
    private val workoutsService: WorkoutsService
) : Fragment(R.layout.fragment_workouts), CoroutineScope by MainScope() {

    private var _binding: FragmentWorkoutsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutsBinding.inflate(
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
        var dailyWorkout by remember {
            mutableStateOf(Workout(0, "Demo", emptyList<Exercise>(), "-1"))
        }
        var workouts by remember {
            mutableStateOf(listOf(dailyWorkout))
        }
        var workoutHistory by remember {
            mutableStateOf(emptyList<WorkoutExecution>())
        }
        LaunchedEffect(key1 = true) {
            workouts = workoutsService.getWorkouts()
            normalizeWorkouts(workouts)
            dailyWorkout = workouts[0]
            workoutHistory = workoutsService.getWorkoutHistory()
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
                    workouts[0]
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
                            workout
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun WorkoutCard(
        modifier: Modifier,
        workout: Workout
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
//                        requireActivity().supportFragmentManager
//                            .beginTransaction()
//                            .apply {
//                                replace(R.id.flFragment, WorkoutFragment(workout, workoutsService))
//                                addToBackStack(null)
//                                commit()
//                            }
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

//
//    override fun onResume() {
//        super.onResume()
//
//        (context as MainActivity).supportActionBar?.title = "Workouts"
//        val otherWorkoutsRecyclerView = binding.rvOtherWorkouts
//        val workoutHistoryRecyclerView = binding.rvWorkoutHistory
//
//        launch {
//            workouts = workoutsService.getWorkouts()
//            workoutHistory = workoutsService.getWorkoutHistory()
//
//            normalizeWorkouts(workouts)
//
//            val datetime = LocalDateTime.now()
//            val dayOfWeekIndex = datetime.dayOfWeek.value - 1
//
//            var todaysWorkout = workouts[0]
//            for (workout: Workout in workouts)
//            {
//                if (dayOfWeekIndex.toString() in workout.days.split(","))
//                {
//                    todaysWorkout = workout
//                }
//            }
//
//            otherWorkoutsRecyclerView.adapter = WorkoutAdapter(requireContext(), workouts.minus(todaysWorkout))
//            otherWorkoutsRecyclerView.setHasFixedSize(true)
//
//            workoutHistoryRecyclerView.adapter = WorkoutHistoryAdapter(workoutHistory)
//            workoutHistoryRecyclerView.setHasFixedSize(true)
//
//            binding.workoutName.text = todaysWorkout.name
//            try {
//                binding.exercise1.text = todaysWorkout.exercises[0].name
//                binding.exercise2.text = todaysWorkout.exercises[1].name
//                binding.exercise3.text = todaysWorkout.exercises[2].name
//                binding.exercise4.text = todaysWorkout.exercises[3].name
//            }
//            catch (e: IndexOutOfBoundsException)
//            {
//                Log.d("Nothing", "Nothing")
//            }
//            binding.btnStartNextWorkout.setOnClickListener {
//                (context as MainActivity).supportFragmentManager.beginTransaction().apply {
//                    replace(R.id.flFragment, WorkoutFragment(todaysWorkout))
//                    addToBackStack(null)
//                    commit()
//                }
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}