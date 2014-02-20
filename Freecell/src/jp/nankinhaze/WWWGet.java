package jp.nankinhaze;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WWWGet {

    public static void WWWGet() {
        String urlString = "http://localhost:8080/jsp-examples/post.jsp";
        try {
            URL url = new URL(urlString);
            URLConnection uc = url.openConnection();
            uc.setDoOutput(true);//POST�\�ɂ���

            uc.setRequestProperty("User-Agent", "@IT java-tips URLConnection");// �w�b�_��ݒ�
            uc.setRequestProperty("Accept-Language", "ja");// �w�b�_��ݒ�
            OutputStream os = uc.getOutputStream();//POST�p��OutputStream���擾
        
            String postStr = "foo1=bar1&foo2=bar2";//POST����f�[�^
            PrintStream ps = new PrintStream(os);
            ps.print(postStr);//�f�[�^��POST����
            ps.close();

            InputStream is = uc.getInputStream();//POST�������ʂ��擾
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String s;
            while ((s = reader.readLine()) != null) {
                System.out.println(s);
            }
            reader.close();
        } catch (MalformedURLException e) {
            System.err.println("Invalid URL format: " + urlString);
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("Can't connect to " + urlString);
            System.exit(-1);
        }
    }
}