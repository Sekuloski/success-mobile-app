package mk.sekuloski.success.utils

import mk.sekuloski.success.data.remote.dto.workouts.Exercise
import mk.sekuloski.success.data.remote.dto.workouts.ExerciseType
import mk.sekuloski.success.data.remote.dto.workouts.Workout

fun normalizeWorkouts(workouts: List<Workout>) {
    for (workout: Workout in workouts) {
        workout.exercises = normalizeExercises(workout.exercises)
    }
}

private fun normalizeExercises(exercises: List<Exercise>): List<Exercise> {
    val finalExerciseList = ArrayList<Exercise>()
    for (exerciseType: ExerciseType in ExerciseType.values()) {
        for (exercise: Exercise in exercises)
            if (exercise.exercise_type == exerciseType.ordinal) {
                finalExerciseList.add(exercise)
            }
    }

    return finalExerciseList
}
