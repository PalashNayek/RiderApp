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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DocumentsActivity extends AppCompatActivity
{
    private DriverDto driverDto;
    String str_car,str_book,str_user;
    Button btn_document;
    ImageView user;
    ImageView car;
    ImageView book;
    ImageView cam1;
    ImageView cam2;
    ImageView iv_back4;
    ImageView cam3;
    EditText lmm, ldd, lyy, rmm, rdd, ryy, imm, idd, iyy;
    String document = "http://45.55.64.233/Miles/driver/drivers_documents.php";
    String identitydetails="http://45.55.64.233/Miles/driver/get_identity_verification_details.php";


    private static final String TAG = DocumentsActivity.class.getSimpleName();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //Bitmap bitmap;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_REQUESTTWO = 1888;
    private static final int CAMERA_REQUESTTHREE = 1888;
    int IMG_REQUEST = 1;
    int IMG_REQUESTTT = 1;
    Bitmap bitmap;
    String imageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        sharedPreferences = getApplicationContext().getSharedPreferences("driver_pref", 0);
        editor = sharedPreferences.edit();
        btn_document = (Button) findViewById(R.id.btn_document);
        cam1 = (ImageView) findViewById(R.id.cam1);
        cam2 = (ImageView) findViewById(R.id.cam2);
        cam3 = (ImageView) findViewById(R.id.cam3);
        user = (ImageView) findViewById(R.id.user);
        car = (ImageView) findViewById(R.id.car);
        book = (ImageView) findViewById(R.id.book);
        lmm = (EditText) findViewById(R.id.lmm);
        ldd = (EditText) findViewById(R.id.ldd);
        lyy = (EditText) findViewById(R.id.lyy);
        rmm = (EditText) findViewById(R.id.rmm);
        rdd = (EditText) findViewById(R.id.rdd);
        ryy = (EditText) findViewById(R.id.ryy);
        imm = (EditText) findViewById(R.id.imm);
        idd = (EditText) findViewById(R.id.idd);
        iyy = (EditText) findViewById(R.id.iyy);
        iv_back4 = (ImageView) findViewById(R.id.iv_back4);



        iv_back4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(DocumentsActivity.this, IdentityVerificationActivity.class);
                // startActivity(intent);
                onBackPressed();
            }
        });


        cam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {

                    int x = ContextCompat.checkSelfPermission(DocumentsActivity.this, Manifest.permission.CAMERA);

                    if (x != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions(DocumentsActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
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

                    int x = ContextCompat.checkSelfPermission(DocumentsActivity.this, Manifest.permission.CAMERA);

                    if (x != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions(DocumentsActivity.this, new String[]{Manifest.permission.CAMERA}, 200);
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

                    int x = ContextCompat.checkSelfPermission(DocumentsActivity.this, Manifest.permission.CAMERA);

                    if (x != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions(DocumentsActivity.this, new String[]{Manifest.permission.CAMERA}, 300);
                    } else {
                        openCamera(300);
                    }

                } else {
                    openCamera(300);
                }

            }

        });

        btn_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               /* if (view.equals(img2)) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmapTwo = ((BitmapDrawable) car.getDrawable()).getBitmap();
                    bitmapTwo.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] imageBytes = baos.toByteArray();
                    imageStringScend = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                }

                if (view.equals(img3)) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmapThree = ((BitmapDrawable) book.getDrawable()).getBitmap();
                    bitmapThree.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] imageBytes = baos.toByteArray();
                    imageStringThree = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                }*/

                StringRequest stringRequest = new StringRequest(Request.Method.POST, document, new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: " + response);
                        JSONObject resObj = null;
                        int status = 400;
                        String msg = "";
                        try {
                            driverDto = DriverMyApplication.gson.fromJson(response, DriverDto.class);
                            resObj = new JSONObject(response);
                            status = resObj.getInt("status");
                            msg = resObj.getString("message");
                        } catch (JSONException e) {
//                            e.printStackTrace();
                            Log.i(TAG, "onResponse: " + e);
                        }
                        if (status == 200) {
                            Toast.makeText(DocumentsActivity.this, msg, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DocumentsActivity.this, AlldoneActivity.class);
                            intent.putExtra("driverDto",driverDto);
                            startActivity(intent);

                        } else {
                            // progressDialog.hide();
                            Toast.makeText(DocumentsActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                        params.put("driver_licence_exp_dt", lyy.getText().toString() + "/" + lmm.getText().toString() + "/" + ldd.getText().toString());
                        params.put("vehicle_reg_exp_dt", ryy.getText().toString() + "/" + rmm.getText().toString() + "/" + rdd.getText().toString());
                        params.put("vehicle_insu_exp_dt", iyy.getText().toString() + "/" + imm.getText().toString() + "/" + idd.getText().toString());

                        params.put("driver_licence_image", str_user);
                        params.put("vehicle_reg_image", str_car);
                        params.put("vehicle_insu_image", str_book);
                        Log.d("DocumentParams", "getParams: " + params);
                        //System.out.println(imageString);

                        return params;
                    }
                };

                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
            }
        });

    }

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
                str_user=convertBitmapToBase64(bmp);
                System.out.println("str_user:"+str_user);
            }
        }
        if (200 == requestCode) {
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                car.setImageBitmap(bmp);
                str_car=convertBitmapToBase64(bmp);
                System.out.println("str_car:"+str_car);
            }
        }
        if (300 == requestCode) {
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                book.setImageBitmap(bmp);

                str_book=convertBitmapToBase64(bmp);
                System.out.println("str_book:"+str_car);
            }
        }
    }

    private String convertBitmapToBase64(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

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
                }

                else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (200 == requestCode) {

            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera(200);
                }

                else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (300 == requestCode) {

            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera(300);
                }

                else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void textChange() {
        lmm.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                System.out.println(s+" "+start+" "+before+" "+count);
                if(s.length()==2)
                {
                    ldd.requestFocus();
                }

            }
        });

        ldd.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length()==2)
                {
                    lyy.requestFocus();
                }

            }
        });

    }
}
