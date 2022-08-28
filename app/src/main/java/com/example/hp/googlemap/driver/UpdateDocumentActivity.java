package com.example.hp.googlemap.driver;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UpdateDocumentActivity extends AppCompatActivity
{
    private ImageView user, car, book;

    private EditText lmm, ldd, lyy, rmm, rdd, ryy, imm, idd, iyy;

    private TextView tv_licensetext, tv_registrationtext, tv_insurancetext;

    private ImageView cam1, cam2, cam3;

    private String str_user = "", str_car = "", str_book = "";

    private Button btn_documentdetails;

    String documentdetails = "http://45.55.64.233/Miles/driver/get_driver_documents.php";
    private static final String TAG = ProfileActivity.class.getSimpleName();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String imageBasePath = "http://45.55.64.233/Miles/driver/";
    String document = "http://45.55.64.233/Miles/driver/update_driver_documents.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_document);
        sharedPreferences = getApplicationContext().getSharedPreferences("driver_pref", 0);
        editor = sharedPreferences.edit();

        cam1 = findViewById(R.id.cam1);
        cam2 = findViewById(R.id.cam2);
        cam3 = findViewById(R.id.cam3);

        user = findViewById(R.id.user);
        car = findViewById(R.id.car);
        book = findViewById(R.id.book);

        lmm = findViewById(R.id.lmm);
        ldd = findViewById(R.id.ldd);
        lyy = findViewById(R.id.lyy);
        rmm = findViewById(R.id.rmm);
        rdd = findViewById(R.id.rdd);
        ryy = findViewById(R.id.ryy);
        imm = findViewById(R.id.imm);
        idd = findViewById(R.id.idd);
        iyy = findViewById(R.id.iyy);

        tv_licensetext = findViewById(R.id.tv_licensetext);
        tv_registrationtext = findViewById(R.id.tv_registrationtext);
        tv_insurancetext = findViewById(R.id.tv_insurancetext);

        btn_documentdetails = findViewById(R.id.btn_documentdetails);

        getProfileDetails();


        /////////////////////////////////


        cam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {

                    int x = ContextCompat.checkSelfPermission(UpdateDocumentActivity.this, Manifest.permission.CAMERA);

                    if (x != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(UpdateDocumentActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                    } else {
                        openCamera(100);
                    }

                } else {
                    openCamera(100);
                }

            }

        });

        cam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {

                    int x = ContextCompat.checkSelfPermission(UpdateDocumentActivity.this, Manifest.permission.CAMERA);

                    if (x != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(UpdateDocumentActivity.this, new String[]{Manifest.permission.CAMERA}, 200);
                    } else {
                        openCamera(200);
                    }

                } else {
                    openCamera(200);
                }

            }

        });

        cam3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {

                    int x = ContextCompat.checkSelfPermission(UpdateDocumentActivity.this, Manifest.permission.CAMERA);

                    if (x != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(UpdateDocumentActivity.this, new String[]{Manifest.permission.CAMERA}, 300);
                    } else {
                        openCamera(300);
                    }

                } else {
                    openCamera(300);
                }

            }

        });

        btn_documentdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, document, new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("DebosmitDada", "onResponse: " + response);
                        JSONObject resObj = null;
                        int status = 400;
                        String msg = "";
                        try {
                            resObj = new JSONObject(response);
                            status = resObj.getInt("status");
                            msg = resObj.getString("message");
                        } catch (JSONException e) {
//                            e.printStackTrace();
                            Log.i(TAG, "onResponse: " + e);
                        }
                        if (status == 200) {
                            Toast.makeText(UpdateDocumentActivity.this, msg, Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        } else {
                            // progressDialog.hide();
                            Toast.makeText(UpdateDocumentActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap <>();
                        params.put("driver_id", sharedPreferences.getString("driver_id", ""));
                        params.put("license_expire", lyy.getText().toString() + "/" + lmm.getText().toString() + "/" + ldd.getText().toString());
                        params.put("registration_expire", ryy.getText().toString() + "/" + rmm.getText().toString() + "/" + rdd.getText().toString());
                        params.put("insurance_expire", iyy.getText().toString() + "/" + imm.getText().toString() + "/" + idd.getText().toString());

                        params.put("licence_image", str_user);
                        params.put("registration_image", str_car);
                        params.put("insurance_image", str_book);
                        Log.d("DocumentParams", "getParams: " + params);
                        //System.out.println(imageString);

                        return params;
                    }

                };

                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
            }
        });



   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                user.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            user.setImageBitmap(photo);
        }
