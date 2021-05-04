package it.laskaridis.blockyard.blockchain;

public class Address {

    private final String addr;

    public Address(String addr) {
        this.addr = addr;
    }

    public int getVersion() {
        throw new UnsupportedOperationException();
    }

    public String getAddress() {
        throw new UnsupportedOperationException();
    }

    public String getChecksum() {
        throw new UnsupportedOperationException();
    }

    public void validate() {
        throw new UnsupportedOperationException();
    }
}
