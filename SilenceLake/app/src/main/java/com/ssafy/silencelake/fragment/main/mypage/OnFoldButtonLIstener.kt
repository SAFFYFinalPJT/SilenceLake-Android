package com.ssafy.silencelake.fragment.main.mypage

import com.ssafy.silencelake.databinding.ItemListRecentOrderBinding
import com.ssafy.smartstore.response.OrderDetailResponse

interface OnFoldButtonLIstener {
    fun onClick(orderDetailList: MutableList<OrderDetailResponse>, binding: ItemListRecentOrderBinding, isExpanded: Boolean)
}