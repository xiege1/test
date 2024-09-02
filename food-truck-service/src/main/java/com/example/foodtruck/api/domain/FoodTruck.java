package com.example.foodtruck.api.domain;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class FoodTruck {
    /*
    locationid: Unique identifier for the food truck location.
    Applicant: Name of the business or individual applying for the permit.
    FacilityType: Type of food facility (in this case, "Truck").
    cnn: Possibly a business registration or license number.
    LocationDescription: Text description of the permitted location.
    Address: Specific street address.
    blocklot: Combined block and lot number for property identification.
    block: City block number.
    lot: Lot number within the block.
    permit: Permit number issued by the city.
    Status: Current status of the permit (e.g., "APPROVED").
    FoodItems: Description of the food items offered by the truck.
    X & Y: Possibly coordinates in a local grid system.
    Latitude & Longitude: GPS coordinates of the location.
    Schedule: URL link to the food truck's operating schedule.
    dayshours: (Appears empty in this data - likely would contain days and hours of operation).
    NOISent: Date when a Notice of Intent was sent (likely part of the application process).
    Approved: Date the permit was approved.
    Received: Date the permit application was received.
    PriorPermit: Indicates if there was a prior permit (0 for No, 1 for Yes).
    ExpirationDate: Date the permit expires.
    Location: GPS coordinates (latitude, longitude).
    Fire Prevention Districts: District for fire services.
    Police Districts: District for police services.
    Supervisor Districts: City supervisor district.
    Zip Codes: Zip code of the location.
    Neighborhoods (old): Neighborhood where the food truck operates.
     */
    private Integer locationId;
    private String locationDescription;
    private String applicant;
    private String facilityType;
    private String cnn;

    private List<String> foodItems;

    private Double x;
    private Double y;

    private String daysHours;

    // related process approval
    private ZonedDateTime approved;
    private ZonedDateTime received;
    private Byte priorPermit;
    private ZonedDateTime expirationDate;
    private String permit;
    private String status;

    // related address
    private String blockLot;
    private Integer block;
    private Integer lot;
    private Double latitude;
    private Double longitude;
    private String location;
    private String address;
    private Integer firePreventionDistricts;
    private Integer policeDistricts;
    private Integer supervisorDistricts;
    private Integer zipCode;
    private Integer neighborhoods;
}
