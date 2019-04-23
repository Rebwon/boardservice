package ko.maeng.boardservice.config;

import com.samskivert.mustache.Mustache;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Converter {

    //템플릿 로더 구현.
    public static String toString(InputStream inputStream) {
        Mustache.Compiler compiler = Mustache.compiler();

        String str = "";
        InputStream is = null;
        BufferedInputStream bis = null;
        int size = 10240;
        try{
            is = inputStream;
            bis = new BufferedInputStream(is);
            byte[] buffer = new byte[size];
            while((bis.read(buffer) != -1)){
                str += new String(buffer, "UTF-8");
            }

            Map<String, Object> map = new HashMap<>();
            map.put("url", str);
            compiler.compile("/templates/").execute(map);
        } catch (IllegalStateException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
