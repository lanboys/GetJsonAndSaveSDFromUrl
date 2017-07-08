package lan.bing.jsonurlfiddlertestproject;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import lan.bing.utils.FileUtils;
import lan.bing.utils.IOUtils;
import lan.bing.utils.LogUtil;
import lan.bing.utils.RequestUtil;


// 抓取url返回的json 并保存到sd卡 demo
public class MainActivity extends AppCompatActivity {

    LogUtil log = LogUtil.getLogUtil(getClass(), 1);
    boolean flag = false;
    String result = null;
    private EditText mEd2;
    private EditText mEd1;
    private EditText mEd3;
    private String mUrl;
    private String mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEd1 = (EditText) findViewById(R.id.editText1);
        mEd2 = (EditText) findViewById(R.id.editText2);
        mEd3 = (EditText) findViewById(R.id.editText3);

        //设置EditText的显示方式为多行文本输入
        mEd1.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mEd2.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        //文本显示的位置在EditText的最上方
        mEd1.setGravity(Gravity.TOP);
        mEd2.setGravity(Gravity.TOP);

        //改变默认的单行模式
        mEd1.setSingleLine(false);
        mEd2.setSingleLine(false);

        //水平滚动设置为False
        mEd1.setHorizontallyScrolling(false);
        mEd2.setHorizontallyScrolling(false);
    }

    public void request(View view) {

        mUrl = mEd1.getText().toString().trim();

        if (mUrl != null) {

            RequestUtil.loadStringByRequest(this, mUrl, new RequestUtil.CallBackListner<String>() {
                @Override
                public void OnFinish(String response) {
                    result = response;
                    flag = true;
                    mEd2.setText(response);
                    log.d(response);
                }

                @Override
                public void OnError(Exception e) {
                    flag = false;
                    result = null;

                    mEd2.setText(e.getLocalizedMessage());
                    log.e("OnError: 请求失败 " + e.getLocalizedMessage());
                    Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "url为空", Toast.LENGTH_SHORT).show();
        }
    }

    public void save(View view) {

        if (flag && !TextUtils.isEmpty(result)) {
            save2SD();
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "没有合适的数据进行保存", Toast.LENGTH_SHORT).show();
        }
    }

    public void clear(View view) {

        if (mEd1 != null && mEd2 != null&& mEd3 != null) {
            mEd1.setText("");
            mEd2.setText("");
            mEd3.setText("");
        }
    }

    public void save2SD() {

        BufferedWriter bufferedWriter = null;
        BufferedWriter bufferedWriter1 = null;

        mText = mEd3.getText().toString().trim();

        try {
            //保存SD卡
            String path = FileUtils.getDir("json");
            // 1 得到key

            // String key = FileUtils.url2FileName(mUrl);

            long time = SystemClock.currentThreadTimeMillis();
            File cacherSd = new File(path, time+ mText+".html");

            bufferedWriter = new BufferedWriter(new FileWriter(cacherSd));
            bufferedWriter.write(result);

            File cacherSd1 = new File(path, time + mText+".txt");
            bufferedWriter1 = new BufferedWriter(new FileWriter(cacherSd1));
            bufferedWriter1.write(mUrl);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(bufferedWriter);
            IOUtils.close(bufferedWriter1);
        }
    }
}
