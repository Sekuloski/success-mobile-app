package mk.sekuloski.success.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mk.sekuloski.success.API
import mk.sekuloski.success.R
import mk.sekuloski.success.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val api = API()
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

        val data = api.getMainInfo()
        while(data.isEmpty())
        {
            continue
        }

        binding.tvAmountLeft.text = data["amount_left"].toString()
        binding.tvBank.text = data["bank"].toString()
        binding.tvCash.text = data["cash"].toString()
        binding.tvSalary.text = data["salary"].toString()
        binding.tvExpenses.text = data["expenses"].toString()
    }
    
}