package com.tolerans.samet.navigationbardenemeler.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.tolerans.samet.navigationbardenemeler.Model.Model;
import com.tolerans.samet.navigationbardenemeler.Picasso.PicassoDownload;
import com.tolerans.samet.navigationbardenemeler.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class KullaniciBilgiFragment extends Fragment {


    public KullaniciBilgiFragment() {
        // Required empty public constructor
    }

    private String userId,snapadres,twadres,insadres;
    private DatabaseReference mDataReference;
    private TextView name,cinsiyet,yas,aciklama,memleket;
    private Button snapchat,instagram,twitter;
    private ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kullanici_bilgi, container, false);

        userId = getArguments().getString("kullaniciverileri");
        //Main Activityden bundle ile gönderilen userId bilgisi (Bizim verilerimizin arasında primary key özelliği var)

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Kullanıcı Bilgileri");

        img = (ImageView) view.findViewById(R.id.kullaniciResim);
        name = (TextView) view.findViewById(R.id.kullaniciName);
        yas = (TextView) view.findViewById(R.id.kullaniciYas);
        cinsiyet = (TextView) view.findViewById(R.id.kullaniciCinsiyet);
        aciklama = (TextView) view.findViewById(R.id.kullaniciAciklama);
        memleket = (TextView) view.findViewById(R.id.kullaniciMemleket);
        snapchat = (Button) view.findViewById(R.id.kullanicisnapekle);
        instagram = (Button) view.findViewById(R.id.kullaniciinstagramziyaret);
        twitter = (Button) view.findViewById(R.id.kullanicitwitterziyaret);

        mDataReference = FirebaseDatabase.getInstance().getReference();

        Typeface t1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Ubuntu.ttf");

        snapchat.setTypeface(t1);
        instagram.setTypeface(t1);
        twitter.setTypeface(t1);

        verileriGetir();


        snapchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(snapadres.equals("")){
                    StyleableToast st = new StyleableToast(getActivity(),"Kullanıcı Snapchat hesabı belirtmemiş", Toast.LENGTH_SHORT);
                    st.setBackgroundColor(Color.parseColor("#FFFFDD31"));
                    st.setTextColor(Color.WHITE);
                    st.show();
                }else{
                    //Snapchat uygulamasını açıp kişiyi arkadaş olarak eklemeyi sağlıyor
                Uri uri = Uri.parse("http://www.snapchat.com/"+snapadres);
                Intent insta = new Intent(Intent.ACTION_VIEW, uri);
                insta.setPackage("com.snapchat.android");

                if (isIntentAvailable(getActivity(), insta)){
                    startActivity(insta);
                } else{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.snapchat.com/add/"+snapadres)));
                }
                }
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(insadres.equals("")){
                    StyleableToast st = new StyleableToast(getActivity(),"Kullanıcı İnstagram hesabı belirtmemiş", Toast.LENGTH_SHORT);
                    st.setBackgroundColor(Color.parseColor("#FFFFDD31"));
                    st.setTextColor(Color.WHITE);
                    st.show();
                }else {
                    Uri uri = Uri.parse("http://instagram.com/_u/" + insadres);
                    Intent insta = new Intent(Intent.ACTION_VIEW, uri);
                    insta.setPackage("com.instagram.android");

                    if (isIntentAvailable(getActivity(), insta)) {
                        startActivity(insta);
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + insadres)));
                    }
                }
            }

        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(twadres.equals("")){
                    StyleableToast st = new StyleableToast(getActivity(),"Kullanıcı Twitter hesabı belirtmemiş", Toast.LENGTH_SHORT);
                    st.setBackgroundColor(Color.parseColor("#FFFFDD31"));
                    st.setTextColor(Color.WHITE);
                    st.show();
                }else{
                    Uri uri = Uri.parse("http://www.twitter.com/"+twadres);
                    Intent insta = new Intent(Intent.ACTION_VIEW, uri);
                    insta.setPackage("com.twitter.android");

                    if (isIntentAvailable(getActivity(), insta)){
                        startActivity(insta);
                    } else{
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/"+twadres)));
                    }
                }
        }
        });

        return view;
    }
    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void verileriGetir() {

       Query query = mDataReference.child("kullanicilar").orderByChild("userId").equalTo(userId);
        //Bir sorgu oluşturuyoruz. UserID bilgisini kullanarak json dosyaları arasından gerekli veriyi çekiyor

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    name.setText(ds.getValue(Model.class).getAd());
                    yas.setText(ds.getValue(Model.class).getYas());
                    cinsiyet.setText(ds.getValue(Model.class).getCinsiyet());
                    aciklama.setText(ds.getValue(Model.class).getAciklama());
                    memleket.setText(ds.getValue(Model.class).getMemleket());
                    PicassoDownload.downloadImage(getActivity(),ds.getValue(Model.class).getResim(),img);
                    snapadres = ds.getValue(Model.class).getSnapchat();
                    insadres= ds.getValue(Model.class).getInstagram();
                    twadres =ds.getValue(Model.class).getTwitter();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
