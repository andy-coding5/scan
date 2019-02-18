package com.E2Execel.scanner;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.E2Execel.scanner.Pojo.result_details.Data;
import com.E2Execel.scanner.Pojo.result_details.Result;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.E2Execel.scanner.LoginActivity.Build_alert_dialog;

public class Information extends AppCompatActivity {

    private TextView t_v_srno, t_v_name, t_v_mobile, t_v_aadhar, t_v_address1, t_v_address2, t_v_zip, t_v_village, t_v_city, t_v_district, t_v_state;
    private ImageView imageview_photo;
    //if size of pv module(received from search result activity is 1 or more, then this activity will call pv module list otherwise pv module called)

    String[] app_permission = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    MultipartBody.Part image_file_to_upload;

    Uri camUri;
    String imageFilePath;

    private int GALLERY = 1, CAMERA = 2;

    private ProgressDialog progressDialog;

    private ApiService api;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private final static int ALL_PERMISSIONS_RESULT = 1240;
    private static int IMAGE_SET = 0;
    private int success_all_permission = 0;
    int size = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        TextView t = view.findViewById(R.id.title);
        t.setText("Information");       //title of the screen ... in custom action bar

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.action_bar_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        imageview_photo = findViewById(R.id.main_image);
        t_v_srno = findViewById(R.id.srno);
        t_v_name = findViewById(R.id.name);
        t_v_mobile = findViewById(R.id.mobile);
        t_v_aadhar = findViewById(R.id.aadhar);
        t_v_address1 = findViewById(R.id.address_line_1);
        t_v_address2 = findViewById(R.id.address_line_2);
        t_v_zip = findViewById(R.id.zip);
        t_v_village = findViewById(R.id.village);
        t_v_city = findViewById(R.id.city);
        t_v_district = findViewById(R.id.district);
        t_v_state = findViewById(R.id.state);


        api = RetroClient.getApiService();
        pref = getSharedPreferences("SCANNER_PREF", MODE_PRIVATE);
        editor = pref.edit();


