package mk.sekuloski.success.adapter.workouts

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.sekuloski.success.MainActivity
import mk.sekuloski.success.data.remote.dto.workouts.Workout
import mk.sekuloski.success.databinding.WorkoutCardBinding
import mk.sekuloski.success.fragments.workouts.WorkoutFragment


class WorkoutAdapter(
    private val context: Context,
    private val workouts: List<Workout>,
) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    inner class WorkoutViewHolder(val binding: WorkoutCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = WorkoutCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        with(holder)
        {
            with(workouts[position])
            {
                binding.workoutName.text = this.name
                try {
                    binding.exercise1.text = this.exercises[0].name
                    binding.exercise2.text = this.exercises[1].name
                    binding.exercise3.text = this.exercises[2].name
                    binding.exercise4.text = this.exercises[3].name
                }
                catch (exception: IndexOutOfBoundsException)
                {
                    Log.w("Workout Adapter", "Workout doesn't have 4 exercises!")
                }
                binding.btnWorkout.setOnClickListener {
                    (context as MainActivity).supportFragmentManager.beginTransaction().apply {
                        replace(mk.sekuloski.success.R.id.flFragment, WorkoutFragment(workouts[position]))
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }

    }

    override fun getItemCount() = workouts.size
}
