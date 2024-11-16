package parceldepot;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Manager {
    private ParcelMap parcelMap;
    private QueueOfCustomers queueOfCustomers;
    private Log log;
    private static final String CUSTOMER_FILE = "F:\\Fiver Work\\Nov - 15 - java Swing Parcel Deport Project\\java Swing Parcel Deport Project\\Parcel_Depot_Manager\\data\\customers.txt";  // Path to customer file
    private static final String PARCEL_FILE = "F:\\Fiver Work\\Nov - 15 - java Swing Parcel Deport Project\\java Swing Parcel Deport Project\\Parcel_Depot_Manager\\data\\parcels.txt";      // Path to parcel file
    private String lastCollectedParcelInfo = "None";
    
    public Manager() {
        this.parcelMap = new ParcelMap();
        this.queueOfCustomers = new QueueOfCustomers();
        this.log = Log.getInstance();
    }
    
    public String getLastCollectedParcelInfo() {
        return lastCollectedParcelInfo;
    }
    
    public QueueOfCustomers getQueueOfCustomers() {
        return queueOfCustomers;
    }

    // Search Parcels by Parcel ID
    public List<Parcel> searchParcelsByID(String parcelID) {
        List<Parcel> foundParcels = new ArrayList<>();
        for (Parcel parcel : parcelMap.getAllParcels()) {
            if (parcel.getParcelID().contains(parcelID)) {
                foundParcels.add(parcel);
            }
        }
        return foundParcels;
    }

    public void loadParcels(Path filePath) {
        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.split(",").length != 4) {  // Updated to expect 4 columns
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    try {
                        String parcelID = parts[0].trim();
                        int daysInDepot = Integer.parseInt(parts[1].trim());
                        double weight = Double.parseDouble(parts[2].trim());
                        boolean isCollected = Boolean.parseBoolean(parts[3].trim());

                        Parcel parcel = new Parcel(parcelID, daysInDepot, weight, isCollected);
                        parcelMap.addParcel(parcel);
                    } catch (NumberFormatException e) {
                        log.logEvent("Skipping line due to number format error: " + line);
                    }
                }
            }
        } catch (IOException e) {
            log.logEvent("Error loading parcels: " + e.getMessage());
        }
    }

    public void processCustomer(String parcelID, String customerName) {
        // Processing logic for customer selection
        Parcel parcel = findParcelByID(parcelID);
        if (parcel != null && !parcel.isCollected()) {
            // Perform processing
            parcel.setCollected(true);
            updateParcelCollectionStatus(parcelID, true);
            
            // Log fee
            double fee = calculateCollectionFee(parcel);
            log.logEvent("Customer " + customerName + " collected Parcel " + parcelID + ". Fee: " + fee);
            
            lastCollectedParcelInfo = "Customer " + customerName + " collected Parcel " + parcelID + ". Fee: " + fee;
        }
    }

    private double calculateCollectionFee(Parcel parcel) {
        return parcel.getWeight() * 0.5 + parcel.getDaysInDepot() * 0.2;
    }

    public void updateParcelCollectionStatus(String parcelID, boolean isCollected) {
        try {
            List<String> parcels = Files.readAllLines(Paths.get(PARCEL_FILE));
            List<String> updatedParcels = new ArrayList<>();
            for (String line : parcels) {
                if (line.contains(parcelID)) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) { // Assuming format: parcelID, daysInDepot, weight, isCollected
                        parts[3] = String.valueOf(isCollected);  // Update the collected status
                        updatedParcels.add(String.join(",", parts));
                    }
                } else {
                    updatedParcels.add(line);
                }
            }
            Files.write(Paths.get(PARCEL_FILE), updatedParcels);
        } catch (IOException e) {
            log.logEvent("Error updating parcel collection status: " + e.getMessage());
        }
    }

    public Parcel findParcelByID(String parcelID) {
        return parcelMap.getParcel(parcelID);  
    }

    public void addNewCustomer(int seqNo, String name, String parcelID) {
        QueueOfCustomers.Customer customer = new QueueOfCustomers.Customer(seqNo, name, parcelID);
        queueOfCustomers.addCustomer(customer);  

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(CUSTOMER_FILE), StandardOpenOption.APPEND)) {
            writer.write(seqNo + "," + name + "," + parcelID);
            writer.newLine();
        } catch (IOException e) {
            log.logEvent("Error adding customer: " + e.getMessage());
        }
    }

    public void addNewParcel(String parcelID, int daysInDepot, double weight, boolean isCollected) {
        Parcel parcel = new Parcel(parcelID, daysInDepot, weight, isCollected);
        parcelMap.addParcel(parcel);

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(PARCEL_FILE), StandardOpenOption.APPEND)) {
            writer.write(parcelID + "," + daysInDepot + "," + weight + ",false"); // Added collected flag (false)
            writer.newLine();
        } catch (IOException e) {
            log.logEvent("Error adding parcel: " + e.getMessage());
        }
    }
}
