---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by momo.
--- DateTime: 2018/11/15 下午8:50
---
--- 入口文件
---

require("Util.SystemDefine")
local NavigationView = require("TestViewPagerCollectionViewCell.NavigationView")
local ViewPagerCell  = require("TestViewPagerCollectionViewCell.ViewPagerCell")


kScreenScale = (kScreenWidth - 15) / 360
kViewPagerHeight = kScreenHeight - kNavBarHeight - kStatusBarHeight


function createSubview()
    titles = Array()
    titles:add("聊天室")
    titles:add("附近")
    titles:add("K歌之王")
    titles:add("KTV")

    navView = NavigationView:new()
    navView:createSubview(titles)
    navView.contentView:width(kScreenWidth):height(kStatusBarHeight + kNavBarHeight)

    viewPager = ViewPager()
    viewPager:bgColor(Color(200, 180, 250, 1))
    viewPager:autoScroll(false)
    viewPager:showIndicator(false)

    viewPager:recurrence(true)


    viewPager:width(kScreenWidth):height(kViewPagerHeight)

    viewPager:setPreRenderCount(0)
    viewPager:setScrollEnable(true)

    viewPager:adapter(createCollectionViewAdapter())
    navView:bindViewPager(viewPager)

    liner = LinearLayout(LinearType.VERTICAL)
    liner:addView(navView.contentView):addView(viewPager)

    window:addView(liner)
    window:bgColor(Color(250, 200, 230, 1))
end


function createCollectionViewAdapter()
    adapter = ViewPagerAdapter()
    adapter:getCount(function(sectionidx)
        return 4
    end)
    adapter:initCell(function(cell, row)
        cell.m_cell = ViewPagerCell:new()
        cell.m_cell:createSubview(kViewPagerHeight)
        cell.m_cell.contentView:width(kScreenWidth):height(kViewPagerHeight)
        cell.contentView:addView(cell.m_cell.contentView)
        cell.contentView:bgColor(WHITE)
    end)

    totoalnumber = 0
    adapter:fillCellData(function(cell, row)
        cell.m_cell.title = titles:get(row)
        cell.m_cell:checkRefresh()

        totoalnumber = totoalnumber + 1;
        print('----------------------------------------'..tostring(totoalnumber))
    end)

    return adapter
end

createSubview()
