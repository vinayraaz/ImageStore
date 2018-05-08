package com.imagestore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

/**
 * Created by admin on 05-May-18.
 */

public class Gallery_Image_Activity extends Activity implements View.OnClickListener {
    private final int select_photo = 1; // request code fot gallery intent

    private static ImageView gallery_image;

    private static TextView uriPath, realPath;
    TextView Add_Linear;
    LinearLayout Add_Linear_Main;
    String Click_event = "1";
    ImageView gallery_image_New;
    Bitmap bitmap;
    Uri imageuri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galley_image_capture);

        gallery_image = (ImageView) findViewById(R.id.gallery_imageview);

        uriPath = (TextView) findViewById(R.id.uri_path);
        realPath = (TextView) findViewById(R.id.real_path);
        Add_Linear = (TextView) findViewById(R.id.add_linear);
        Add_Linear_Main = (LinearLayout) findViewById(R.id.add_linear_new);


        // Implement click listener over button
        findViewById(R.id.change_image).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        // Intent to gallery
                        Intent in = new Intent(Intent.ACTION_PICK);
                        in.setType("image/*");
                        startActivityForResult(in, select_photo);// start
                        // activity
                        // for
                        // result

                    }
                });
        Add_Linear.setOnClickListener(this);
    }

    protected void onActivityResult(int requestcode, int resultcode,
                                    Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        switch (requestcode) {
            case select_photo:
                if (resultcode == RESULT_OK) {
                    try {

                         imageuri = imagereturnintent.getData();// Get intent
                        // data

                        Log.i("IMAGE NAME", imageuri.toString());
                        uriPath.setText("URI Path: " + imageuri.toString());
                        // Get real path and show over text view
                        String real_Path = getRealPathFromUri(Gallery_Image_Activity.this, imageuri);
                        realPath.setText("Real Path: " + real_Path);

                        uriPath.setVisibility(View.VISIBLE);
                        realPath.setVisibility(View.VISIBLE);

                         bitmap = decodeUri(Gallery_Image_Activity.this, imageuri, 300);// call
                        // deocde
                        // uri
                        // method
                        // Check if bitmap is not null then set image else show
                        // toast
                        if (bitmap != null)
                            gallery_image.setImageBitmap(bitmap);// Set image over
                            /*if (Click_event.equals("1")) {
                                gallery_image.setImageBitmap(bitmap);// Set image over
                            } else if (Click_event.equals("2")) {
                                gallery_image_New.setImageBitmap(bitmap);// Set image over
                            }
*/
                            // bitmap

                            else
                                Toast.makeText(Gallery_Image_Activity.this, "Error while decoding image.", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {

                        e.printStackTrace();
                        Toast.makeText(Gallery_Image_Activity.this, "File not found.", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    // Method that deocde uri into bitmap. This method is necessary to deocde
    // large size images to load over imageview
    public static Bitmap decodeUri(Context context, Uri uri,
                                   final int requiredSize) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o2);
    }


    // Get Original image path
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onClick(View v) {

        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.add_new_linear, null);
        final TextView Change_Image_New = (TextView) addView.findViewById(R.id.change_image_new);
        gallery_image_New = (ImageView) addView.findViewById(R.id.gallery_imageview_new);
        System.out.println("imageuri.toString()"+imageuri.toString());
        gallery_image_New.setImageBitmap(bitmap);
        System.out.println("bitmap***"+bitmap);
        Change_Image_New.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout)addView.getParent()).removeView(addView);
               /* Click_event = "2";
                Intent in_new = new Intent(Intent.ACTION_PICK);
                in_new.setType("image");
                startActivityForResult(in_new, select_photo);// start
                // activity*/
            }
        });
        Add_Linear_Main.addView(addView);

    }


}
