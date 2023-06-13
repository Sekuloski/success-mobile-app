package mk.sekuloski.success.fragments.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import mk.sekuloski.success.R
import mk.sekuloski.success.data.remote.dto.workouts.Workout
import mk.sekuloski.success.databinding.FragmentWorkoutBinding


class WorkoutFragment(_workout: Workout) : Fragment(R.layout.fragment_workouts), CoroutineScope by MainScope() {
    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!
    private val workout: Workout = _workout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}