package util.cmcc.johndon.com.show;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by DELL on 2016/12/8.
 */

public class CMCCProgressDialog extends ProgressDialog {
    private static CMCCProgressDialog myProgressDialog;
    private Context mContext;
    private TextView mTvMessage;
    private ImageView mIv;
    public CMCCProgressDialog(Context context) {
        super(context, R.style.NewDialog);
        this.mContext=context;
    }

    public CMCCProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         setContentView(R.layout.progress_layout);
        myProgressDialog = new CMCCProgressDialog(mContext);
        mTvMessage= (TextView) findViewById(R.id.tv_message);
        mIv= (ImageView) findViewById(R.id.iv_progress);
        AnimationDrawable animationDrawable= (AnimationDrawable) mIv.getBackground();
        animationDrawable.start();
    }

    @Override
    public void setMessage(CharSequence message) {
        if (mTvMessage!=null){
            mTvMessage.setText(message);
        }
    }

    public static CMCCProgressDialog show(Context context) {
        //myProgressDialog = new MyProgressDialog(context);
        myProgressDialog.show();
        return myProgressDialog;
    }

    public static void dismissDialog() {
        myProgressDialog.dismiss();
    }
}
