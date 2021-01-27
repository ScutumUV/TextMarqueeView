package com.superc.textmarqueeview.app

import android.graphics.Color
import android.os.Bundle
import android.text.TextPaint
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.superc.textmarqueeview.MarqueeAdapter
import com.superc.textmarqueeview.OnMarqueeItemClickListener
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Copyright (C), 2021, 重庆八进制有限公司
 *
 * @author: SuperChen
 * @last-modifier: SuperChen
 * @version: 1.0
 * @create-date: 2021/1/26 11:24
 * @last-modify-date: 2021/1/26 11:24
 * @description:
 */
open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arr: MutableList<CharSequence> = mutableListOf(
                "昆明劫持案细节曝光：嫌犯被击毙前要求见女记者，她站出来了！",
                "拜登贴身的亚裔特工一夜爆红，他到底是什么来头？",
                "新华社批郑爽：让失德艺人彻底凉凉。",
                "11人获救！90秒回顾栖霞金矿被困矿工升井瞬间 每一次都掌声雷动！",
                "国家发改委有关负责人回应热点 菜价有望逐步回归常年水平。"
        )
//        tv1.setMarqueeAdapter(object : MarqueeAdapter {
//            override fun bind(ds: TextPaint, position: Int, item: Any?) {
//                ds.isUnderlineText = false
//                ds.color = Color.parseColor("#FFA500")
//            }
//        })
        tv1.setOnMarqueeItemClickListener(object : OnMarqueeItemClickListener {
            override fun onMarqueeItemClick(widget: View, position: Int, item: Any?) {
                Toast.makeText(this@MainActivity, position.toString() + " " + item?.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        tv1
        tv1.postDelayed({ tv1.setDatas(arr) }, 5000)
    }

}