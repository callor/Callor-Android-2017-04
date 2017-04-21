package com.callor.hello.myphonebook;

import android.content.ContentUris;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.callor.hello.myphonebook.databinding.ActivityMainBinding;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecylerView();
            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    }


    private void setRecylerView(){
        List<PhoneVO> phoneDTO = new ArrayList<PhoneVO>();
        String[] selectionArgs = {""};
        String selectionClause = null;

        // 검색창이 비어있으면 전체를 보여주기
        if(binding.textSearch.getText().toString().isEmpty()) {
            selectionArgs = null;
            selectionClause = null;
        } else {
            selectionArgs[0] = binding.textSearch.getText().toString() + "%" ;
            selectionClause = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " LIKE ?" ;
        }

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data.PHOTO_ID,
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.PHOTO_FILE_ID,
                ContactsContract.Data.DATA1,  // phone.number, organization.company
                ContactsContract.Data.DATA2, // Type
                ContactsContract.CommonDataKinds.Phone.DATA1, // .NUMBER,
                ContactsContract.CommonDataKinds.Photo.PHOTO_URI
        };

        Cursor cursor = this.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, // PROJECTION
                selectionClause, // 조건
                selectionArgs, // 조건값
                ContactsContract.Data.DISPLAY_NAME ); // 정렬

        if(cursor.getColumnCount()>0) {
            // 이름
            int nameColumn = cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Email.DISPLAY_NAME_PRIMARY);
            // 전화번호
            int phoneColumn = cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Email.ADDRESS) ;

            while(cursor.moveToNext()){
                String name = cursor.getString(nameColumn);
                String phone = cursor.getString(phoneColumn);

                // 전화번호 고유 ID
                int cIndex = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID );
                long cId = cursor.getLong(cIndex); // 사용자 사진이 있는 인덱 주소

                // 사진이 저장된 실제위치
                Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,cId);

                // 사진읽기
                InputStream inputStream = ContactsContract
                                                .Contacts.openContactPhotoInputStream(
                                                        this.getContentResolver(),uri);

                // 읽은 사진을 Bitmap으로 변환
                Bitmap cPhoto = null ;
                if(inputStream != null) {
                    cPhoto = BitmapFactory.decodeStream(inputStream);
                }
                phoneDTO.add(new PhoneVO(name,phone,cPhoto));
            }

            // 아답터 초기화
            RcAdapter rcAdapter = new RcAdapter(this,phoneDTO);
            binding.listView.setLayoutManager(new LinearLayoutManager(this));
            binding.listView.setAdapter(rcAdapter);
        }

    }

}
