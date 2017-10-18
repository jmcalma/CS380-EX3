import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;

public class Ex3Client {

    public static void main(String[] args) {
        try (Socket socket = new Socket("18.221.102.182", 38103)) {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

        } catch (Exception e) {
            System.out.println(e.getMessage());		
        }
    }

    u_short cksum(u_short *buf, int count) {
        register u_long sum = 0;
        while (count--) {
            sum += *buf++;
            if (sum & 0xFFFF0000) {
                /* carry occurred. so wrap around */
                sum &= 0xFFFF;
                sum++;
            }
        }
        return ~(sum & 0xFFFF);
    }
}