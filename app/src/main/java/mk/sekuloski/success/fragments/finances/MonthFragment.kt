package mk.sekuloski.success.fragments.finances

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mk.sekuloski.success.*
import mk.sekuloski.success.adapter.finances.PaymentAdapter
import mk.sekuloski.success.adapter.finances.SubscriptionAdapter
import mk.sekuloski.success.data.remote.services.finances.FinancesService
import mk.sekuloski.success.databinding.FragmentMonthBinding
import mk.sekuloski.success.data.remote.dto.finances.Month
import mk.sekuloski.success.data.remote.dto.finances.Payment
import mk.sekuloski.success.data.remote.dto.finances.PaymentType
import mk.sekuloski.success.data.remote.dto.finances.Subscription
import mk.sekuloski.success.utils.initPie
import mk.sekuloski.success.utils.resetCategories

const val openedRecyclerViewHeight = 480
const val closedRecyclerViewHeight = 40
class MonthFragment(
    private val month: Month,
    private val client: FinancesService,
    private val current: Boolean
    ) : Fragment(R.layout.fragment_month), CoroutineScope by MainScope() {

    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!
    private lateinit var fullNormalAdapter: PaymentAdapter
    private lateinit var fullSixMonthAdapter: PaymentAdapter
    private lateinit var fullThreeMonthAdapter: PaymentAdapter
    private lateinit var fullLoanAdapter: PaymentAdapter
    private lateinit var fullSubscriptionAdapter: SubscriptionAdapter
    private var categories = ArrayList<Int>()

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

        binding.monthName.text = month.name
    }

    override fun onResume() {
        super.onResume()

        (context as MainActivity).supportActionBar?.title = month.name
        val normalPaymentsRecyclerView = binding.rvPayments
        val sixMonthPaymentsRecyclerView = binding.rvSixMonthPayments
        val threeMonthPaymentsRecyclerView = binding.rvThreeMonthPayments
        val loansRecyclerView = binding.rvLoans
        val subscriptionsRecyclerView = binding.rvSubscriptions
        val emptyAdapter = PaymentAdapter(requireContext(), ArrayList())
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
            val payments = client.getMonthPayments(month.id, month.name.split(" ")[1].toInt())
            val allSubscriptions = client.getSubscriptions()
            categories = resetCategories()

            var normalSum = 0; var threeMonthSum = 0; var sixMonthSum = 0; var loanSum = 0; var subscriptionSum = 0
            val normal = ArrayList<Payment>(); val threeMonth =  ArrayList<Payment>(); val sixMonth = ArrayList<Payment>(); val loan =  ArrayList<Payment>()
            val activeSubscriptions = ArrayList<Subscription>()

            for (subscription: Subscription in allSubscriptions)
            {
                if (subscription.active)
                {
                    activeSubscriptions.add(subscription)
                    subscriptionSum += subscription.amount
                    if (!current && subscription.hypothetical && subscription.amount > 0)
                        categories[subscription.expense_type] += subscription.amount
                }
            }

            for (payment: Payment in payments)
            {
                if (payment.amount > 0) categories[payment.expense_type] += payment.amount
                when (payment.payment_type) {
                    PaymentType.SINGLE_PAYMENT.ordinal ->
                    {
                        normal.add(payment)
                        if (payment.amount > 0) normalSum += payment.amount
                    }
                    PaymentType.THREE_MONTHS.ordinal ->
                    {
                        threeMonth.add(payment)
                        if (payment.amount > 0) threeMonthSum += payment.amount
                    }
                    PaymentType.SIX_MONTHS.ordinal ->
                    {
                        sixMonth.add(payment)
                        if (payment.amount > 0) sixMonthSum += payment.amount
                    }
                    PaymentType.LOAN.ordinal ->
                    {
                        loan.add(payment)
                        if (payment.amount > 0) loanSum += payment.amount
                    }
                }
            }
            fullNormalAdapter = PaymentAdapter(requireContext(), normal.sortedBy { it.name })
            fullSixMonthAdapter = PaymentAdapter(requireContext(), sixMonth.sortedBy { it.name })
            fullThreeMonthAdapter = PaymentAdapter(requireContext(), threeMonth.sortedBy { it.name })
            fullLoanAdapter = PaymentAdapter(requireContext(), loan.sortedBy { it.name })
            fullSubscriptionAdapter = SubscriptionAdapter(requireContext(), activeSubscriptions.sortedBy { it.name })

            binding.amountLeft.text = month.left.toString()
            binding.expensesAmount.text = month.expenses.toString()
            binding.tvNormalSum.text = normalSum.toString()
            binding.tvSixMonthSum.text = sixMonthSum.toString()
            binding.tvThreeMonthSum.text = threeMonthSum.toString()
            binding.tvLoansSum.text = loanSum.toString()
            binding.tvSubscriptionsSum.text = subscriptionSum.toString()

            initPie(
                binding.pieChart,
                requireContext(),
                categories
            )

            binding.btnShowMoreNormal.setOnClickListener {
                if (binding.btnShowMoreNormal.text.toString() == "See more") {
                    normalPaymentsRecyclerView.swapAdapter(fullNormalAdapter, true)
                    normalPaymentsRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreNormal.text = getString(R.string.see_less)
                } else {
                    normalPaymentsRecyclerView.swapAdapter(emptyAdapter, true)
                    normalPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreNormal.text = getString(R.string.see_more)
                }
            }

            binding.btnShowMoreSixMonth.setOnClickListener {
                if (binding.btnShowMoreSixMonth.text.toString() == "See more") {
                    sixMonthPaymentsRecyclerView.swapAdapter(fullSixMonthAdapter, true)
                    sixMonthPaymentsRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreSixMonth.text = getString(R.string.see_less)
                } else {
                    sixMonthPaymentsRecyclerView.swapAdapter(emptyAdapter, true)
                    sixMonthPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreSixMonth.text = getString(R.string.see_more)
                }
            }

            binding.btnShowMoreThreeMonth.setOnClickListener {
                if (binding.btnShowMoreThreeMonth.text.toString() == "See more") {
                    threeMonthPaymentsRecyclerView.swapAdapter(fullThreeMonthAdapter, true)
                    threeMonthPaymentsRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreThreeMonth.text = getString(R.string.see_less)
                } else {
                    threeMonthPaymentsRecyclerView.swapAdapter(emptyAdapter, true)
                    threeMonthPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreThreeMonth.text = getString(R.string.see_more)
                }
            }

            binding.btnShowMoreLoans.setOnClickListener {
                if (binding.btnShowMoreLoans.text.toString() == "See more") {
                    loansRecyclerView.swapAdapter(fullLoanAdapter, true)
                    loansRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreLoans.text = getString(R.string.see_less)
                } else {
                    loansRecyclerView.swapAdapter(emptyAdapter, true)
                    loansRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreLoans.text = getString(R.string.see_more)
                }
            }

            binding.btnShowMoreSubscriptions.setOnClickListener {
                if (binding.btnShowMoreSubscriptions.text.toString() == "See more") {
                    subscriptionsRecyclerView.swapAdapter(fullSubscriptionAdapter, true)
                    subscriptionsRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreSubscriptions.text = getString(R.string.see_less)
                } else {
                    subscriptionsRecyclerView.swapAdapter(emptyAdapter, true)
                    subscriptionsRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreSubscriptions.text = getString(R.string.see_more)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}