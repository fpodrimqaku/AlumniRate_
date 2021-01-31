
package com.mindorks.framework.mvvm.ui.feed.blogs;

import com.mindorks.framework.mvvm.data.model.api.BlogResponse;
import java.util.List;


public interface BlogNavigator {

    void handleError(Throwable throwable);

    void updateBlog(List<BlogResponse.Blog> blogList);
}
