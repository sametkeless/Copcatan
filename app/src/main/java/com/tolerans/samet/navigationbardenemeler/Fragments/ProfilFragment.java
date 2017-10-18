package com.tolerans.samet.navigationbardenemeler.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
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
public class ProfilFragment extends Fragment {


    public ProfilFragment() {
        // Required empty public constructor
    }

    private String userId,adi;
    private Button duzenleBtn,kayitBtn,silBtn,TasiBtn;
    private  ImageView img;
    private EditText nameTxt,yasTxt,memleketTxt,snapchatTxt,instagramTxt,twitterTxt,aciklamaTxt,cinsiyetTxt;
    private DatabaseReference mDataReference;
    private RadioGroup cinsiyet;
    private RadioButton cinsiyetE,cinsiyetButon,cinsiyetK;
    private LinearLayout txtLinear,radioLinear;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profil");

        final View v= inflater.inflate(R.layout.fragment_profil, container, false);

        radioLinear = (LinearLayout) v.findViewById(R.id.radioCinsiyetLayout);
        txtLinear = (LinearLayout) v.findViewById(R.id.txtCinsiyetLayout);
        img = (ImageView) v.findViewById(R.id.profilResim);
        kayitBtn = (Button) v.findViewById(R.id.profilKayitButton);
        duzenleBtn = (Button) v.findViewById(R.id.profilDuzenleButton);
        TasiBtn = (Button) v.findViewById(R.id.profilTasi);
        nameTxt = (EditText) v.findViewById(R.id.profilName);
        yasTxt = (EditText) v.findViewById(R.id.profilYas);
        cinsiyetTxt= (EditText) v.findViewById(R.id.profilCinsiyetEdit);
        memleketTxt = (EditText) v.findViewById(R.id.profilMemleket);
        cinsiyet = (RadioGroup) v.findViewById(R.id.profilCinsiyet);
        cinsiyetE = (RadioButton) v.findViewById(R.id.profilCinsiyetErkek);
        cinsiyetK = (RadioButton) v.findViewById(R.id.profilCinsiyetKadin);
        snapchatTxt = (EditText) v.findViewById(R.id.profilSnapchat);
        instagramTxt = (EditText) v.findViewById(R.id.profilInstagram);
        twitterTxt = (EditText) v.findViewById(R.id.profilTwitter);
        aciklamaTxt = (EditText) v.findViewById(R.id.profilAciklama);
        silBtn = (Button) v.findViewById(R.id.profilSilButton);

        mDataReference = FirebaseDatabase.getInstance().getReference();

        Typeface t1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Ubuntu.ttf");
        kayitBtn.setTypeface(t1);
        duzenleBtn.setTypeface(t1);
        silBtn.setTypeface(t1);
        TasiBtn.setTypeface(t1);

        userId = getArguments().getString("profilveriler");//bundle ile mainden gelen userıdyi aldık


