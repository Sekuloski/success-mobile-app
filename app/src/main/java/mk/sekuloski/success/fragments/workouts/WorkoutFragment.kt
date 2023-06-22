package mk.sekuloski.success.fragments.workouts

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mk.sekuloski.success.MainActivity
import mk.sekuloski.success.R
import mk.sekuloski.success.data.remote.dto.workouts.Workout
import mk.sekuloski.success.data.remote.services.workouts.WorkoutsService
import mk.sekuloski.success.databinding.FragmentWorkoutBinding


class WorkoutFragment(private val workout: Workout) : Fragment(R.layout.fragment_workouts), CoroutineScope by MainScope() {
    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!
    private var onPause = false
    private lateinit var timer: CountDownTimer
    private val client: WorkoutsService = WorkoutsService.create()

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
        var currentReps = mutableListOf(0, 0, 0, 0, 0)
        val exercisesFromWorkout = workout.exercises
        val firstExercise = exercisesFromWorkout[0]

        setImage(currentExercise)

        binding.tvExerciseName.text = firstExercise.name
        binding.tvReps.text = currentReps.toString()
        binding.tvBestSet.text = firstExercise.best_set
        binding.tvLastSet.text = firstExercise.last_set
        updateTimer(firstExercise.rest.toLong())

        binding.btnNext.setOnClickListener {
            // On final exercise and set
            if (!onPause && currentSet == 4 && currentExercise == exercisesFromWorkout.size - 1)
            {
                currentReps[currentSet] = binding.etReps.text.toString().toInt()
                binding.tvReps.text = currentReps.toString()
                launch {
                    val response = client.updateExercise(workout.exercises[currentExercise].id, currentReps.joinToString(separator = ","))
                    val toast = Toast(requireContext())
                    toast.setText(response)
                    toast.show()
                }
                binding.llRepCount.visibility = View.INVISIBLE
                timer.cancel()
                Log.v("Workout", "Workout Finished!")
                return@setOnClickListener
            }

            onPause = if (onPause) {
                // Exercise
                binding.btnNext.text = getString(R.string.done)
                timer.cancel()
                binding.tvTimer.visibility = View.INVISIBLE
                binding.llRepCount.visibility = View.VISIBLE
                false
            } else {
                // On rest
                timer.start()
                binding.tvTimer.visibility = View.VISIBLE
                binding.llRepCount.visibility = View.INVISIBLE

                if (currentSet != 4) {
                    // Not on final set
                    currentReps[currentSet] = binding.etReps.text.toString().toInt()
                    binding.tvReps.text = currentReps.toString()
                    currentSet++
                }
                else {
                    // Final set not on final exercise
                    currentReps[currentSet] = binding.etReps.text.toString().toInt()
                    val exerciseId = workout.exercises[currentExercise].id
                    val newSet = currentReps.joinToString(separator = ",")
                    launch {
                        val response = client.updateExercise(exerciseId, newSet)
                        val toast = Toast(requireContext())
                        toast.setText(response)
                        toast.show()
                    }

                    currentReps = mutableListOf(0, 0, 0, 0, 0)
                    binding.tvReps.text = currentReps.toString()
                    currentExercise++
                    setImage(currentExercise)
                    currentSet = 0
                    binding.tvExerciseName.text = workout.exercises[currentExercise].name
                    binding.tvBestSet.text = workout.exercises[currentExercise].best_set
                    binding.tvLastSet.text = workout.exercises[currentExercise].last_set
                    updateTimer(workout.exercises[currentExercise].rest.toLong())
                }
                binding.btnNext.text = getString(R.string.skip)
                true
            }
        }
    }

    private fun setImage(currentExercise: Int) {
        binding.loadingPanel.visibility = View.VISIBLE
        if (workout.exercises[currentExercise].image_url == null)
        {
            Log.e("Workout", "Can't download image for ${workout.exercises[currentExercise].name}! URL is empty.")
            return
        }
        try {
            launch {
                Picasso.get().load(workout.exercises[currentExercise].image_url)
                    .into(binding.ivExercise)
                binding.loadingPanel.visibility = View.GONE
            }
        } catch (e: IllegalArgumentException) {
            Log.e("Workout", "Can't download image for ${workout.exercises[currentExercise].name}! URL is empty.")
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

    override fun onResume() {
        super.onResume()
        (context as MainActivity).supportActionBar?.title = workout.name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}