package utilities.objects;

import model.field.Field;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GameStartedObj {
    @Expose
    @SerializedName("gameMap")
    private ArrayList<ArrayList<ArrayList<Field>>> mapBody = new ArrayList<>();

    public GameStartedObj(ArrayList<ArrayList<ArrayList<Field>>> mapBody) {
        this.mapBody = mapBody;
    }

    public ArrayList<ArrayList<ArrayList<Field>>> getMap() {
        return mapBody;
    }

}
