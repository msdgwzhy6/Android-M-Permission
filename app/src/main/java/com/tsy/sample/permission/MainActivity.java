package com.tsy.sample.permission;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    //permission code
    private final int RC_STORAGE = 1; //存储文件权限申请

    //request code
    private final int REQUEST_APPSET = 1;       //跳转appset的返回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_read_file).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_read_file:
                doReadFilePermisssion();
                break;
        }
    }

    //动态权限申请
    private void doReadFilePermisssion() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};

        performCodeWithPermission(getString(R.string.permission_rc_storage), RC_STORAGE, perms,
                new PermissionCallback() {
                    @Override
                    public void hasPermission() {
                        doReadFile();
                    }

                    @Override
                    public void noPermission(Boolean hasPermanentlyDenied) {
                        if(hasPermanentlyDenied) {
                            //只是提供跳转系统设置的提示 系统返回后不做检查处理
//                            alertAppSetPermission(getString(R.string.permission_storage_deny_again));

                            //如果需要跳转系统设置页后返回自动再次检查和执行业务
                            alertAppSetPermission(getString(R.string.permission_storage_deny_again), REQUEST_APPSET);
                        }
                    }
                });
    }

    //开始读写文件业务
    private void doReadFile() {
        File file = new File(Environment.getExternalStorageDirectory() + "/com.tsy/a.zip");
        // ...

        Toast.makeText(getApplicationContext(), "成功执行读写文件业务", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_APPSET) {         //如果需要跳转系统设置页后返回自动再次检查和执行业务 如果不需要则不需要重写onActivityResult
            doReadFilePermisssion();
        }
    }
}
