package com.niuyun.hire.ui.bean;

import com.niuyunzhipin.greendao.AllTagBeanDao;
import com.niuyunzhipin.greendao.DaoSession;
import com.niuyunzhipin.greendao.DataBeanDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by chen.zhiwei on 2017-7-25.
 */
@Entity
public class AllTagBean {

    /**
     * code : 1000
     * msg : 成功
     * data : [{"galias":"QS_trade","gname":"行业分类","gid":1,"gsys":1},{"galias":"QS_company_type","gname":"企业性质分类","gid":2,"gsys":1},{"galias":"QS_wage","gname":"月薪分类","gid":3,"gsys":1},{"galias":"QS_jobs_nature","gname":"职位性质分类","gid":4,"gsys":1},{"galias":"QS_education","gname":"学历分类","gid":5,"gsys":1},{"galias":"QS_experience","gname":"工作经验分类","gid":6,"gsys":1},{"galias":"QS_scale","gname":"企业规模","gid":7,"gsys":1},{"galias":"QS_jobtag","gname":"职位标签","gid":10,"gsys":1},{"galias":"QS_resumetag","gname":"简历标签","gid":11,"gsys":1},{"galias":"QS_language","gname":"语言分类","gid":22,"gsys":1},{"galias":"QS_language_level","gname":"语言熟练程度","gid":23,"gsys":1},{"galias":"QS_current","gname":"目前状态分类","gid":25,"gsys":1},{"galias":"QS_age","gname":"年龄段分类","gid":28,"gsys":1}]
     */
    @Id
    private Long id;
    private int code;
    private String msg;

    @ToMany(referencedJoinProperty = "gid")
    private List<DataBean> data;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 977082931)
    private transient AllTagBeanDao myDao;

    @Generated(hash = 967215069)
    public AllTagBean(Long id, int code, String msg) {
        this.id = id;
        this.code = code;
        this.msg = msg;
    }

    @Generated(hash = 82631099)
    public AllTagBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1663408823)
    public List<DataBean> getData() {
        if (data == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DataBeanDao targetDao = daoSession.getDataBeanDao();
            List<DataBean> dataNew = targetDao._queryAllTagBean_Data(id);
            synchronized (this) {
                if (data == null) {
                    data = dataNew;
                }
            }
        }
        return data;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1283600904)
    public synchronized void resetData() {
        data = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1746794195)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAllTagBeanDao() : null;
    }
}