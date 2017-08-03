package com.niuyun.hire.ui.bean;

import java.util.List;

/**
 * Created by chen.zhiwei on 2017-8-3.
 */

public class LiveListBean {

    /**
     * code : 1000
     * msg : 成功
     * data : {"pageNo":1,"totalCount":2,"pageCount":1,"pageSize":10,"data":[{"id":1,"uid":10,"channelId":128593,"userId":"fb3a182807","name":"小明正在直播","url":"http://uflive.videocc.net/recordf/","stream":"fb3a18280720170803103548577","m3u8Url":"http://uhlive.videocc.net/record/fb3a18280720170803103548577/playlist.m3u8","coverImage":""},{"id":2,"uid":30,"channelId":128647,"userId":"fb3a182807","name":"&#x8001;&#x738B;&#x76F4;&#x8058;","url":"http://uflive.videocc.net/recordf/","stream":"fb3a18280720170803155740298","m3u8Url":"http://uhlive.videocc.net/record/fb3a18280720170803155740298/playlist.m3u8","coverImage":""}]}
     */

    private int code;
    private String msg;
    private DataBeanX data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * pageNo : 1
         * totalCount : 2
         * pageCount : 1
         * pageSize : 10
         * data : [{"id":1,"uid":10,"channelId":128593,"userId":"fb3a182807","name":"小明正在直播","url":"http://uflive.videocc.net/recordf/","stream":"fb3a18280720170803103548577","m3u8Url":"http://uhlive.videocc.net/record/fb3a18280720170803103548577/playlist.m3u8","coverImage":""},{"id":2,"uid":30,"channelId":128647,"userId":"fb3a182807","name":"&#x8001;&#x738B;&#x76F4;&#x8058;","url":"http://uflive.videocc.net/recordf/","stream":"fb3a18280720170803155740298","m3u8Url":"http://uhlive.videocc.net/record/fb3a18280720170803155740298/playlist.m3u8","coverImage":""}]
         */

        private int pageNo;
        private int totalCount;
        private int pageCount;
        private int pageSize;
        private List<DataBean> data;

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 1
             * uid : 10
             * channelId : 128593
             * userId : fb3a182807
             * name : 小明正在直播
             * url : http://uflive.videocc.net/recordf/
             * stream : fb3a18280720170803103548577
             * m3u8Url : http://uhlive.videocc.net/record/fb3a18280720170803103548577/playlist.m3u8
             * coverImage :
             */

            private int id;
            private int uid;
            private int channelId;
            private String userId;
            private String name;
            private String url;
            private String stream;
            private String m3u8Url;
            private String coverImage;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getChannelId() {
                return channelId;
            }

            public void setChannelId(int channelId) {
                this.channelId = channelId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getStream() {
                return stream;
            }

            public void setStream(String stream) {
                this.stream = stream;
            }

            public String getM3u8Url() {
                return m3u8Url;
            }

            public void setM3u8Url(String m3u8Url) {
                this.m3u8Url = m3u8Url;
            }

            public String getCoverImage() {
                return coverImage;
            }

            public void setCoverImage(String coverImage) {
                this.coverImage = coverImage;
            }
        }
    }
}
