package citrusbits.com.customcamera.Service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Touseef on 5/24/16.
 */
public class Datum {



    @SerializedName("funny")
    @Expose
    private String funny;
    @SerializedName("normal")
    @Expose
    private String normal;

    /**
     *
     * @return
     * The funny
     */
    public String getFunny() {
        return funny;
    }

    /**
     *
     * @param funny
     * The funny
     */
    public void setFunny(String funny) {
        this.funny = funny;
    }

    /**
     *
     * @return
     * The normal
     */
    public String getNormal() {
        return normal;
    }

    /**
     *
     * @param normal
     * The normal
     */
    public void setNormal(String normal) {
        this.normal = normal;
    }

}
