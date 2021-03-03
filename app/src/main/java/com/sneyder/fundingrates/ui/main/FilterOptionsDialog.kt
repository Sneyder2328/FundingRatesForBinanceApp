/*
 * Copyright (C) 2018 Sneyder Angulo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sneyder.fundingrates.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sneyder.fundingrates.R
import com.sneyder.fundingrates.data.model.Order

class FilterOptionsDialog : BottomSheetDialogFragment() {

    companion object {

        private const val EXTRA_INDEX_SCORE_RANGE = "indexScoreRange"
        private const val EXTRA_ORDER = "order"

        fun newInstance(
            indexScoreRangeSelected: Int = 0,
            order: Order = Order.DESC
        ): FilterOptionsDialog {
            val selectImageDialog = FilterOptionsDialog()
            selectImageDialog.arguments = bundleOf(
                EXTRA_INDEX_SCORE_RANGE to indexScoreRangeSelected,
                EXTRA_ORDER to order,
            )
            return selectImageDialog
        }

    }

    private var filterOptionsListener: SelectFilterOptionsListener? = null
    private val indexScoreRange by lazy { arguments?.getInt(EXTRA_INDEX_SCORE_RANGE, 0) ?: 0 }
    private val order: Order by lazy { arguments?.getParcelable<Order>(EXTRA_ORDER) ?: Order.DESC }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.bottom_sheet_filter_options, container, false)
        val scoreRadioGroup = view.findViewById<RadioGroup>(R.id.scoreRangeRadioGroup)
        (scoreRadioGroup.getChildAt(indexScoreRange) as RadioButton).isChecked = true
        scoreRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            filterOptionsListener?.onScoreRangeSelected(
                scoreRadioGroup.indexOfChild(view.findViewById(checkedId))
            )
            dismissAllowingStateLoss()
        }
        val orderRadioGroup = view.findViewById<RadioGroup>(R.id.sortByRadioGroup)
        if (order == Order.ASC) view.findViewById<RadioButton>(R.id.ascRadioBtn).isChecked = true
        else view.findViewById<RadioButton>(R.id.descRadioBtn).isChecked = true
        orderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            filterOptionsListener?.onOrderSelected(if (checkedId == R.id.ascRadioBtn) Order.ASC else Order.DESC)
            dismissAllowingStateLoss()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        filterOptionsListener = when {
            context is SelectFilterOptionsListener -> {
                context
            }
            parentFragment is SelectFilterOptionsListener -> {
                parentFragment as SelectFilterOptionsListener
            }
            else -> {
                throw RuntimeException("$context or $parentFragment must implement SelectScoreRangeListener")
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        filterOptionsListener = null
    }

    interface SelectFilterOptionsListener {
        fun onScoreRangeSelected(index: Int)
        fun onOrderSelected(order: Order)
    }

}