package com.train.config.https;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

/**
 * Created by sungang on 2017/3/20.
 */
@ConfigurationProperties(prefix = "custom.tomcat.https")
public class TomcatSslConnectorProperties {

    private Integer port;
    private Boolean ssl = true;
    private Boolean secure = true;
    private String scheme = "https";
    private File keystore;
    private String keystorePassword;
    //这里为了节省空间，省略了getters和setters，读者在实践的时候要加上


    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getSsl() {
        return ssl;
    }

    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public File getKeystore() {
        return keystore;
    }

    public void setKeystore(File keystore) {
        this.keystore = keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public void configureConnector(Connector connector) {
        if (port != null) {
            connector.setPort(port);
        }
        if (secure != null) {
            connector.setSecure(secure);
        }
        if (scheme != null) {
            connector.setScheme(scheme);
        }
        if (ssl != null) {
            connector.setProperty("SSLEnabled", ssl.toString());
        }
        if (keystore != null && keystore.exists()) {
            connector.setProperty("keystoreFile", keystore.getAbsolutePath());
            connector.setProperty("keystorePassword", keystorePassword);
        }
    }
}
