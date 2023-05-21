package com.example.izheco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity3 extends AppCompatActivity {

    String phoneNumberS = "", websiteS = "", vkS = "";

    public String phoneBeautify(String s) {
        String s2 = "";
        if (s.length() == 12) {
            for (int i = 0; i < 12; i++) {
                if (i == 2) {
                    s2 += " (";
                }
                if (i == 5) {
                    s2 += ") ";
                }
                if (i == 8 || i == 10) {
                    s2 += "-";
                }
                s2 += s.charAt(i);
            }
        }
        if (s.length() == 6) {
            for (int i = 0; i < 6; i++) {
                if (i == 3)
                    s2 += "-";
                s2 += s.charAt(i);
            }
        }
        return s2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ImageView back = findViewById(R.id.backbutton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        String logo = "";
        ImageView logotype = findViewById(R.id.company_logo_src);
        TextView companyName = findViewById(R.id.company_name);
        TextView extraInfo = findViewById(R.id.extra_information_src);
        TextView phoneNumber = findViewById(R.id.phone_number_src);
        TextView website = findViewById(R.id.website_src);
        TextView vk = findViewById(R.id.vk_src);
        CardView extraInfoCard = findViewById(R.id.extra_information);
        CardView phoneNumberCard = findViewById(R.id.phone_number);
        CardView websiteCard = findViewById(R.id.website);
        CardView vkCard = findViewById(R.id.vk);
        if (getIntent().hasExtra("logo")) {
            logo = getIntent().getStringExtra("logo");
        }
        int logoInt = 0;
        try {
            logoInt = R.drawable.class.getField(logo).getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        logotype.setImageResource(logoInt);
        if (getIntent().hasExtra("name")) {
            companyName.setText(getIntent().getStringExtra("name"));
        }
        if (getIntent().hasExtra("extra_info")) {
            if (getIntent().getStringExtra("extra_info").equals("none")) {
                extraInfoCard.setVisibility(View.GONE);
            }
            else {
                extraInfo.setText(getIntent().getStringExtra("extra_info"));
            }
        }
        if (getIntent().hasExtra("phone_number")) {
            if (getIntent().getStringExtra("phone_number").equals("none")) {
                phoneNumberCard.setVisibility(View.GONE);
            }
            else {
                phoneNumber.setText(phoneBeautify(getIntent().getStringExtra("phone_number")));
                phoneNumberS = getIntent().getStringExtra("phone_number");
            }
        }
        if (getIntent().hasExtra("website")) {
            if (getIntent().getStringExtra("website").equals("none")) {
                websiteCard.setVisibility(View.GONE);
            }
            else {
                websiteS = getIntent().getStringExtra("website");
            }
        }
        if (getIntent().hasExtra("vk")) {
            if (getIntent().getStringExtra("vk").equals("none")) {
                vkCard.setVisibility(View.GONE);
            }
            else {
                vkS = getIntent().getStringExtra("vk");
            }
        }
        vk.setText(Html.fromHtml("<a href=" + vkS + "> Группа ВКонтакте </a>"));
        vk.setMovementMethod(LinkMovementMethod.getInstance());
        website.setText(Html.fromHtml("<a href=" + websiteS + "> Сайт организации </a>"));
        website.setMovementMethod(LinkMovementMethod.getInstance());
        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumberS));
                startActivity(intent);
            }
        });
    }
}