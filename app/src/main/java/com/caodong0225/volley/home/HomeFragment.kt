package com.caodong0225.volley.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.caodong0225.volley.R

class HomeFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button

    private val imageList = listOf(
        "https://jwc.usst.edu.cn/_upload/article/images/a8/67/4d5275ea4d309d88b7b63233cc66/35edfab3-7c96-4bef-9f16-a999502ffd5b.jpg",
        "https://jwc.usst.edu.cn/_upload/article/images/a3/89/a259e26c418e81bff222836f7ffe/522a891c-5bd2-4e66-b0ef-daeaef320b3a.jpg",
        "https://jwc.usst.edu.cn/_upload/article/images/9b/5a/ca3ef39b442cacc6d8745f8b682a/9ceb1fd5-edf6-4248-9c74-34acc2ee0d62.jpg"
    )

    private var currentIndex = 0
    private lateinit var requestQueue: RequestQueue
    private val handler = Handler(Looper.getMainLooper())
    private val autoPlayRunnable = object : Runnable {
        override fun run() {
            showNextImage()
            handler.postDelayed(this, 3000) // 每 3 秒切换一张
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        imageView = view.findViewById(R.id.imageView)
        prevButton = view.findViewById(R.id.prevButton)
        nextButton = view.findViewById(R.id.nextButton)

        // 初始化 Volley 请求队列
        requestQueue = Volley.newRequestQueue(requireContext())

        // 初始化图片
        showImage()

        // 按钮事件
        prevButton.setOnClickListener { showPrevImage() }
        nextButton.setOnClickListener { showNextImage() }

        // 启动自动播放
        handler.postDelayed(autoPlayRunnable, 3000)

        return view
    }

    private fun showImage() {
        val imageUrl = imageList[currentIndex]

        // 使用 ImageRequest 加载网络图片
        val imageRequest = ImageRequest(
            imageUrl,
            { bitmap ->
                imageView.setImageBitmap(bitmap)
            },
            0, 0, ImageView.ScaleType.CENTER_CROP,
            null,
            { _ ->
                // 处理错误情况
                imageView.setImageResource(R.drawable.error_placeholder)
            }
        )

        // 将请求添加到队列中
        requestQueue.add(imageRequest)
    }

    private fun showNextImage() {
        currentIndex = (currentIndex + 1) % imageList.size
        showImage()
    }

    private fun showPrevImage() {
        currentIndex = if (currentIndex - 1 < 0) imageList.size - 1 else currentIndex - 1
        showImage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 停止自动播放
        handler.removeCallbacks(autoPlayRunnable)
        requestQueue.cancelAll { true } // 清理所有请求
    }
}