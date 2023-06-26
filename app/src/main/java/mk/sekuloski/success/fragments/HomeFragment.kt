package mk.sekuloski.success.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import mk.sekuloski.success.R
import mk.sekuloski.success.data.remote.dto.finances.FinancesMain
import mk.sekuloski.success.data.remote.dto.workouts.Exercise
import mk.sekuloski.success.data.remote.dto.workouts.Workout
import mk.sekuloski.success.data.remote.services.finances.FinancesService
import mk.sekuloski.success.data.remote.services.workouts.WorkoutsService
import mk.sekuloski.success.databinding.FragmentHomeBinding
import mk.sekuloski.success.fragments.finances.FinancesFragment
import mk.sekuloski.success.fragments.workouts.WorkoutFragment
import mk.sekuloski.success.ui.theme.AppTheme
import mk.sekuloski.success.utils.normalizeWorkouts
import java.time.LocalTime

class HomeFragment(
    private val financesService: FinancesService,
    private val workoutService: WorkoutsService
) : Fragment(R.layout.fragment_home), CoroutineScope by MainScope(),
    com.github.mikephil.charting.listener.OnChartValueSelectedListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
            .apply {
                composeView.setContent {
                    AppTheme {
                        Home(Modifier.fillMaxSize())
                    }
                }
            }
        return binding.root
    }

    @Composable
    fun Home(modifier: Modifier) {
        Column(
            modifier = modifier
        ) {
            Box(modifier.weight(1.5f)) {
                Greeting()
            }
            Box(modifier.weight(4f)) {
                FinancesHome()
            }
            Box(modifier.weight(4f)) {
                WorkoutHome()
            }
        }
    }

    @Composable
    private fun FinancesHome() {
        var financesMain by remember {
            mutableStateOf(FinancesMain(0, 0, 0, 0, 0, 0))
        }
        LaunchedEffect(key1 = true) {
            financesMain = financesService.getMainInfo()
        }
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                "Your Financial Situation:",
                modifier = Modifier.padding(start = 62.dp),
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            FinancesCard(
                Modifier.align(CenterHorizontally),
                financesMain,
            )
        }
    }

    @Composable
    private fun FinancesCard(
        modifier: Modifier,
        financesMain: FinancesMain
    ) {
        Box(
            modifier = modifier.fillMaxSize(0.8f),
            contentAlignment = Center,
        ) {
            Card(
                modifier = modifier,
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
            ) {
                Box(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable {
                            requireActivity().supportFragmentManager
                                .beginTransaction()
                                .apply {
                                    replace(R.id.flFragment, FinancesFragment(financesService))
                                    addToBackStack(null)
                                    commit()
                                }
                        }
                ) {
                    Column(modifier = Modifier) {
                        TextRow("Amount Left", financesMain.amount_left)
                        TextRow("Bank Amount", financesMain.bank)
                        TextRow("Cash Amount", financesMain.cash)
                        TextRow("Expenses", financesMain.expenses)
                    }
                }
            }
        }
    }

    @Composable
    fun WorkoutHome() {
        var workout by remember {
            mutableStateOf(Workout(0, "", emptyList(), "0,1,2,3,4,5,6"))
        }
        LaunchedEffect(key1 = true) {
            // Get only today's workout. This is inefficient.
            workout = workoutService.getWorkouts()[0]
            normalizeWorkouts(listOf(workout))
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                "Today's Workout:",
                modifier = Modifier.padding(start = 62.dp),
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            WorkoutCard(
                Modifier.align(CenterHorizontally),
                workout
            )

        }
    }

    @Composable
    fun WorkoutCard(
        modifier: Modifier,
        workout: Workout
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.65f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .clickable {
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .apply {
                                replace(R.id.flFragment, WorkoutFragment(workout))
                                addToBackStack(null)
                                commit()
                            }
                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Start
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 10.dp, top = 10.dp)
                            .align(CenterHorizontally)
                    ) {
                        val fontSize = 28.sp
                        ShadowText(fontSize, workout.name)
                        Text(
                            text = workout.name,
                            fontSize = fontSize,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                        )
                    }
                    for (exercise: Exercise in workout.exercises) {
                        val fontSize = 28.sp
                        Box {
                            ShadowText(fontSize, exercise.name)
                            Text(
                                text = exercise.name,
                                fontSize = fontSize,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TextRow(
        text: String,
        sum: Int,
        modifier: Modifier = Modifier
    ) {
        val amount by animateIntAsState(targetValue = sum)
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val fontSize = 28.sp
            Box {
                ShadowText(fontSize, text)
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = fontSize
                )
            }
            Box {
                ShadowText(fontSize, amount.toString())
                Text(
                    text = amount.toString(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = fontSize
                )
            }
        }
    }

    @Composable
    private fun ShadowText(
        fontSize: TextUnit,
        text: String
    ) {
        Text(
            modifier = Modifier
                .alpha(alpha = 0.5f)
                .offset(x = 0.dp, y = 2.dp)
                .blur(radius = 1.dp),
            color = Color.Black,
            fontSize = fontSize.times(1.03f),
            text = text
        )
    }

    @Composable
    fun Greeting() {
        Text(
            text = "Good ${getTimeOfDay()}!",
            fontSize = 45.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(16.dp)
                .padding(start = 48.dp, top = 36.dp),
        )
    }

    private fun getTimeOfDay(): String {
        val currentTime = LocalTime.now()
        return when (currentTime.hour) {
            in 0..11 -> "Morning"
            in 12..16 -> "Afternoon"
            in 17..19 -> "Evening"
            else -> "Night"
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null)
            return
        if (h != null) {
            Log.i(
                "VAL SELECTED",
                "Value: " + e.y + ", index: " + h.x
                        + ", DataSet index: " + h.dataSetIndex
            )
        }
    }

    override fun onNothingSelected() {
        return
    }
}