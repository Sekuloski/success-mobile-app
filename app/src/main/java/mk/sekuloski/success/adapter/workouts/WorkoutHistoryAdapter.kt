package mk.sekuloski.success.adapter.workouts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.sekuloski.success.data.remote.dto.workouts.WorkoutExecution
import mk.sekuloski.success.databinding.ListWorkoutHistoryBinding


class WorkoutHistoryAdapter(
    private val workoutHistory: List<WorkoutExecution>,
) : RecyclerView.Adapter<WorkoutHistoryAdapter.WorkoutHistoryViewHolder>() {

    inner class WorkoutHistoryViewHolder(val binding: ListWorkoutHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHistoryViewHolder {
        val binding = ListWorkoutHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return WorkoutHistoryViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WorkoutHistoryViewHolder, position: Int) {
        with(holder)
        {
            with(workoutHistory[position])
            {
                binding.workoutName.text = this.workout_id
                binding.tvWorkoutDate.text = this.date.dayOfMonth.toString().padStart(2, '0') +
                        ".${this.date.monthValue.toString().padStart(2, '0')}"
            }
        }

    }

    override fun getItemCount() = workoutHistory.size
}
