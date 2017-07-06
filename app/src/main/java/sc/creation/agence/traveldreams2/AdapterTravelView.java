package sc.creation.agence.traveldreams2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.R.id.content;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Stef on 02/07/2017.
 */

public class AdapterTravelView extends RecyclerView.Adapter<AdapterTravelView.ViewHolderTravel> {


    private LayoutInflater layoutInflater;
    private ArrayList<Travel>  travels= new ArrayList<>();
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;


    public AdapterTravelView(Context context){

        layoutInflater=LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance(context);
        imageLoader = volleySingleton.getImageLoader();

    }
    public void setTravelList(ArrayList<Travel> travels){

            this.travels = travels;
            notifyItemRangeChanged(0,travels.size());


    }
    @Override
    public ViewHolderTravel onCreateViewHolder(ViewGroup parent, int viewType) {
      View view =  layoutInflater.inflate(R.layout.adapterview,parent,false);
        ViewHolderTravel viewHolderTravel = new ViewHolderTravel(view);

        return viewHolderTravel;
    }

    @Override
    public void onBindViewHolder(final ViewHolderTravel holder, int position) {
        final Travel currentposition = travels.get(position);
       // holder.duree.setText(currentposition.getType());
        holder.price.setText(currentposition.getPrice());
        holder.rest.setText(currentposition.getPays());
        holder.arrival.setText(currentposition.getArrivee());
        final String urlphoto = currentposition.getUrl();
        holder.type.setText(currentposition.getTitle());
       final Context context = holder.imageView.getContext();
        setAnimation(holder.price,position);

        int min = 3;
        int max = 5;

        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;
        holder.ratingBar.setRating(i1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Showdetails.class);
                intent.putExtra("id",currentposition.getTravelid());
                v.getContext().startActivity(intent);


            }
        });
        holder.facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
// intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
                intent.putExtra(Intent.EXTRA_TEXT, currentposition.getUrltoshare());

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
                    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + currentposition.getUrltoshare();
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                }

                v.getContext().startActivity(intent);
            }
        });

        holder.twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.twitter.com/intent/tweet?url="+currentposition.getUrltoshare()+"&text="+currentposition.getPays()+"-"+currentposition.getTitle()+"-"+currentposition.getArrivee()+"";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);
            }
        });
     /*   holder.googleplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new PlusShare.Builder()
                        .setType("text/plain")
                        .setText("Welcome to the Google+ platform.")
                        .setContentUrl(Uri.parse("https://developers.google.com/+/"))
                        .getIntent();

                ((AdapterTravelView) context).startActivityForResult(shareIntent, 0);
            }
        });*/


        if(urlphoto != null){
            imageLoader.get(urlphoto, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Picasso.with(context).load(urlphoto).placeholder(R.drawable.progress_animation)   // optional
                            .error(R.drawable.palmtree)
                            .resize(320,200)
                            .into(holder.imageView);
                  //  holder.imageView.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });


        }
    }


    @Override
    public int getItemCount() {
        return travels.size();
    }

    static class ViewHolderTravel extends RecyclerView.ViewHolder{

         ImageView imageView;
         TextView titre;
         TextView rest;
         RatingBar ratingBar;
         TextView price;
         TextView type;
         ImageButton facebook;
         ImageButton twitter;
         ImageButton googleplus;
         TextView arrival;

        //TextView duree;
        // Button shoxoffer;

        public ViewHolderTravel(View itemView){
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.movieThumbnail);
         //   titre = (TextView) itemView.findViewById(R.id.movieTitle);
            rest = (TextView)itemView.findViewById(R.id.movieReleaseDate);
            ratingBar = (RatingBar)itemView.findViewById(R.id.movieAudienceScore);
            price = (TextView)itemView.findViewById(R.id.price);
            type = (TextView)itemView.findViewById(R.id.type);
            arrival = (TextView)itemView.findViewById(R.id.arrival);
            facebook =(ImageButton)itemView.findViewById(R.id.facebook);
            twitter = (ImageButton)itemView.findViewById(R.id.twitter);
            googleplus = (ImageButton)itemView.findViewById(R.id.googleplus);

           // duree = (TextView)itemView.findViewById(R.id.duree);
           // shoxoffer = (Button)itemView.findViewById(R.id.button2);

        }
    }
    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }
    protected static void setItemTranslationY(RecyclerView rv, RecyclerView.ViewHolder holder, float y) {
        final RecyclerView.ItemAnimator itemAnimator = rv.getItemAnimator();
        if (itemAnimator != null) {
            itemAnimator.endAnimation(holder);
        }
        ViewCompat.setTranslationY(holder.itemView, y);
    }


}
