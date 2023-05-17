package mk.sekuloski.success.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mk.sekuloski.success.*
import mk.sekuloski.success.adapter.MonthAdapter
import mk.sekuloski.success.databinding.FragmentFinancesBinding
import mk.sekuloski.success.models.Month
import mk.sekuloski.success.models.Payment
import okhttp3.OkHttpClient

class FinancesFragment(_months: ArrayList<Month>) : Fragment(R.layout.fragment_finances) {
    private var _binding: FragmentFinancesBinding? = null
    private val binding get() = _binding!!
    private val months = _months
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinancesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val monthsRecyclerView = binding.monthsRecyclerView
        monthsRecyclerView.adapter = MonthAdapter(view.context, months)
        monthsRecyclerView.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}