        Query query = mDataReference.child("kullanicilar").orderByChild("userId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    cinsiyetTxt.setText(ds.getValue(Model.class).getCinsiyet());
                    nameTxt.setText(ds.getValue(Model.class).getAd());
                    yasTxt.setText(ds.getValue(Model.class).getYas());
                    snapchatTxt.setText(ds.getValue(Model.class).getSnapchat());
                    memleketTxt.setText(ds.getValue(Model.class).getMemleket());
                    instagramTxt.setText(ds.getValue(Model.class).getInstagram());
                    twitterTxt.setText(ds.getValue(Model.class).getTwitter());
                    aciklamaTxt.setText(ds.getValue(Model.class).getAciklama());
                    PicassoDownload.downloadImage(getActivity(),ds.getValue(Model.class).getResim(),img);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        duzenleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameTxt.setEnabled(true);yasTxt.setEnabled(true);memleketTxt.setEnabled(true);
                instagramTxt.setEnabled(true);twitterTxt.setEnabled(true);aciklamaTxt.setEnabled(true);
                snapchatTxt.setEnabled(true);
                radioLinear.setVisibility(v.VISIBLE);
                txtLinear.setVisibility(v.GONE);
                if(cinsiyetTxt.getText().toString().equals("Erkek")){
                    cinsiyetE.setChecked(true);
                }
                else
                {
                    cinsiyetK.setChecked(true);
                }

                kayitBtn.setVisibility(v.VISIBLE);
                duzenleBtn.setVisibility(v.GONE);
            }
        });
        kayitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //önce eski kaydı sildireceğiz sonra yeni kaydı ekleteceğiz
                Query applesQuery = mDataReference.child("kullanicilar").orderByChild("userId").equalTo(userId);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });//mevcut kaydı sildik

                int selectedId = cinsiyet.getCheckedRadioButtonId();
                cinsiyetButon = (RadioButton) v.findViewById(selectedId);

                if(nameTxt.getText().toString().equals("") || yasTxt.getText().toString().equals("") || memleketTxt.getText().toString().equals("")){
                    StyleableToast st = new StyleableToast(getActivity(),"İsim, yaş ve memleket alanları boş bırakılamaz",Toast.LENGTH_SHORT);
                    st.setBackgroundColor(Color.parseColor("#FFFFDD31"));
                    st.setTextColor(Color.WHITE);
                    st.show();
                }else {
                    Model model = new Model(userId, "https://graph.facebook.com/" + userId + "/picture?height=500",
                            nameTxt.getText().toString(), yasTxt.getText().toString(),
                            memleketTxt.getText().toString(), cinsiyetButon.getText().toString(),
                            snapchatTxt.getText().toString(), instagramTxt.getText().toString(), twitterTxt.getText().toString(),
                            aciklamaTxt.getText().toString());

                    String indexDegeri = String.valueOf((-1 * new Date().getTime() / 1000));
                    //Şimdiki zamanı alıp en yuksek değerden cıkardık ve böylece en son eklenen dğer en üstte olabiliyor
                    mDataReference.child("kullanicilar").child(indexDegeri).setValue(model);
                    //Yeni verileri yeni kişi oalrak kaydettik

                    editTextKapat();
                    StyleableToast st = new StyleableToast(getActivity(),"Değişiklikler kaydedildi",Toast.LENGTH_SHORT);
                    st.setBackgroundColor(Color.parseColor("#FF47C72A"));
                    st.setTextColor(Color.WHITE);
                    st.show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }

            }
        });
        TasiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Model yeni = new Model();
                Query applesQuery = mDataReference.child("kullanicilar").orderByChild("userId").equalTo(userId);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren()){
                        yeni.setUserId(ds.getValue(Model.class).getUserId());
                        yeni.setAd(ds.getValue(Model.class).getAd());
                        yeni.setYas(ds.getValue(Model.class).getYas());
                        yeni.setCinsiyet(ds.getValue(Model.class).getCinsiyet());
                        yeni.setMemleket(ds.getValue(Model.class).getMemleket());
                        yeni.setAciklama(ds.getValue(Model.class).getAciklama());
                        yeni.setInstagram(ds.getValue(Model.class).getInstagram());
                        yeni.setResim(ds.getValue(Model.class).getResim());
                        yeni.setSnapchat(ds.getValue(Model.class).getSnapchat());
                        yeni.setTwitter(ds.getValue(Model.class).getTwitter());
                        ds.getRef().removeValue();
                            String indexDegeri = String.valueOf((-1 * new Date().getTime() / 1000));
                            //Şimdiki zamanı alıp en yuksek değerden cıkardık ve böylece en son eklenen dğer en üstte olabiliyor
                            mDataReference.child("kullanicilar").child(indexDegeri).setValue(yeni);
                            //Yeni verileri yeni kişi oalrak kaydettik
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                StyleableToast st = new StyleableToast(getActivity(),"Profiliniz üst sıralara taşındı",Toast.LENGTH_SHORT);
                st.setBackgroundColor(Color.parseColor("#FF47C72A"));
                st.setTextColor(Color.WHITE);
                st.show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });
        silBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Hesabınızı Silmek İstediğinizden Emin misiniz?");
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Query query = mDataReference.child("kullanicilar").orderByChild("userId").equalTo(userId);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds:dataSnapshot.getChildren()){
                                    ds.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        StyleableToast st = new StyleableToast(getActivity(),"Hesabınız Silindi",Toast.LENGTH_SHORT);
                        st.setBackgroundColor(Color.parseColor("#FF47C72A"));
                        st.setTextColor(Color.WHITE);
                        st.show();
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                    }

                });
                builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        return v;
    }
    private void editTextKapat(){
        nameTxt.setEnabled(false);
        yasTxt.setEnabled(false);
        snapchatTxt.setEnabled(false);
        memleketTxt.setEnabled(false);
        instagramTxt.setEnabled(false);
        twitterTxt.setEnabled(false);
        aciklamaTxt.setEnabled(false);
        cinsiyet.setEnabled(false);
        cinsiyetE.setEnabled(false);
        cinsiyetK.setEnabled(false);
    }
}
