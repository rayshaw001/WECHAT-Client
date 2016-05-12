package ray.hubu.edu.tools;

/**
 * Created by Ray on 2016/3/16.
 */
//处理消息的工具
public class MessageTool {
    // str format "fromID:msg:msgID"
    public static String getID(String str)
    {
        return str.substring(0,str.indexOf(":"));
    }
    public static String getMsg(String str)
    {
        return str.substring(str.indexOf(":"),str.lastIndexOf(":"));
    }
    public static String getMsgId(String str)
    {
        return str.substring(str.lastIndexOf(":"));
    }

}
