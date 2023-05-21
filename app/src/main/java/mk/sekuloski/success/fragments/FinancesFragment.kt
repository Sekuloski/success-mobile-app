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

class FinancesFragment(_months: ArrayList<Month>) : Fragment(R.layout.fragment_finances) {
    private var _binding: FragmentFinancesBinding? = null
    private val binding get() = _binding!!
    private var months = _months
    private val api = API()

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
        val locations = api.getLocations()
        val monthsRecyclerView = binding.rvMonths

        monthsRecyclerView.adapter = MonthAdapter(view.context, months)
        monthsRecyclerView.setHasFixedSize(true)

        binding.swipeRefresh.setOnRefreshListener {
            months = ArrayList()
            months = api.getMonths()

            while(true)
            {
                if (months.size == 0)
                {
                    continue
                }

                break
            }
            monthsRecyclerView.swapAdapter(MonthAdapter(view.context, months), true)
            binding.swipeRefresh.isRefreshing = false
        }

        while(true)
        {
            if (locations.size == 0)
            {
                continue
            }

            break
        }

        binding.fabAddPayment.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, AddPaymentFragment(locations.associate { it.name to it.id } as HashMap<String, Int>))
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        months = ArrayList()
        months = api.getMonths()

        while(true)
        {
            if (months.size == 0)
            {
                continue
            }

            break
        }

        binding.rvMonths.swapAdapter(context?.let { MonthAdapter(it, months) }, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}