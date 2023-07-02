package scan;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProtegendoIntegridade {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public void calculateHashesInDirectory(String directoryPath, String outputFilePath)
            throws NoSuchAlgorithmException, IOException {
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            System.out.println("O caminho especificado não é um diretório válido.");
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            System.out.println("Não foi possível listar os arquivos no diretório.");
            return;
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                String filePath = file.getAbsolutePath();
                String hash = doHash(filePath);

                writer.write(filePath + ": " + hash);
                writer.newLine();
            }
        }
        writer.close();

        System.out.println("Valores de hash foram calculados e salvos em: " + outputFilePath);
    }

    public String doHash(String filePath) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        md.update(content.getBytes());
        byte[] digest = md.digest();
        return bytesToHex(digest).toLowerCase();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        ProtegendoIntegridade integridade = new ProtegendoIntegridade();
//		Mudar o caminho dos diretórios conforme necessário	
//      integridade.calculateHashesInDirectory("caminho/do/diretorio", "caminho/do/arquivoDeSaida.txt");
        integridade.calculateHashesInDirectory("C:\\Users\\Cliente\\eclipse-workspace\\HashScanner\\src\\Hash test", "C:\\Users\\Cliente\\eclipse-workspace\\HashScanner\\HASHES.txt");
    }
}

