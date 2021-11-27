package com.drv.shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.drv.shopping.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class RiderForm extends AppCompatActivity {
    private EditText nameEditText,ipEditText,expEditText,carEditText,numEditText,termEditText;
    private Button confirmRiderBtn;
    private String totalAmount = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_form);

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Общая стоимость = Рб. "+totalAmount,Toast.LENGTH_SHORT).show();
        confirmRiderBtn = (Button) findViewById(R.id.confirm_rider);
        nameEditText =(EditText) findViewById(R.id.rider_name);
        ipEditText =(EditText) findViewById(R.id.rider_ip);
        expEditText =(EditText) findViewById(R.id.rider_exp);
        numEditText =(EditText) findViewById(R.id.rider_number_osago);
        carEditText = (EditText)findViewById(R.id.rider_car);
        termEditText = (EditText)findViewById(R.id.ride_term);
        confirmRiderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });
    }

    private void Check() {
        if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this,"Пожалуйста напишите своё ФИО",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(ipEditText.getText().toString())){
            Toast.makeText(this,"Пожалуйста напишите свой номер телефона",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(expEditText.getText().toString())){
            Toast.makeText(this,"Пожалуйста напишите правильный адрес",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(numEditText.getText().toString())){
            Toast.makeText(this,"Пожалуйста напишите вашего города",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(carEditText.getText().toString())){
            Toast.makeText(this,"Пожалуйста напишите вашего города",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(termEditText.getText().toString())){
            Toast.makeText(this,"Пожалуйста напишите вашего города",Toast.LENGTH_SHORT).show();
        }
        else {

            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd. yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());
        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
                .child("RiderCheck")
                .child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",nameEditText.getText().toString());
        ordersMap.put("ip",ipEditText.getText().toString());
        ordersMap.put("exp",expEditText.getText().toString());
        ordersMap.put("car",carEditText.getText().toString());
        ordersMap.put("term",termEditText.getText().toString());
        ordersMap.put("num",numEditText.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("state", "Not Shipped");
        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User view")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RiderForm.this,"Ваша заявка принята, ожидайте.",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RiderForm.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });


    }
}
