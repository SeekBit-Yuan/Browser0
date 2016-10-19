package com.seek.browser;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private ImageView button_go;
    private ProgressBar progressBar;
    private WebView webView;
    private ImageView button_back;
    private ImageView button_forward;
    private ImageView button_home;
    private ImageView button_share;
    private ImageView button_menu;
    private List<String> titles;
    private List<List<String>> item_names; // 选项名称
    private List<List<Integer>> item_images; // 选项图标
    private MyPopupMenu myPopupMenu;
//    private PopupWindow mPopupWindow;

    private static boolean isExit = false;
    private static Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }

    };

    private InputMethodManager mInputMethodManager;//输入法管理器

    //    private final String HOME_PAGE="http://www.sznews.com/";
//    private final String HOME_PAGE="http://www.baidu.com/";
    private final String HOME_PAGE="http://i.sznews.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShareSDK.initSDK(getApplicationContext());

        editText = (EditText)findViewById(R.id.editText);
        button_go = (ImageView) findViewById(R.id.image_go);
        progressBar =(ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.webView);
        button_back = (ImageView) findViewById(R.id.image_back);
        button_forward = (ImageView) findViewById(R.id.image_forward);
        button_home = (ImageView) findViewById(R.id.image_home);
        button_share = (ImageView) findViewById(R.id.image_share);
        button_menu = (ImageView) findViewById(R.id.image_menu);

        button_go.setOnClickListener(this);
        button_back.setOnClickListener(this);
        button_forward.setOnClickListener(this);
        button_home.setOnClickListener(this);
        button_share.setOnClickListener(this);
        button_menu.setOnClickListener(this);

        /**
         * 菜单栏分类标题
         */
        titles = new ArrayList<String>();
        titles = addItems(new String[]{"常用", "设置", "工具"});

        /**
         * 选项图标
         */
        item_images = new ArrayList<List<Integer>>();
        item_images.add(addItems(new Integer[] { R.mipmap.ic_bookmark1,
                R.mipmap.ic_bookmark1, R.mipmap.ic_bookmark1,
                R.mipmap.ic_bookmark1, R.mipmap.ic_bookmark1,
                R.mipmap.ic_bookmark1, R.mipmap.ic_bookmark1,
                R.mipmap.ic_bookmark1 }));
        item_images.add(addItems(new Integer[] { R.mipmap.ic_bookmark1,
                R.mipmap.ic_bookmark1, R.mipmap.ic_bookmark1,
                R.mipmap.ic_bookmark1, R.mipmap.ic_bookmark1,
                R.mipmap.ic_bookmark1, R.mipmap.ic_bookmark1,
                R.mipmap.ic_bookmark1 }));
        item_images.add(addItems(new Integer[] { R.mipmap.ic_bookmark1,
                R.mipmap.ic_bookmark1, R.mipmap.ic_bookmark1,
                R.mipmap.ic_bookmark1, R.mipmap.ic_bookmark1,
                R.mipmap.ic_bookmark1, R.mipmap.ic_bookmark1,
                R.mipmap.ic_bookmark1 }));
        /**
         * 选项名称
         */
        item_names = new ArrayList<List<String>>();
        item_names.add(addItems(new String[] { "电话", "相机", "复制", "裁剪", "剪切",
                "删除", "下载", "编辑" }));
        item_names.add(addItems(new String[] { "邮件", "全屏", "帮助", "收藏", "地图",
                "语音", "图片", "定位" }));
        item_names.add(addItems(new String[] { "刷新", "保存", "搜索", "分享", "切换",
                "录像", "浏览器", "旋转屏幕" }));

        myPopupMenu = new MyPopupMenu(this, titles, item_names, item_images);
        /**
         * 设置菜单栏推拉动画效果
         * res/anim中的xml文件与styles.xml中的style配合使用
         */
        myPopupMenu.setAnimationStyle(R.style.PopupAnimation);

        //弹出窗口
