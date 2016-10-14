package com.vincent.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 项目名称：SocketClient
 * 类名：com.vincent.socket
 * 类描述：
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/14 10:15
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */

public class TcpSocketClient {
    // 服务端地址
    private String serverIp = "192.168.0.166";
    // 服务端端口号
    private int serverPort = 8888;
    // 套接字
    private Socket mSocket = null;
    // 缓冲区读取
    private BufferedReader in = null;
    // 字符打印流
    private PrintWriter out = null;
    // tcp套接字监听
    private TcpSocketListener mTcpSocketListener;
    // 内容
    private String content = "";

    /**
     * 构造函数
     * @author leibing
     * @createTime 2016/10/06
     * @lastModify 2016/10/06
     * @param mTcpSocketListener tcp套接字监听
     * @return
     */
    public TcpSocketClient(TcpSocketListener mTcpSocketListener){
        this.mTcpSocketListener = mTcpSocketListener;
    }

    /**
     * 构造函数
     * @author leibing
     * @createTime 2016/10/06
     * @lastModify 2016/10/06
     * @param serverIp = 服务端地址
     * @param serverPort 服务端口号
     * @param mTcpSocketListener tcp套接字监听
     * @return
     */
    public TcpSocketClient(String serverIp, int serverPort , TcpSocketListener mTcpSocketListener){
        this.serverIp  = serverIp;
        this.serverPort = serverPort;
        this.mTcpSocketListener = mTcpSocketListener;
    }

    /**
     * 启动tcp套接字连接
     * @author leibing
     * @createTime 2016/10/06
     * @lastModify 2016/10/06
     * @param
     * @return
     */
    public void startTcpSocketConnect(){
        // 开启一个线程启动tcp socket
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(serverIp, serverPort);
                    in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            mSocket.getOutputStream())), true);
                    while (true) {
                        if (mSocket.isConnected()) {
                            if (!mSocket.isInputShutdown()) {
                                if ((content = in.readLine()) != null) {
                                    content += "\n";
                                    if (mTcpSocketListener != null)
                                        mTcpSocketListener.callBackContent(content);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 通过tcp套接字发送消息
     * @author leibing
     * @createTime 2016/10/06
     * @lastModify 2016/10/06
     * @param
     * @return
     */
    public void sendMessageByTcpSocket(String msg){
        if (mSocket != null && mSocket.isConnected()){
            if (!mSocket.isOutputShutdown() && out != null){
                out.println(msg);
                if (mTcpSocketListener != null)
                    mTcpSocketListener.clearInputContent();
            }
        }
    }

    /**
     * @interfaceName:
     * @interfaceDescription: tcp套接字监听
     * @author: leibing
     * @createTime: 2016/10/06
     */
    public interface TcpSocketListener{
        // 回调内容
        void callBackContent(String content);
        // 清除输入框内容
        void clearInputContent();
    }
}
