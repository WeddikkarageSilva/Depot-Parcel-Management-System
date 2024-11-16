package parceldepot;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class QueueOfCustomers {

    public static class Customer {
        private int seqNo;
        private String name;
        private String parcelID;

        public Customer(int seqNo, String name, String parcelID) {
            this.seqNo = seqNo;
            this.name = name;
            this.parcelID = parcelID;
        }

        public int getSeqNo() { return seqNo; }
        public String getName() { return name; }
        public String getParcelID() { return parcelID; }

        @Override
        public String toString() {
            return "Customer[seqNo=" + seqNo + ", name=" + name + ", parcelID=" + parcelID + "]";
        }
    }

    private Queue<Customer> customerQueue = new LinkedList<>();

    public void addCustomer(Customer customer) {
        customerQueue.add(customer);
    }

    public Customer getNextCustomer() {
        return customerQueue.poll();  
    }

    public void removeCustomer(Customer customer) {
        customerQueue.remove(customer);
    }

    public boolean isQueueEmpty() {
        return customerQueue.isEmpty();
    }

    public int getQueueSize() {
        return customerQueue.size();
    }

    // New method to get all customers
    public List<Customer> getAllCustomers() {
        return new LinkedList<>(customerQueue);  // Return a list copy of the queue
    }
}
