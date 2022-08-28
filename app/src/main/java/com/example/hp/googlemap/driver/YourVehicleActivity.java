package com.example.hp.googlemap.driver;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class YourVehicleActivity extends AppCompatActivity
{
    public String picturePath = "";
    private final int COMPRESS = 100;

    TextView tv_text2;
    Button btn_vechicle;
    CircleImageView circleImageView;
    EditText selectcar;
    EditText selectcarmake;
    EditText selectcarmodel;
    EditText licenseplate;
    CheckBox checkbox;
    String str_img;
    int PICK_IMAGE_REQUEST = 111;
    Bitmap bitmap;

    ImageView iv_back2;
    String url = "http://45.55.64.233/Miles/driver/get_car_make.php";
    String model = "http://45.55.64.233/Miles/driver/get_car_model.php";
    String register = "http://45.55.64.233/Miles/driver/vehicle_details.php";
    String showvehicledetails="http://45.55.64.233/Miles/driver/get_vehicle_details.php";

    private static final String TAG = YourVehicleActivity.class.getSimpleName();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Spinner SPN_CARMAKE;
    Spinner SPN_CARMODEL;
    // List<String> myList;

    String driver_id;
    //String car_make;

    String selectedYear;
    String make_send_id;
    String model_id;
    String licence_plate;
    String chk_val;
    String vel_img;
    String makers_name;


    ArrayList<spn_getter_setter> arrayList = new ArrayList <>();


    List<String> carmaker = new ArrayList <>();
    ArrayAdapter<String> carmaker_adapter;

    ArrayList <SpinSetGet> stringArrayList = new ArrayList <>();
    private Spinner Spn_Year;
    ArrayList <String> year = new ArrayList <>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_vehicle2);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        sharedPreferences = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreferences.edit();

        SPN_CARMAKE = (Spinner) findViewById(R.id.spn_car_make);
        SPN_CARMODEL = (Spinner) findViewById(R.id.spn_car_model);
        Spn_Year = (Spinner) findViewById(R.id.spn_year);
        chk_val = "N";

        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
        if (!marshMallowPermission.checkPermissionForCamera())
            marshMallowPermission.requestPermissionForCamera();

        year.add("Select Car Year");
        for (int k = 2000; k <= 2018; k++) {
            year.add(String.valueOf(k));
            ArrayAdapter <String> adapter = new ArrayAdapter <String>(YourVehicleActivity.this, android.R.layout.simple_spinner_item, year);
            Spn_Year.setAdapter(adapter);
        }

        Spn_Year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {
                selectedYear = Spn_Year.getSelectedItem().toString();


                // Toast.makeText(YourVehicleActivity.this, "" + selectedYear, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {

            }
        });


        getcarmake();

        iv_back2 = (ImageView) findViewById(R.id.iv_back2);
        iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YourVehicleActivity.this, AboutyourselfActivity.class);
                startActivity(intent);
            }
        });

       /* iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });*/


       /* sharedPreferences = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreferences.edit();*/

        tv_text2 = (TextView) findViewById(R.id.tv_text2);
        btn_vechicle = (Button) findViewById(R.id.btn_vechicle);
        circleImageView = (CircleImageView)     findViewById(R.id.circleImageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPicture(YourVehicleActivity.this);
            }
        });
        //   selectcar = (EditText) findViewById(R.id.selectcar);
        //  selectcarmake = (EditText) findViewById(R.id.selectcarmake);
        //  selectcarmodel = (EditText) findViewById(R.id.selectcarmodel);
        licenseplate = (EditText) findViewById(R.id.licenseplate);
        checkbox = (CheckBox) findViewById(R.id.checkbox);

       /* car_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);


                 SpannableString demo*/
        String underlineData = "To drive with MILES you must be at least 19 years of age and have a qualifying vehicle that meets the following requirements:";

        SpannableString content = new SpannableString(underlineData);
        content.setSpan(new UnderlineSpan(), 0, underlineData.length(), 0);
        // 0 specify start index and underlineData.length() specify end index of styling
        tv_text2.setText(content);
        // End of SpannableString demo


        // Paint.STRIKE_THRU_TEXT_FLAG ;0
        // Paint.SUBPIXEL_TEXT_FLAG ;

        // Html.fromHtml(htmlString)
        String htmlString = "To drive with MILES you must be at least <u>19 years of age</u> and have a qualifying vehicle that meets the following requirements:";
        tv_text2.setText(Html.fromHtml(htmlString));

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox.isChecked()) {
                    chk_val = "Y";
                }
                if (!checkbox.isChecked()) {
                    chk_val = "N";
                }
            }
        });


        btn_vechicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(YourVehicleActivity.this, IdentityVerificationActivity.class);
                //startActivity(intent);


                if (selectedYear.equalsIgnoreCase("Select Car Year")) {

                    //Toast.makeText(YourVehicleActivity.this, "Select Car Year", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (make_send_id.equalsIgnoreCase("")) {

                    //Toast.makeText(YourVehicleActivity.this, "Select Car Make", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (model_id.equalsIgnoreCase("")) {

                    // Toast.makeText(YourVehicleActivity.this, "Select Car Model", Toast.LENGTH_SHORT).show();
                    return;

                }


                driver_id = sharedPreferences.getString("driver_id", "");
                licence_plate = licenseplate.getText().toString();

                //Toast.makeText(getApplicationContext(), ""+driver_id + " " +selectedYear + " " +make_send_id + " " +model_id +  " " +licence_plate + " "  +chk_val, Toast.LENGTH_LONG).show();


                StringRequest addresss_request = new StringRequest(Request.Method.POST, register, new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: " + response);
                        JSONObject resObj = null;
                        int status = 400;
                        String msg = "";
                        try {
                            resObj = new JSONObject(response);
                            status = resObj.getInt("status");
                            msg = resObj.getString("message");
                        } catch (JSONException e) {

                            Log.i(TAG, "onResponse: " + e);
                        }

                        if (status == 200) {
                            //progressDialog.hide();
                            Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(getApplicationContext(), IdentityVerificationActivity.class);
                            startActivity(intent1);
                            finish();

                        } else {
                            //progressDialog.hide();
                            //display error msg here
                            Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error ", "Error" + error);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map <String, String> params = new HashMap<>();
                        params.put("driver_id", driver_id);
                        params.put("vehicle_year", selectedYear);
                        params.put("vehicle_make_id", make_send_id);
                        params.put("vehicle_model_id", model_id);
                        params.put("vehicle_licence_plate", licence_plate);
                        params.put("vehicle_agree_flag", chk_val);
                        params.put("vehicle_image", str_img);


                        Log.i("YourVehicleActivity", "getParams: " + params);
                        return params;
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(addresss_request);

            }
        });


              /*  btn_vechicle.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    }



            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri filePath = data.getData();

                    try {
                        //getting image from gallery
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                        //Setting image to ImageView
                        car_image.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });*/

    }



    public void getcarmake() {
        final StringRequest req = new StringRequest(Request.Method.POST, url,
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i("res_i_get", response);
                        try {
                            JSONObject obj = new JSONObject(String.valueOf(response));
                            JSONArray jsonArray = obj.getJSONArray("details");
                            Log.i("res_i_get_array", String.valueOf(jsonArray));
                            spn_getter_setter getterSetter1 = new spn_getter_setter();
                            getterSetter1.setMake_id("");
                            getterSetter1.setMake_name("Select Car Make");
                            arrayList.add(getterSetter1);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject car_details = jsonArray.getJSONObject(i);
                                /*car_details.getString("make_id");

                                stringArrayList.add(car_details.getString("make_name"));
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, stringArrayList);
                                SPN_CARMAKE.setAdapter(adapter);
                                Log.i("arr_list", String.valueOf(stringArrayList));*/

                                String make_id = car_details.getString("make_id");
                                String make_name = car_details.getString("make_name");

                                spn_getter_setter getterSetter = new spn_getter_setter();
                                getterSetter.setMake_id(make_id);
                                getterSetter.setMake_name(make_name);
                                arrayList.add(getterSetter);
                            }
                            if (arrayList.size() > 0) {
                                SPN_CARMAKE.setAdapter(new customAdapter(YourVehicleActivity.this, arrayList));
                            }


                            SPN_CARMAKE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView <?> adapterView, View view, final int legpos, long l) {
                                   /* String text = SPN_CARMAKE.getSelectedItem().toString();
                                   Toast.makeText(getApplicationContext(),""+text,Toast.LENGTH_SHORT).show();
                                   selectcar.setSelection(legpos);*/

                                    make_send_id = arrayList.get(SPN_CARMAKE.getSelectedItemPosition()).getMake_id();
                                    makers_name = arrayList.get(SPN_CARMAKE.getSelectedItemPosition()).getMake_name();
                                    // Toast.makeText(YourVehicleActivity.this, "" + make_send_id, Toast.LENGTH_SHORT).show();

                                    getCardetails(make_send_id);
                                }

                                @Override
                                public void onNothingSelected(AdapterView <?> adapterView) {

                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(req);
    }

    private void getCardetails(final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, model, new Response.Listener <String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.i("res", response);
                    stringArrayList = new ArrayList <>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    SpinSetGet spinSetGet1 = new SpinSetGet();
                    spinSetGet1.setModel_id("");
                    spinSetGet1.setModel_name("Select Car Model");
                    stringArrayList.add(spinSetGet1);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String model_id = object.getString("model_id");
                        String model_name = object.getString("model_name");

                        SpinSetGet spinSetGet = new SpinSetGet();
                        spinSetGet.setModel_id(model_id);
                        spinSetGet.setModel_name(model_name);
                        stringArrayList.add(spinSetGet);
                    }
                    if (stringArrayList.size() > 0) {
                        SPN_CARMODEL.setAdapter(new CustomAdapterSpinner(YourVehicleActivity.this, stringArrayList));
                    }

                    SPN_CARMODEL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {
                            model_id = stringArrayList.get(SPN_CARMODEL.getSelectedItemPosition()).getModel_id();
                        }

                        @Override
                        public void onNothingSelected(AdapterView <?> adapterView) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map <String, String> getParams() throws AuthFailureError {
                HashMap <String, String> params = new HashMap <>();
                params.put("make_id", id);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    public void selectPicture(Activity mActivity)
    {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       // dialog.setCancelable(false);
        dialog.setContentView(R.layout.picture_dialog);

        ImageView pic_camera = dialog.findViewById(R.id.pic_camera);
        ImageView pic_gallery = dialog.findViewById(R.id.pic_gallery);

        pic_camera.setOnClickListener(new View.OnClickListener() { // Request for CAMERA
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 200);
            }
        });

        pic_gallery.setOnClickListener(new View.OnClickListener() { // rEQUEST FOR aLBUM
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2572);
            }
        });
