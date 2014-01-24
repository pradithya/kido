package com.progrema.superbaby.util;

import org.apache.http.util.EncodingUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by aria on 23/1/14.
 * @author aria
 */
public class SecurityUtils {

    public static String computeSHA1(String inputPlainText)
    {
        MessageDigest sha1 = null;

        try{
            sha1 = MessageDigest.getInstance("SHA-1");
        }catch (NoSuchAlgorithmException e)
        {
            //do nothing
        }

        try{
            sha1.update(inputPlainText.getBytes("ASCII"));
        }catch(UnsupportedEncodingException e){
            // do nothing
        }

        byte[] result = sha1.digest();

        return EncodingUtils.getAsciiString(result);

    }
}
