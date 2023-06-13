package mk.sekuloski.success.adapter.finances

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.sekuloski.success.MainActivity
import mk.sekuloski.success.data.remote.services.FinancesService
import mk.sekuloski.success.databinding.ListMonthBinding
import mk.sekuloski.success.fragments.finances.MonthFragment
import mk.sekuloski.success.data.remote.dto.finances.Month


class MonthAdapter(
    private val context: Context,
    private val months: List<Month>,
    private val client: FinancesService
) : RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    inner class MonthViewHolder(val binding: ListMonthBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val binding = ListMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MonthViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        with(holder)
        {
            with(months[position])
            {
                binding.monthName.text = this.name
                binding.amountLeft.text = this.left.toString()

                binding.btnDetails.setOnClickListener {
                    (context as MainActivity).supportFragmentManager.beginTransaction().apply {
                        replace(mk.sekuloski.success.R.id.flFragment, MonthFragment(months[position], client, months[position].name))
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }

    }

    override fun getItemCount() = months.size
}
