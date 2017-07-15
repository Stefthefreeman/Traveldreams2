package sc.creation.agence.traveldreams2;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;




/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FlightSearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FlightSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlightSearch extends Fragment {
    private final static String TRAVEL_PAYOUTS_MARKER = "140944";
    private final static String TRAVEL_PAYOUTS_TOKEN = "46cf4a645754a1935b4be3c9a76055c8";

    WebView flightsearch;
    Context context;

    private OnFragmentInteractionListener mListener;

    public FlightSearch() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FlightSearch newInstance() {
        FlightSearch fragment = new FlightSearch();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_flight_search, null);
        flightsearch = (WebView)view.findViewById(R.id.flights_searching);

        showflightssearch();


        return view;
    }
    public void showflightssearch(){
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Chargement en cours...", true);
        dialog.setCanceledOnTouchOutside(true);
        flightsearch  .getSettings().setJavaScriptEnabled(true);
        flightsearch  .getSettings().setLoadWithOverviewMode(true);
        flightsearch  .getSettings().setUseWideViewPort(true);
        flightsearch  .getSettings().setBuiltInZoomControls(true);

        flightsearch  .setWebViewClient(new WebViewClient() {

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
                flightsearch .getUrl();
            }


        });

        flightsearch  .loadUrl("http://vols.traveldreams.fr");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
