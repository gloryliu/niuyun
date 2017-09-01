package com.niuyun.hire.utils.map;

import com.baidu.location.BDLocation;

/**
 */
public interface LocationLinstener {

    public void locationSuccess(BDLocation location);

    public void locationError();
}
