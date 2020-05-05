/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import javafx.application.Platform;

/**
 *
 * @author ander
 */
public class HashAlgorithm {

    /**
     * Hashes the input with a SHA-256 hash algorithm and converts it to a HexString of a fixed size of 15.
     *
     * @param password The password entered
     * @param salt The salt saved for the specific user
     * @return HexString version of entered password after hashing
     */
    public static String generateHash(String password, String salt) {
        StringBuilder hash = new StringBuilder();

        String saltedPassword = salt + password;

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = sha.digest(saltedPassword.getBytes());

            for (int idx = 0; idx < hashedBytes.length; ++idx) {
                byte b = hashedBytes[idx];
                hash.append(Integer.toHexString((b & 0xf0) >> 4)); //Uses bitwise operations in order to calculate the byte to hex. Uses a max size of 15. This is so that it is always of a fixed size.
                hash.append(Integer.toHexString(b & 0x0f)); //Does the same and uses a max size of 15 again. Though, this hex is different from the other to ensure a longer length
            }
        } catch (NoSuchAlgorithmException e) {
            Platform.runLater(()->{JFXAlert.openUtilityError("No such hashing algorithm was found");});
        }
        return hash.toString();
    }

    /**
     * Generates a random salt of size 6
     * @return a randomly generated salt
     */
    public static String generateSalt() {

        StringBuilder hash = new StringBuilder();
        final Random r = new SecureRandom();
        byte[] salt = new byte[6];
        r.nextBytes(salt);

        for (byte b : salt) {
            hash.append(Integer.toHexString((b & 0xf0) >> 4)); //Uses bitwise operations in order to calculate the byte to hex. Uses a max size of 15. This is so that it is always of a fixed size. 
        }
        return hash.toString();
    }

    
    
}