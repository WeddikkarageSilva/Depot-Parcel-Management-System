package parceldepot;

public class Parcel {
    private String parcelID;
    private int daysInDepot;
    private double weight;
    private boolean isCollected;

    public Parcel(String parcelID, int daysInDepot, double weight, boolean isCollected) {
        this.parcelID = parcelID;
        this.daysInDepot = daysInDepot;
        this.weight = weight;
        this.isCollected = false;
    }

    public String getParcelID() { return parcelID; }
    public int getDaysInDepot() { return daysInDepot; }
    public double getWeight() { return weight; }
    public boolean isCollected() { return isCollected; }

    public void setCollected(boolean collected) { this.isCollected = collected; }
}
