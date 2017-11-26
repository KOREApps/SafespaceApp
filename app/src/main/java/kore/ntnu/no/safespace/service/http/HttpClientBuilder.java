package kore.ntnu.no.safespace.service.http;

import android.content.Context;
import android.util.Log;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import kore.ntnu.no.safespace.service.keystore.KeyStoreReader;
import kore.ntnu.no.safespace.utils.ApplicationContext;
import okhttp3.OkHttpClient;

/**
 * Class description..
 *
 * @author Robert
 */
public class HttpClientBuilder {

    /**
     * @return Default OkHttpClient
     */
    public static OkHttpClient getHttpClient() {
        return new OkHttpClient();
    }

    /**
     * !!! NOT WORKING !!!
     * Returns an OkHttpClient that accepts the loaded certificate. This should allow for use
     * of self signed certificates.
     * @return an OkHttpClient that accepts the loaded certificate. This should allow for use
     * of self signed certificates.
     */
    public static OkHttpClient getHttpSelfSignedCertClient() {
        try {
            Context context = ApplicationContext.getContext();
            KeyStoreReader keyStoreReader = new KeyStoreReader();
            KeyStore keyStore = keyStoreReader.readKeyStore(getPassword());
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

    /**
     * @return Returns an unsafe OkHttpClient that will accept any certificate.
     */
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return password string for keystore
     */
    private static String getPassword() {
        return "asdf1234";
    }

}
