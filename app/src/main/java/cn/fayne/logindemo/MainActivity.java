package cn.fayne.logindemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fayne.logindemo.adapter.AdapterComment;
import cn.fayne.logindemo.bean.Comment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mWelcome;
    private Button mBtnLogout;

    private static final String TAG = "MainActivity";
    private ListView comment_list;
    private ImageView comment;
    private ImageView chat;
    private LinearLayout rl_enroll;
    private TextView hide_down;
    private EditText comment_content;
    private Button comment_send;
    private RelativeLayout rl_comment;
    private AdapterComment adapterComment;
    private List<Comment> data;
    private String user;
    private String url = "http://www.fayne.cn/hello.htm";
    private String connectUrl = "http://project.fayne.cn/getcomment.php";
    private String sendCommentUrl = "http://project.fayne.cn/comment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Log.d(TAG, "onCreate: ");
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String name = pref.getString("user", "null");
        if (!name.equals("null")) {
            mWelcome.setText("Hello " + name + "!");
            user = name;
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("user", "null");
                editor.commit();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                MainActivity.this.finish();
            }
        });

        new LoadCommentHandle().run();
    }

    class LoadCommentHandle implements Runnable {
        @Override
        public void run() {
            loadComment();
        }
    }

    class SendCommentHandle implements Runnable {
        String content = null;
        public SendCommentHandle(String content) {
            this.content = content;
        }
        @Override
        public void run() {
            sendServerComment(content);
        }
    }

    private void loadComment() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);


        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: " + response.toString());

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        String id = object.getString("userid");
                        String content = object.getString("content");
                        Comment com = new Comment(id, content);
                        com.setmName(com.getmName() + ":");
                        com.setmContent(com.getmContent());
                        adapterComment.addComment(com);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: error", error);
            }
        };

        StringRequest request = new StringRequest(Request.Method.POST, connectUrl, listener, errorListener) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("url", url);
                return map;
            }
        };


        requestQueue.add(request);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MainActivity.this.finish();
        }
        return true;
    }

    private void initView() {
        mWelcome = findViewById(R.id.tv_welcome);
        mBtnLogout = findViewById(R.id.btn_logout);
        comment_list = (ListView) findViewById(R.id.comment_list);
        //comment_list.setOnItemClickListener();
        comment = (ImageView) findViewById(R.id.comment);
        comment.setOnClickListener(this);
        chat = (ImageView) findViewById(R.id.chat);
        chat.setOnClickListener(this);
        rl_enroll = (LinearLayout) findViewById(R.id.rl_enroll);
        rl_enroll.setOnClickListener(this);
        hide_down = (TextView) findViewById(R.id.hide_down);
        hide_down.setOnClickListener(this);
        comment_content = (EditText) findViewById(R.id.comment_content);
        comment_content.setOnClickListener(this);
        comment_send = (Button) findViewById(R.id.comment_send);
        comment_send.setOnClickListener(this);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
        rl_comment.setOnClickListener(this);
        data = new ArrayList<>();
        adapterComment = new AdapterComment(getApplicationContext(), data);
        comment_list.setAdapter(adapterComment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String name = pref.getString("user", "null");
        if (name.equals("null")) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String name = pref.getString("user", "null");
        if (name.equals("null")) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment:
                showInput();
                break;
            case R.id.hide_down:
                // hide comment
                hideInput();
                break;
            case R.id.comment_send:
                sendComment();
                break;
        }
    }

    private void showInput() {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        // show comment
        rl_enroll.setVisibility(View.GONE);
        rl_comment.setVisibility(View.VISIBLE);
        comment_content.requestFocus();
    }

    private void hideInput() {
        rl_enroll.setVisibility(View.VISIBLE);
        rl_comment.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(comment_content.getWindowToken(), 0);
    }

    private void sendComment() {
        if (comment_content.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "评论不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Comment comment = new Comment();
            comment.setmName(user + ":");
            comment.setmContent(comment_content.getText().toString());
            adapterComment.addComment(comment);
            new SendCommentHandle(comment_content.getText().toString()).run();
            comment_content.setText("");
            Toast.makeText(getApplicationContext(), "评论成功", Toast.LENGTH_SHORT).show();
            hideInput();
        }
    }

    private void sendServerComment(final String content) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, sendCommentUrl, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user", user);
                map.put("touser", "");
                map.put("url", url);
                map.put("content", content);
                return map;
            }
        };
        Log.d(TAG, "sendServerComment: "+ content + ", " + url);

        requestQueue.add(stringRequest);
    }

    private void submit() {
        // validate
        String content = comment_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "content不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
