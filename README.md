# TextMarqueeView

原生TextView的marquee写法，只是针对点击position做了判断，直接下载后使用即可

具体用法：

1、默认字体颜色为#999999，如果需要设置字体颜色，请使用：
<br>
```Java
  app:marqueeTextColor="@color/colorPrimary"
```

  或
<br>
```Java
  setTextColor(color: Int){
  }
```

  或
<br>
```Java
  setMarqueeTextColors(color: Int){
  }
```


  如果想每一个数据都设置不同的字体，请使用：
<br>
```Java
  tv1.setMarqueeAdapter(object : MarqueeAdapter {
            override fun bind(ds: TextPaint, position: Int, item: Any?) {
                ds.isUnderlineText = false
                ds.color = Color.parseColor("#FFA500")
            }
  })
```


2、如果想要拥有点击事件，请使用：
<br>
```Java
  tv1.setOnMarqueeItemClickListener(object : OnMarqueeItemClickListener {
             override fun onMarqueeItemClick(widget: View, position: Int, item: Any?) {
                Toast.makeText(this@MainActivity, position.toString() + " " + item?.toString(), Toast.LENGTH_SHORT).show()
             }
  })
```

3、创建数据
<br>
```Java
  val arr: MutableList<CharSequence> = mutableListOf(
                  "昆明劫持案细节曝光：嫌犯被击毙前要求见女记者，她站出来了！",
                  "拜登贴身的亚裔特工一夜爆红，他到底是什么来头？",
                  "新华社批郑爽：让失德艺人彻底凉凉。",
                  "11人获救！90秒回顾栖霞金矿被困矿工升井瞬间 每一次都掌声雷动！",
                  "国家发改委有关负责人回应热点 菜价有望逐步回归常年水平。"
  )
```

<br>
```Java
  tv1.setDatas(arr)
```


  需要注意的是，设置字体颜色需要在setDatas()方法之前调用


- ![演示.gif](演示.gif)
