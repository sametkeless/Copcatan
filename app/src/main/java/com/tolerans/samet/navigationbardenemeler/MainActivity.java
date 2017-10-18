package com.tolerans.samet.navigationbardenemeler;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.tolerans.samet.navigationbardenemeler.Fragments.GirisYapFragment;
import com.tolerans.samet.navigationbardenemeler.Fragments.KayitFragment;
import com.tolerans.samet.navigationbardenemeler.Fragments.KullaniciBilgiFragment;
import com.tolerans.samet.navigationbardenemeler.Fragments.ProfilFragment;
import com.tolerans.samet.navigationbardenemeler.Model.Model;
import com.tolerans.samet.navigationbardenemeler.Picasso.PicassoDownload;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseUser user;
    private RecyclerView mRecyclerView;
    private ImageView headerimg;
    private TextView  headertxt;
    private String userId,adi;
    private  NavigationView navigationView;
    private ProgressDialog dialog;
    private AdView mAdView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Anasayfa");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Buraya kadar ki kısım hazır kod navigation bar ile alakalı sadece title kodu var
        View hView = navigationView.getHeaderView(0);//nav bardaki image viewe


        //erişmemizi sağlayan view tanımladık
        headerimg = (ImageView) hView.findViewById(R.id.HeaderImageView);//imageviewi tanımladık
        headertxt = (TextView) hView.findViewById(R.id.HeaderTxt);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);

        //reklam ile ilgili kısım
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6002079974130698~3828119568");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        user = FirebaseAuth.getInstance().getCurrentUser();

        //burada navbara kişinin profil bilgilerini çekiyor
        if (user != null) {
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
            //Navigationbardaki buttonları kullanıcının giriş yapıp yapmadığına göre gizliyor yada gösteriyor
            navigationView.getMenu().findItem(R.id.nav_profileadd).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);


            for (final UserInfo profile : user.getProviderData()) {
                userId = profile.getUid();
                PicassoDownload.downloadImage(MainActivity.this, "https://graph.facebook.com/" + userId
                        + "/picture?height=500", headerimg);
                headertxt.setText(user.getDisplayName());

            }
        } else {//kullanıcı giriş yapmamışsa ekranda bunlar olacak
            headerimg.setImageResource(R.drawable.default_user);
            headertxt.setText("Ziyaretçi");
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_profileadd).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
        }
        if (mRecyclerView != null) {
            //recyclerview boş değilse tamamlama işlemi yapıyor
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));


        //Veriler çekilene kadar progress bar açacğız

        dialog= new ProgressDialog(this);
        dialog.setMessage("Veriler Yükleniyor..");
        dialog.show();


        //firebase ile recycler view bağlantısı firebase ui kutuphanesi ile gerçekleşiyor
        FirebaseRecyclerAdapter<Model, ModelViewHolder> adapter = new FirebaseRecyclerAdapter<Model, ModelViewHolder>(
                Model.class,
                R.layout.activity_recycler_icerik,
                ModelViewHolder.class,
                mDatabaseReference.child("kullanicilar").getRef())
        {
            @Override
            protected void populateViewHolder(ModelViewHolder viewHolder, final Model model, int position) {
                //recyclerview içindeki cardviewlerde gösterilecek bilgileri içeriyor
                PicassoDownload.downloadImage(MainActivity.this, model.getResim(), viewHolder.imgUserPoster);
                dialog.dismiss();//progressbar verileri çekince kapanıyor
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("kullaniciverileri",model.getUserId());
                        KullaniciBilgiFragment fragment = new KullaniciBilgiFragment();
                        FragmentManager manager = getSupportFragmentManager();
                        fragment.setArguments(bundle);
                        manager.beginTransaction().replace(R.id.relative_ana,fragment).addToBackStack(null).commit();
                    }
                });

            }
        };

        mRecyclerView.setAdapter(adapter);//recyclerviewin adapterini belirledi

        adi=null;
        ProfilKontrol();//profil kontrolü yapıyoruz
        //databaseye kayıtlı kullanıcı olup olmadıgını kontrol ediyor.


    }
    public void profilLogout(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        headerimg.setImageResource(R.drawable.default_user);
        headertxt.setText("Ziyaretçi");

        StyleableToast st = new StyleableToast(getApplicationContext(),"Çıkış gerçekleştirildi",Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#FF47C72A"));
        st.setTextColor(Color.WHITE);
        st.show();

        user=null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ProfilKontrol(){
        //profil penceresine girecek olan kullanıcının daha önceden kayıt olup olmadığını kontrol etmek için
        //userid ile name kısmını çektik ve aşağıda namenin null olup olmadığını kontrol ettik böylede kişinin
        //firebasede kayıtlı verisi olup olmadığını anlıyoruz.
        //nameyi firebaseden cekiyor ve null değilse demekki kullanıcı kaydı vardır diye diğer sayfa acııyor
        Query query = mDatabaseReference.child("kullanicilar").orderByChild("userId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    adi = ds.getValue(Model.class).getAd();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {

            GirisYapFragment gfragment = new GirisYapFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relative_ana,gfragment).addToBackStack(null).commit();

        }else if (id == R.id.nav_logout) {

            profilLogout();
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_profileadd).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);


        }
        else if (id == R.id.nav_profile) {
                if(user!=null){//kullanıcı null değil
                    ProfilKontrol();
                    if(adi!=null){//ve adi değişteni null değilse yani firebasede kaydı varsa
                        //profil fragmenti çalısacak
                        //bundle ile veri gönderdil
                        Bundle bundle = new Bundle();
                        bundle.putString("profilveriler",userId);
                        ProfilFragment fragment = new ProfilFragment();
                        FragmentManager manager = getSupportFragmentManager();
                        fragment.setArguments(bundle);
                        manager.beginTransaction().replace(R.id.relative_ana,fragment).addToBackStack(null).commit();
                        adi=null;
                    }
                    else{
                        StyleableToast st = new StyleableToast(getApplicationContext(),"Önce Kaydolmalısınız",Toast.LENGTH_SHORT);
                        st.setBackgroundColor(Color.parseColor("#c62828"));
                        st.setTextColor(Color.WHITE);
                        st.show();
                    }
                }
                else{
                    StyleableToast st = new StyleableToast(getApplicationContext(),"Önce giriş yapmalısınız",Toast.LENGTH_SHORT);
                    st.setBackgroundColor(Color.parseColor("#c62828"));
                    st.setTextColor(Color.WHITE);
                    st.show();
                }


            }else if (id == R.id.nav_profileadd) {

            if(user!=null){
                Bundle bundle = new Bundle();
                bundle.putString("veriler", userId);
                KayitFragment kayitFragment = new KayitFragment();
                FragmentManager manager = getSupportFragmentManager();
                kayitFragment.setArguments(bundle);
                manager.beginTransaction().addToBackStack(null).replace(R.id.relative_ana,kayitFragment).commit();
            }
            else{
                StyleableToast st = new StyleableToast(getApplicationContext(),"Sisteme giriş yapmalısınız",Toast.LENGTH_SHORT);
                st.setBackgroundColor(Color.parseColor("#c62828"));
                st.setTextColor(Color.WHITE);
                st.show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static class ModelViewHolder extends RecyclerView.ViewHolder{
        //recycler view için gerekli olan holder
        //cardviewlerde ne görmek istiyorsak onları burada tanımlıyoruz.
        ImageView imgUserPoster;

        public ModelViewHolder(View v) {
            super(v);
            imgUserPoster = (ImageView) v.findViewById(R.id.recyclerImg);
        }
    }
}
