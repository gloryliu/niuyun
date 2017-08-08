package com.niuyun.hire.ui.polyvLive.bean;

/**
 * Created by chen.zhiwei on 2017-8-8.
 */

public class CreateLiveBean {

    /**
     * code : 1000
     * msg : 成功
     * data : {"id":10,"uid":0,"channelId":128907,"userId":"fb3a182807","name":"string","url":"http://uflive.videocc.net/recordf/","stream":"fb3a18280720170808113809931","m3u8Url":"http://uhlive.videocc.net/record/fb3a18280720170808113809931/playlist.m3u8","coverImage":"string","publisher":"string","logoImage":"","tag":"string","tagCn":"string","liveDescribe":"string"}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 10
         * uid : 0
         * channelId : 128907
         * userId : fb3a182807
         * name : string
         * url : http://uflive.videocc.net/recordf/
         * stream : fb3a18280720170808113809931
         * m3u8Url : http://uhlive.videocc.net/record/fb3a18280720170808113809931/playlist.m3u8
         * coverImage : string
         * publisher : string
         * logoImage :
         * tag : string
         * tagCn : string
         * liveDescribe : string
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
        private String publisher;
        private String logoImage;
        private String tag;
        private String tagCn;
        private String liveDescribe;

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

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getLogoImage() {
            return logoImage;
        }

        public void setLogoImage(String logoImage) {
            this.logoImage = logoImage;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTagCn() {
            return tagCn;
        }

        public void setTagCn(String tagCn) {
            this.tagCn = tagCn;
        }

        public String getLiveDescribe() {
            return liveDescribe;
        }

        public void setLiveDescribe(String liveDescribe) {
            this.liveDescribe = liveDescribe;
        }
    }
}
