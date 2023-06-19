package mk.sekuloski.success.adapter.finances

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.sekuloski.success.MainActivity
import mk.sekuloski.success.data.remote.dto.finances.Payment
import mk.sekuloski.success.R
import mk.sekuloski.success.databinding.ListPaymentBinding
import mk.sekuloski.success.fragments.finances.MonthFragment
import mk.sekuloski.success.fragments.finances.PaymentFragment

class PaymentAdapter(
    private val context: Context,
    private val payments: List<Payment>
    ) : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    inner class PaymentViewHolder(val binding: ListPaymentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentAdapter.PaymentViewHolder {
        val binding = ListPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        with(holder)
        {
            with(payments[position])
            {
                binding.paymentName.text = this.name
                if (this.paid)
                {
                    binding.ivPaid.setImageResource(R.drawable.ic_paid)
                }
                binding.paymentAmount.text = this.amount.toString()

                binding.btnPayment.setOnClickListener {
                    (context as MainActivity).supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, PaymentFragment(payments[position]))
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }

    }

    override fun getItemCount() = payments.size

}