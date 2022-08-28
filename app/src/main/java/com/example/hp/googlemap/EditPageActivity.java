package com.example.hp.googlemap;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.googlemap.rider.DashActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPageActivity extends AppCompatActivity
{
    private static final String TAG = EditPageActivity.class.getSimpleName();
    String user_details_show_url="http://45.55.64.233/Miles/api/login_user.php";
    String url_update="http://45.55.64.233/Miles/api/edit_user.php";
    String ProfileDetailsAPIURL = "http://45.55.64.233/Miles/api/getUserImg.php";
    TextView ed_name,ed_last_name,ed_email;
    EditText ed_phone,ed_password;
    ImageView btn_update;
    CircleImageView circleImageViewPro;
    ProgressDialog progressDialog;
    String imagePath;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView backArrowImgBtn;
    int PICK_IMAGE_REQUEST = 111;
    String BaseUrl = "http://45.55.64.233/Miles/api/";
    String updatePhotoUrl = BaseUrl + "update_photo.php";
    Bitmap bitmap;
    String UpImage;
    private Animator mCurrentAnimatorEffect;
    private int mShortAnimationDurationEffect;
    RelativeLayout linearLayoutLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        circleImageViewPro=(CircleImageView) findViewById(R.id.circleImageView);
        ed_name=(TextView)findViewById(R.id.ed_name);
        ed_last_name=(TextView)findViewById(R.id.ed_last_name);
        ed_email=(TextView)findViewById(R.id.ed_email);
        ed_phone=(EditText) findViewById(R.id.ed_phone);
        ed_password=(EditText) findViewById(R.id.ed_password);
        btn_update=(ImageView) findViewById(R.id.btn_update);

        backArrowImgBtn=(ImageView)findViewById(R.id.backArrowImgBtn);
        backArrowImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditPageActivity.this,DashActivity.class);
                startActivity(intent);
            }
        });


        sharedPreferences = getApplicationContext().getSharedPreferences("user_pref", 0);
        editor = sharedPreferences.edit();
        linearLayoutLogin = (RelativeLayout) findViewById(R.id.container);

        // Set Image ...................//////////////
        StringRequest profile_details_request = new StringRequest(Request.Method.POST, ProfileDetailsAPIURL, new Response.Listener<String>() {

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
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e);
                }
                if (status == 1) {
                    try {

                        imagePath = resObj.getString("image");
                        Picasso.with(EditPageActivity.this).load(imagePath).resize(100, 100).into(circleImageViewPro);
                       // Log.d(TAG, "onResponse: "+imagePath);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put( "prk_veh_trc_dtl_id", "7" );
                params.put("user_email", sharedPreferences.getString("mail", ""));
                Log.d("ImageDDD", "getParams: " + params);
                return params;

                //user_admin_id,token

            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(profile_details_request);

        //User Details Show .................


        StringRequest signupRequest = new StringRequest(Request.Method.POST, user_details_show_url, new Response.Listener<String>() {
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
//                    progressDialog.hide();
                    try {
                        ed_name.setText( resObj.getString( "firstname" ) );
                        String lName=resObj.getString( "lastname" );
                        if (lName.equals("null"))
                        {
                            ed_last_name.setText("");
                        }else {
                            ed_last_name.setText(lName);
                        }
                        ed_email.setText( resObj.getString( "mail" ) );
                        ed_phone.setText( resObj.getString( "phone" ) );

                    }catch (Exception e){
                    }

                } else {
                //    progressDialog.hide();
                    //display error msg here
                    Toast.makeText(getApplicationContext(), ""+msg, Toast.LENGTH_SHORT).show();
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

                params.put("user_email", sharedPreferences.getString("mail", ""));
                params.put("user_password", sharedPreferences.getString("pass", ""));
                Log.i("EditPage", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(signupRequest);


        //Done Button Click.....................//////////////
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // validate();
                submitBtn();
            }
        });

        // ..........................................Image Upload................................................................

        circleImageViewPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
               // zoomImageFromThumb(circleImageViewPro);
            }
        });

        mShortAnimationDurationEffect = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
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
                circleImageViewPro.setImageBitmap(bitmap);
                imgUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void imgUp()

    {

        //converting image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //sending image to server
        StringRequest getDriverRequest = new StringRequest(Request.Method.POST, updatePhotoUrl, new Response.Listener<String>() {
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
                                Intent intent=new Intent(EditPageActivity.this,EditPageActivity.class);
                                startActivity(intent);

                                UpImage = (resObj.getString("user_image"));
                                Picasso.with(EditPageActivity.this).load(UpImage).resize(100,100).into(circleImageViewPro);
                            } catch (Exception e) {
                            }
                        } else {
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
                params.put("user_image", imageString);
                params.put("user_email", sharedPreferences.getString("mail", ""));
                Log.i("ImgUploads", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(getDriverRequest);
    }


    public void submitBtn()
    {

        StringRequest signupRequest = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
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
  //                  progressDialog.hide();
                    Intent intent=new Intent(EditPageActivity.this,DashActivity.class);
                    startActivity(intent);

                } else {
                    Snackbar snackbar = Snackbar.make(linearLayoutLogin, "" + msg, Snackbar.LENGTH_LONG);// Snackbar message
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#ff0000")); // snackbar background color
                    snackbar.setActionTextColor(Color.parseColor("#FFFFFF")); // snackbar action text color
                    snackbar.show();
                   // Toast.makeText(getApplicationContext(), ""+msg, Toast.LENGTH_SHORT).show();
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

                params.put("user_email", sharedPreferences.getString("mail", ""));
                params.put("user_password", ed_password.getText().toString());
                params.put("user_phone", ed_phone.getText().toString());
                Log.i("EditPage", "getParams: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(signupRequest);
    }
    private boolean validate() {
        boolean flag = true;


        if (!ed_phone.getText().toString().matches("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$")) {
            ed_phone.setError("Check Phone number");
            flag = false;
        }

        if (!ed_password.getText().toString().matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})")) {
            ed_password.setError("Check password");
            flag = false;
        }

        return flag;
    }

    //Zoom Image....................
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void zoomImageFromThumb(final View thumbView) {
        if (mCurrentAnimatorEffect != null) {
            mCurrentAnimatorEffect.cancel();
        }

        final ImageView expandedImageView = (ImageView) findViewById(R.id.expanded_image);
       // expandedImageView.setImageResource(imageResId);
        Picasso.with(EditPageActivity.this).load(imagePath).resize(100, 100).into(expandedImageView);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDurationEffect);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimatorEffect = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimatorEffect = null;
            }
        });
        set.start();
        mCurrentAnimatorEffect = set;

        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimatorEffect != null) {
                    mCurrentAnimatorEffect.cancel();
                }

                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDurationEffect);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimatorEffect = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimatorEffect = null;
                    }
                });
                set.start();
                mCurrentAnimatorEffect = set;
            }
        });
    }
}
