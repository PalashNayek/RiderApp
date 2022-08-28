package com.example.hp.googlemap.driver;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutyourselfActivity extends AppCompatActivity
{
    private static final String TAG = AboutyourselfActivity.class.getSimpleName();
    public String picturePath = "";
    private final int COMPRESS = 100;
    String str_img="";
    String selectcity;
    EditText fname, lname, address, apt, zcode, state, email, mnumber, pass, icode;
    Spinner spn_city;
    Button btn_about;
    TextView tv_text, city;
    ImageView iv_back1;
    String register = "http://45.55.64.233/Miles/driver/driver_registration.php";
    String showdriverdetails="http://45.55.64.233/Miles/driver/get_driver_details.php";
    CircleImageView circleImageView;// Image I clicked and set a pic


    int PICK_IMAGE_REQUEST = 111;
    Bitmap bitmap;
    SharedPreferences.Editor editor;
    private SharedPreferences sharedPreference;
    private String driver_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutyourself);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        spn_city=findViewById(R.id.spn_city);
        String []city_str={"Select City","Philadelphia"};
        System.out.print(city_str);
        ArrayAdapter<String> currency_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, city_str){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
//set the spinners adapter to the previously created one.
        spn_city.setAdapter(currency_adapter);

        spn_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {

                //city.setText(city_str[i]);

            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {

            }
        });


        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
        if (!marshMallowPermission.checkPermissionForCamera())
            marshMallowPermission.requestPermissionForCamera();

        sharedPreference = getApplicationContext().getSharedPreferences("driver_pref", 0);
        editor = sharedPreference.edit();
        initView();



        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPicture(AboutyourselfActivity.this);
            }
        });
        iv_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutyourselfActivity.this, DriverLogInPage.class);
                startActivity(intent);
            }
        });

       /* circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });
*/

        // SpannableString demo
        String underlineData = "By applying, I agree to MILES's Terms of Use and confirm that I have read and understand the Privacy Policy";

        SpannableString content = new SpannableString(underlineData);
        content.setSpan(new UnderlineSpan(), 0, underlineData.length(), 0);
        // 0 specify start index and underlineData.length() specify end index of styling
        tv_text.setText(content);
        String htmlString = "By applying, I agree to MILES's <u>Terms of Use</u> and confirm that I have read and understand the <u>Privacy Policy</u>";
        tv_text.setText(Html.fromHtml(htmlString));

        btn_about.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {

                /*  if (validate(email,pass)) {*/

                  /*  fname.getText().toString();
                   lname.getText().toString();
                    address.getText().toString();
                    apt.getText().toString();
                    zcode.getText().toString();
                    city.getText().toString();
                    state.getText().toString();
                    email.getText().toString();
                    mnumber.getText().toString();
                    pass.getText().toString();
                    icode.getText().toString();*/

/*
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
*/

                //final String selectcity = spn_city.getSelectedItem().toString();
                //System.out.print(selectcity);
                StringRequest signupRequest = new StringRequest(Request.Method.POST, register, new Response.Listener <String>() {
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
//                            e.printStackTrace();
                            Log.i(TAG, "onResponse: " + e);
                        }
                        if (status == 200) {
                            // progressDialog.hide();
                            //String user_ac_status = null;
                            try {

                                //   user_ac_status = (resObj.getString("user_ac_status"));
//                                    Log.i( TAG, "token" + resObj.getString( "abc" ) );
                                driver_id=resObj.getString("driver_id");

                                editor.putString("driver_id", resObj.getString("driver_id"));
                                Log.i(TAG, "token" + resObj.getString("driver_id"));
                                /*editor.putString("lastname", resObj.getString("lastname"));
                                editor.putString("phone", resObj.getString("phone"));
                                editor.putString("mail", resObj.getString("mail"));
                                editor.putString("pass", resObj.getString("pass"));
                                editor.putString("user_id", resObj.getString("user_id"));
                                editor.putString("joined", resObj.getString("joined"));*/
                                editor.putString("isLoggedIn", "200");
                                editor.commit();
                                Toast.makeText(AboutyourselfActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                Intent dashIntent = new Intent(AboutyourselfActivity.this, YourVehicleActivity.class);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                startActivity(dashIntent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // progressDialog.hide();
                            Toast.makeText(AboutyourselfActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", "error" + error);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {


                        Map <String, String> params = new HashMap<>();
                       /* if(driver_id.equalsIgnoreCase("")) {
                            params.put("driver_id", driver_id);
                        }*/
                        params.put("first_name", fname.getText().toString());
                        params.put("last_name", lname.getText().toString());
                        params.put("city", city.getText().toString());
                        params.put("work_city","Philadelphia" );
                        params.put("driver_zip", zcode.getText().toString());
                        params.put("driver_address", address.getText().toString());
                        params.put("driver_apt", apt.getText().toString());
                        params.put("driver_state", state.getText().toString());
                        params.put("driver_email", email.getText().toString());
                        params.put("driver_mobile", mnumber.getText().toString());
                        params.put("driver_image", str_img);
                        params.put("driver_invite_code", icode.getText().toString());
                        params.put("driver_password", pass.getText().toString());
                        Log.i("Registration Value", "getParams: " + params);
                        return params;

                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(signupRequest);


            }
        });
    }

    private void initView() {
        btn_about = (Button) findViewById(R.id.btn_about);
        tv_text = (TextView) findViewById(R.id.tv_text);
        circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        address = (EditText) findViewById(R.id.address);
        apt = (EditText) findViewById(R.id.apt);
        zcode = (EditText) findViewById(R.id.zcode);
        city = findViewById(R.id.city);
        // city.setText("ababa");
        state = (EditText) findViewById(R.id.state);
        email = (EditText) findViewById(R.id.email);
        mnumber = (EditText) findViewById(R.id.mnumber);
        pass = (EditText) findViewById(R.id.pass);
        icode = (EditText) findViewById(R.id.icode);
        iv_back1=(ImageView)findViewById(R.id.iv_back1);
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



    private boolean validate(EditText email, EditText pass) {
        boolean flag = true;

        if (!email.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())) {
            email.setError("Please check your Email ID!");
            flag = false;
        }


        if (!pass.getText().toString().matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})")) {
            pass.setError("Must contain uppercase, lowercase, special character, min length 8!");
            flag = false;
        }


        return flag;
    }
}
