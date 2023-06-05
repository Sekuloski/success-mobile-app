package mk.sekuloski.success.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mk.sekuloski.success.*
import mk.sekuloski.success.adapter.MonthAdapter
import mk.sekuloski.success.data.remote.FinancesService
import mk.sekuloski.success.data.remote.dto.Location
import mk.sekuloski.success.databinding.FragmentFinancesBinding
import mk.sekuloski.success.data.remote.dto.Month

class FinancesFragment(_client: FinancesService) : Fragment(R.layout.fragment_finances), CoroutineScope by MainScope() {
    private var _binding: FragmentFinancesBinding? = null
    private val binding get() = _binding!!
    private val client = _client
    private lateinit var locations: ArrayList<Location>
    private lateinit var months: ArrayList<Month>

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
        val monthsRecyclerView = binding.rvMonths

        launch {
            months = client.getMonths()
            locations = client.getLocations()
            val mainData = client.getMainInfo()
            if (!mainData!!.salary_received)
            {
                binding.addSalary.visibility = View.VISIBLE
            }
            monthsRecyclerView.adapter = MonthAdapter(view.context, months, client)
        }

        monthsRecyclerView.setHasFixedSize(true)

        binding.swipeRefresh.setOnRefreshListener {
            launch {
                months = client.getMonths()
                monthsRecyclerView.adapter = MonthAdapter(view.context, months, client)
                monthsRecyclerView.swapAdapter(MonthAdapter(view.context, months, client), true)
                binding.swipeRefresh.isRefreshing = false
            }
        }

        binding.fabAddPayment.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, AddPaymentFragment(locations.associate { it.name to it.id } as HashMap<String, Int>, client))
                addToBackStack(null)
                commit()
            }
        }

        binding.addSalary.setOnClickListener {
            launch {
                client.addSalary()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        launch {
            months = client.getMonths()
            binding.rvMonths.swapAdapter(context?.let { MonthAdapter(it, months, client) }, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}