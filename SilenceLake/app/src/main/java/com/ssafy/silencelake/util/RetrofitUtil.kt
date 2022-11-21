package com.ssafy.silencelake.util

import com.ssafy.silencelake.api.UserApi
import com.ssafy.smartstore.api.CommentApi
import com.ssafy.smartstore.api.OrderApi
import com.ssafy.smartstore.api.ProductApi

class RetrofitUtil {
    companion object{
        val orderService = ApplicationClass.retrofit.create(OrderApi::class.java)
        val productService = ApplicationClass.retrofit.create(ProductApi::class.java)
        val userService = ApplicationClass.retrofit.create(UserApi::class.java)
        val commentService = ApplicationClass.retrofit.create(CommentApi::class.java)
    }
}