package com.E2Execel.scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.E2Execel.scanner.Pojo.login_details.Login;
import com.E2Execel.scanner.Pojo.update_details.UpdateDetails;
import com.E2Execel.scanner.Retrofit.ApiService;
import com.E2Execel.scanner.Retrofit.RetroClient;
import com.E2Execel.scanner.global.globalValues;
import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.E2Execel.scanner.LoginActivity.Build_alert_dialog;

public class Hpmotor extends AppCompatActivity {
    Activity activity;
    TextView srno_textview;
    private ImageView imageview;


    MultipartBody.Part image_file_to_upload;

    Uri camUri;
    String imageFilePath;

    private int GALLERY = 1, CAMERA = 2;


    private ProgressDialog progressDialog;

    private ApiService api;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static int IMAGE_SET = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hpmotor);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        TextView t = view.findViewById(R.id.title);
        t.setText("Hp Motor");       //title of the screen ... in custom action bar

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.action_bar_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activity = this;

        srno_textview = findViewById(R.id.input_srno);
        imageview = findViewById(R.id.iv);

        api = RetroClient.getApiService();
        pref = getSharedPreferences("SCANNER_PREF", MODE_PRIVATE);
        editor = pref.edit();

        // Set up progress before call
        progressDialog = new ProgressDialog(Hpmotor.this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        update_token();

        check_first();

    }

    public void update_token() {
        //pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //Toast.makeText(this, "email from pref: " + pref.getString("email", "not fetched from pref"), Toast.LENGTH_SHORT).show();
        ApiService api = RetroClient.getApiService();


        Call<Login> call = api.getLoginJason(pref.getString("email", null), pref.getString("password", null), "Android");

        progressDialog.show();

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    //Toast.makeText(Hpmotor.this, "new token: " + "token " + response.body().getData().getToken(), Toast.LENGTH_SHORT).show();
                    editor.putString("token", response.body().getData().getToken());
                    editor.commit();

                } else {
                    //but but i can access the error body here.,
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String status = jObjError.getString("message");
                        String error_msg = jObjError.getJSONObject("data").getString("errors");
                        Build_alert_dialog(getApplicationContext(), status, error_msg);

                    } catch (Exception e) {
                       // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                progressDialog.dismiss();
                Build_alert_dialog(Hpmotor.this, "Connection Error", "Please Check You Internet Connection");
            }
        });


    }

    private void check_first() {
        if (globalValues.getHpmotorsrno() != null) {
            srno_textview.setText(globalValues.getHpmotorsrno());
        }
        if (globalValues.getHpmotorimage() != null) {
            if ("".equals(globalValues.getHpmotorimage())) {
                imageview.setImageDrawable(getResources().getDrawable(R.drawable.upload_photo));
            } else {
                Glide.with(this).load("http://192.168.0.110:8000" + globalValues.getHpmotorimage()).into(imageview);
                IMAGE_SET = 1;
                Log.v("image_set", "http://192.168.0.110:800" + globalValues.getHpmotorimage());
            }

        } else {
            imageview.setImageDrawable(getResources().getDrawable(R.drawable.upload_photo));
        }

    }

    public void scan_code(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Scan a Code");

        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();
    }

    public void take_image(View view) {
        showPictureDialog();
    }

    private void showPictureDialog() {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {

        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.E2Execel.scanner.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, CAMERA);
            }
        }

    }

    private File createImageFile() throws IOException {

        String imageFileName = "IMG_" + System.currentTimeMillis() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 || requestCode == 2) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == this.RESULT_CANCELED) {
                return;
            }
            if (requestCode == GALLERY) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    //File file_glr = new File(contentURI.getPath());
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        //String path = saveImage(bitmap);
                        //  Toast.makeText(Pvmodules.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                        imageview.setImageBitmap(bitmap);

                        IMAGE_SET = 1;


                        //TAKE PROPER PATH OF FILE.
                        String[] filePath = {MediaStore.Images.Media.DATA};
                        Cursor c = getContentResolver().query(contentURI, filePath,
                                null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        String FilePathStr = c.getString(columnIndex);
                        c.close();

                        File f = new File(FilePathStr);

                        image_file_to_upload = MultipartBody.Part.createFormData("hpmotorimage", f.getName(), RequestBody.create(MediaType.parse("image/*"), f));


                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(Hpmotor.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            if (requestCode == CAMERA) {
                //don't compare the data to null, it will always come as  null because we are providing a file URI, so load with the imageFilePath we obtained before opening the cameraIntent
                Glide.with(this).load(imageFilePath).into(imageview);
                IMAGE_SET = 1;

                camUri = Uri.fromFile(new File(imageFilePath));


                File file = new File(camUri.getPath());


                image_file_to_upload = MultipartBody.Part.createFormData("hpmotorimage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));


            }

            // Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show();

        } else {      //if request code is not 1 or 2 then the intent result is of scanner intent
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                   // Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    srno_textview.setText(result.getContents());
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

    public void next_activity(View view) {


        //CALL
        Call<UpdateDetails> call = api.uploadHpmotorInfo(globalValues.APIKEY, "Token " + pref.getString("token", null),
                image_file_to_upload, RequestBody.create(MediaType.parse("text/plain"), srno_textview.getText().toString()), RequestBody.create(MediaType.parse("text/plain"), "Android"), globalValues.getID());

        progressDialog.show();


        call.enqueue(new Callback<UpdateDetails>() {
            @Override
            public void onResponse(Call<UpdateDetails> call, Response<UpdateDetails> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("Success")) {
                        Toast.makeText(Hpmotor.this, "successfully uploaded", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Hpmotor.this, Installation.class));

                    }

                } else {

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String status = jObjError.getString("message");
                        String error_msg = jObjError.getJSONObject("data").getString("errors");
                        Build_alert_dialog(Hpmotor.this, status, error_msg);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                Log.v("upload", "success");
            }

            @Override
            public void onFailure(Call<UpdateDetails> call, Throwable t) {

                progressDialog.dismiss();
                Build_alert_dialog(Hpmotor.this, "Connection Error", "Please Check You Internet Connection");

            }
        });

    }
}
