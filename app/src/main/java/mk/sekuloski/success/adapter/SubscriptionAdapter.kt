package mk.sekuloski.success.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mk.sekuloski.success.R
import mk.sekuloski.success.data.remote.dto.Subscription

class SubscriptionAdapter(
    private val context: Context,
    private val dataset: List<Subscription>
    ) : RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder>() {

    class SubscriptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subscriptionName: TextView = view.findViewById(R.id.subscription_name)
        val subscriptionAmount: TextView = view.findViewById(R.id.subscription_amount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_subscription, parent, false)

        return SubscriptionViewHolder(adapterLayout)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        val item = dataset[position]
        holder.subscriptionName.text = item.name
        holder.subscriptionAmount.text = item.amount.toString()
    }

}