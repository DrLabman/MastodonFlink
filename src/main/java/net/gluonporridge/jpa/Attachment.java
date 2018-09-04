package net.gluonporridge.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Attachment {

    @Id
    private long id;
    @Column(columnDefinition="TEXT")
    private String url;
    @Column(columnDefinition="TEXT")
    private String previewUrl;
    @Column(columnDefinition="TEXT")
    private String remoteUrl;
    @Column(columnDefinition="TEXT")
    private String textUrl;
    @Column(columnDefinition="TEXT")
    private String type;

    public Attachment() {

    }

    public Attachment(com.sys1yagi.mastodon4j.api.entity.Attachment attachment) {
        url = attachment.getUrl();
        id = attachment.getId();
        previewUrl = attachment.getPreviewUrl();
        remoteUrl = attachment.getRemoteUrl();
        textUrl = attachment.getTextUrl();
        type = attachment.getType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getTextUrl() {
        return textUrl;
    }

    public void setTextUrl(String textUrl) {
        this.textUrl = textUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
