package com.basti;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.basti.bean.MyContacts;
import com.basti.utils.ContactUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    AppCompatButton exportButton;
    RxPermissions rxPermissions;
    ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exportButton = findViewById(R.id.export_bt);

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxPermissions = new RxPermissions(MainActivity.this);
                rxPermissions.request(Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean accept) throws Exception {

                                if (accept) {
                                    ArrayList<MyContacts> contacts = ContactUtil.getAllContacts(MainActivity.this);

                                    String result = createDoc(contacts);

                                    clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clipData = ClipData.newPlainText("contact", result);
                                    clipboardManager.setPrimaryClip(clipData);
                                    Toast.makeText(MainActivity.this, "导出成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "权限未申请，无法导出", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                String result = readFileData("电话本导出结果.doc");
                System.out.println(result);
            }
        });


    }


    private String createDoc(ArrayList<MyContacts> contacts) {
        String result = "";
        String tempLine = "";
        for (MyContacts myContacts : contacts) {
            tempLine = "";
            tempLine += myContacts.getName();

            List<String> phoneList = myContacts.getPhone();
            for (String phone : phoneList) {
                tempLine += "    " + phone;
            }
            result += tempLine + "\r\n";
        }

        return result;
        //writeFileData("电话本导出结果.doc", result);

    }

    //向指定的文件中写入指定的数据
    public void writeFileData(String filename, String content) {

        try {

            FileOutputStream fos = this.openFileOutput(filename, MODE_PRIVATE);//获得FileOutputStream

            //将要写入的字符串转换为byte数组

            byte[] bytes = content.getBytes();

            fos.write(bytes);//将byte数组写入文件

            fos.close();//关闭文件输出流

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //打开指定文件，读取其数据，返回字符串对象
    public String readFileData(String fileName) {

        String result = "";

        try {

            FileInputStream fis = openFileInput(fileName);

            //获取文件长度
            int length = fis.available();

            byte[] buffer = new byte[length];

            fis.read(buffer);

            //将byte数组转换成指定格式的字符串
            result = new String(buffer, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
