package sc.creation.agence.traveldreams2;

import android.text.SpannableString;

/**
 * Created by Stef on 02/07/2017.
 */

public class Travel {

    public SpannableString title ;
    public String name ;
    public String idstream;
    public String pays;
    public String price;
    public String url;
    public String type;
    public String travelid;
    public String urltoshare;
    public String arrivee;

    public String edittime;

    public String getUrltoshare() {
        return urltoshare;
    }

    public void setUrltoshare(String urltoshare) {
        this.urltoshare = urltoshare;
    }

    public String getArrivee() {
        return arrivee;
    }

    public void setArrivee(String arrivee) {
        this.arrivee = arrivee;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String getTravelid() {
        return travelid;
    }

    public void setTravelid(String travelid) {
        this.travelid = travelid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SpannableString getTitle() {
        return title;
    }

    public void setTitle(SpannableString  title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdstream() {
        return idstream;
    }

    public void setIdstream(String idstream) {
        this.idstream = idstream;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEdittime() {
        return edittime;
    }

    public void setEdittime(String edittime) {
        this.edittime = edittime;
    }


}
