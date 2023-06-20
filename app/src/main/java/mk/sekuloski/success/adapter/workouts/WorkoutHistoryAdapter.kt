package mk.sekuloski.success.adapter.workouts

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.sekuloski.success.data.remote.dto.workouts.Workout
import mk.sekuloski.success.data.remote.dto.workouts.WorkoutExecution
import mk.sekuloski.success.databinding.ListWorkoutHistoryBinding
import mk.sekuloski.success.databinding.WorkoutCardBinding


class WorkoutHistoryAdapter(
    private val context: Context,
    private val workouts: List<Workout>,
    private val workoutHistory: List<WorkoutExecution>,
) : RecyclerView.Adapter<WorkoutHistoryAdapter.WorkoutHistoryViewHolder>() {

    inner class WorkoutHistoryViewHolder(val binding: ListWorkoutHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHistoryViewHolder {
        val binding = ListWorkoutHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return WorkoutHistoryViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WorkoutHistoryViewHolder, position: Int) {
        val finalWorkouts = workouts.associateBy { it.id }
        with(holder)
        {
            with(workoutHistory[position])
            {
                binding.workoutName.text = finalWorkouts[this.workout_id]?.name.toString()
                binding.tvWorkoutDate.text = this.date.dayOfMonth.toString().padStart(2, '0') +
                        ".${this.date.monthValue.toString().padStart(2, '0')}"
            }
        }

    }

    override fun getItemCount() = workouts.size
}
