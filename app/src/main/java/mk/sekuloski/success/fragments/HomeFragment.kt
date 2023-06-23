package mk.sekuloski.success.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mk.sekuloski.success.MainActivity
import mk.sekuloski.success.R
import mk.sekuloski.success.data.remote.dto.finances.FinancesMain
import mk.sekuloski.success.data.remote.services.finances.FinancesService
import mk.sekuloski.success.data.remote.services.workouts.WorkoutsService
import mk.sekuloski.success.databinding.FragmentHomeBinding

class HomeFragment(private val financesService: FinancesService, private val workoutService: WorkoutsService) : Fragment(R.layout.fragment_home), CoroutineScope by MainScope(),
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
                    Home()
                }
            }
        return binding.root
    }

    @Composable
    fun Home() {
        var financesMain by remember {
            mutableStateOf(FinancesMain(0, 0, 0, 0, 0, 0))
        }
        LaunchedEffect (key1 = true) {
            financesMain = financesService.getMainInfo()
        }
        Column {
            TextRow("Amount Left", financesMain.amount_left)
            TextRow("Bank Amount", financesMain.bank)
            TextRow("Cash Amount", financesMain.cash)
            TextRow("Expenses", financesMain.expenses)
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
////        binding.swipeRefresh.setOnRefreshListener {
////            update()
////        }
//
////        launch {
////            val newData = financesService.getMainInfo()
////            if (newData != null) {
////                binding.tvAmountLeft.text = newData.amount_left.toString()
////                binding.tvBank.text = newData.bank.toString()
////                binding.tvCash.text = newData.cash.toString()
////                binding.tvExpenses.text = newData.expenses.toString()
////                binding.swipeRefresh.isRefreshing = false
////            }
////            binding.tvWorkoutStatus.text = workoutService.getWorkoutStatus().toString()
////        }
//    }

    @Composable
    fun TextRow (
        text: String,
        sum: Int,
        modifier: Modifier = Modifier
    ) {
        Row (
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text (
                text = text,
                color = colorResource(R.color.md_theme_dark_onBackground),
                fontSize = 28.sp
            )
            Text (
                text = sum.toString(),
                color = colorResource(R.color.md_theme_dark_onBackground),
                fontSize = 28.sp
            )
        }
    }

//    private fun update() {
//        launch {
//            val newData = financesService.getMainInfo()
//            if (newData != null) {
//                binding.tvAmountLeft.text = newData.amount_left.toString()
//                binding.tvBank.text = newData.bank.toString()
//                binding.tvCash.text = newData.cash.toString()
//                binding.tvExpenses.text = newData.expenses.toString()
//                binding.swipeRefresh.isRefreshing = false
//            }
//        }
//    }

    override fun onResume() {
        super.onResume()
//        update()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null)
            return
        if (h != null) {
            Log.i("VAL SELECTED",
                "Value: " + e.y + ", index: " + h.x
                        + ", DataSet index: " + h.dataSetIndex
            )
        }
    }

    override fun onNothingSelected() {
        return
    }
}