*/
        ////////////////////////////////////////////////////////////////////////////////////////////


    /*  if (requestCode == IMG_REQUESTTT && resultCode == RESULT_OK && data != null) {
          Uri path = data.getData();
          try {
              bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
              car.setImageBitmap(bitmap);

          } catch (IOException e) {
              e.printStackTrace();
          }
      }
      if (requestCode == CAMERA_REQUESTTWO && resultCode == Activity.RESULT_OK) {
          Bitmap photo = (Bitmap) data.getExtras().get("data");
          car.setImageBitmap(photo);
      }*/
        //}
    }

    private void openCamera(int activityREQUEST_CODE) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, activityREQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (100 == requestCode) {
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                user.setImageBitmap(bmp);
                str_user = convertBitmapToBase64(bmp);
                System.out.println("str_user:" + str_user);
            }
        }
        if (200 == requestCode) {
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                car.setImageBitmap(bmp);
                str_car = convertBitmapToBase64(bmp);
                System.out.println("str_car:" + str_car);
            }
        }
        if (300 == requestCode) {
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                book.setImageBitmap(bmp);

                str_book = convertBitmapToBase64(bmp);
                System.out.println("str_book:" + str_car);
            }
        }
    }

    private String convertBitmapToBase64(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (100 == requestCode) {

            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera(100);
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (200 == requestCode) {

            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera(200);
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (300 == requestCode) {

            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera(300);
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void textChange() {
        lmm.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                System.out.println(s + " " + start + " " + before + " " + count);
                if (s.length() == 2) {
                    ldd.requestFocus();
                }

            }
        });

        ldd.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 2) {
                    lyy.requestFocus();
                }

            }
        });
    }
  /* public void textChange() {
        rmm.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                System.out.println(s+" "+start+" "+before+" "+count);
                if(s.length()==2)
                {
                    rdd.requestFocus();
                }

            }
        });

        rdd.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length()==2)
                {
                    ryy.requestFocus();
                }

            }
        });
    }*/


/////////////////////

    private void getProfileDetails() {
        // Set Image ...................
        StringRequest profile_details_request = new StringRequest(Request.Method.POST, documentdetails, new Response.Listener <String>() {

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                JSONObject resObj = null;
                int status = 400;
                // String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    //msg = resObj.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }
                if (status == 200) {
                    try {
                        JSONObject jsonRootObject = new JSONObject(response);
                        JSONObject jsonObject = jsonRootObject.getJSONObject("documents_dtl");


                        String licence_image = jsonObject.optString("licence_image");
                        String registration_image = jsonObject.optString("registration_image");
                        String insurance_image = jsonObject.optString("insurance_image");
                        Picasso.with(UpdateDocumentActivity.this).load(imageBasePath + licence_image).resize(100, 100).into(user);
                        Picasso.with(UpdateDocumentActivity.this).load(imageBasePath + registration_image).resize(100, 100).into(car);
                        Picasso.with(UpdateDocumentActivity.this).load(imageBasePath + insurance_image).resize(100, 100).into(book);

                        tv_licensetext.setText(jsonObject.optString("license_stat_statement"));
                        tv_registrationtext.setText(jsonObject.optString("registration_stat_statement"));
                        tv_insurancetext.setText(jsonObject.optString("insurance_stat_statement"));


                        String license_expire = jsonObject.optString("license_expire");
                        String registration_expire = jsonObject.optString("registration_expire");
                        String insurance_expire = jsonObject.optString("insurance_expire");

                        String license_expire_arr[] = license_expire.split("/");
                        String registration_expire_arr[] = registration_expire.split("/");
                        String insurance_expire_arr[] = insurance_expire.split("/");

                        lyy.setText(license_expire_arr[2]);
                        lmm.setText(license_expire_arr[0]);
                        ldd.setText(license_expire_arr[1]);

                        ryy.setText(registration_expire_arr[2]);
                        rmm.setText(registration_expire_arr[0]);
                        rdd.setText(registration_expire_arr[1]);

                        iyy.setText(insurance_expire_arr[2]);
                        imm.setText(insurance_expire_arr[0]);
                        idd.setText(insurance_expire_arr[1]);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ", "Error" + error);
            }
        }) {
            @Override
            protected Map <String, String> getParams() throws AuthFailureError {
                Map <String, String> params = new HashMap <>();
                params.put("driver_id", sharedPreferences.getString("driver_id", ""));
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(profile_details_request);
    }

    public void onButtonClickback(View view) {
        Intent intent = new Intent(UpdateDocumentActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
