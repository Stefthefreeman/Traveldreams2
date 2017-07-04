package sc.creation.agence.traveldreams2;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Promos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Promos extends Fragment {

    private RecyclerView stream;
    String pt;
    ConnectivityManager cm;
    ProgressBar progressBar;
    FrameLayout frameLayout;
    TextView loadermsg;
    private AdapterTravelView adapterTravelView;


    public Promos() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Promos newInstance() {
        Promos fragment = new Promos();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stream, null);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Recherche en cours...");
        cm = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

       // progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        stream = (RecyclerView) view.findViewById(R.id.streamlist);
        frameLayout = (FrameLayout)view.findViewById(R.id.progressBarHolder);
        loadermsg = (TextView)view.findViewById(R.id.load) ;
        frameLayout.setVisibility(VISIBLE);
       // loadermsg.setVisibility(view.VISIBLE);
       // loadermsg.setText("Recherche en cours, patientez SVP!");
        adapterTravelView =new AdapterTravelView(getActivity());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        stream.setLayoutManager(llm);
        return view;
    }
    private void readStream()  {
        try {

            String url="http://www.traveldreams.fr/app/promos.php";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,jsonRequestListener ,errorListener);
            getRequestQueue().add(request);
        }
        catch (Exception e) {
            Log.e("promos",e.getLocalizedMessage());
        }
    }


    private Response.Listener<JSONObject> jsonRequestListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            //  Toast.makeText(getActivity(),"On a une reponse", Toast.LENGTH_LONG).show();
            try {

                frameLayout.setVisibility(GONE);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("EN PROMO !");
                JSONArray jsonArray = response.getJSONArray("promos");

                ArrayList<Travel> travel = new ArrayList<Travel>();

                for (int i =0;i<jsonArray.length();i++) {
                    JSONObject jsonObject =jsonArray.getJSONObject(i);
                    Travel prom = new Travel();
                    prom.title =new SpannableString(jsonObject.getString("titre").replace("&ocirc;","ô").replace("&eacute;","é")) ;
                    prom.name = jsonObject.getString("type");
                    prom.travelid = jsonObject.getString("id");
                    prom.pays = jsonObject.getString("pays");
                    prom.price = jsonObject.getString("prix");
                    prom.url = jsonObject.getString("url");
                    prom.type = jsonObject.getString("type");


                    travel.add(prom);
                }
                adapterTravelView.setTravelList(travel);
                stream.setAdapter(adapterTravelView);

                //  streamreader.setDivider(new ColorDrawable(0xAAFFCC00));   //0xAARRGGBB
                //  streamreader.setDividerHeight(1);
            } catch (JSONException e) {
                Log.e("JSON", e.getLocalizedMessage());
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
//            Toast.makeText(getActivity(),"On a une erreur", Toast.LENGTH_LONG).show();
            Log.d("REQUEST", error.toString());
        }
    };

   /* public static class Travel  {
        public String title ;
        public String name ;
        public String idstream;
        public String pays;
        public String price;

        public String edittime;

    }*/

    RequestQueue requestQueue;
    RequestQueue getRequestQueue() {
        if(requestQueue==null)
            requestQueue = Volley.newRequestQueue(getActivity());
        return requestQueue;
    }

    ImageLoader imageLoader;
    ImageLoader getImageLoader()  {
        if(imageLoader==null) {
            ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
                LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);
                public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url, bitmap);
                }
                public Bitmap getBitmap(String url) {
                    return cache.get(url);
                }
            };

            imageLoader = new ImageLoader(getRequestQueue(),imageCache);
        }
        return imageLoader;
    }
    class SearchListAdapter extends ArrayAdapter<Travel> {

        Context context;
        public SearchListAdapter(Context context,  List<Travel> omdbFilms) {
            super(context, R.layout.adaptstream, omdbFilms);
            this.context = context;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            View view=null;
            if(convertView==null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.adaptstream,null);
            } else {
                view = convertView;
            }

            final Travel omdbFilm = getItem(pos);
            frameLayout.setVisibility(GONE);
            loadermsg.setVisibility(GONE);
            view.setTag(omdbFilm);
//            progressBar.setVisibility(GONE);
            TextView titre =(TextView)view.findViewById(R.id.listItemOMdbFilm_title);
            TextView annee =(TextView)view.findViewById(R.id.listItemOMdbFilm_year);
            TextView pays =(TextView)view.findViewById(R.id.pays);
            TextView theprice =(TextView)view.findViewById(R.id.textprice);
            final Button detailButton =(Button)view.findViewById(R.id.button);
            // final Button closeButton=(Button)view.findViewById(R.id.listItemOMdbFilm_closeDetail);
            //   final RelativeLayout detailLayout = (RelativeLayout)view.findViewById(R.id.listItemOMdbFilm_detailLayout);
            // final NetworkImageView detailPoster = (NetworkImageView)view.findViewById(R.id.picto_jlm);
            //   final TextView detailPlot = (TextView)view.findViewById(R.id.listItemOMdbFilm_plot);

            //   detailLayout.setVisibility(View.GONE);
            detailButton.setVisibility(VISIBLE);

            detailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //seedetails(context,omdbFilm.idstream);
                    Intent intent = new Intent(getActivity(),Showdetails.class);
                    intent.putExtra("id",omdbFilm.idstream);
                    startActivity(intent);
                }
            });
           /* closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailLayout.setVisibility(View.GONE);
                    detailButton.setVisibility(View.VISIBLE);
                }
            });*/
            titre.setText(omdbFilm.pays);
            annee.setText(omdbFilm.name);
            pays.setText(omdbFilm.title);
            theprice.setText(omdbFilm.price);

            return view;
        }

    }

    public void seedetails(final Context context,String id){
        String url ="http://www.traveldreams.fr/app/details.php?id="+id+"";
        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            final Dialog dialog = new Dialog(context,R.style.AlertDialogCustom);
                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View view1= inflater.inflate(R.layout.customdialoog,null);

                            dialog.setContentView(view1);
                            final ImageView networkImageView = (ImageView) view1.findViewById(R.id.picto_dialog);
                            final TextView text = (TextView) view1.findViewById(R.id.text);
                            final Button dialogButtonOK =(Button) view1.findViewById(R.id.dialogButtonOK);
                            final TextView alerttitlte = (TextView) view1.findViewById(R.id.TextView_title);
                            Button share =(Button) view1.findViewById(R.id.buttonshare);
                            final Travel dt = new Travel();
                            String url = response.getString("img");
                            if(url.isEmpty()){
                                url = "http://www.agence-creation-sc.com/uploads/logo.png";
                            }
                            Picasso.with(context).load(url).placeholder(R.drawable.airplane)   // optional
                                    .error(R.drawable.airplane)
                                    .resize(400,400)
                                    .into(networkImageView);

                            final SpannableString plot = new SpannableString(Html.fromHtml(response.getString("resume").toString().replace("<br/>","\n")));
                            //Linkify.addLinks(plot,Linkify.ALL);

                            final String titre = response.getString("pays")+"\n"+response.getString("titre");

                            // Toast.makeText(getActivity(),url,Toast.LENGTH_LONG).show();
                            alerttitlte.setText(Html.fromHtml(titre));
                            text.setText(plot);
                            //  networkImageView.setImageUrl(url, getImageLoader());
                            dialog.show();
                            share.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            dialogButtonOK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
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

    public boolean isOnline() {


        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable() && netInfo.isConnected()) {
            boolean wifi = netInfo.getType() == ConnectivityManager.TYPE_WIFI;
            boolean radio = netInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifi) {
                //  reseau.setText("Connecté en wifi");
            }
            else if (radio){
                //  reseau.setText("Connecté en Radio ");
            }
            else if (!wifi && !radio){
                //reseau.setText("Aucune connexion disponible");
            }

            return true;
        }

        return false;
    }
    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of ShowstreamRT");
        super.onResume();
        if(isOnline()){
            readStream();
        }

    }


    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of loginFragment");
        super.onPause();
    /*    if (requestQueue  != null) {
            requestQueue.cancelAll(this);
        }*/
    }


    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue  != null) {
            requestQueue.cancelAll(this);
        }

        Log.e("DEBUG", "OnStop of Showstreamrt fragment");
    }

}
