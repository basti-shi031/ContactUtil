package com.basti.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.basti.bean.MyContacts;

import java.util.ArrayList;

public class ContactUtil {
    public static ArrayList<MyContacts> getAllContacts(Context context) {
        ArrayList<MyContacts> contacts = new ArrayList<>();
        MyContacts temp;
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            //新建一个联系人实例
            temp = new MyContacts();
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            temp.setName(name);

            //获取联系人电话号码
            Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            while (phoneCursor.moveToNext()) {
                String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phone = phone.replace("-", "");
                phone = phone.replace(" ", "");
                temp.getPhone().add(phone);
            }
            contacts.add(temp);
            //记得要把cursor给close掉
            phoneCursor.close();
        }
        cursor.close();
        return contacts;
    }

}
