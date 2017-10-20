import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class Ex3Client {

    public static void main(String[] args) {
        try (Socket socket = new Socket("18.221.102.182", 38103)) {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            System.out.println("Connected to server.");
            int numBytes = is.read();
            System.out.println("Reading " + numBytes + " bytes.");
            byte[] data = new byte[numBytes];
            printData(data, is);
            short result = checkSum(data);
            printResult(is, os, result);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static short checkSum(byte[] b) {
        int sum = 0, count = 0;
        byte firstHalf, secondHalf;

        while(count < b.length - 1) {
            firstHalf = b[count];
            secondHalf = b[count+1];
            sum += ((firstHalf << 8 & 0xFF00) | (secondHalf & 0xFF));
            if((sum & 0xFFFF0000) > 0) {
                sum &= 0xFFFF;
                sum++;
            }
            count += 2;
        }

        if((b.length) % 2 == 1) {
            byte overflow = b[b.length - 1];
            sum += ((overflow << 8) & 0xFF00);
            if((sum & 0xFFFF0000) > 0) {
                sum &= 0xFFFF;
                sum++;
            }
        }
        return (short) ~(sum & 0xFFFF);
    }

    private static void printData(byte[] data, InputStream is) throws IOException {
        byte val;
        System.out.println("Data received: ");
        for(int i = 0; i < data.length; i++) {
            val = (byte) is.read();
            data[i] = val;
            if(i !=0 && i % 10 == 0) {
                System.out.println();
            }
            System.out.printf("%02X", data[i]);
        }
    }

    private static void printResult(InputStream is, OutputStream os, short result) throws IOException {
        System.out.print("\nChecksum calculated: ");
        System.out.printf("0x%02X", result);
        os.write(result >> 8);
        os.write(result >> 0);
        int response = is.read();
        if(response == 1) {
            System.out.println("\nResponse good.");
        } else {
            System.out.println("\nIncorrect response.");
        }
    }
}