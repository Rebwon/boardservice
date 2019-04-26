package ko.maeng.boardservice.web;

import javax.servlet.http.HttpSession;

public class HttpSessionUtils {
    // Util 클래스를 작성함으로써, 세션 관련 중복 코드 제거.
    public static final String USER_SESSION_KEY = "sessionedUser";

    public static boolean isLogin(HttpSession session){
        Object sessionedUser = session.getAttribute(USER_SESSION_KEY);
        if(sessionedUser == null){
            return false;
        }
        return true;
    }

    public static Object getUserFromSession(HttpSession session){
        if(!isLogin(session)){
            return null;
        }

        return session.getAttribute(USER_SESSION_KEY);
    }
}
