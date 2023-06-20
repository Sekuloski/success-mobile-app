package mk.sekuloski.success.fragments.workouts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mk.sekuloski.success.MainActivity
import mk.sekuloski.success.R
import mk.sekuloski.success.adapter.workouts.WorkoutAdapter
import mk.sekuloski.success.adapter.workouts.WorkoutHistoryAdapter
import mk.sekuloski.success.data.remote.dto.workouts.Workout
import mk.sekuloski.success.data.remote.dto.workouts.WorkoutExecution
import mk.sekuloski.success.data.remote.services.workouts.WorkoutsService
import mk.sekuloski.success.databinding.FragmentWorkoutsBinding
import java.time.LocalDateTime

//const val TAG = "Workouts Fragment"

class WorkoutsFragment(
    private val workoutsService: WorkoutsService
    ) : Fragment(R.layout.fragment_workouts), CoroutineScope by MainScope() {

    private var _binding: FragmentWorkoutsBinding? = null
    private val binding get() = _binding!!
    private lateinit var workouts: List<Workout>
    private lateinit var workoutHistory: List<WorkoutExecution>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        (context as MainActivity).supportActionBar?.title = "Workouts"
        val otherWorkoutsRecyclerView = binding.rvOtherWorkouts
        val workoutHistoryRecyclerView = binding.rvWorkoutHistory

        launch {
            workouts = workoutsService.getWorkouts()
            workoutHistory = workoutsService.getWorkoutHistory()

            val datetime = LocalDateTime.now()
            val dayOfWeekIndex = datetime.dayOfWeek.value - 1

            var todaysWorkout = workouts[0]
            for (workout: Workout in workouts)
            {
                if (dayOfWeekIndex.toString() in workout.days.split(","))
                {
                    todaysWorkout = workout
                }
            }

            otherWorkoutsRecyclerView.adapter = WorkoutAdapter(requireContext(), workouts.minus(todaysWorkout))
            otherWorkoutsRecyclerView.setHasFixedSize(true)

            workoutHistoryRecyclerView.adapter = WorkoutHistoryAdapter(requireContext(), workouts, workoutHistory)
            workoutHistoryRecyclerView.setHasFixedSize(true)

            binding.workoutName.text = todaysWorkout.name
            try {
                binding.exercise1.text = todaysWorkout.exercises[0].name
                binding.exercise2.text = todaysWorkout.exercises[1].name
                binding.exercise3.text = todaysWorkout.exercises[2].name
                binding.exercise4.text = todaysWorkout.exercises[3].name
            }
            catch (e: IndexOutOfBoundsException)
            {
                Log.d("Nothing", "Nothing")
            }
            binding.btnStartNextWorkout.setOnClickListener {
                (context as MainActivity).supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, WorkoutFragment(todaysWorkout))
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}