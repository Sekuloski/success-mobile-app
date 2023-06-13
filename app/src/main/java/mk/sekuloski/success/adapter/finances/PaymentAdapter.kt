package mk.sekuloski.success.adapter.finances

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mk.sekuloski.success.data.remote.dto.finances.Payment
import mk.sekuloski.success.R

class PaymentAdapter(
    private val context: Context,
    private val dataset: List<Payment>
    ) : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    class PaymentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val paymentName: TextView = view.findViewById(R.id.payment_name)
        val paymentAmount: TextView = view.findViewById(R.id.payment_amount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_payment, parent, false)

        return PaymentViewHolder(adapterLayout)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val item = dataset[position]
        holder.paymentName.text = item.name
        holder.paymentAmount.text = item.amount.toString()
    }

}