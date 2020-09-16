package org.mujeeb.mosquemanager;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity {

    Resources resources;

    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resources = getResources();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        ImageView btnInstagram = (ImageView) findViewById(R.id.imgInstagram);
//        btnInstagram.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                UIUtil.goToUrl(AboutActivity.this, resources.getString(R.string.url_instagram));
//            }
//        });
//
//        ImageView btnGooglePlus = (ImageView) findViewById(R.id.imgGooglePlus);
//        btnGooglePlus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                UIUtil.goToUrl(AboutActivity.this, resources.getString(R.string.url_googleplus));
//            }
//        });
//
//        ImageView btnFacebook = (ImageView) findViewById(R.id.imgFacebook);
//        btnFacebook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                UIUtil.goToUrl(AboutActivity.this, resources.getString(R.string.url_facebook));
//            }
//        });
//
//        ImageView btnTwitter = (ImageView) findViewById(R.id.imgTwitter);
//        btnTwitter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                UIUtil.goToUrl(AboutActivity.this, resources.getString(R.string.url_twitter));
//            }
//        });

        btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
