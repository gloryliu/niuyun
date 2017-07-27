package com.niuyun.hire.ui.chat.widget;

import java.util.List;

/**
 * Created by chen.zhiwei on 2017-7-27.
 */

public class trsss {

    /**
     * action : post
     * application : 3aa67eb0-71c7-11e7-9659-bd6bc34492fc
     * path : /users
     * uri : https://a1.easemob.com/1117170712115890/niuyunzp/users
     * entities : [{"uuid":"749b8050-7269-11e7-b041-7d65cbf62023","type":"user","created":1501118291157,"modified":1501118291157,"username":"aaaa1234567","activated":true}]
     * timestamp : 1501118291162
     * duration : 0
     * organization : 1117170712115890
     * applicationName : niuyunzp
     */

    private String action;
    private String application;
    private String path;
    private String uri;
    private long timestamp;
    private int duration;
    private String organization;
    private String applicationName;
    private List<EntitiesBean> entities;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public List<EntitiesBean> getEntities() {
        return entities;
    }

    public void setEntities(List<EntitiesBean> entities) {
        this.entities = entities;
    }

    public static class EntitiesBean {
        /**
         * uuid : 749b8050-7269-11e7-b041-7d65cbf62023
         * type : user
         * created : 1501118291157
         * modified : 1501118291157
         * username : aaaa1234567
         * activated : true
         */

        private String uuid;
        private String type;
        private long created;
        private long modified;
        private String username;
        private boolean activated;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getCreated() {
            return created;
        }

        public void setCreated(long created) {
            this.created = created;
        }

        public long getModified() {
            return modified;
        }

        public void setModified(long modified) {
            this.modified = modified;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public boolean isActivated() {
            return activated;
        }

        public void setActivated(boolean activated) {
            this.activated = activated;
        }
    }
}
