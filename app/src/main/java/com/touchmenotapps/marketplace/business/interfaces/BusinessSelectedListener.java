package com.touchmenotapps.marketplace.business.interfaces;

import com.touchmenotapps.marketplace.dao.BusinessDao;

/**
 * Created by arindamnath on 30/12/17.
 */

public interface BusinessSelectedListener {

    void onBusinessSelected(BusinessDao businessDao);
}
