package mk.sekuloski.success.fragments.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import mk.sekuloski.success.MainActivity
import mk.sekuloski.success.R
import mk.sekuloski.success.adapter.workouts.WorkoutAdapter
import mk.sekuloski.success.data.remote.dto.workouts.getWorkouts
import mk.sekuloski.success.databinding.FragmentWorkoutsBinding


class WorkoutsFragment : Fragment(R.layout.fragment_workouts), CoroutineScope by MainScope() {
    private var _binding: FragmentWorkoutsBinding? = null
    private val binding get() = _binding!!
    private val workouts get() = getWorkouts()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val otherWorkoutsRecyclerView = binding.rvOtherWorkouts

        otherWorkoutsRecyclerView.adapter = WorkoutAdapter(view.context, workouts.drop(1))
        otherWorkoutsRecyclerView.setHasFixedSize(true)

        binding.workoutName.text = workouts[0].name
        binding.exercise1.text = workouts[0].exercises[0].name
        binding.exercise2.text = workouts[0].exercises[1].name
        binding.exercise3.text = workouts[0].exercises[2].name
        binding.exercise4.text = workouts[0].exercises[3].name
        binding.btnStartNextWorkout.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, WorkoutFragment(workouts[0]))
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}