package com.yamo.cdcommoncore.config;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * http信任
 */
public class TrustingSSLSocketFactory extends SSLSocketFactory implements X509TrustManager, X509KeyManager {
    private static final Map<String, SSLSocketFactory> sslSocketFactory = new LinkedHashMap<String, SSLSocketFactory>();
    private static final char[] KEYSTORE_PASSWORD = "password".toCharArray();
    private final static String[] ENABLED_CIPHER_SUITES = {"TLS_RSA_WITH_AES_256_CBC_SHA"};
    private SSLSocketFactory delegate;
    private final String serverAlias;
    private PrivateKey privateKey;
    private X509Certificate[] certificates;

    private TrustingSSLSocketFactory(String serverAlias){
        try{
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(new KeyManager[]{this}, new TrustManager[]{this}, new SecureRandom());
            this.delegate = sc.getSocketFactory();
        }catch (Exception e){
            e.printStackTrace();
        }
        this.serverAlias = serverAlias;
        if(serverAlias.isEmpty()){
            this.privateKey = null;
            this.certificates = null;
        }else{
            try{
                KeyStore keyStore = loadKeyStore(TrustingSSLSocketFactory.class.getResourceAsStream("/keystore.jks"));
                this.privateKey = (PrivateKey) keyStore.getKey(serverAlias, KEYSTORE_PASSWORD);
                Certificate[] rawChain = keyStore.getCertificateChain(serverAlias);
                this.certificates = Arrays.copyOf(rawChain, rawChain.length, X509Certificate[].class);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static SSLSocketFactory get(){
        return get("");
    }

    public synchronized static SSLSocketFactory get(String serverAlias){
        if(!sslSocketFactory.containsKey(serverAlias)){
            sslSocketFactory.put(serverAlias, new TrustingSSLSocketFactory(serverAlias));
        }
        return sslSocketFactory.get(serverAlias);
    }

    static Socket setEnabledCipherSuites(Socket socket){
        SSLSocket.class.cast(socket).setEnabledCipherSuites(ENABLED_CIPHER_SUITES);
        return socket;
    }

    private static KeyStore loadKeyStore(InputStream inputStream) throws IOException{
        try{
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, KEYSTORE_PASSWORD);
            return keyStore;
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            inputStream.close();
        }
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return ENABLED_CIPHER_SUITES;
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return ENABLED_CIPHER_SUITES;
    }

    @Override
    public Socket createSocket(Socket socket, String s, int i, boolean b) throws IOException {
        return setEnabledCipherSuites(delegate.createSocket(socket, s, i, b));
    }

    @Override
    public Socket createSocket(String s, int i) throws IOException, UnknownHostException {
        return setEnabledCipherSuites(delegate.createSocket(s, i));
    }

    @Override
    public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException, UnknownHostException {
        return setEnabledCipherSuites(delegate.createSocket(s, i, inetAddress, i1));
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return setEnabledCipherSuites(delegate.createSocket(inetAddress, i));
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) throws IOException {
        return setEnabledCipherSuites(delegate.createSocket(inetAddress, i, inetAddress1, i1));
    }

    @Override
    public String[] getClientAliases(String s, Principal[] principals) {
        return null;
    }

    @Override
    public String chooseClientAlias(String[] strings, Principal[] principals, Socket socket) {
        return null;
    }

    @Override
    public String[] getServerAliases(String s, Principal[] principals) {
        return null;
    }

    @Override
    public String chooseServerAlias(String s, Principal[] principals, Socket socket) {
        return serverAlias;
    }

    @Override
    public X509Certificate[] getCertificateChain(String s) {
        return certificates;
    }

    @Override
    public PrivateKey getPrivateKey(String s) {
        return privateKey;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
