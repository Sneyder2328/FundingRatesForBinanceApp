package com.sneyder.fundingrates.ui.symbolDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.sneyder.fundingrates.R
import com.sneyder.fundingrates.data.model.CoinData
import com.sneyder.fundingrates.data.model.Resource
import com.sneyder.fundingrates.databinding.ActivityDetailsSymbolBinding
import com.sneyder.fundingrates.ui.base.BaseActivity
import com.sneyder.fundingrates.utils.debug
import com.sneyder.fundingrates.utils.getColorFromAttr
import java.text.SimpleDateFormat
import java.util.*


class SymbolDetailsActivity : BaseActivity() {

    companion object {

        private const val EXTRA_COIN_DATA = "coinData"

        fun starterIntent(context: Context, coinData: CoinData): Intent {
            val starter = Intent(context, SymbolDetailsActivity::class.java)
            starter.putExtra(EXTRA_COIN_DATA, coinData as Parcelable)
            return starter
        }

    }

    private val symbolDetailsViewModel by viewModels<SymbolDetailsViewModel> { viewModelFactory }
    private val coinData: CoinData? by lazy { intent.getParcelableExtra(EXTRA_COIN_DATA) }
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityDetailsSymbolBinding>(
            this,
            R.layout.activity_details_symbol
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.lifecycleOwner = this@SymbolDetailsActivity
        symbolDetailsViewModel.fundingRates.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {
                    debug("loading")
                }
                is Resource.Failure -> {
                    debug("failure")
                }
                is Resource.Success -> {
                    val dataset = response.data.sortedBy { it.fundingTime }.takeLast(14 * 3)
                    debug("dataset=$dataset")
                    with(binding.graph) {
                        setTouchEnabled(true)
                        setPinchZoom(true)

                        val ll1 = LimitLine(30f, "Title")
                        ll1.lineColor = resources.getColor(R.color.greyish_brown)
                        ll1.lineWidth = 4f
                        ll1.enableDashedLine(10f, 10f, 0f)
                        ll1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
                        ll1.textSize = 10f

                        val ll2 = LimitLine(35f, "")
                        ll2.lineWidth = 4f
                        ll2.enableDashedLine(10f, 10f, 0f)

                        val lineEntries = dataset.map {
                            Entry(
                                it.fundingTime.toFloat(),
                                it.fundingRate.toFloat()
                            )
                        }
                        val lineDataSet = LineDataSet(lineEntries, "Funding rate")
                        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
                        lineDataSet.isHighlightEnabled = true
                        lineDataSet.lineWidth = 2f
                        lineDataSet.color = getColorFromAttr(R.attr.colorControlHighlight)
                        lineDataSet.setCircleColor(getColorFromAttr(R.attr.colorSecondaryVariant))
                        lineDataSet.circleRadius = 2f
                        lineDataSet.setDrawHighlightIndicators(true)
                        lineDataSet.highLightColor = getColorFromAttr(R.attr.colorOnSecondary)
                        lineDataSet.setDrawValues(false)
                        xAxis.valueFormatter = DateFormatter()
                        xAxis.position = XAxis.XAxisPosition.BOTTOM
                        xAxis.textColor = getColorFromAttr(R.attr.colorSecondaryVariant)
                        axisLeft.textColor = getColorFromAttr(R.attr.colorSecondaryVariant)
                        axisLeft.valueFormatter = PercentageFormatter()
                        axisRight.textColor = getColorFromAttr(R.attr.colorSecondaryVariant)
                        axisRight.valueFormatter = PercentageFormatter()
                        description.isEnabled = false
                        legend.textColor = getColorFromAttr(R.attr.colorControlHighlight)

                        val lineData = LineData(lineDataSet)
                        data = lineData
                        invalidate()
//                        // set manual x bounds to have nice steps
//                        val minX = dataset.minOf { it.fundingTime }.toDouble()
//                        val maxX = dataset.maxOf { it.fundingTime }.toDouble()
//                        debug("minX=$minX, maxX=$maxX")
//                        val minY = dataset.minOf { it.fundingRate } - 0.01
//                        val maxY = dataset.maxOf { it.fundingRate } + 0.01
//                        val numVerticalLabels = (maxY / 0.01).toInt()
//                        debug("minY=$minY, maxY=$maxY, numVerticalLabels=$numVerticalLabels")
                    }
                }
            }
        }
        coinData?.let {
            supportActionBar?.title = it.symbol
            binding.coinData = it
            symbolDetailsViewModel.getFundingRatesBySymbol(it.symbol)
        }
        binding.executePendingBindings()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

}

class DateFormatter : ValueFormatter() {

    // override this for custom formatting of XAxis or YAxis labels
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val sdf = SimpleDateFormat("MMM dd")
        val date = Date(value.toLong())
        val format = sdf.format(date)
        debug("getAxisLabel $value = $format")
        return format
    }
}

class PercentageFormatter : ValueFormatter() {

    // override this for custom formatting of XAxis or YAxis labels
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return "${value.toString().take(4)}%"
    }
}