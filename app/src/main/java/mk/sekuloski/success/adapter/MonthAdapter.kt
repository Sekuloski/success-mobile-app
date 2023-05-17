package mk.sekuloski.success.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.sekuloski.success.MainActivity
import mk.sekuloski.success.databinding.ListMonthBinding
import mk.sekuloski.success.fragments.MonthFragment
import mk.sekuloski.success.models.Month
import mk.sekuloski.success.models.Payment


class MonthAdapter(
    private val context: Context,
    private val months: List<Month>,
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
                binding.amountLeft.text = this.amountLeft.toString()

                binding.btnDetails.setOnClickListener {
                    (context as MainActivity).supportFragmentManager.beginTransaction().apply {
                        replace(mk.sekuloski.success.R.id.flFragment, MonthFragment(months[position]))
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }

    }

    override fun getItemCount() = months.size
}
