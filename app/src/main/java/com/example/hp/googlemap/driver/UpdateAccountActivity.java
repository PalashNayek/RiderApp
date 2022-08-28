package com.example.hp.googlemap.driver;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateAccountActivity extends AppCompatActivity
{
    public String picturePath = "";
    private final int COMPRESS = 100;
    int pos;
    int modelpos;
    String car_type_id, car_type_name;

    private CircleImageView civ_profileimage, civ_carimage;

    private EditText et_address, et_state, et_zipcode, et_apt, et_city,
            et_licenseplate, et_routingno, et_accountno;

    private Button btn_profiledetails;

    private TextView et_fname, et_lname, et_email, et_mobileno, tv_selectcity;

    private Spinner spn_year, spn_carmake, spn_carmodel, spn_cartier;
    String selectedYear, selectdailypay;
    int PICK_IMAGE_REQUEST = 111;
    Bitmap bitmap;

    Button mOrder;
    TextView mItemSelected;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList <>();

    String make_send_id;
    String model_id;

    String makers_name;
    String cid;
    String cmake;
    String modelname;
    int car_pos;
    String str_profleimg = "";
    String str_carimg = "";
    String driver_id;

    String accoutinformation = "http://45.55.64.233/Miles/driver/get_driver_profile.php";
    String updateaccountinformation = "http://45.55.64.233/Miles/driver/update_driver_profile.php";

    private static final String TAG = ProfileActivity.class.getSimpleName();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String imagePath = "";
    String driverImagePath = "";
    String imageBasePath = "http://45.55.64.233/Miles/driver/";
    String url = "http://45.55.64.233/Miles/driver/get_car_make.php";
    String model = "http://45.55.64.233/Miles/driver/get_car_model.php";
    ArrayList <spn_getter_setter> arrayList = new ArrayList <>();
    ArrayList <SpinSetGet> stringArrayList = new ArrayList <>();
    ArrayList <String> year = new ArrayList <>();

    spn_getter_setter getterSetter1;
    spn_getter_setter getterSetter;
    private String car_year;

    private boolean isProfileImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        sharedPreferences = getApplicationContext().getSharedPreferences("driver_pref", 0);
        editor = sharedPreferences.edit();

        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
        if (!marshMallowPermission.checkPermissionForCamera())
            marshMallowPermission.requestPermissionForCamera();

        ///// All id define
        spn_year = findViewById(R.id.spn_year);
        spn_carmake = findViewById(R.id.spn_carmake);
        spn_carmodel = findViewById(R.id.spn_carmodel);
        spn_cartier = findViewById(R.id.spn_cartier);

        civ_profileimage = findViewById(R.id.civ_profileimage);
        civ_carimage = findViewById(R.id.civ_carimage);

        btn_profiledetails = findViewById(R.id.btn_profiledetails);

        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_email = findViewById(R.id.et_email);
        et_address = findViewById(R.id.et_address);
        et_apt = findViewById(R.id.et_apt);
        et_city = findViewById(R.id.et_city);
        et_zipcode = findViewById(R.id.et_zipcode);
        et_state = findViewById(R.id.et_state);
        et_mobileno = findViewById(R.id.et_mobileno);
        et_licenseplate = findViewById(R.id.et_licenseplate);
        //et_bankname = findViewById(R.id.et_bankname);
        et_routingno = findViewById(R.id.et_routingno);
        et_accountno = findViewById(R.id.et_accountno);
        // et_verifyno=findViewById(R.id.et_verifyno);
        tv_selectcity = findViewById(R.id.tv_selectcity);

        mOrder = (Button) findViewById(R.id.btnOrder);
        mItemSelected = (TextView) findViewById(R.id.tv_selectcity);

        listItems = getResources().getStringArray(R.array.select_item);
        checkedItems = new boolean[listItems.length];

        civ_profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProfileImage = true;
                selectPicture(UpdateAccountActivity.this);
            }
        });
        civ_carimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProfileImage = false;
                selectPicture(UpdateAccountActivity.this);
            }
        });

        getProfileDetails();
        //getcaryear();
        getcarmake();


        //// array order work list list item

        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(UpdateAccountActivity.this);
                mBuilder.setTitle("");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//                        if (isChecked) {
