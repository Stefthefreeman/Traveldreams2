package sc.creation.agence.traveldreams2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.R.attr.startYear;
import static android.view.View.GONE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FormSearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FormSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormSearch extends Fragment {

    private TextView dtdepart;
    private OnFragmentInteractionListener mListener;
    private String[] pays = {"Afghanistan","Afrique-du-sud","Albanie","Allemagne","Andalousie","Andorre","Antilles-neerlandaises","Argentine","Armenie",
            "Australie", "Autriche", "Bahamas", "Baleares", "Bali", "Barbados", "Belgique", "Birmanie", "Bolivie", "Bresil", "Bulgarie", "Cambodge", "Canada",
            "Canaries", "Cap-vert", "Chili", "Chine", "Chypre", "Colombie", "Costa-rica", "Crete", "Croatie", "Cuba", "Danemark", "Dubai", "Ecosse", "Egypte",
            "Emirats-arabes-unis", "Equateur", "Espagne", "Estonie", "Etats-unis", "Finlande", "France", "Fuerteventura", "Grece", "Guadeloupe", "Guatemala", "Haiti",
            "Hong-kong", "Hongrie", "Iceland", "Ile-maurice", "Iles-turks-et-caicos", "Inde", "Indonesie", "Iran", "Irlande", "Islande", "Israel", "Italie",
            "Jamaique", "Japon", "Jordanie", "Kenya", "Kirghizistan", "La Reunion", "Laos", "Lettonie", "Lituanie", "Madagascar", "Madere", "Malaisie", "Maldives", "Malte", "Maroc", "Martinique", "Maurice", "Mexique",
            "Monaco", "Mongolie", "Montenegro", "Mozambique", "Myanmar", "Namibie", "Nepal", "Norvege", "Nouvelle-zelande", "Oman", "Ouzbekistan", "Panama", "Pays-bas", "Perou", "Philippines", "Pologne", "Polynesie-francaise", "Portugal", "Republique Dominicaine",
            "Republique-islamique-d-iran", "Republique-tcheque", "Roumanie", "Royaume-uni", "Russie", "Saint-barthelemy", "Saint-martin",
            "Sainte-lucie", "Sao-tome-et-principe", "Sardaigne", "Senegal", "Seychelles", "Sicile", "Sicile-et-italie-du-sud", "Singapour", "Slovenie",
            "Sri-lanka", "Suede", "Suisse", "Tanzanie", "Thailande", "Tunisie", "Turquie", "Ukraine", "Vanuatu", "Venezuela", "Vietnam", "Yougoslavie",
            "Zanzibar", "Zimbabwe"};


    private AutoCompleteTextView autoCompleteTextView;
    private Spinner spinner;
    private Button search;
    private AdapterTravelView searchresults;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private FrameLayout isloading;
    private String land;
    private String form;
    private String embed;
    private String landing;
    private ProgressBar pgsearch;
    private String dating;
    public FormSearch() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FormSearch newInstance() {
        FormSearch fragment = new FormSearch();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form_search, null);
        dtdepart = (TextView) view.findViewById(R.id.dtdepart);

        autoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, pays);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(adapter);
        recyclerView = (RecyclerView)view.findViewById(R.id.research);
        linearLayout = (LinearLayout)view.findViewById(R.id.one);
        isloading = (FrameLayout)view.findViewById(R.id.progressBarHolder2) ;
        pgsearch = (ProgressBar)view.findViewById(R.id.pgsearch);
        searchresults = new AdapterTravelView(getActivity());
        search = (Button)view.findViewById(R.id.searching);
        spinner = (Spinner)view.findViewById(R.id.spinner) ;

        List<String> list = new ArrayList<String>();
        list.add("Autotour");
        list.add("Camping");
        list.add("Circuit");
        list.add("Hôtel seul");
        list.add("Circuit");
        list.add("Séjour");
        list.add("Séjour Balnéaire");
        list.add("Séjour ville");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               land = (String)parent.getItemAtPosition(position);
                landing = land.replace(" ","-").trim();

                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              form = (String)parent.getItemAtPosition(position);
                embed=form.replace("é","e").trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        dating = dtdepart.getText().toString();

        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm2);
        //stream.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        recyclerView.addItemDecoration( new android.support.v7.widget.DividerItemDecoration(getActivity(), android.support.v7.widget.DividerItemDecoration.VERTICAL));
        dtdepart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                showDatePickerDialog();
                return false;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pgsearch.setVisibility(View.VISIBLE);
                isloading.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(GONE);
                searchtravels(landing,dating,embed);
            }
        });

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


    public void showDatePickerDialog() {

       final  Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

                dtdepart.setText(sdf.format(myCalendar.getTime()));
            }

        };
        new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }



   /* private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        dtdepart.setText(sdf.format(myCalendar.getTime()));
    }*/
   private void searchtravels(String destination, String when, String how)  {
       try {

           String url="http://www.traveldreams.fr/app/searching.php?pays="+destination+"&date="+when+"&type="+how+"";
            System.out.println(url);
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

                pgsearch.setVisibility(GONE);
                isloading.setVisibility(GONE);
                linearLayout.setVisibility(GONE);
                recyclerView.setVisibility(View.VISIBLE);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("RESULTATS!");
                JSONArray jsonArray = response.getJSONArray("promos");

                ArrayList<Travel> travel2 = new ArrayList<Travel>();

                for (int i =0;i<jsonArray.length();i++) {
                    JSONObject jsonObject =jsonArray.getJSONObject(i);
                    Travel result = new Travel();
                    result.title =new SpannableString(jsonObject.getString("titre").replace("&ocirc;","ô").replace("&eacute;","é")
                            .replace("&#xf;","ô")) ;
                    result.name = jsonObject.getString("type");
                    result.travelid = jsonObject.getString("id");
                    result.pays = jsonObject.getString("pays");
                    result.price = jsonObject.getString("prix");
                    result.url = jsonObject.getString("url");
                    result.type = jsonObject.getString("type");
                    result.urltoshare ="http://www.traveldreams.fr/"+jsonObject.getString("urltoshare");
                    result.arrivee = jsonObject.getString("ville").replace("&ocirc;","ô").replace("&eacute;","é");


                    travel2.add(result);
                }
                searchresults.setTravelList(travel2);
                recyclerView.setAdapter(searchresults);

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



    RequestQueue requestQueue;
    RequestQueue getRequestQueue() {
        if(requestQueue==null)
            requestQueue = Volley.newRequestQueue(getActivity());
        return requestQueue;
    }


}