//        dialog.show();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });

    }

    private String convertBitmapToBase64(Bitmap bmp) { // bITMAP TO BASE 64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (200 == requestCode) { // cEMERA
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                circleImageView.setImageBitmap(bmp);
                str_img = convertBitmapToBase64(bmp);
                System.out.println("str_img:" + str_img);
            }
        } else if (requestCode == 2572) { // aLBUM

            if (resultCode == RESULT_OK)
            {  System.out.println(" Abcd " );
                if (data != null) {
                    if (Build.VERSION.SDK_INT < 19) {
//                        Uri selectedImage = data.getData();
//                        System.out.println("selectedImage " + selectedImage + " SDK " + Build.VERSION.SDK_INT);
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                        if (cursor != null) {
//                            cursor.moveToFirst();
//                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                            picturePath = cursor.getString(columnIndex);
//                            cursor.close();
//                        }
//                        picturePath = saveScaledBitMap(decodeFile(800, 800,picturePath),
//                                picturePath.substring(picturePath.indexOf("."), picturePath.length()));
                    } else {
                        try {
                            InputStream imInputStream = getContentResolver().openInputStream(data.getData());
                            Bitmap bitmap = BitmapFactory.decodeStream(imInputStream);
                            //str_img = convertBitmapToBase64(bitmap);

                            bitmap = getResizedBitmap(bitmap, 400);
                            circleImageView.setImageBitmap(bitmap);
                            str_img = convertBitmapToBase64(bitmap);
                            // picturePath = saveGalleryImageOnKitkat(bitmap);
                            System.out.println("Abcd3");

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        // finishAndSetResult(RESULT_OK, picturePath, false);
                    }
                }
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) { // bIT MAP reSIZE
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
