package com.niuyun.hire.utils.map;

import com.baidu.mapapi.search.poi.PoiResult;

/**
 * Created by dai.fengming on 2015/12/2.
 */
public interface PoiSearchResultListener {
    void pointResult(PoiResult result, String text);
}
