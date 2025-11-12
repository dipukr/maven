package maven;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;

import com.github.javafaker.Code;
import com.github.javafaker.Faker;

class HashUtil {
    public static int hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            return bigInt.mod(BigInteger.valueOf(Integer.MAX_VALUE)).intValue();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
class Node {
    private final String id;
    private final int hash;
    Map<String, String> data = new HashMap<>();

    public Node(String id) {
        this.id = id;
        this.hash = HashUtil.hash(id);
    }

    public String getId() {
        return id;
    }

    public int getHash() {
        return hash;
    }
    
    public void put(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public Map<String, String> getAllData() {
        return data;
    }

    public void removeKeys(Collection<String> keys) {
        keys.forEach(data::remove);
    }

    @Override
    public String toString() {
        return id + " (" + hash + ")";
    }
}

class ConsistentHashRing {
    private final TreeMap<Integer, Node> ring = new TreeMap<>();

    public void addNode(Node node) {
        ring.put(node.getHash(), node);
        System.out.println("Added node: " + node);
    }
    
    public void removeNode(Node node) {
        ring.remove(node.getHash());
        Entry<Integer, Node> successor = ring.higherEntry(node.getHash());
        if (successor == null) successor = ring.firstEntry(); // wrap around

        if (successor != null) {
            successor.getValue().getAllData().putAll(node.getAllData());
        }

        System.out.println("Removed node: " + node + " and moved data to " + successor.getValue());
    }

    public Node getNode(String key) {
        if (ring.isEmpty()) return null;
        int hash = HashUtil.hash(key);

        // Find first node >= key hash (wrap around if needed)
        Map.Entry<Integer, Node> entry = ring.ceilingEntry(hash);
        if (entry == null) {
            entry = ring.firstEntry(); // wrap around
        }
        return entry.getValue();
    }

    public void printRing() {
        System.out.println("Ring: " + ring.values());
    }
}

public class DHT {
	public static void main(String[] args) {
        ConsistentHashRing ring = new ConsistentHashRing();
       
        Node n1 = new Node("Node-A");
        Node n2 = new Node("Node-B");
        Node n3 = new Node("Node-C");

        ring.addNode(n1);
        ring.addNode(n2);
        ring.addNode(n3);
        ring.printRing();

       String[] keys = {"apple", "banana", "grape", "mango", "orange", "Leonardo"};
        for (String key : keys) {
        	Node nd = ring.getNode(key);
        	
            System.out.println(key+" => "+nd);
        }

        System.out.println("\n>>> Removing Node-B");
        ring.removeNode(n2);

        for (String key : keys) {
            System.out.println(key + " → " + ring.getNode(key));
        }
    }
}
