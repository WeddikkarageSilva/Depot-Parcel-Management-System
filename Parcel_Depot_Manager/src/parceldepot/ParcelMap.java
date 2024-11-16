package parceldepot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ParcelMap {
    private Map<String, Parcel> parcels = new HashMap<>();

    public void addParcel(Parcel parcel) {
        parcels.put(parcel.getParcelID(), parcel);
    }

    public Parcel getParcel(String parcelID) {
        return parcels.get(parcelID);
    }

    // Method to get all parcels in the map
    public Collection<Parcel> getAllParcels() {
        return parcels.values(); // Returns a Collection of Parcel objects
    }
}
