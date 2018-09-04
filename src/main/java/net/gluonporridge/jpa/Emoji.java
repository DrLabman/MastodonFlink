package net.gluonporridge.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Emoji {

    @Id
    @Column(columnDefinition="TEXT")
    private String url;
    @Column(columnDefinition="TEXT")
    private String shortcode;
    @Column(columnDefinition="TEXT")
    private String staticUrl;

    public Emoji() {

    }

    public Emoji(com.sys1yagi.mastodon4j.api.entity.Emoji emoji) {
        shortcode = emoji.getShortcode();
        url = emoji.getUrl();
        staticUrl = emoji.getStaticUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emoji emoji = (Emoji) o;
        return Objects.equals(url, emoji.url);
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

    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public String getStaticUrl() {
        return staticUrl;
    }

    public void setStaticUrl(String staticUrl) {
        this.staticUrl = staticUrl;
    }
}