//        View popupView = getLayoutInflater().inflate(R.layout.menu,null);
//
//        mPopupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MEMORY_TYPE_CHANGED,true);
//        mPopupWindow.setTouchable(true);
//        mPopupWindow.setOutsideTouchable(true);
//        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),(Bitmap) null));
//        mPopupWindow.getContentView().setFocusableInTouchMode(true);
//        mPopupWindow.getContentView().setFocusable(true);
//        mPopupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
//        mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
//                        && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
//                        mPopupWindow.dismiss();
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });

        //webView属性
        webView.getSettings().setJavaScriptEnabled(true);//是否支持JS
        webView.setHorizontalScrollBarEnabled(false);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(),"local_obj");
        webView.getSettings().setSupportZoom(true);//是否支持缩放
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);//不加载网络上的图片资源
        webView.setInitialScale(70);
        webView.setHorizontalScrollBarEnabled(true);
        webView.getSettings().setUseWideViewPort(true);//webview网页自适应手机屏幕大小,设置webview推荐使用的窗口
        webView.getSettings().setLoadWithOverviewMode(true);//设置webview加载的页面的模式
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        //WebChromeClient是辅助处理JavaScript的对话框、网站图标。网站title，加载进度等
        webView.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView view,int progress)
            {
                super.onProgressChanged(view, progress);
                System.out.println("onProgressChanged " + progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(progress);
                }
            }
        });

        //WebViewClient辅助出处理webview的各种通知，请求事件
        webView.setWebViewClient(new WebViewClient()
                                 {
                                     @Override
                                     public void onReceivedError(WebView view,int errorCode,String description,String failingUrl)
                                     {
                                         Log.i("MainActivity",description);
                                         if(errorCode==WebViewClient.ERROR_HOST_LOOKUP&&failingUrl!=null){
                                             //找不到页面，调用百度搜搜
                                             failingUrl = "http://www.baidu.com/baidu?word=" + failingUrl;
                                             Log.d(null,"errorRedirect:"+failingUrl);
                                             webView.loadUrl(failingUrl);
                                         }
                                     }

                                     @Override
                                     public void onPageFinished(WebView view,String url)
                                     {
                                         super.onPageFinished(view, url);

                                         setTitle(webView.getTitle());//设置标题栏为当前网页标题
                                         editText.setText("");//清空文本框

                                         if(webView.canGoBack()){
                                             button_back.setImageResource(R.mipmap.ic_canback);
                                         }else {
                                             button_back.setImageResource(R.mipmap.ic_back);
                                         }
                                         webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('img')[0].src);");
                                     }



                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view,String url)
                                     {
                                         view.loadUrl(url);
                                         return true;
                                     }
                                 }
        );
        //查看模块器能否正常上网
        webView.loadUrl(HOME_PAGE);

        //EditText隐藏输入法
        initUtil();
        initView();
    }

    /**
     * 转换为List<String>
     * 用于菜单栏中的菜单项图标赋值
     * @param values
     * @return
     */
    private List<String> addItems(String[] values) {

        List<String> list = new ArrayList<String>();
        for (String var : values) {
            list.add(var);
        }

        return list;
    }

    /**
     * 转换为List<Integer>
     * 用于菜单栏中的标题赋值
     * @param values
     * @return
     */
    private List<Integer> addItems(Integer[] values) {

        List<Integer> list = new ArrayList<Integer>();
        for (Integer var : values) {
            list.add(var);
        }

        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * 系统菜单必须要加一个，才有效果
         */
        menu.add("menu");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        if (myPopupMenu.isShowing()) {
//            myPopupMenu.dismiss();
            myPopupMenu.isShowing();
        } else {
            /**
             * 这句代码可以使菜单栏如对话框一样弹出的效果
             * myPopupMenu.setAnimationStyle(android.R.style.Animation_Dialog);
             */
            /**
             * 设置菜单栏显示位置
             */
            myPopupMenu.showAtLocation(findViewById(R.id.webView),
                    Gravity.BOTTOM, 0, 0);
            myPopupMenu.setAnimationStyle(android.R.style.Animation_Dialog);
            myPopupMenu.isShowing();
        }
        return false;
    }
    /**
     * 初始化必须工具
     */
    private void initUtil() {
        //初始化输入法
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * EditText输入法隐藏控件初始化
     */
    private void initView() {
        editText.setOnClickListener(this);
        button_go.setOnClickListener(this);
    }

    @Override
    public  boolean onKeyDown(int keyCode,KeyEvent event)
    {
        if ((keyCode==KeyEvent.KEYCODE_BACK)&&webView.canGoBack())
        {
            webView.goBack();
            return true;
        }else if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            ConfirmExit();//按了返回键，但已经不能返回，则执行退出确认
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0) {
            if (myPopupMenu != null && !myPopupMenu.isShowing()) {
                myPopupMenu.showAtLocation(findViewById(R.id.tool_bar1), Gravity.NO_GRAVITY, 0, 0);
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }


    public void ConfirmExit()//退出确认
    {
        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setTitle("退出");
        ad.setMessage("是否退出软件？");
        ad.setPositiveButton("是",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int i) {
                finish();//退出Activity
            }
        });
        ad.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //不执行任何操作
            }
        });
        ad.show();//显示对话框
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(webView.getTitle());
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(webView.getUrl());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(webView.getTitle());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(webView.getUrl());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/ic.png");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(webView.getUrl());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("爱深圳");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(webView.getUrl());

