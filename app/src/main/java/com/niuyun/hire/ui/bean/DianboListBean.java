package com.niuyun.hire.ui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chen.zhiwei on 2017-9-29.
 */

public class DianboListBean implements Serializable{

    /**
     * code : 1000
     * msg : 成功
     * data : {"pageNo":1,"totalCount":6,"pageCount":6,"pageSize":1,"data":[{"id":1,"fileId":"9031868223279867845","videoUrl":"http://1254373020.vod2.myqcloud.com/78e1171avodgzp1254373020/fa5d9b579031868223279867845/ulorQUoTqOoA.wmv","videoVerifyContent":"qKkxmFjkhr4pTeguwNND6tibJgdFeHBUaW1lPTE1MDY1NjU4MjQmRmlsZUlkPTkwMzE4NjgyMjMyNzk4Njc4NDU=","imageUrl":"http://1254373020.vod2.myqcloud.com/78e1171avodgzp1254373020/fa5d9b579031868223279867845/9031868223279867846.jpg","imageVerifyContent":"HNDLIbpc8ot2dKb17JsH7Tu6Ez5FeHBUaW1lPTE1MDY1NjU4MjQmRmlsZUlkPTkwMzE4NjgyMjMyNzk4Njc4NDY=","title":null,"describe":null}]}
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

    public static class DataBeanX implements Serializable{
        /**
         * pageNo : 1
         * totalCount : 6
         * pageCount : 6
         * pageSize : 1
         * data : [{"id":1,"fileId":"9031868223279867845","videoUrl":"http://1254373020.vod2.myqcloud.com/78e1171avodgzp1254373020/fa5d9b579031868223279867845/ulorQUoTqOoA.wmv","videoVerifyContent":"qKkxmFjkhr4pTeguwNND6tibJgdFeHBUaW1lPTE1MDY1NjU4MjQmRmlsZUlkPTkwMzE4NjgyMjMyNzk4Njc4NDU=","imageUrl":"http://1254373020.vod2.myqcloud.com/78e1171avodgzp1254373020/fa5d9b579031868223279867845/9031868223279867846.jpg","imageVerifyContent":"HNDLIbpc8ot2dKb17JsH7Tu6Ez5FeHBUaW1lPTE1MDY1NjU4MjQmRmlsZUlkPTkwMzE4NjgyMjMyNzk4Njc4NDY=","title":null,"describe":null}]
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

        public static class DataBean implements Serializable{
            /**
             * id : 1
             * fileId : 9031868223279867845
             * videoUrl : http://1254373020.vod2.myqcloud.com/78e1171avodgzp1254373020/fa5d9b579031868223279867845/ulorQUoTqOoA.wmv
             * videoVerifyContent : qKkxmFjkhr4pTeguwNND6tibJgdFeHBUaW1lPTE1MDY1NjU4MjQmRmlsZUlkPTkwMzE4NjgyMjMyNzk4Njc4NDU=
             * imageUrl : http://1254373020.vod2.myqcloud.com/78e1171avodgzp1254373020/fa5d9b579031868223279867845/9031868223279867846.jpg
             * imageVerifyContent : HNDLIbpc8ot2dKb17JsH7Tu6Ez5FeHBUaW1lPTE1MDY1NjU4MjQmRmlsZUlkPTkwMzE4NjgyMjMyNzk4Njc4NDY=
             * title : null
             * describe : null
             */

            private int id;
            private String fileId;
            private String videoUrl;
            private String videoVerifyContent;
            private String imageUrl;
            private String imageVerifyContent;
            private String title;
            private String describe;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getFileId() {
                return fileId;
            }

            public void setFileId(String fileId) {
                this.fileId = fileId;
            }

            public String getVideoUrl() {
                return videoUrl;
            }

            public void setVideoUrl(String videoUrl) {
                this.videoUrl = videoUrl;
            }

            public String getVideoVerifyContent() {
                return videoVerifyContent;
            }

            public void setVideoVerifyContent(String videoVerifyContent) {
                this.videoVerifyContent = videoVerifyContent;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getImageVerifyContent() {
                return imageVerifyContent;
            }

            public void setImageVerifyContent(String imageVerifyContent) {
                this.imageVerifyContent = imageVerifyContent;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDescribe() {
                return describe;
            }

            public void setDescribe(String describe) {
                this.describe = describe;
            }
        }
    }
}
