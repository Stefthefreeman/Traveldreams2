package sc.creation.agence.traveldreams2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class Showdetails extends AppCompatActivity
        implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{



    SliderLayout sliderLayout;
    HashMap<String,String> Hash_file_maps ;
    TextView mTextView;
    TextView price;
    TextView pays ;
    TextView resume;
    Button close;
    Button show;
    ImageButton fb;
    ImageButton tw;
    Button save;
    Favoris favoris;
    RequestQueue requestQueue;
    RequestQueue getRequestQueue() {
        if(requestQueue==null)
            requestQueue = Volley.newRequestQueue(this);
        return requestQueue;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        final String id = i.getStringExtra("id");
        setContentView(R.layout.showdetails);
        fb = (ImageButton) findViewById(R.id.fb);
        tw= (ImageButton) findViewById(R.id.tw);
        save = (Button) findViewById(R.id.save);
        String url ="http://www.traveldreams.fr/app/details.php?id="+id+"";
        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
// Display the first 500 characters of the response string.
                            Hash_file_maps = new HashMap<String, String>();

                            sliderLayout = (SliderLayout)findViewById(R.id.slider);
                            mTextView = (TextView)findViewById(R.id.details) ;
                            price = (TextView)findViewById(R.id.price);
                            pays = (TextView)findViewById(R.id.country) ;
                            resume = (TextView)findViewById(R.id.content) ;
                            close = (Button)findViewById(R.id.close) ;
                            show = (Button)findViewById(R.id.showoffer) ;
                            final Travel ext = new Travel();

                          final  String img0 = response.getString("img");
                            String img1 = response.getString("img1");
                            String img2 = response.getString("img2");
                            String img3 = response.getString("img3");
                            String img4 = response.getString("img4");
                          final  SpannableString   titre =new SpannableString(Html.fromHtml(response.getString("titre"))) ;
                            ext.pays = response.getString("pays");
                            String prix = response.getString("prix")+"€";
                          final  String country = response.getString("pays");
                            String type = response.getString("type");
                            ext.arrivee = response.getString("arrivee").replace("Non communiqu&eacute;","NC");
                            ext.urltoshare =response.getString("urltoshare");
                            ext.title = new SpannableString(response.getString("titre"));
                            final String url = response.getString("url");
                            SpannableString resa = new SpannableString(Html.fromHtml(response.getString("resume")));

                            setTitle(titre);
                            fb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("text/plain");
// intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
                                    intent.putExtra(Intent.EXTRA_TEXT, ext.getUrltoshare());

// See if official Facebook app is found
                                    boolean facebookAppFound = false;
                                    List<ResolveInfo> matches = v.getContext().getPackageManager().queryIntentActivities(intent, 0);
                                    for (ResolveInfo info : matches) {
                                        if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                                            intent.setPackage(info.activityInfo.packageName);
                                            facebookAppFound = true;
                                            break;
                                        }
                                    }

// As fallback, launch sharer.php in a browser
                                    if (!facebookAppFound) {
                                        String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + ext.getUrltoshare();
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                                    }

                                    v.getContext().startActivity(intent);
                                }
                            });
                            tw.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = "http://www.twitter.com/intent/tweet?url="+ext.getUrltoshare()+"&text="+ext.getPays()+" | "+ext.getTitle()+"-"+ext.getArrivee()+"";
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    v.getContext().startActivity(i);
                                }
                            });
                            save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FavorisBDD favorisBDD = new FavorisBDD(Showdetails.this);
                                    favorisBDD.open();
                                    favoris=new Favoris(titre,)

                                }
                            });

                            Hash_file_maps.put("1", img0);
                            Hash_file_maps.put("2", img1);
                            Hash_file_maps.put("3", img2);
                            Hash_file_maps.put("4", img3);
                            Hash_file_maps.put("5", img4);

                            for(String name : Hash_file_maps.keySet()){

                                TextSliderView textSliderView = new TextSliderView(Showdetails.this);
                                textSliderView
                                        .description(name)
                                        .image(Hash_file_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener(Showdetails.this);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra",name);
                                sliderLayout.addSlider(textSliderView);
                            }
                            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            sliderLayout.setCustomAnimation(new DescriptionAnimation());
                            sliderLayout.setDuration(3000);
                            sliderLayout.addOnPageChangeListener(Showdetails.this);

                            mTextView.setText(titre);
                            price.setText(prix);
                            pays.setText(country+" - "+ext.getArrivee());
                            resume.setText(type+"\n"+ resa);

                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent back = new Intent(Showdetails.this,MainActivity.class);
                                    startActivity(back);
                                }
                            });
                            show.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent ptr = new Intent(Showdetails.this,ShowOffer.class);
                                    ptr.putExtra("url",url);
                                    startActivity(ptr);
                                }
                            });


                        } catch (JSONException e) {
                            Log.e("JSON", e.getLocalizedMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.e("DETAIL", error.getLocalizedMessage());
                    }
                });
        getRequestQueue().add(jsonObjectRequest);


    }
    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}






}
