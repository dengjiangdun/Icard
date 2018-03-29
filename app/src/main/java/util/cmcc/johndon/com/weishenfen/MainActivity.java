package util.cmcc.johndon.com.weishenfen;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bright.cmcc.umclib.JumpUmcActivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import util.cmcc.johndon.com.show.CMCCProgressDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private CMCCProgressDialog mPd;
    private Button mBtnClear;
    private Button mBtnSearch;
    private TextView mTvAddress;
    private TextView mTvBirthday;
    private TextView mTvMale;
    private EditText mEtIdNumber;

    private static final String APP_ID = "300011857556";
    /*shenfenheyan"300011857555";*/
    /*Super ID  "300011857554";*/
    /* icarde"300011857553";*/
    /* icard"300011648391";*/
    private static final String APP_KEY = "2A0AC39DD72F8964E12E54E5E7D734AC";
    /*shenfenheyan"BF56EAF237E14562E365C0B79B20741B";*/
    /*Super ID"64295780B53543265B62BFF823ED5476";*/
    /*icarde"531826EA12DAB5DF220E87E8BE2DA4EF";*/
    /* icard"4448DD0E7652E3132DF6011508AE5391"*/
    private static final String GET_ID_URL = "http://api.avatardata.cn/IdCard/LookUp?" +
            "key=e0146e6d4ac847e8a11063145a4ca26a&id=%s";
    private static final String ERROR_MESSAGE = "出错（错误码：%d,原因：%s）";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPd = new CMCCProgressDialog(this);
        mBtnClear = (Button) findViewById(R.id.btn_clear_number);
        mBtnSearch = (Button) findViewById(R.id.btn_search_number);
        mTvAddress = (TextView) findViewById(R.id.tv_id_address);
        mTvBirthday = (TextView) findViewById(R.id.tv_id_birthday);
        mTvMale = (TextView) findViewById(R.id.tv_id_male);
        mEtIdNumber = (EditText) findViewById(R.id.et_id_number);
        findViewById(R.id.iv_bar_back).setVisibility(View.INVISIBLE);
        ((TextView)findViewById(R.id.tv_bar_title)).setText(getResources().getText(R.string.app_name));

        mBtnSearch.setOnClickListener(this);
        mBtnClear.setOnClickListener(this);

        mEtIdNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    String idNumber = mEtIdNumber.getText().toString();
                    if (! idNumber.isEmpty()){

                        search(idNumber);
                    } else {
                        showShortToast(getResources().getString(R.string.hint_input_id_number));
                    }
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.iv_top_bar_right_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUmcActivity.StartActivity(MainActivity.this,APP_ID,APP_KEY);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPd.isShowing()){
            mPd.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_clear_number:{
                mEtIdNumber.getText().clear();
                break;
            }

            case R.id.btn_search_number:{
                String idNumber = mEtIdNumber.getText().toString().trim();
                if (idNumber.isEmpty()) {
                    showShortToast(getResources().getString(R.string.please_input_id_number));
                } else {
                    search(idNumber);
                }
                break;
            }
        }

    }

    private void search(String idNumber){
        FinalHttp finalHttp = new FinalHttp();
        mPd.show();
        finalHttp.get(String.format(GET_ID_URL, idNumber), new AjaxCallBack<String>() {
            @Override
            public void onSuccess(final String s) {
                super.onSuccess(s);
                Context context = getBaseContext();
                if (context != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dealResult(s);
                            if (mPd.isShowing()) {
                                mPd.dismiss();
                            }
                            }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t, final int errorNo, final String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Context context = getBaseContext();
                if (context != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mPd.isShowing()){
                                mPd.dismiss();
                            }
                            showShortToast(String.format(ERROR_MESSAGE,errorNo,strMsg));
                        }
                    });
                }
            }
        });
    }

    private void showShortToast(String message){
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    private void dealResult(String resultStr) {
        JSONObject jsonObject = JSON.parseObject(resultStr);
        String code = jsonObject.getString("error_code");
        String reason = jsonObject.getString("reason");
        Log.d("TAG", "dealResult: "+resultStr);
        if (code.equals("0")){
            Result result = (Result) JSON.parseObject(jsonObject.getString("result"),Result.class);
            mTvAddress.setText((result.getAddress().isEmpty() ? "--" : result.getAddress()));
            mTvMale.setText((result.getSex().isEmpty() ? "--" :( result.getSex().equals("M") ? "男" : "女")));
            mTvBirthday.setText((result.getBirthday().isEmpty() ? "--" : result.getBirthday()));
        } else {
            showShortToast(reason);
        }
    }

}
