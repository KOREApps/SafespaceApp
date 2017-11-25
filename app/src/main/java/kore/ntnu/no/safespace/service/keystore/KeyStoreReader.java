package kore.ntnu.no.safespace.service.keystore;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import kore.ntnu.no.safespace.utils.ApplicationContext;

/**
 * Class description..
 *
 * @author Robert
 */
public class KeyStoreReader {

    public KeyStore readKeyStore(String passwordString) throws KeyStoreException {
        Context c = ApplicationContext.getContext();
        String file = "keystore";
        String path = "raw";
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] password = passwordString.toCharArray();
        try {
            InputStream stream = c.getResources().openRawResource(c.getResources().getIdentifier(file, path, c.getPackageName()));
            ks.load(stream, password);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (CertificateException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ks;
    }

}
