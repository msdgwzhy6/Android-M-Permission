package com.tsy.sample.permission;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    //permission code
    private final int RC_STORAGE = 1; //存储文件权限申请

    //request code
    private final int REQUEST_APPSET = 1;       //跳转appset的返回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

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

        if (EasyPermissions.hasPermissions(this, perms)) {
            doReadFile();   //权限通过
        } else {
            //请求权限
            EasyPermissions.requestPermissions(this, getString(R.string.permission_rc_storage), RC_STORAGE, perms);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //权限通过
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        doReadFile();
    }

    //权限拒绝
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //点了不再询问 弹出提示跳转系统设置打开权限
            new AppSettingsDialog.Builder(this, getString(R.string.permission_storage_deny_again))
                    .setTitle(getString(R.string.permission_deny_again_title))
                    .setPositiveButton(getString(R.string.permission_deny_again_positive))
                    .setNegativeButton(getString(R.string.permission_deny_again_nagative), null)
                    .setRequestCode(REQUEST_APPSET)
                    .build()
                    .show();
        } else {
            //拒绝权限
        }
    }
}
