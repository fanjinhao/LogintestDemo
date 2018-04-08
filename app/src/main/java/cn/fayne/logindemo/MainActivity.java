package cn.fayne.logindemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private  TextView mWelcome;
    private Button mBtnLogout;

    private static final String TAG = "MainActivity";

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
}
