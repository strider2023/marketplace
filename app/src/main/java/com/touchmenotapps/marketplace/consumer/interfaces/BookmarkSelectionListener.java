package com.touchmenotapps.marketplace.consumer.interfaces;

import com.touchmenotapps.marketplace.consumer.dao.BookmarksDAO;

/**
 * Created by i7 on 21-10-2017.
 */

public interface BookmarkSelectionListener {

    void onBookmarkSelected(BookmarksDAO bookmarksDAO);
}
