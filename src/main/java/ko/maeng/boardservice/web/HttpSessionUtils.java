package ko.maeng.boardservice.web;

import javax.servlet.http.HttpSession;

public class HttpSessionUtils {
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
