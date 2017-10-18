package com.tolerans.samet.navigationbardenemeler.Picasso;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Samet on 14.02.2017.
 */

public class PicassoDownload {
    public static void downloadImage(Context c, String resimUrl, ImageView img){
        Picasso.with(c).load(resimUrl).resize(400,400).into(img);
        //Picasso kütüphanesini kullanarak resimUrl isimli gelen facebook profil fotoğrafını imageviewe ekliyor.
    }
}