        // Set up progress before call
        progressDialog = new ProgressDialog(Information.this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        update_token();

        Call<Result> call = api.getResultsJson(globalValues.APIKEY, "Token " + pref.getString("token", null), globalValues.getID());
        progressDialog.show();

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {

                    Data data = response.body().getData();

                    /*set some values in global class, required in nest 3 activities
                     *
                     */
                    //getting the size of pv module lst
                    size = response.body().getData().getPvmodule().size();
                    globalValues.setPvmodule(data.getPvmodule());

                    globalValues.setID(data.getId().toString());

                    globalValues.setPumpsrno(data.getPumpsrno());
                    globalValues.setPumpimage(data.getPumpimage());
                    globalValues.setControllersrno(data.getControllersrno());
                    globalValues.setControllerimage(data.getControllerimage());
                    globalValues.setHpmotorsrno(data.getHpmotorsrno());
                    globalValues.setHpmotorimage(data.getHpmotorimage());
                    globalValues.setInstallationstatus(data.getInstallationstatus());
                    globalValues.setInstallationimage(data.getInstallationimage());


                    Glide.with(Information.this).load(globalValues.IP + data.getPhoto()).into(imageview_photo);
                    t_v_srno.setText(data.getSrno());
                    t_v_name.setText(data.getName());
                    t_v_mobile.setText(data.getMobile());
                    t_v_aadhar.setText(data.getAadhar());
                    t_v_address1.setText(data.getAddressLine1());
                    t_v_address2.setText(data.getAddressLine2());
                    t_v_village.setText(data.getVillage());
                    t_v_city.setText(data.getCity());
                    t_v_zip.setText(data.getZipcode());
                    t_v_district.setText(data.getDistrict());
                    t_v_state.setText(data.getState());


                } else {
                    update_token();

                    Toast.makeText(Information.this, "response not received", Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        /* String status = jObjError.getString("detail");
                         */
                        Toast.makeText(getApplicationContext(), jObjError.toString(), Toast.LENGTH_LONG).show();

                        //Build_alert_dialog(getApplicationContext(), "Error", status);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Information.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });


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
                    //Toast.makeText(Information.this, "new token: " + "token " + response.body().getData().getToken(), Toast.LENGTH_SHORT).show();
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
                Build_alert_dialog(Information.this, "Connection Error", "Please Check You Internet Connection");
            }
        });


    }

    public void add_details(View view) {
        //update user photo if edited;
        if (IMAGE_SET == 1) {
            //CALL
            Call<UpdateDetails> call = api.uploadUserPhoto(globalValues.APIKEY, "Token " + pref.getString("token", null),
                    image_file_to_upload, RequestBody.create(MediaType.parse("text/plain"), "Android"), globalValues.getID());

            progressDialog.show();


            call.enqueue(new Callback<UpdateDetails>() {
                @Override
                public void onResponse(Call<UpdateDetails> call, Response<UpdateDetails> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("Success")) {
                            Toast.makeText(Information.this, "successfully uploaded", Toast.LENGTH_SHORT).show();

                            IMAGE_SET = 0;
                            if (size == 0) {
                                Intent i = new Intent(Information.this, Pvmodules.class);

                                startActivity(i);
                            } else {
                                Intent i = new Intent(Information.this, PvModulesList.class);
                                i.putExtra("pvmodulelist", (Serializable) globalValues.getPvmodule());

                                startActivity(i);
                            }
                        }

                    } else {

                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String status = jObjError.getString("message");
                            String error_msg = jObjError.getJSONObject("data").getString("errors");
                            Build_alert_dialog(Information.this, status, error_msg);

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    Log.v("upload", "success");
                }

                @Override
                public void onFailure(Call<UpdateDetails> call, Throwable t) {

                    progressDialog.dismiss();
                    Build_alert_dialog(Information.this, "Connection Error", "Please Check You Internet Connection");

                }
            });

        } else {
            if (size == 0) {
                Intent i = new Intent(Information.this, Pvmodules.class);

                startActivity(i);
            } else {
                Intent i = new Intent(Information.this, PvModulesList.class);
                i.putExtra("pvmodulelist", (Serializable) globalValues.getPvmodule());

                startActivity(i);
            }
        }


    }

    private boolean checkAndRequestPermission() {
        List<String> listPermissionNeeded = new ArrayList<>();
        for (String perm : app_permission) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                //ask to grant the permission
                listPermissionNeeded.add(perm);

            }

        }

        if (!listPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), ALL_PERMISSIONS_RESULT);
            return false;
        }
        success_all_permission = 1;
        return true;
    }

    public void edit_photo(View view) {
        //camera module or galley module
        showPictureDialog();

    }

    private void showPictureDialog() {
        if (checkAndRequestPermission()) {
        }

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
                                if (!checkAndRequestPermission()) {
                                    Build_alert_dialog(Information.this, "Permission request", "Please Allow to access ");
                                } else {
                                    choosePhotoFromGallary();
                                }

                                break;
                            case 1:
                                if (!checkAndRequestPermission()) {
                                    Build_alert_dialog(Information.this, "Permission request", "Please Allow to access ");
                                } else {
                                    takePhotoFromCamera();
                                }

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
                        //Toast.makeText(Pvmodules.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                        imageview_photo.setImageBitmap(bitmap);

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

                        image_file_to_upload = MultipartBody.Part.createFormData("photo", f.getName(), RequestBody.create(MediaType.parse("image/*"), f));


                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(Information.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            if (requestCode == CAMERA) {
                //don't compare the data to null, it will always come as  null because we are providing a file URI, so load with the imageFilePath we obtained before opening the cameraIntent
                Glide.with(this).load(imageFilePath).into(imageview_photo);
                IMAGE_SET = 1;

                camUri = Uri.fromFile(new File(imageFilePath));


                File file = new File(camUri.getPath());


                image_file_to_upload = MultipartBody.Part.createFormData("photo", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));


            }

            // Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (IMAGE_SET == 1) {

        } else {
            Call<Result> call = api.getResultsJson(globalValues.APIKEY, "Token " + pref.getString("token", null), globalValues.getID());
            progressDialog.show();

            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {

                        Data data = response.body().getData();

                        /*set some values in global class, required in nest 3 activities
                         *
                         */
                        //getting the size of pv module lst
                        size = response.body().getData().getPvmodule().size();
                        globalValues.setPvmodule(data.getPvmodule());

                        globalValues.setID(data.getId().toString());

                        globalValues.setPumpsrno(data.getPumpsrno());
                        globalValues.setPumpimage(data.getPumpimage());
                        globalValues.setControllersrno(data.getControllersrno());
                        globalValues.setControllerimage(data.getControllerimage());
                        globalValues.setHpmotorsrno(data.getHpmotorsrno());
                        globalValues.setHpmotorimage(data.getHpmotorimage());
                        globalValues.setInstallationstatus(data.getInstallationstatus());
                        globalValues.setInstallationimage(data.getInstallationimage());


                        Glide.with(Information.this).load(globalValues.IP + data.getPhoto()).into(imageview_photo);
                        t_v_srno.setText(data.getSrno());
                        t_v_name.setText(data.getName());
                        t_v_mobile.setText(data.getMobile());
                        t_v_aadhar.setText(data.getAadhar());
                        t_v_address1.setText(data.getAddressLine1());
                        t_v_address2.setText(data.getAddressLine2());
                        t_v_village.setText(data.getVillage());
                        t_v_city.setText(data.getCity());
                        t_v_zip.setText(data.getZipcode());
                        t_v_district.setText(data.getDistrict());
                        t_v_state.setText(data.getState());


                    } else {
                        update_token();

                        //Toast.makeText(Information.this, "response not received", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            /* String status = jObjError.getString("detail");
                             */
                            Toast.makeText(getApplicationContext(), jObjError.toString(), Toast.LENGTH_LONG).show();

                            //Build_alert_dialog(getApplicationContext(), "Error", status);

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(Information.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}
