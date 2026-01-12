package hms.model;

public final class Facility {
    private final String facilityId;
    private final String facilityName;
    private final String facilityType;
    private final String address;
    private final String postcode;
    private final String phoneNumber;
    private final String email;
    private final String openingHours;
    private final String managerName;
    private final int capacity;
    private final String specialitiesOffered;

    public Facility(
            String facilityId,
            String facilityName,
            String facilityType,
            String address,
            String postcode,
            String phoneNumber,
            String email,
            String openingHours,
            String managerName,
            int capacity,
            String specialitiesOffered
    ) {
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.facilityType = facilityType;
        this.address = address;
        this.postcode = postcode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.openingHours = openingHours;
        this.managerName = managerName;
        this.capacity = capacity;
        this.specialitiesOffered = specialitiesOffered;
    }

    public String facilityId() { return facilityId; }
    public String facilityName() { return facilityName; }
    public String facilityType() { return facilityType; }
    public String address() { return address; }
    public String postcode() { return postcode; }
    public String phoneNumber() { return phoneNumber; }
    public String email() { return email; }
    public String openingHours() { return openingHours; }
    public String managerName() { return managerName; }
    public int capacity() { return capacity; }
    public String specialitiesOffered() { return specialitiesOffered; }
}

