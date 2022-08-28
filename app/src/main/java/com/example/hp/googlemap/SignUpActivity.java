package com.example.hp.googlemap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.rider.DashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getSimpleName();
    EditText name, phone, zip, email, password, card_name, card_num, Month_year, cvc, edtdob;
    Button btn_done;
    ProgressDialog progressDialog;
    String BaseUrl = "http://45.55.64.233/Miles/api/";
    String register = BaseUrl + "register_user.php";
    ImageView backArrowbtn, circleImageView;
    TextView tv_text;
    String str_img = "";
    int count = 0;
    LinearLayout linearLayoutSnackBar, linearLayoutProgress;
    SharedPreferences.Editor editor;
    private SharedPreferences sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        linearLayoutProgress = (LinearLayout) findViewById(R.id.linearLayoutProgress);

        //Initialization ...............
        linearLayoutSnackBar = (LinearLayout) findViewById(R.id.linearLayoutSnackBar);
        name = (EditText) findViewById(R.id.ed_name);
        phone = (EditText) findViewById(R.id.ed_phone);
        zip = (EditText) findViewById(R.id.ed_zip);
        email = (EditText) findViewById(R.id.ed_email);
        password = (EditText) findViewById(R.id.ed_password);
        card_name = (EditText) findViewById(R.id.card_name);
        card_num = (EditText) findViewById(R.id.ed_number);
        Month_year = (EditText) findViewById(R.id.date_month);
        cvc = (EditText) findViewById(R.id.cvc);
        circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
        edtdob = (EditText) findViewById(R.id.edtdob);
        tv_text = (TextView) findViewById(R.id.tv_text);
        String underlineData = "By applying, I agree to MILES's Terms of Use and confirm that I have read and understand the Privacy Policy";
        SpannableString content = new SpannableString(underlineData);
        content.setSpan(new UnderlineSpan(), 0, underlineData.length(), 0);
        tv_text.setText(content);
        String htmlString = "By applying, I agree to MILES's <u>Terms of Use</u> and confirm that I have read and understand the <u>Privacy Policy</u>";
        tv_text.setText(Html.fromHtml(htmlString));

        sharedPreference = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreference.edit();

        if (sharedPreference.getString("isLoggedIn", "").equals("1")) {
            startActivity(new Intent(SignUpActivity.this, DashActivity.class));
//            if (sharedPreference.getString( "isLoggedIn", "" ).equals( "1" )){
//                startActivity( new Intent( LoginActivity.this, ParkUserInActivitity.class ) );
//            }
        }

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheet();
            }
        });
        Month_year.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void afterTextChanged(Editable s) {
                String str = Month_year.getText().toString();
                if (str.length() == 2 && len < str.length()) {//len check for backspace
                    Month_year.append("/");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                String str = Month_year.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        //Card Number Space.......................
        card_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (count <= card_num.getText().toString().length()
                        && (card_num.getText().toString().length() == 4
                        || card_num.getText().toString().length() == 9
                        || card_num.getText().toString().length() == 14)) {
                    card_num.setText(card_num.getText().toString() + " ");
                    int pos = card_num.getText().length();
                    card_num.setSelection(pos);
                } else if (count >= card_num.getText().toString().length()
                        && (card_num.getText().toString().length() == 4
                        || card_num.getText().toString().length() == 9
                        || card_num.getText().toString().length() == 14)) {
                    card_num.setText(card_num.getText().toString().substring(0, card_num.getText().toString().length() - 1));
                    int pos = card_num.getText().length();
                    card_num.setSelection(pos);
                }
                count = card_num.getText().toString().length();
            }
        });

        //Date Of Births..............
        edtdob.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void afterTextChanged(Editable s) {
                String str = edtdob.getText().toString();
                if (str.length() == 2 && len < str.length()) {//len check for backspace
                    edtdob.append("/");
                }
                if (str.length() == 5 && len < str.length()) {//len check for backspace
                    edtdob.append("/");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                String str = edtdob.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        backArrowbtn = (ImageView) findViewById(R.id.backArrowbtn);
        backArrowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LogInPageActivity.class);
                startActivity(intent);
            }
        });

        btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
                //isValidPassword(String password);
                StringRequest signupRequest = new StringRequest(Request.Method.POST, register, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: " + response);
                        JSONObject resObj = null;
                        int status = 0;
                        String msg = "";
                        try {
                            resObj = new JSONObject(response);
                            status = resObj.getInt("status");
                            msg = resObj.getString("message");
                        } catch (JSONException e) {
                            //e.printStackTrace();
                            Log.i(TAG, "onResponse: " + e);
                        }
                        if (status == 1) {
                            try {
                                String lName=(resObj.getString("lastname"));
                                if (lName.equals("null"))
                                {
                                    editor.putString("","lastname");
                                }else {
                                    editor.putString("lastname", resObj.getString("lastname"));
                                }
                                editor.putString("firstname", resObj.getString("firstname"));
                                editor.putString("phone", resObj.getString("phone"));
                                editor.putString("mail", resObj.getString("mail"));
                                editor.putString("pass", resObj.getString("pass"));
                                editor.putString("user_id", resObj.getString("user_id"));
                                editor.putString("joined", resObj.getString("joined"));
                                editor.putString("isLoggedIn", "1");
                                editor.commit();
                                //Toast.makeText(SignUpActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(SignUpActivity.this, LogInPageActivity.class);
                                startActivity(intent1);
                                finish();
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        } else {
                            Snackbar snackbar = Snackbar.make(linearLayoutSnackBar, "" + msg, Snackbar.LENGTH_LONG);
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#ff0000"));
                            snackbar.setActionTextColor(Color.parseColor("#FFFFFF"));
                            snackbar.show();
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
                        Map<String, String> params = new HashMap<>();
                        params.put("user_name", name.getText().toString());
                        params.put("user_phone", phone.getText().toString());
                        params.put("zip_code", zip.getText().toString());
                        params.put("user_email", email.getText().toString());
                        params.put("user_password", password.getText().toString());
                        params.put("name_on_card", card_name.getText().toString());
                        params.put("card_no", card_num.getText().toString());
                        params.put("validity_dt", Month_year.getText().toString());
                        params.put("cvc_no", cvc.getText().toString());
                        params.put("user_image", str_img);
                        params.put("user_dob", edtdob.getText().toString());
                        // params.put("device_id", uniqueID);

                        Log.i("Registration Value", "getParams: " + params);
                        return params;
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(signupRequest);
            }
        });
    }

    // Validation.......................
    private boolean validate() {
        boolean flag = true;


        if (!email.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())) {
            email.setError("Invalid Email");
            flag = false;
        }
        return flag;
    }

    private void openBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.image_dialog, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);
        ImageView pic_camera = dialog.findViewById(R.id.pic_camera);
        ImageView pic_gallery = dialog.findViewById(R.id.pic_gallery);
        pic_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 200);
                dialog.dismiss();
            }
        });

        pic_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2572);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //Camera & Gallery code/........................

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
        if (200 == requestCode) { // CEMERA
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                circleImageView.setImageBitmap(bmp);
                str_img = convertBitmapToBase64(bmp);
                System.out.println("str_img:" + str_img);
            }
        } else if (requestCode == 2572) { // ALBUM
            if (resultCode == RESULT_OK) {
                System.out.println(" Abcd ");
                if (data != null) {
                    if (Build.VERSION.SDK_INT < 19) {
                    } else {
                        try {
                            InputStream imInputStream = getContentResolver().openInputStream(data.getData());
                            Bitmap bitmap = BitmapFactory.decodeStream(imInputStream);
                            bitmap = getResizedBitmap(bitmap, 400);
                            circleImageView.setImageBitmap(bitmap);
                            str_img = convertBitmapToBase64(bitmap);
                            System.out.println("Abcd3");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
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


