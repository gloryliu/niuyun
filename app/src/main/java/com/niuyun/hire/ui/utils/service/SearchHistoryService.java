package com.niuyun.hire.ui.utils.service;

import android.content.Context;

import com.niuyun.hire.base.BaseContext;
import com.niuyun.hire.ui.bean.SearchHistoryBean;
import com.niuyunzhipin.greendao.SearchHistoryBeanDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * Created by sun.luwei on 2015/12/14.
 * 搜索 关键词 业务逻辑
 */
public class SearchHistoryService {
    private Context context;
    private SearchHistoryBeanDao searchHistoryBeanDao;

    public SearchHistoryService(Context context) {
        this.context = context;
        searchHistoryBeanDao = BaseContext.getInstance().getDaoSession().getSearchHistoryBeanDao();
    }

    /**
     * @param searchHistory
     * @return 保存一对象
     */
    public void save(SearchHistoryBean searchHistory) {
        searchHistoryBeanDao.save(searchHistory);
    }

    /**
     * @param searchHistory
     * @return 保存最近 搜索 条目
     */
    public static int MAX = 10;

    public void saveRecent(SearchHistoryBean searchHistory) {
        List<SearchHistoryBean> listdelete = null;
        Query<SearchHistoryBean> querydelete = null;

        querydelete = searchHistoryBeanDao.queryBuilder().where(SearchHistoryBeanDao.Properties.Name.eq(searchHistory.getName()),
                SearchHistoryBeanDao.Properties.Type.eq(searchHistory.getType())).build();
        //查找同名同类型
        listdelete = querydelete.list();
        //删除同名同类型
        searchHistoryBeanDao.deleteInTx(listdelete);
        //保存
        searchHistoryBeanDao.insert(searchHistory);

        if (getCount(searchHistory.getType()) > MAX) {
            Query<SearchHistoryBean> maxdelete = null;
            maxdelete = searchHistoryBeanDao.queryBuilder().where(SearchHistoryBeanDao.Properties.Type.eq(searchHistory.getType())).build();
            List<SearchHistoryBean> listmax = maxdelete.list();

            List<SearchHistoryBean> newlist = listmax.subList(0, MAX);

            searchHistoryBeanDao.deleteInTx(listmax);

            searchHistoryBeanDao.insertInTx(newlist);

        }
    }

    /**
     * 删除一条
     *
     * @param searchHistory
     */
    public void deleteByItem(SearchHistoryBean searchHistory) {
        Query<SearchHistoryBean> querydelete = null;
        List<SearchHistoryBean> listdelete = null;
        querydelete = searchHistoryBeanDao.queryBuilder().where(SearchHistoryBeanDao.Properties.Name.eq(searchHistory.getName()),
                SearchHistoryBeanDao.Properties.Type.eq(searchHistory.getType())).build();
        //查找同名同类型
        listdelete = querydelete.list();
        //删除同名同类型
        searchHistoryBeanDao.deleteInTx(listdelete);
    }

    /**
     * @return 获取全部数量
     */

    public long getCount(int type) {
        List<SearchHistoryBean> list = null;
        Query<SearchHistoryBean> querydelete = null;

        querydelete = searchHistoryBeanDao.queryBuilder().where(SearchHistoryBeanDao.Properties.Type.eq(type)).build();

        list = querydelete.list();

        if (list != null) {
            return list.size();
        }

        return 0l;
    }

    /**
     * @return 获取 全部
     */
    public List<SearchHistoryBean> getAll(int type) {
        List<SearchHistoryBean> list = null;
        Query<SearchHistoryBean> querydelete = null;

        querydelete = searchHistoryBeanDao.queryBuilder().where(SearchHistoryBeanDao.Properties.Type.eq(type)).build();

        list = querydelete.list();
        return list;
    }

    /**
     * 清空所有的数据
     */
    public void ClearAllData(int type) {
        List<SearchHistoryBean> list = null;
        Query<SearchHistoryBean> querydelete = null;

        querydelete = searchHistoryBeanDao.queryBuilder().where(SearchHistoryBeanDao.Properties.Type.eq(type)).build();

        list = querydelete.list();

        searchHistoryBeanDao.deleteInTx(list);
    }

}
