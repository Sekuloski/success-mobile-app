package mk.sekuloski.success.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mk.sekuloski.success.API
import mk.sekuloski.success.APISingleton
import mk.sekuloski.success.R
import mk.sekuloski.success.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val api = APISingleton.getInstance()
    private var data = api?.getMainInfo()
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
            data = HashMap()
            data = api?.getMainInfo()

            while(data?.isEmpty() == true)
            {
                continue
            }

            binding.tvAmountLeft.text = data?.get("amount_left")?.toString()
            binding.tvBank.text = data?.get("bank")?.toString()
            binding.tvCash.text = data?.get("cash")?.toString()
            binding.tvSalary.text = data?.get("salary")?.toString()
            binding.tvExpenses.text = data?.get("expenses")?.toString()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()

        data = HashMap()
        data = api?.getMainInfo()

        while(data?.isEmpty() == true)
        {
            continue
        }

        binding.tvAmountLeft.text = data?.get("amount_left").toString()
        binding.tvBank.text = data?.get("bank").toString()
        binding.tvCash.text = data?.get("cash").toString()
        binding.tvSalary.text = data?.get("salary").toString()
        binding.tvExpenses.text = data?.get("expenses").toString()
    }
    
}