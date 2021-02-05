import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.file.*;
import java.util.*;

class ex02_fileCrypto {
    static public void Encrypt(String inFileName, String outFileName, String password) {
        try {
            byte[] file = Files.readAllBytes(Paths.get(inFileName));
            byte[] checkedFile = Arrays.copyOf(checker.getBytes(), checker.length() + file.length);

            System.arraycopy(file, 0, checkedFile, checker.length(), file.length);
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(password.getBytes());
            SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");
            Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aes.init(Cipher.ENCRYPT_MODE, key);
            Files.write(Paths.get(outFileName), aes.doFinal(checkedFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public boolean Decrypt(String inFileName, String outFileName, String password) {
        try {
            byte[] checkedFile = Files.readAllBytes(Paths.get(inFileName));

            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(password.getBytes());
            SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");
            Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aes.init(Cipher.DECRYPT_MODE, key);
            String cleartext = new String(aes.doFinal(checkedFile));

            if (!cleartext.substring(0, checker.length()).equals(checker))
                throw new Exception();

            Files.write(Paths.get(outFileName), cleartext.substring(checker.length()).getBytes());

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    static public void main(String[] args) {
        String inFileName = "ex02_mobydick.enc";
        String outFileName = "ex02_mobydick";
        List<Character> candidateChars = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            candidateChars.add(c);
        }
        for (int i = 0; i <= 9; i++) {
            candidateChars.add(Character.forDigit(i, 10));
        }
        List<String> candidatePasswords = new ArrayList<>();
        // omp parallel
        for (char c1 : candidateChars) {
            for (char c2 : candidateChars) {
                for (char c3 : candidateChars) {
                    for (char c4 : candidateChars) {
                        candidatePasswords.add("" + c1 + c2 + c3 + c4);
                    }
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            final int start = candidatePasswords.size() / 4 * i;
            final int end = Math.min(candidatePasswords.size() / 4 * (i + 1), candidatePasswords.size());
            new Thread(() -> {
                for (int j = start; j < end; j++) {
                    String password = candidatePasswords.get(j);
                    boolean valid = Decrypt(inFileName, outFileName, password);
                    if (valid) {
                        System.out.println(password);
                    }
                }
            }).start();
        }
    }

    private static final String checker = "correct header";
}