// 启动分享GUI
        oks.show(this);
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            Log.e("", "______"+html+"____");
        }
    }

    private long firstTime = 0;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {                                         //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {                                                    //两次按键小于2秒时，退出应用
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.editText:
            {
                editText.setFocusable(true);//设置输入框可聚集
                editText.setFocusableInTouchMode(true);//设置触摸聚焦
                editText.requestFocus();//请求焦点
                editText.findFocus();//获取焦点
                mInputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);// 显示输入法
            }
            break;
            case R.id.image_go:
            {
                String url_text;
                String url_head = "http://";
                url_text=editText.getText().toString();
                if(!url_text.contains("http://")){
                    url_text=url_head.concat(url_text);

                }
                webView.loadUrl(url_text);
                editText.setFocusable(false);//设置输入框不可聚焦，即失去焦点和光标
                if (mInputMethodManager.isActive()) {
                    mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);// 隐藏输入法
                }
            }
            break;
            case R.id.image_back:
            {
                if(webView.canGoBack()){
                    webView.goBack();
                }else{
                    if(!isExit){
                        isExit = true;
                        Toast.makeText(getApplicationContext(), "再按一次退出程序",
                                Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessageDelayed(0,2000);
                    }else{
                        finish();
                        System.exit(0);
                    }
                }
            }
            break;
            case R.id.image_forward:
            {
                if(webView.canGoForward()){
                    webView.goForward();
                }
            }
            break;
            case R.id.image_home:
            {
                if(!webView.getUrl().equals(HOME_PAGE)){
                    webView.loadUrl(HOME_PAGE);
                }
            }
            break;
            case R.id.image_share:
            {
//                //一键分享
//                OnekeyShare onekeyShare=new OnekeyShare();
//                //设置标题
//                onekeyShare.setTitle(getString(R.string.share));
//                onekeyShare.setTitleUrl(webView.getUrl());
//                onekeyShare.setImageUrl("http://sweetystory.com/Public/ttwebsite/theme1/style/img/special-1.jpg");
//                //设置信息
//                onekeyShare.setText(webView.getTitle());
//                //设置图片
//                onekeyShare.setImagePath("http://sweetystory.com/Public/ttwebsite/theme1/style/img/special-1.jpg");
//                //url仅在微信（包括好友和朋友圈）中使用
//                onekeyShare.setUrl(webView.getUrl());
//                onekeyShare.setSite(webView.getTitle());
//                onekeyShare.setSiteUrl(webView.getUrl());
//                //显示分享列表
//                onekeyShare.show(MainActivity.this);
//                OnekeyShare share = new OnekeyShare();
//                share.disableSSOWhenAuthorize();
//                share.setText("text");
//                // text是分享文本，所有平台都需要这个字段
//                share.setTitle("title");
//                // url仅在微信（包括好友和朋友圈）中使用
//                share.setUrl("http://sweetystory.com/");
//                share.setTitleUrl("http://sweetystory.com/");
//                share.setImageUrl("http://sweetystory.com/Public/ttwebsite/theme1/style/img/special-1.jpg");
//
//                share.show(MainActivity.this);

                showShare();
            }
            break;
            case R.id.image_menu:
            {
                myPopupMenu.showAtLocation(findViewById(R.id.tool_bar1), Gravity.BOTTOM, 0, 0);
            }
            break;
        }

        //设置按钮状态
        if(webView.canGoBack()){
            button_back.setImageResource(R.mipmap.ic_canback);
        }
        else
        {
            button_back.setImageResource(R.mipmap.ic_back);
        }

        if(webView.canGoForward()){
            button_forward.setImageResource(R.mipmap.ic_canforward);
        }
        else
        {
            button_forward.setImageResource(R.mipmap.ic_forward);
        }


    }
}


