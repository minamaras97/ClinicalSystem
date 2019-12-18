package com.example.ClinicalSystem.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="holidays")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="typeholiday")
    private HolidayType type;

    public enum HolidayType {
        HOLIDAY,
        ABSENCE
    }

    @Column(name="reason")
    private String reason;

    @Column(name="startdate")
    private Date start;

    @Column(name="enddate")
    private Date end;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Nurse nurse;


    public Holiday() {

    }

    public Holiday(HolidayType type, String reason, Date start, Date end, Nurse nurse){
        this.type = type;
        this.reason = reason;
        this.start = start;
        this.end = end;
        this.nurse = nurse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HolidayType getType() {
        return type;
    }

    public void setType(HolidayType type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Nurse getNurse() {
        return nurse;
    }

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }
}
