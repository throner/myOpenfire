package test;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import test.face.IChatListener;

/**
 * Created by chenjiansheng on 2016-5-26.
 */
public class ApiManager {
    public void init(String server, String userName, String pwd, String resource, IChatListener chatListener){
        boolean bResult = false;
        OpenfireManager om = new OpenfireManager();
        try {
            om.init(server,userName,pwd,resource);
            chatListener.onComplete("0","");
        } catch (Exception e) {
            e.printStackTrace();
            bResult = false;
            chatListener.onComplete("1","出错");
        }
    }

    public static void main(String[] args) throws Exception{
        ApiManager am = new ApiManager();
        am.init("127.0.0.1","test1","1","test",new IChatListener(){//直接NEW 接口然后实现是可以的！！！！
            public void onComplete(String result, String error) {
                System.out.print("111result:"+result+",111error:"+result);
            }
        });
        Thread.sleep(1000*60*5);
    }
}
