package com.ssafy.silencelake.fragment.main.menu.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.ssafy.silencelake.R
import com.ssafy.silencelake.databinding.FragmentOrderBinding
import com.ssafy.silencelake.fragment.main.menu.shoppinglist.ShoppingListViewModel


class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding
    private val activityViewModel by activityViewModels<ShoppingListViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateTotalPrice()
        binding.apply {
            buttonMinusOrder.setOnClickListener {
                if (textQuantityOrder.text.toString().toInt() > 1) {
                    textQuantityOrder.text = "${textQuantityOrder.text.toString().toInt() - 1}"
                    updateTotalPrice()
                }
            }
            buttonPlusOrder.setOnClickListener {
                textQuantityOrder.text = "${textQuantityOrder.text.toString().toInt() + 1}"
                updateTotalPrice()

            }
            buttonOrderOrder.setOnClickListener {

            }
        }
    }

    fun updateTotalPrice() {
        binding.textTotalPriceOrder.text = "${
            activityViewModel.selectedProduct.value!!.get(0)!!.productPrice * binding.textQuantityOrder.text.toString()
                .toInt()
        }원"
    }
}