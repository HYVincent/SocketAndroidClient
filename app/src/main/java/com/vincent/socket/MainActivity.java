package com.vincent.socket;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    // 服务端地址
    private final static String serverIp = "192.168.0.166";
    // 服务端口号
    private final static int serverPort = 8888;
    // 控件
    private TextView showTv;
    private EditText contentEdt;
    private Button sendBtn;
    // tcp套接字客户端
    private TcpSocketClient mTcpSocketClient;
    // 自定义Handler,用于更新Ui
    private Handler mHandler = new Handler();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new GoogleApiClient.Builder(MainActivity.this).addApi(AppIndex.API).build();
        // findView
        showTv = (TextView) findViewById(R.id.tv_show);
        contentEdt = (EditText) findViewById(R.id.edt_content);
        // 初始化tcp套接字客户端
        mTcpSocketClient = new TcpSocketClient(serverIp, serverPort,
                new TcpSocketClient.TcpSocketListener() {
                    @Override
                    public void callBackContent(final String content) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (showTv != null)
                                    showTv.setText(showTv.getText().toString() + content);
                            }
                        });
                    }

                    @Override
                    public void clearInputContent() {
                        if (contentEdt != null)
                            contentEdt.setText("");
                    }
                });

        // onClick
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = contentEdt.getText().toString().trim();
                mTcpSocketClient.sendMessageByTcpSocket(msg);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        findViewById(R.id.btn_connect_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动tcp套接字连接
                mTcpSocketClient.startTcpSocketConnect();
            }
        });
    }

    @Override
    protected void onDestroy() {
        // 断开tcp链接
        if (mTcpSocketClient != null)
            mTcpSocketClient.sendMessageByTcpSocket("exit");
        super.onDestroy();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
