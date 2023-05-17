package mk.sekuloski.success.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mk.sekuloski.success.Month
import mk.sekuloski.success.Payment
import mk.sekuloski.success.R
import mk.sekuloski.success.databinding.ListMonthBinding

class MonthAdapter(
//    private val context: Context,
    private val dataset: List<Month>,
//    private val payments: List<Payment>
) : RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    inner class MonthViewHolder(val binding: ListMonthBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val binding = ListMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MonthViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        with(holder)
        {
            with(dataset[position])
            {
                binding.monthName.text = this.name
                binding.amountLeft.text = this.amountLeft.toString()
                binding.expensesAmount.text = this.expenses.toString()
            }
        }

    }

    override fun getItemCount() = dataset.size
}
