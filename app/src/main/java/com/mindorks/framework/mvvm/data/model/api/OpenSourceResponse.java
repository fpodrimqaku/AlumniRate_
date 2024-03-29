
package com.mindorks.framework.mvvm.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;



public class OpenSourceResponse {

    @Expose
    @SerializedName("data")
    private List<Repo> data;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("status_code")
    private String statusCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpenSourceResponse)) {
            return false;
        }

        OpenSourceResponse that = (OpenSourceResponse) o;

        if (!statusCode.equals(that.statusCode)) {
            return false;
        }
        if (!message.equals(that.message)) {
            return false;
        }
        return data != null ? data.equals(that.data) : that.data == null;

    }

    @Override
    public int hashCode() {
        int result = statusCode.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    public List<Repo> getData() {
        return data;
    }

    public void setData(List<Repo> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public static class Repo {

        @Expose
        @SerializedName("img_url")
        private String coverImgUrl;

        @Expose
        @SerializedName("description")
        private String description;

        @Expose
        @SerializedName("project_url")
        private String projectUrl;

        @Expose
        @SerializedName("title")
        private String title;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Repo)) {
                return false;
            }

            Repo repo = (Repo) o;

            if (!projectUrl.equals(repo.projectUrl)) {
                return false;
            }
            if (!coverImgUrl.equals(repo.coverImgUrl)) {
                return false;
            }
            if (!title.equals(repo.title)) {
                return false;
            }
            return description.equals(repo.description);

        }

        @Override
        public int hashCode() {
            int result = projectUrl.hashCode();
            result = 31 * result + coverImgUrl.hashCode();
            result = 31 * result + title.hashCode();
            result = 31 * result + description.hashCode();
            return result;
        }

        public String getCoverImgUrl() {
            return coverImgUrl;
        }

        public String getDescription() {
            return description;
        }

        public String getProjectUrl() {
            return projectUrl;
        }

        public String getTitle() {
            return title;
        }
    }
}
