package com.cmcc.paycmcchealth.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmcc.paycmcchealth.PayResult;
import com.cmcc.paycmcchealth.R;
import com.cmcc.paycmcchealth.bean.PayInfoBean;
import com.cmcc.paycmcchealth.utils.PayUtils;

public class MainActivity extends Activity {
	
	private EditText name, content, price;
	private Button pay, h5pay;
	private String nameString = "", contentString = "", priceString = "";
	
	private static final int SDK_PAY_FLAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        
        pay.setOnClickListener(
        		new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						nameString = name.getText().toString();
						contentString = content.getText().toString();
						priceString = price.getText().toString();
						
						if(nameString.equals("")){
							nameString = "测试的商品";
						}
						
						if(contentString.equals("")){
							contentString = "商品详情";
						}
						
						if(priceString.equals("")){
							priceString = "0.01";
						}
						
						PayInfoBean payInfo = new PayInfoBean();
						payInfo.setTradeNo(getOutTradeNo());
						payInfo.setSubject(nameString);
						payInfo.setBody(contentString);
						payInfo.setPrice(priceString);
						
						PayUtils payUtils = new PayUtils(MainActivity.this, payInfo, mHandler);
						payUtils.pay();
					}
				});
    }
    
    private void initViews(){
    	name = (EditText)findViewById(R.id.product_subject);
    	content = (EditText)findViewById(R.id.product_content);
    	price = (EditText)findViewById(R.id.product_price);
    	pay = (Button)findViewById(R.id.pay);
    	h5pay = (Button)findViewById(R.id.h5pay);
    }
    
    @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息

				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(MainActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			default:
				break;
			}
		};
	};
	
	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}
	
	//判断是否安装支付宝
	public boolean checkApkExist(String packageName) {
		if (packageName == null || "".equals(packageName))
		return false;
		try {
		ApplicationInfo info = MainActivity.this.getPackageManager()
		.getApplicationInfo(packageName,
		PackageManager.GET_UNINSTALLED_PACKAGES);
		return true;
		} catch (NameNotFoundException e) {
		return false;
		}
		}
    
}
