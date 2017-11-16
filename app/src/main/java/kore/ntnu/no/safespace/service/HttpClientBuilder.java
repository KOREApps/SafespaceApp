package kore.ntnu.no.safespace.service;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;

/**
 * Created by robert on 11/16/17.
 */

public class HttpClientBuilder extends ContextWrapper {

    public HttpClientBuilder(Context base) {
        super(base);
    }

    public static OkHttpClient getHttpClient() {
        return new OkHttpClient();
    }

    public static OkHttpClient getHttpSelfSignedCertClient(Object object) {
        try {
            KeyStore keyStore = readKeyStore(object);
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, getPassword().toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .build();
            return client;
        } catch (Exception ex) {
            Log.e(HttpClientBuilder.class.getSimpleName(), ex.getMessage());
            return null;
        }
    }

    private static KeyStore readKeyStore(Object object) throws Exception {
        String file = "keystore";
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] password = getPassword().toCharArray();
        try {
            InputStream in = HttpClientBuilder.class.getResourceAsStream(file);

            ks.load(in, password);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return ks;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public KeyStore getKeyStoreFile() throws KeyStoreException {
        String file = "keystore";
        String path = "raw";
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] password = getPassword().toCharArray();
        try {
            InputStream stream = getResources().openRawResource(getResources().getIdentifier(file, path, getPackageName()));
            ks.load(stream, password);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (CertificateException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ks;
    }

    private static String getPassword(){
        return "asdf1234";
    }

}
