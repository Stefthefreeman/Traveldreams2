package sc.creation.agence.traveldreams2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

    EditText departure;
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
        context = (Context) getActivity();
        View view = inflater.inflate(R.layout.fragment_flight_search, null);
        departure=(EditText)view.findViewById(R.id.departure) ;




        return view;
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
