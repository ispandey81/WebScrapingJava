package entity;

import javax.persistence.*;

@Entity
@Table(name="HolidayDetails")
public class HolidayDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column
    private String holiday;

    public HolidayDetails(String holiday) {
        this.holiday = holiday;
    }

    public HolidayDetails() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    @Override
    public String toString() {
        return "HolidayDetails{" +
                "id=" + id +
                ", holiday='" + holiday + '\'' +
                '}';
    }
}
