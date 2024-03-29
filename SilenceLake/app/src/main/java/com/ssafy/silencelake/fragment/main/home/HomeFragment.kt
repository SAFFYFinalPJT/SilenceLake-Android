package com.ssafy.silencelake.fragment.main.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.hw_android_network_05_hojojeong.util.CustomDialog
import com.ssafy.silencelake.R
import com.ssafy.silencelake.databinding.FragmentHomeBinding
import com.ssafy.silencelake.dto.ProductDto
import com.ssafy.silencelake.fragment.main.menu.ProductMenuViewModel
import com.ssafy.silencelake.fragment.main.menu.detail.ProductDetailFragment
import com.ssafy.silencelake.fragment.main.menu.shoppinglist.ShoppingListViewModel
import com.ssafy.silencelake.util.ApplicationClass
import pyxis.uzuki.live.rollingbanner.RollingViewPagerAdapter


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    lateinit var mContext: Context
    private var bannerItemList = arrayListOf<String>()
    private val productMenuViewModel by activityViewModels<ProductMenuViewModel>()
    private val shoppingListViewModel by activityViewModels<ShoppingListViewModel>()
    private val nfcStateViewModel by activityViewModels<NFCStateViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initBanner()
        startNFCTag()
    }

    private fun initData() {
        productMenuViewModel.getRecommendedProduct(ApplicationClass.sharedPreferencesUtil.getUser().id)

        productMenuViewModel.recommendedMenuLiveData.observe(viewLifecycleOwner) {
            initAdapter()
        }
        initBannerItemData()
    }

    private fun initBannerItemData() {
        val list = mutableListOf<String>()
        list.add("banner_ssafy1")
        list.add("banner_ssafy_ad")
        list.add("banner_ssafy_logo")
        bannerItemList = list as ArrayList<String>
    }

    private fun initBanner() {
        val banner = binding.viewpagerBannerHomefg
        val bannerAdapter = BannerAdapter(requireContext(), bannerItemList)
        banner.setAdapter(bannerAdapter)
    }

    private fun initAdapter() {
        val adapter = RecommendedMenuAdapter(requireContext())
        val recommendedMenu = binding.rcvRecommendedHome
        recommendedMenu.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter.itemlist =
            productMenuViewModel.recommendedMenuLiveData.value as MutableList<ProductDto>

        adapter.onClickRecommendedItem = object : OnClickRecommendedItem {
            override fun onClick(product: ProductDto) {
                shoppingListViewModel.getSelectedProduct(product.id)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_main, ProductDetailFragment())
                    .addToBackStack(null).commit()
            }
        }

        recommendedMenu.adapter = adapter
    }


    inner class BannerAdapter : RollingViewPagerAdapter<String> {
        constructor(context: Context, itemList: ArrayList<String>) : super(context, itemList)

        override fun getView(position: Int, item: String): View {
            val view = LayoutInflater.from(mContext).inflate(R.layout.container_banner, null, false)
            val bannerImg = view.findViewById<ImageView>(R.id.img_banner_container)

            val bannerItem = getItem(position)
            val resId = requireContext().resources.getIdentifier(
                bannerItem,
                "drawable",
                requireContext().packageName
            )
            bannerImg.setImageResource(resId)
            return view
        }
    }

    private fun startNFCTag() {
        binding.layoutContainerNfc.setOnClickListener {
            nfcStateViewModel.customDialog.showCafeInfo(false)
            nfcStateViewModel.customDialog.showDialog()
            nfcStateViewModel.customDialog.confirmBtnEventListener()
        }
    }
}