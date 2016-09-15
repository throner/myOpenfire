package test;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import test.pojo.MessageExtension;

/**
 * Created by chenjiansheng on 2016-5-15.
 */
public class OpenfireManager {
    private Connection connection;
    /**
     * 初始化
     * @param server    服务器IP
     * @param userName  用户名
     * @param pwd 密码
     * @param resource 资源名
     * @return opnefire连接
     * @throws Exception
     */
    public boolean init(String server,String userName,String pwd,String resource) throws Exception{
        boolean bResult = false;
        try {
            //connection = new XMPPConnection(server);
            //connection.connect();
            /** 5222是openfire服务器默认的通信端口，你可以登录http://192.168.8.32:9090/到管理员控制台查看客户端到服务器端口 */
            ConnectionConfiguration config = new ConnectionConfiguration(server, 5222);
            /** 是否启用压缩 */
            config.setCompressionEnabled(true);
            /** 是否启用安全验证 */
            config.setSASLAuthenticationEnabled(true);
            /** 是否启用调试 */
            config.setDebuggerEnabled(false);
            config.setDebuggerEnabled(true);
            //config.setReconnectionAllowed(true);
            //config.setRosterLoadedAtLogin(true);

            /** 创建connection链接 */
            connection = new XMPPConnection(config);
            /** 建立连接 */
            connection.connect();
            connection.login(userName,pwd,resource);
            bResult = true;
        } catch (XMPPException e) {
            e.printStackTrace();
            bResult = false;
            throw e;
        }
        return bResult;
    }

    /**
     * 发送包
     * @param jid 对方jid
     * @param messageListener 消息接收监听器
     */
    public Chat getChat(String jid,MessageListener messageListener){
        ChatManager chatmanager = connection.getChatManager();
        Chat newChat = chatmanager.createChat(jid,messageListener);
        return newChat;
    }

    /**
     * 发送消息
     * @param chat 聊天对象
     * @param msgContent 聊天内容
     * @throws Exception
     */
    public void sendMessage(Chat chat,String msgContent) throws Exception{
        try {
            Message message = new Message();
            message.setBody(msgContent);
            MessageExtension me = new MessageExtension();
            message.addExtension(me);
            message.setBody("hello world");
            chat.sendMessage(message);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 发送消息
     * @param chat 聊天对象
     * @param msgContent 聊天内容
     * @throws Exception
     */
    public void sendMessageByCon(String msgContent) throws Exception{
        try {
            Message message = new Message();
            message.setBody(msgContent);
            MessageExtension me = new MessageExtension();
            message.addExtension(me);
            message.setSubject("");
            message.setFrom("test1@cjs/test");
            message.setTo("test@cjs/Spark 2.6.3");
            connection.sendPacket(message);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



    public void sendIQ(String jid) throws Exception{
        try{
            MyIQ myIQ = new MyIQ();//也可以使用继承,IQ的类
            myIQ.setTo(jid);
            myIQ.setFrom(connection.getUser());
            myIQ.setType(IQ.Type.SET);
            connection.sendPacket(myIQ);
        }catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public static class MyIQ extends IQ{
        public static final String ELEMENT = "MyIQ";
        public static final String NAMESPACE = "MyIQ.com";
        public static final String TAGNAME = "DATA";
        private String data="";
        public String getData(){
            return this.data;
        }
        public void setData(String data){
            this.data=data;
        }
        @Override
        public String getChildElementXML(){
            StringBuffer sb = new StringBuffer();
            sb.append("<").append(ELEMENT).append(" xmlns=\"").append(NAMESPACE).append("\">");
            sb.append("<DATA>").append(data).append("</DATA>");
            sb.append("</").append(ELEMENT).append(">");
            return sb.toString();
        }
    }

    public void sendPresence(String jid) throws Exception{
        Presence presence = new Presence(Presence.Type.subscribe);
        presence.setTo(jid);
        connection.sendPacket(presence);
    }

    public void setPacketFilterAndListener(PacketListener listener, PacketFilter filter){
        connection.addPacketListener(listener,filter);
    }

    public static void main(String[] args) throws Exception{
        OpenfireManager openfireManager = new OpenfireManager();
        try{
            openfireManager.init("127.0.0.1","test1","1","test");
            MessageListener messageListener = new MessageListener() {
                public void processMessage(Chat chat, Message message) {
                    System.out.println("Received message: " + message.getBody());
                }
            };
            String sToJid = "test@cjs/Spark 2.6.3";
            Chat chat = openfireManager.getChat(sToJid,messageListener);
            //openfireManager.sendMessage(chat,"hello world");
            openfireManager.sendMessageByCon("hello world1111");
            openfireManager.sendIQ(sToJid);
            openfireManager.sendPresence(sToJid);


            PacketFilter filter = new AndFilter(new PacketTypeFilter(Message.class),
                    new FromContainsFilter(sToJid));

            PacketListener myListener = new PacketListener() {
                public void processPacket(Packet packet) {
                    System.out.print("listen:"+packet.toXML());
                }
            };
            openfireManager.setPacketFilterAndListener(myListener,filter);
            Thread.sleep(1000*60*5);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
