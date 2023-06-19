package mk.sekuloski.success.fragments.workouts

import android.annotation.SuppressLint
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
import mk.sekuloski.success.data.remote.dto.workouts.Exercise
import mk.sekuloski.success.data.remote.dto.workouts.Workout
import mk.sekuloski.success.databinding.FragmentWorkoutBinding
import java.io.InputStream
import java.net.URL


class WorkoutFragment(private val workout: Workout) : Fragment(R.layout.fragment_workouts), CoroutineScope by MainScope() {
    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!
    private var onPause = false
    private lateinit var timer: CountDownTimer

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

    private fun startWorkout()
    {
        var currentExercise = 0
        var currentSet = 0
        val currentReps = mutableListOf(0, 0, 0, 0, 0)
        val exercisesFromWorkout = workout.exercises

        try {
            val inputStream = URL("https://upload.wikimedia.org/wikipedia/commons/5/59/Dipexercise.svg").content as InputStream

            binding.ivExercise.setImageDrawable(Drawable.createFromStream(inputStream, "Dips"))
        } catch (e: Exception) {
            Log.e("Workout", "Can't download image!")
        }

        binding.tvExerciseName.text = workout.exercises[currentExercise].name
        binding.tvReps.text = currentReps.toString()
        updateTimer(workout.exercises[currentExercise].rest.toLong())

        binding.btnNext.setOnClickListener {
            if (!onPause && currentSet == 4 && currentExercise == exercisesFromWorkout.size - 1)
            {
                currentReps[currentSet] = binding.etReps.text.toString().toInt()
                binding.tvReps.text = currentReps.toString()
                binding.llRepCount.visibility = View.INVISIBLE
                timer.cancel()
                Log.v("Workout", "Workout Finished!")
                return@setOnClickListener
            }
            onPause = if (onPause) {
                binding.btnNext.text = getString(R.string.done)
                timer.cancel()
                binding.tvTimer.visibility = View.INVISIBLE
                binding.llRepCount.visibility = View.VISIBLE
                false
            } else {
                timer.start()
                binding.tvTimer.visibility = View.VISIBLE
                binding.llRepCount.visibility = View.INVISIBLE
                if (currentSet != 4) {
                    currentReps[currentSet] = binding.etReps.text.toString().toInt()
                    binding.tvReps.text = currentReps.toString()
                    currentSet++
                }
                else {
                    if (currentExercise != exercisesFromWorkout.size - 1) {
                        currentReps[currentSet] = binding.etReps.text.toString().toInt()
                        binding.tvReps.text = currentReps.toString()

                        currentExercise++
                        currentSet = 0
                        binding.tvExerciseName.text = workout.exercises[currentExercise].name
                        updateTimer(workout.exercises[currentExercise].rest.toLong())
                    }
                }
                binding.btnNext.text = getString(R.string.skip)
                true
            }
        }
    }

    private fun updateTimer(time: Long)
    {
        timer = object: CountDownTimer(time * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val minutes = (seconds / 60).toInt()
                val secondsLeft = (seconds % 60).toInt()
                binding.tvTimer.text = "${minutes.toString().padStart(2, '0')}:${secondsLeft.toString().padStart(2, '0')}"
            }

            override fun onFinish() {
                binding.btnNext.text = getString(R.string.done)
                timer.cancel()
                binding.tvTimer.visibility = View.INVISIBLE
                binding.llRepCount.visibility = View.VISIBLE
                onPause = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}