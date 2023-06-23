package mk.sekuloski.success.fragments

import android.app.ActivityManager.AppTask
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
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
import mk.sekuloski.success.ui.theme.AppTheme
import mk.sekuloski.success.ui.theme.md_theme_dark_onBackground
import mk.sekuloski.success.ui.theme.md_theme_dark_onPrimaryContainer
import mk.sekuloski.success.ui.theme.md_theme_dark_primaryContainer

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
            FinancesHome(modifier.weight(1f))
            WorkoutHome(modifier.weight(1f))
        }
    }

    @Composable
    private fun FinancesHome(modifier: Modifier = Modifier) {
        var financesMain by remember {
            mutableStateOf(FinancesMain(0, 0, 0, 0, 0, 0))
        }
        LaunchedEffect(key1 = true) {
            financesMain = financesService.getMainInfo()
        }
        Column(
            modifier = modifier
        ) {
            Text(
                "Financial Situation:",
                modifier = modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(start = 48.dp, top = 36.dp),
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(3f),
                contentAlignment = Alignment.Center
            )
            {
                FinancesCard(
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    financesMain
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .weight(1f)
            )
        }
    }

    @Composable
    private fun FinancesCard(
        modifier: Modifier,
        financesMain: FinancesMain
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clickable {

                },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = modifier) {
                    TextRow("Amount Left", financesMain.amount_left)
                    TextRow("Bank Amount", financesMain.bank)
                    TextRow("Cash Amount", financesMain.cash)
                    TextRow("Expenses", financesMain.expenses)
                }
            }
        }
    }

    @Composable
    fun WorkoutHome(modifier: Modifier = Modifier) {
        var workout by remember {
            mutableStateOf(Workout(0, "", emptyList(), "0,1,2,3,4,5,6"))
        }
        LaunchedEffect(key1 = true) {
            workout = workoutService.getWorkouts()[0]
        }
        Column(
            modifier = modifier
        ) {
            Text(
                "Today's Workout:",
                modifier = modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(start = 48.dp, top = 36.dp),
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.7f),
                contentAlignment = Alignment.Center
            ) {
                WorkoutCard(
                    workout, Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                )
            }
            Spacer(
                modifier = Modifier
                    .width(100.dp)
                    .weight(2f)
            )
        }
    }

    @Composable
    fun WorkoutCard(
        workout: Workout,
        modifier: Modifier = Modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clickable {

                },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = workout.name,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
                    )
                    for (exercise: Exercise in workout.exercises) {
                        Text(
                            text = exercise.name,
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
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
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 28.sp
            )
            Text(
                text = amount.toString(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 28.sp
            )
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