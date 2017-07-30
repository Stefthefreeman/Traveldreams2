package sc.creation.agence.traveldreams2;

import android.text.SpannableString;

/**
 * Created by Stef on 30/07/2017.
 */

public class Favoris {

    private int id;

    private SpannableString titre;
    private String idf;
    private String urlf;
    private String urlim;
    private String urlresa;
    private String resume;
    private String pays;
    private String ville;

    public Favoris(){


    }

    public Favoris( SpannableString titre,String urlf,String urlim, String urlresa, String resume,String pays,String ville){

        super();

        this.titre = titre;
        this.urlf = urlf;
        this.urlresa = urlresa;
        this.urlim = urlim;
        this.resume = resume;
        this.pays = pays;
        this.ville = ville;



    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public SpannableString getTitre() {
        return titre;
    }

    public void setTitre(SpannableString titre) {
        this.titre = titre;
    }

    public String getIdf() {
        return idf;
    }

    public void setIdf(String idf) {
        this.idf = idf;
    }

    public String getUrlf() {
        return urlf;
    }

    public void setUrlf(String urlf) {
        this.urlf = urlf;
    }

    public String getUrlim() {
        return urlim;
    }

    public void setUrlim(String urlim) {
        this.urlim = urlim;
    }

    public String getUrlresa() {
        return urlresa;
    }

    public void setUrlresa(String urlresa) {
        this.urlresa = urlresa;
    }
}
