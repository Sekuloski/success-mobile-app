package mk.sekuloski.success.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mk.sekuloski.success.MainActivity
import mk.sekuloski.success.R
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefresh.setOnRefreshListener {
            launch {
                val newData = financesService.getMainInfo()
                if (newData != null) {
                    binding.tvAmountLeft.text = newData.amount_left.toString()
                    binding.tvBank.text = newData.bank.toString()
                    binding.tvCash.text = newData.cash.toString()
                    binding.tvExpenses.text = newData.expenses.toString()
                    binding.swipeRefresh.isRefreshing = false

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        (context as MainActivity).supportActionBar?.title = "Success"
        launch {
            val newData = financesService.getMainInfo()
            if (newData != null) {
                binding.tvAmountLeft.text = newData.amount_left.toString()
                binding.tvBank.text = newData.bank.toString()
                binding.tvCash.text = newData.cash.toString()
                binding.tvExpenses.text = newData.expenses.toString()
                binding.swipeRefresh.isRefreshing = false
            }
        }
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