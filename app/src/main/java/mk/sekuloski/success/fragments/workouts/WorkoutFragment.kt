package mk.sekuloski.success.fragments.workouts

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import mk.sekuloski.success.R
import mk.sekuloski.success.data.remote.dto.workouts.Workout
import mk.sekuloski.success.databinding.FragmentWorkoutBinding
import java.io.InputStream
import java.net.URL


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

        binding.btnAdd.setOnClickListener {
            val value = binding.etReps.text.toString().toInt() + 1
            binding.etReps.text = value.toString()
        }
        binding.btnRemove.setOnClickListener {
            if (binding.etReps.text.toString().toInt() != 0)
            {
                val value = binding.etReps.text.toString().toInt() - 1
                binding.etReps.text = value.toString()
            }
        }

        startWorkout()
    }

    override fun onResume() {
        super.onResume()
    }

    fun startWorkout()
    {
        val timer = object: CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished % 1000 == 0L)
                {
                    Log.v("Workout", "Timer: $millisUntilFinished")
                }
            }

            override fun onFinish() {
                Log.v("Workout", "Finished!")
            }
        }
        var currentExercise = 0
        var currentSet = 0
        val currentReps = mutableListOf(0, 0, 0, 0, 0)

        try {
            val inputStream = URL("https://upload.wikimedia.org/wikipedia/commons/5/59/Dipexercise.svg").content as InputStream

            binding.ivExercise.setImageDrawable(Drawable.createFromStream(inputStream, "Dips"))
        } catch (e: Exception) {
            Log.e("Workout", "Can't download image!")
        }

        binding.tvExerciseName.text = workout.exercises[0].name
        binding.tvReps.text = currentReps.toString()

        binding.btnNext.setOnClickListener {
            if (currentSet != workout.exercises[currentExercise].sets - 1) {
                currentReps[currentSet] = binding.etReps.text.toString().toInt()
                binding.tvReps.text = currentReps.toString()
                currentSet++
            }
            else {
                if (currentExercise != workout.exercises.size - 1) {
                    currentReps[currentSet] = binding.etReps.text.toString().toInt()
                    binding.tvReps.text = currentReps.toString()

                    currentExercise++
                    currentSet = 0
                    binding.tvExerciseName.text = workout.exercises[currentExercise].name
                }
                else
                {
                    Log.v("Workout", "Workout Finished!")
                }
            }
//            timer.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}