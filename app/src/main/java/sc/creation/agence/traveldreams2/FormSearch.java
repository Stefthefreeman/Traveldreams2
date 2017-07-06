package sc.creation.agence.traveldreams2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.R.attr.startYear;


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
        spinner = (Spinner)view.findViewById(R.id.spinner) ;
        List<String> list = new ArrayList<String>();
        list.add("Autotour");
        list.add("Circuit");
        list.add("Hôtel seul");
        list.add("Circuit");
        list.add("Séjour");
        list.add("Séjour Balnéaire");
        list.add("Séjour ville");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        dtdepart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                showDatePickerDialog();
                return false;
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
                String myFormat = "dd/MM/yyyy"; //In which you need put here
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


}
