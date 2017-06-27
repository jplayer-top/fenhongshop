package com.fanglin.fhlib.other;

import org.apache.http.conn.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

/**
 * 青岛芳林信息
 * Created by Plucky on 2017/1/4.
 * 功能描述: xutil2 需要继承自org.apache.http.conn.ssl下的类
 */

public class FHSSLSocketFactory extends SSLSocketFactory {

    private javax.net.ssl.SSLSocketFactory delegate;

    public FHSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, null, null);
        delegate = context.getSocketFactory();
    }

    @Override
    public Socket createSocket() throws IOException {
        return enableTLSOnSocket(delegate.createSocket());
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return enableTLSOnSocket(delegate.createSocket(socket, host, port, autoClose));
    }

    private Socket enableTLSOnSocket(Socket socket) {
        if (socket != null && (socket instanceof SSLSocket)) {

            String[] protocols = ((SSLSocket) socket).getEnabledProtocols();
            List<String> supports = new ArrayList<>();
            if (protocols != null && protocols.length > 0) {
                supports.addAll(Arrays.asList(protocols));
            }
            Collections.addAll(supports, "TLSv1.1", "TLSv1.2");
            ((SSLSocket) socket).setEnabledProtocols(supports.toArray(new String[supports.size()]));
        }
        return socket;
    }
}
