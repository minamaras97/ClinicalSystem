package com.example.ClinicalSystem.service;

import com.example.ClinicalSystem.DTO.ClinicDTO;
import com.example.ClinicalSystem.DTO.DoctorDTO;
import com.example.ClinicalSystem.DTO.FilterDTO;
import com.example.ClinicalSystem.model.*;
import com.example.ClinicalSystem.repository.AppointmentRequestRepository;
import com.example.ClinicalSystem.repository.ClinicRepository;
import com.example.ClinicalSystem.repository.DoctorRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DoctorServiceUnitTest {

    @MockBean
    private ClinicService clinicService;

    @Autowired
    private DoctorService doctorService;


    @Before
    public void setup() {

        ExamType examType1 = new ExamType("Stomatoloski pregled", 1500, 15);
        ExamType examType2 = new ExamType("Oftamoloski pregled", 1800, 30);

        OR or1 = new OR(2L, 1,"Soba1", examType1);
        OR or2 = new OR(3L, 2,"Soba2", examType2);


        Clinic clinic3 = new Clinic();
        clinic3.setName("Klinika Sunce");
        clinic3.setAdress("Fruskogorska 1");
        clinic3.setDescription("Treca klinika");
        clinic3.setId(1L);

        Rating rating1 = new Rating();
        rating1.setValue(1);

        Rating rating2 = new Rating();
        rating2.setValue(5);

        Rating rating3 = new Rating();
        rating3.setValue(5);

        Set<Rating> ratings = new HashSet<>();
        ratings.add(rating1);
        ratings.add(rating2);
        ratings.add(rating3);
        clinic3.setSingleratings(ratings);

        Doctor doctor2 = new Doctor();
        doctor2.setId(1L);
        doctor2.setClinic(clinic3);
        doctor2.setName("Jelena");
        doctor2.setLastname("Bojanic");
        doctor2.setEmail("jelenaa@gmail.com");
        doctor2.setExamType(examType2);
        doctor2.setSpecialization("Oftamolog");

        Long workStart = Time.valueOf("08:00:00").getTime();
        Long workEnd = Time.valueOf("20:00:00").getTime();


        doctor2.setStart(new Time(workStart));
        doctor2.setEnd(new Time(workEnd));
        Set<Holiday> holidays1 = new HashSet<>();
        doctor2.setHolidays(holidays1);

        Appointment appointment1 = new Appointment();
        Date examdate1 = Date.valueOf("2020-10-10");

        Long examStart1 = Time.valueOf("13:00:00").getTime();
        Long examEnd1 = Time.valueOf("13:30:00").getTime();

        appointment1.setId(1L);
        appointment1.setStart(examdate1);
        appointment1.setType(examType2);
        appointment1.setDoctor(doctor2);
        appointment1.setOr(or2);
        appointment1.setStartTime(new Time(examStart1));
        appointment1.setEndTime(new Time(examEnd1));
        appointment1.setName("PREGLED1");
        appointment1.setStatus(AppointmentStatus.SHEDULED);
        appointment1.setClassification(AppointmentClassification.NORMAL);

        Appointment appointment2 = new Appointment();
        Date examdate2 = Date.valueOf("2020-03-01");

        Long examStart = Time.valueOf("08:00:00").getTime();
        Long examEnd = Time.valueOf("08:30:00").getTime();


        appointment2.setEndTime(new Time(examStart));
        appointment2.setStartTime(new Time(examEnd));

        appointment2.setId(2L);
        appointment2.setStart(examdate2);
        appointment2.setType(examType2);
        appointment2.setDoctor(doctor2);
        appointment2.setOr(or2);
        appointment2.setName("PREGLED2");
        appointment2.setStatus(AppointmentStatus.SHEDULED);
        appointment2.setClassification(AppointmentClassification.NORMAL);

        doctor2.getAppointments().add(appointment1);
        doctor2.getAppointments().add(appointment2);

        Doctor doctor3 = new Doctor();
        doctor3.setId(2L);
        doctor3.setClinic(clinic3);
        doctor3.setName("Mina");
        doctor3.setLastname("Maras");
        doctor3.setEmail("minaa@gmail.com");
        doctor3.setExamType(examType1);
        doctor3.setSpecialization("Stomatolog");

        Long workStart1 = Time.valueOf("08:00:00").getTime();
        Long workEnd1 = Time.valueOf("20:00:00").getTime();


        doctor3.setStart(new Time(workStart1));
        doctor3.setEnd(new Time(workEnd1));
        Set<Holiday> holidays2 = new HashSet<>();
        doctor3.setHolidays(holidays2);


        Appointment appointment3 = new Appointment();

        Date examdate3 = Date.valueOf("2020-06-10");

        Long examStart3 = Time.valueOf("12:00:00").getTime();
        Long examEnd3 = Time.valueOf("12:15:00").getTime();

        appointment3.setId(3L);
        appointment3.setStart(examdate3);
        appointment3.setType(examType1);
        appointment3.setDoctor(doctor3);
        appointment3.setOr(or1);
        appointment3.setStartTime(new Time(examStart3));
        appointment3.setEndTime(new Time(examEnd3));
        appointment3.setName("PREGLED3");
        appointment3.setStatus(AppointmentStatus.SHEDULED);
        appointment3.setClassification(AppointmentClassification.NORMAL);

        Appointment appointment4 = new Appointment();
        Date examdate4 = Date.valueOf("2020-04-15");

        Long astarttime4 = Time.valueOf("10:00:00").getTime();
        Long aendtime4 = Time.valueOf("10:15:00").getTime();

        appointment4.setEndTime(new Time(aendtime4));
        appointment4.setStartTime(new Time(astarttime4));
        appointment4.setId(4L);
        appointment4.setStart(examdate4);
        appointment4.setType(examType1);
        appointment4.setDoctor(doctor3);
        appointment4.setOr(or1);
        appointment4.setName("PREGLED4");

        appointment4.setStatus(AppointmentStatus.SHEDULED);
        appointment4.setClassification(AppointmentClassification.NORMAL);

        doctor3.getAppointments().add(appointment3);
        doctor3.getAppointments().add(appointment4);

        Set<Doctor> doctors = new HashSet<>();
        doctors.add(doctor2);
        doctors.add(doctor3);

        clinic3.setDoctors(doctors);


        given(clinicService.findName("Klinika Sunce")
        ).willReturn(clinic3);

    }


    @Test
    public void testFindAllDoctorsFromAClinic() throws ParseException {

        Set<DoctorDTO> doctors = doctorService.findAllDoctorsFromAClinic("KlinikaSunce");
        Assert.assertEquals(2, doctors.size());

    }


}