//                            if (!mUserItems.contains(position)) {
//                                mUserItems.add(position);
//                            }
//                        } else if (mUserItems.contains(position)) {
//                            mUserItems.remove(position);
//                        }
                        if (isChecked) {
                            mUserItems.add(position);
                        } else {
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        mItemSelected.setText(item);
                    }
                });

                mBuilder.setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                            mItemSelected.setText("");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


        /// Spinner item of year
        year.add("Select Car Year");
        for (int k = 2000; k <= 2018; k++) {
            year.add(String.valueOf(k));
            ArrayAdapter<String> adapter = new ArrayAdapter <String>(UpdateAccountActivity.this, android.R.layout.simple_spinner_item, year);
            spn_year.setAdapter(adapter);


        }
        /*for (int i = 0; i < year.size(); i++) {
            if (car_year.equalsIgnoreCase(year.get(i))) {
                spn_year.setSelection(i);
                break;
            }
        }*/

        spn_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {
                selectedYear = spn_year.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), "" + selectedYear, Toast.LENGTH_SHORT).show();
                // Toast.makeText(YourVehicleActivity.this, "" + selectedYear, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {

            }
        });


        btn_profiledetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(YourVehicleActivity.this, IdentityVerificationActivity.class);
                //startActivity(intent);


                driver_id = sharedPreferences.getString("driver_id", "");

                //Toast.makeText(getApplicationContext(), ""+driver_id + " " +selectedYear + " " +make_send_id + " " +model_id +  " " +licence_plate + " "  +chk_val, Toast.LENGTH_LONG).show();


                StringRequest addresss_request = new StringRequest(Request.Method.POST, updateaccountinformation, new Response.Listener <String>() {
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
                            Intent intent1 = new Intent(UpdateAccountActivity.this, ProfileActivity.class);
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
                        params.put("driver_image", str_profleimg);
                        params.put("first_name", et_fname.getText().toString());
                        params.put("last_name", et_lname.getText().toString());
                        params.put("driver_address", et_address.getText().toString());
                        params.put("driver_apartment", et_apt.getText().toString());
                        params.put("driver_city", et_city.getText().toString());
                        params.put("driver_state", et_state.getText().toString());
                        params.put("driver_zip_code", et_zipcode.getText().toString());
                        params.put("work_city", tv_selectcity.getText().toString());
                        params.put("car_image", str_carimg);
                        params.put("car_year", car_year);
                        params.put("car_make_id", make_send_id);
                        params.put("car_model_id", model_id);
                        params.put("car_type_id", car_type_id);
                        params.put("car_number", et_licenseplate.getText().toString());
                        params.put("routing", et_routingno.getText().toString());
                        params.put("driver_account_number", et_accountno.getText().toString());
                        Log.i("", "getParams: " + params);
                        return params;
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(addresss_request);

            }
        });

    }

    /// update activity to profile activity
    public void onButtonClickback(View view) {
        Intent intent = new Intent(UpdateAccountActivity.this, ProfileActivity.class);
        startActivity(intent);
    }


    private void getProfileDetails() {

        ////set all account information
        StringRequest profile_details_request = new StringRequest(Request.Method.POST, accoutinformation, new Response.Listener <String>() {

            @Override
            public void onResponse(String response) {
                Log.i("profileonres", "Response: " + response);
                JSONObject resObj = null;
                int status = 400;
                String msg = "";
                try {
                    resObj = new JSONObject(response);
                    status = resObj.getInt("status");
                    msg = resObj.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }
                if (status == 200) {
                    try {
                        imagePath = imageBasePath + resObj.getString("car_image");
                        driverImagePath = imageBasePath + resObj.getString("driver_image");
                        Picasso.with(UpdateAccountActivity.this).load(imagePath).resize(100, 100).into(civ_carimage);
                        Picasso.with(UpdateAccountActivity.this).load(driverImagePath).resize(100, 100).into(civ_profileimage);
                        //Log.d("ImgPath", "onResponse: "+imagePath);
                        cid = resObj.getJSONObject("car_make").getString("car_make_id");
                        cmake = resObj.getJSONObject("car_make").getString("car_make");
                        modelname = resObj.getJSONObject("car_model").getString("car_model_name");
                        car_type_id = resObj.getJSONObject("car_type").getString("car_type_id");
                        car_type_name = resObj.getJSONObject("car_type").getString("car_type_name");
                        Log.d("CMake", "onResponse: " + cmake);
                        Log.d("Ctype", "onResponse: " + car_type_id + car_type_name);

                        make_send_id = cid;//default make send id

                        car_year = resObj.getString("car_year");
                        setCarYear(car_year);
                        setCarTier(car_type_id, car_type_name);
                        Log.i(TAG, "year " + car_year);
                        et_fname.setText(resObj.getString("first_name"));
                        et_lname.setText(resObj.getString("last_name"));
                        et_email.setText(resObj.getString("driver_email"));
                        et_address.setText(resObj.getString("driver_address"));
                        et_apt.setText(resObj.getString("driver_apartment"));
                        et_city.setText(resObj.getString("driver_city"));
                        et_state.setText(resObj.getString("driver_state"));
                        et_zipcode.setText(resObj.getString("driver_zip_code"));
                        et_mobileno.setText(resObj.getString("driver_phone"));
                        tv_selectcity.setText(resObj.getString("work_city"));
                        et_licenseplate.setText(resObj.getString("car_number"));
                        //et_bankname.setText(resObj.getString("driver_bank_name"));
                        et_routingno.setText(resObj.getString("routing"));
                        et_accountno.setText(resObj.getString("driver_account_number"));
                        et_mobileno.setText(resObj.getString("driver_phone"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private void setCarYear(String car_year) {

        for (int i = 0; i < year.size(); i++) {
            if (car_year.equalsIgnoreCase(year.get(i))) {
                spn_year.setSelection(i);
                break;
            }
        }
    }


    ///// set all car make name and get the item from spinner
    public void getcarmake() {
        final StringRequest req = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {

                       /* Log.i("res_i_get", response);
                        JSONObject resObj = null;
                        int status = 400;
                        String msg = "";
                        try {
                            resObj = new JSONObject(response);
                            status = resObj.getInt("status");
                            msg = resObj.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(TAG, "onResponse: " + e);
                        }
                        if (status == 200) {
*/
                        try {
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                JSONArray jsonArray = obj.getJSONArray("details");
                                Log.i("res_i_get_array", String.valueOf(jsonArray));
                                getterSetter1 = new spn_getter_setter();
                                getterSetter1.setMake_id("");
                                getterSetter1.setMake_name("Select Car Make");
                                arrayList.add(getterSetter1);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject car_details = jsonArray.getJSONObject(i);
                                    String make_id = car_details.getString("make_id");
                                    String make_name = car_details.getString("make_name");

                                    getterSetter = new spn_getter_setter();
                                    getterSetter.setMake_id(make_id);
                                    getterSetter.setMake_name(make_name);
                                    arrayList.add(getterSetter);
                                    //Log.d("list", arrayList.toString());
                                }
                                if (arrayList.size() > 0) {
                                    spn_carmake.setAdapter(new customAdapter(getApplicationContext(), arrayList));
                                    //spn_carmake.setSelection(0);
                                    //  findPosition(arrayList);
                                    //Log.i("chking_val", String.valueOf(arrayList.indexOf("Volvo")));
//                                spn_carmake.setSelection(arrayList.indexOf("Volvo"));
                                    for (int i = 0; i < arrayList.size(); i++) {

                                        Log.i("foundit", arrayList.get(i).getMake_name());
                                        if (arrayList.get(i).getMake_name().equals(cmake)) {
                                            pos = i;
                                            //spn_carmake.setSelection(i);
                                            break;
                                        }

                                        //Log.i("foundit","not avl");
                                    }
                                    Log.i("list_pos", String.valueOf(pos));
                                    spn_carmake.setSelection(pos);

                                }

                            }
                            spn_carmake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView <?> adapterView, View view, final int legpos, long l) {
                                    make_send_id = arrayList.get(spn_carmake.getSelectedItemPosition()).getMake_id();
                                    makers_name = arrayList.get(spn_carmake.getSelectedItemPosition()).getMake_name();
                                    getCardetails(make_send_id);
                                }

                                @Override
                                public void onNothingSelected(AdapterView <?> adapterView) {

                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //                      }
                    }
                }, new com.android.volley.Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(

                getApplicationContext()).

                add(req);
    }

    /*private int findPosition(ArrayList<spn_getter_setter> arrayList) {
        for(int i=0;i<arrayList.size();i++)
        {
            if(arrayList.get(i).getMake_id().equals())
            return
        }
        return 0;
    }*/


    //// set the item car model in spinner and get the item car model
    private void getCardetails(final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, model, new com.android.volley.Response.Listener <String>() {
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
                        spn_carmodel.setAdapter(new CustomAdapterSpinner(getApplicationContext(), stringArrayList));
                        for (int i = 0; i < stringArrayList.size(); i++) {

                            Log.i("foundit", stringArrayList.get(i).getModel_name());
                            if (stringArrayList.get(i).getModel_name().equals(modelname)) {
                                modelpos = i;
                                //spn_carmake.setSelection(i);
                                break;
                            }

                            //Log.i("foundit","not avl");
                        }
                        Log.i("list_pos", String.valueOf(modelpos));
                        spn_carmodel.setSelection(modelpos);
                    }

                    spn_carmodel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {

                            model_id = stringArrayList.get(spn_carmodel.getSelectedItemPosition()).getModel_id();


                        }

                        @Override
                        public void onNothingSelected(AdapterView <?> adapterView) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map <String, String> getParams() throws AuthFailureError {
                HashMap <String, String> params = new HashMap <>();
//                params.put("make_id", id);
                params.put("make_id", make_send_id);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    private void setCarTier(String car_type_id, String car_type_name) {

        ArrayList <SpinSetGet> spinSetGets = new ArrayList <>();
        SpinSetGet spinSetGet = new SpinSetGet();
        spinSetGet.setModel_id(car_type_id);
        spinSetGet.setModel_name(car_type_name);
        spinSetGets.add(spinSetGet);

        spn_cartier.setAdapter(new CustomAdapterSpinner(UpdateAccountActivity.this, spinSetGets));

    }

    public void selectPicture(Activity mActivity) {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  dialog.setCancelable(false);
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
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (200 == requestCode) { // cEMERA
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                if (isProfileImage) {
                    civ_profileimage.setImageBitmap(bmp);
                    str_profleimg = convertBitmapToBase64(bmp);
                } else {
                    civ_carimage.setImageBitmap(bmp);
                    str_carimg = convertBitmapToBase64(bmp);
                }
                System.out.println("str_img:" + str_profleimg);
            }
        } else if (requestCode == 2572) { // aLBUM

            if (resultCode == RESULT_OK) {
                System.out.println(" Abcd ");
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
                            if (isProfileImage) {
                                civ_profileimage.setImageBitmap(bitmap);
                                str_profleimg = convertBitmapToBase64(bitmap);
                            } else {
                                civ_carimage.setImageBitmap(bitmap);
                                str_carimg = convertBitmapToBase64(bitmap);
                            }
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
