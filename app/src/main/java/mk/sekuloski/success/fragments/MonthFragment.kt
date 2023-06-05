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
import mk.sekuloski.success.adapter.PaymentAdapter
import mk.sekuloski.success.adapter.SubscriptionAdapter
import mk.sekuloski.success.data.remote.FinancesService
import mk.sekuloski.success.databinding.FragmentMonthBinding
import mk.sekuloski.success.data.remote.dto.Month

const val openedRecyclerViewHeight = 480
const val closedRecyclerViewHeight = 40

class MonthFragment(_month: Month, _client: FinancesService, _name: String) : Fragment(R.layout.fragment_month), CoroutineScope by MainScope() {
    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!
    private val month: Month = _month
    private val name: String = _name
    private val client: FinancesService = _client
    private lateinit var fullNormalAdapter: PaymentAdapter
    private lateinit var fullSixMonthAdapter: PaymentAdapter
    private lateinit var fullThreeMonthAdapter: PaymentAdapter
    private lateinit var fullLoanAdapter: PaymentAdapter
    private lateinit var fullSubscriptionAdapter: SubscriptionAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val normalPaymentsRecyclerView = binding.rvPayments
        val sixMonthPaymentsRecyclerView = binding.rvSixMonthPayments
        val threeMonthPaymentsRecyclerView = binding.rvThreeMonthPayments
        val loansRecyclerView = binding.rvLoans
        val subscriptionsRecyclerView = binding.rvSubscriptions

        binding.monthName.text = name

        val emptyAdapter = PaymentAdapter(view.context, ArrayList())
        normalPaymentsRecyclerView.adapter = emptyAdapter
        normalPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
        normalPaymentsRecyclerView.setHasFixedSize(true)

        sixMonthPaymentsRecyclerView.adapter = emptyAdapter
        sixMonthPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
        sixMonthPaymentsRecyclerView.setHasFixedSize(true)

        threeMonthPaymentsRecyclerView.adapter = emptyAdapter
        threeMonthPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
        threeMonthPaymentsRecyclerView.setHasFixedSize(true)

        loansRecyclerView.adapter = emptyAdapter
        loansRecyclerView.layoutParams.height = closedRecyclerViewHeight
        loansRecyclerView.setHasFixedSize(true)

        subscriptionsRecyclerView.adapter = emptyAdapter
        subscriptionsRecyclerView.layoutParams.height = closedRecyclerViewHeight
        subscriptionsRecyclerView.setHasFixedSize(true)


        launch {
            fullNormalAdapter = PaymentAdapter(view.context, client.getPayments(month.normal_ids))
            fullSixMonthAdapter = PaymentAdapter(view.context, client.getPayments(month.six_month_ids))
            fullThreeMonthAdapter = PaymentAdapter(view.context, client.getPayments(month.three_month_ids))
            fullLoanAdapter = PaymentAdapter(view.context, client.getPayments(month.loan_ids))
            fullSubscriptionAdapter = SubscriptionAdapter(view.context, client.getSubscriptions(month.subscription_ids))

            binding.amountLeft.text = month.left.toString()
            binding.expensesAmount.text = month.expenses.toString()
            binding.tvNormalSum.text = month.normal_sum.toString()
            binding.tvSixMonthSum.text = month.six_month_sum.toString()
            binding.tvThreeMonthSum.text = month.three_month_sum.toString()
            binding.tvLoansSum.text = month.loan_sum.toString()
            binding.tvSubscriptionsSum.text = month.subscription_sum.toString()

            binding.btnShowMoreNormal.setOnClickListener {
                if (binding.btnShowMoreNormal.text.toString() == "See more") {
                    normalPaymentsRecyclerView.swapAdapter(fullNormalAdapter, true)
                    normalPaymentsRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreNormal.text = "See less"
                } else {
                    normalPaymentsRecyclerView.swapAdapter(emptyAdapter, true)
                    normalPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreNormal.text = "See more"
                }
            }

            binding.btnShowMoreSixMonth.setOnClickListener {
                if (binding.btnShowMoreSixMonth.text.toString() == "See more") {
                    sixMonthPaymentsRecyclerView.swapAdapter(fullSixMonthAdapter, true)
                    sixMonthPaymentsRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreSixMonth.text = "See less"
                } else {
                    sixMonthPaymentsRecyclerView.swapAdapter(emptyAdapter, true)
                    sixMonthPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreSixMonth.text = "See more"
                }
            }

            binding.btnShowMoreThreeMonth.setOnClickListener {
                if (binding.btnShowMoreThreeMonth.text.toString() == "See more") {
                    threeMonthPaymentsRecyclerView.swapAdapter(fullThreeMonthAdapter, true)
                    threeMonthPaymentsRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreThreeMonth.text = "See less"
                } else {
                    threeMonthPaymentsRecyclerView.swapAdapter(emptyAdapter, true)
                    threeMonthPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreThreeMonth.text = "See more"
                }
            }

            binding.btnShowMoreLoans.setOnClickListener {
                if (binding.btnShowMoreLoans.text.toString() == "See more") {
                    loansRecyclerView.swapAdapter(fullLoanAdapter, true)
                    loansRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreLoans.text = "See less"
                } else {
                    loansRecyclerView.swapAdapter(emptyAdapter, true)
                    loansRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreLoans.text = "See more"
                }
            }

            binding.btnShowMoreSubscriptions.setOnClickListener {
                if (binding.btnShowMoreSubscriptions.text.toString() == "See more") {
                    subscriptionsRecyclerView.swapAdapter(fullSubscriptionAdapter, true)
                    subscriptionsRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreSubscriptions.text = "See less"
                } else {
                    subscriptionsRecyclerView.swapAdapter(emptyAdapter, true)
                    subscriptionsRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreSubscriptions.text = "See more"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}