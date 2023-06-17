package mk.sekuloski.success.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mk.sekuloski.success.R
import mk.sekuloski.success.data.remote.services.FinancesService
import mk.sekuloski.success.databinding.FragmentHomeBinding
import mk.sekuloski.success.utils.CustomPieChartRenderer
import mk.sekuloski.success.utils.ValuesFormatter

class HomeFragment(_client: FinancesService) : Fragment(R.layout.fragment_home), CoroutineScope by MainScope(),
    com.github.mikephil.charting.listener.OnChartValueSelectedListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val client = _client
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
                val newData = client.getMainInfo()
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

        launch {
            val newData = client.getMainInfo()
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