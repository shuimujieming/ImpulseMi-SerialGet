package com.impulsemi.serialget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {
    public String getSerialNumber(){
        String serial = null;
        try {
            Class<?> sys =Class.forName("android.os.SystemProperties");
            Method get =sys.getMethod("get", String.class);
            serial = (String)get.invoke(sys, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.getpass);
        Button button1 = findViewById(R.id.getmypass);
        final EditText textView = findViewById(R.id.myserial);
        final EditText editText = findViewById(R.id.serial);
        final EditText editText1 = findViewById(R.id.serialget);
        final EditText textView1 = findViewById(R.id.myserial1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = editText.getText().toString();
                BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(uri.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
                String line = null;
                StringBuffer strbuf = new StringBuffer();
                while(true)
                {
                    try {
                        if (!((line = br.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!line.trim().equals(""))
                    {
                        strbuf.append(AESUtil.encryptString2Base64(line,"mihayolove35710","scp173049682166"));
                    }
                }
                //String local_pass =AESUtil.encryptString2Base64(editText.getText().toString(),"mihayolove35710","scp173049682166");
                //local_pass = local_pass.replaceAll("\n|\r","");
                editText1.setText(strbuf);
                final ClipboardManager cp = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cp.setPrimaryClip(ClipData.newPlainText(null, editText1.getText()));
                Toast.makeText(getApplicationContext(), "密文已复制到剪切板", Toast.LENGTH_SHORT).show();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(getSerialNumber());
                String local_pass =AESUtil.encryptString2Base64(getSerialNumber(),"mihayolove35710","scp173049682166");
                local_pass = local_pass.replaceAll("\n|\r","");
                textView1.setText(local_pass);
                final ClipboardManager cp = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cp.setPrimaryClip(ClipData.newPlainText(null, textView1.getText()));
                Toast.makeText(getApplicationContext(), "密文已复制到剪切板", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
