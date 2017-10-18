package com.tolerans.samet.navigationbardenemeler.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.tolerans.samet.navigationbardenemeler.MainActivity;
import com.tolerans.samet.navigationbardenemeler.Model.Model;
import com.tolerans.samet.navigationbardenemeler.Picasso.PicassoDownload;
import com.tolerans.samet.navigationbardenemeler.R;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class KayitFragment extends Fragment
{


    public KayitFragment() {
        // Required empty public constructor
    }

    private DatabaseReference mDataReference;
    private Button kaydolBtn;
    private ImageView imageView;
    private EditText nameTxt,yasTxt,memleketTxt,snapchatTxt,instagramTxt,twitterTxt,aciklamaTxt;
    private RadioGroup cinsiyet;
    private RadioButton cinsiyetE,cinsiyetButon;
    private String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Kayıt Ekranı");

        // Inflate the layout for this fragment
        //fragmentlerin background colorunu white yap default olarak transparenttir.

        mDataReference = FirebaseDatabase.getInstance().getReference();

        userId = getArguments().getString("veriler");//bundle ile mainden gelen userıdyi aldık


        //Kullanıcının daha önce kayıt olup olmadıgını sorguluyor
        Query query = mDataReference.child("kullanicilar").orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if(ds.getValue(Model.class).getUserId()!=null){
                        StyleableToast st = new StyleableToast(getActivity(),"Yalnız bir kez kaydolabilirsiniz",Toast.LENGTH_SHORT);
                        st.setBackgroundColor(Color.parseColor("#c62828"));
                        st.setTextColor(Color.WHITE);
                        st.show();
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final View v = inflater.inflate(R.layout.fragment_kayit_fragmet, container, false);

        imageView = (ImageView) v.findViewById(R.id.kayitResim);
        nameTxt = (EditText) v.findViewById(R.id.kayitName);
        yasTxt = (EditText) v.findViewById(R.id.kayitYas);
        memleketTxt = (EditText) v.findViewById(R.id.kayitMemleket);
        cinsiyet = (RadioGroup) v.findViewById(R.id.kayitCinsiyet);
        snapchatTxt = (EditText) v.findViewById(R.id.kayitSnapchat);
        instagramTxt = (EditText) v.findViewById(R.id.kayitInstagram);
        twitterTxt = (EditText) v.findViewById(R.id.kayitTwitter);
        aciklamaTxt = (EditText) v.findViewById(R.id.kayitAciklama);
        kaydolBtn = (Button) v.findViewById(R.id.kayitButton);
        cinsiyetE = (RadioButton) v.findViewById(R.id.kayitCinsiyetErkek);
        cinsiyetButon = (RadioButton) v.findViewById(R.id.kayitCinsiyetKadin);

        PicassoDownload.downloadImage(getActivity(),"https://graph.facebook.com/" + userId + "/picture?height=500",imageView);
        //imageviwe facebook profil fotografını cektik

        cinsiyetE.setChecked(true);

        kaydolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = cinsiyet.getCheckedRadioButtonId();


                cinsiyetButon = (RadioButton) v.findViewById(selectedId);
                myNewUser(userId,"https://graph.facebook.com/" + userId + "/picture?height=1000"
                        ,nameTxt.getText().toString(),yasTxt.getText().toString()
                        ,memleketTxt.getText().toString(),cinsiyetButon.getText().toString(),
                        snapchatTxt.getText().toString(),instagramTxt.getText().toString(),
                        twitterTxt.getText().toString(),aciklamaTxt.getText().toString());

            }
        });


        return v;
    }

    private void myNewUser(String userId, String resimUrl,String name,String yas,String memleket,String cinsiyet,
                           String snapchat,String instagram,String twitter,String aciklama) {
        if(name.equals("") || yas.equals("") || memleket.equals("")){
            StyleableToast st = new StyleableToast(getActivity(),"İsim, yaş ve memleket alanları boş bırakılamaz",Toast.LENGTH_SHORT);
            st.setBackgroundColor(Color.parseColor("#FFFFDD31"));
            st.setTextColor(Color.WHITE);
            st.show();
           }
        else{
            Model model = new Model(userId,resimUrl,name,yas,memleket,cinsiyet,snapchat,instagram,twitter,aciklama);
            String indexDegeri= String.valueOf((-1 * new Date().getTime()/1000));
            //Şimdiki zamanı alıp en yuksek değerden cıkardık ve böylece en son eklenen dğer en üstte olabiliyor
            mDataReference.child("kullanicilar").child(indexDegeri).setValue(model);
            getActivity().onBackPressed();
            //bu kod önceki sayfaya dönmemizi sağlıyor.
        }


    }
}
