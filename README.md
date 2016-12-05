# Android-M-Permission

> 把Android运行时权限处理封装在BaseActivity中，方便业务申请时只需要简单的1，2行代码即可成功处理权限申请

详细说明可以参见文章：http://www.jianshu.com/p/5675c5230052

## 对外接口

#### 1. 请求权限操作

```java
/**
 * 请求权限操作
 * @param rationale 请求权限提示语
 * @param permissionRequestCode 权限requestCode
 * @param perms 申请的权限列表
 * @param callback 权限结果回调
 */
 void performCodeWithPermission(@NonNull String rationale,
                                final int permissionRequestCode,
                                @NonNull String[] perms,
                                @NonNull PermissionCallback callback)
```

#### 2. 跳转设置弹框

```java
/**
 * 跳转设置弹框 建议在权限被设置为不在询问时弹出 提示用户前往设置页面打开权限
 * @param tips 提示信息
 */
void alertAppSetPermission(String tips)

/**
 * 跳转设置弹框 建议在权限被设置为不在询问时弹出 提示用户前往设置页面打开权限
 * @param tips 提示信息
 * @param requestCode 页面返回时onActivityResult的requestCode
 */
void alertAppSetPermission(String tips, int requestCode)
```

## 示例

```java
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
```

## 欢迎关注我的公众号

![我的公众号](https://github.com/tsy12321/PayAndroid/blob/master/wxmp_avatar.jpg)
