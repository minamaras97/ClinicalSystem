package com.example.ClinicalSystem.service;

import com.example.ClinicalSystem.DTO.*;
import com.example.ClinicalSystem.model.*;
import com.example.ClinicalSystem.repository.AppointmentRequestRepository;
import org.joda.time.LocalDate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;


@Service
public class AppointmentRequestService {


    @Autowired
    private AppointmentRequestRepository appointmentRequestRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ExamTypeService examTypeService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private OperationRoomService operationRoomService;


    public boolean saveAppointmentRequest(AppointmentRequestDTO appointmentRequestDTO) throws ParseException, UnsupportedEncodingException {

        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) a.getPrincipal();

        Patient p = patientService.findPatient(user.getEmail());

        AppointmentRequest appointmentRequest = modelMapper.map(appointmentRequestDTO, AppointmentRequest.class);
        appointmentRequest.setPatient(p);

        String startDate=appointmentRequestDTO.getDate();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd");
        java.util.Date date = sdf1.parse(startDate);
        java.sql.Date finaldate = new java.sql.Date(date.getTime());
        appointmentRequest.setStart(finaldate);

        String decoded = URLDecoder.decode(appointmentRequestDTO.getExamTypeName(), "UTF-8");
        ExamType examType =examTypeService.findOne(decoded);
        if(examType != null){
            appointmentRequest.setType(examType);
            java.sql.Time startTime = java.sql.Time.valueOf(appointmentRequest.getStartTime().toString());
            LocalTime localtime = startTime.toLocalTime();
            localtime = localtime.plusMinutes(examType.getDuration());
            Time endTime= Time.valueOf(localtime);
            appointmentRequest.setEndTime(endTime);
        }

        Doctor doctor = doctorService.findOneById(appointmentRequestDTO.getDoctorid());

        if(doctor != null){
            appointmentRequest.setDoctor(doctor);
            appointmentRequestDTO.setDoctorEmail(doctor.getEmail());
        }

        Clinic clinic = doctor.getClinic();
        List<ClinicAdmin> admins = new ArrayList<>();
        for(ClinicAdmin ca : clinic.getClinicAdmins()){
            admins.add(ca);
        }

        int randomNumberAdmin = (int)(Math.random() * admins.size());


        //provera da li postoji vec taj u bazi

        AppointmentRequest reqpatient = appointmentRequestRepository.findByPatient(p);
        AppointmentRequest reqdate = appointmentRequestRepository.findByStart((Date) appointmentRequest.getStart());
        AppointmentRequest reqtime = appointmentRequestRepository.findByStartTime(appointmentRequest.getStartTime());
        AppointmentRequest reqtype = appointmentRequestRepository.findByType(appointmentRequest.getType());


        if(reqpatient != null && reqdate != null & reqtime != null && reqtype != null){
            return false;
        }

        if (appointmentRequestRepository.save(appointmentRequest) != null) {

            try {
                emailService.sendAdminNotificaitionAsync(admins.get(randomNumberAdmin),p,finaldate,appointmentRequest.getStartTime(),appointmentRequest.getEndTime(),appointmentRequest.getType());
            } catch (Exception e) {
                return false;
            }

            return true;
        } else {
            return false;


        }
    }

    public List<AppointmentRequestDTO> findAll() throws ParseException {
        List<AppointmentRequest> requests = appointmentRequestRepository.findAll();

        List<AppointmentRequestDTO> appointmentRequestDTOS = new ArrayList<>();
        for (AppointmentRequest c : requests) {
            AppointmentRequestDTO appointmentRequestDTO =modelMapper.map(c,AppointmentRequestDTO.class);

            appointmentRequestDTO.setStart(c.getStart());
            appointmentRequestDTO.setStartTime(c.getStartTime());
            appointmentRequestDTO.setEndTime(c.getEndTime());

            appointmentRequestDTOS.add(appointmentRequestDTO);
        }

        return appointmentRequestDTOS;
    }


    public AppointmentRequestDTO findById(Long id){
        Optional<AppointmentRequest> ap =appointmentRequestRepository.findById(id);
        return modelMapper.map(ap.get(),AppointmentRequestDTO.class);
    }

    public boolean IsCreated(String roomId, String examdate, String examtime, String endtime, AppointmentRequestDTO appointmentRequestDTO) throws ParseException {
        AppointmentRequest apreq = modelMapper.map(findById(appointmentRequestDTO.getId()),AppointmentRequest.class);

        Optional<AppointmentRequest> appointmentRequest = appointmentRequestRepository.findById(appointmentRequestDTO.getId());

        if(appointmentRequest.isPresent()) {
            apreq.setPatient(appointmentRequest.get().getPatient());
        }

        //Doctor doctor = appointmentRequest.get().getDoctor();

        Long id = Long.parseLong(roomId);

        OperationRoomDTO roomDTO = operationRoomService.findById(id);

        apreq.setAppointmentRequestStatus(AppointmentRequestStatus.WAITING);

        if(roomDTO != null) {

            //apreq.setDoctor(doctor);

            Time t = Time.valueOf(examtime);
            apreq.setStartTime(t);

            Time endtimeTime = Time.valueOf(endtime);
            apreq.setEndTime(endtimeTime);

            Date date = Date.valueOf(examdate);
            apreq.setStart(date);

            apreq.setRoomNumber(roomDTO.getNumber());

            LocalDate requestDate = LocalDate.fromDateFields(date);

            if(appointmentRequest.get().getDoctor().getAppointments().isEmpty()) {

                apreq.setDoctor(appointmentRequest.get().getDoctor());

            } else {
                List<Doctor> doctors = doctorService.findAllDoctors();

                List<Doctor> typeDoctors = new ArrayList<>();

                for(Doctor d : doctors) {
                    if(d.getExamType().getName().equals(apreq.getType().getName())) {
                        Set<Holiday> doctorHolidays = d.getHolidays();
                        for(Holiday h : doctorHolidays) {

                            org.joda.time.LocalTime holidayStart = org.joda.time.LocalTime.fromDateFields(h.getStart());
                            org.joda.time.LocalTime holidayEnd = org.joda.time.LocalTime.fromDateFields(h.getEnd());

                            if((!holidayStart.equals(requestDate)) || (holidayStart.isBefore(requestDate) && holidayEnd.isBefore(requestDate)) || (holidayStart.isAfter(requestDate))) {
                                typeDoctors.add(d);
                            }
                        }

                    }
                }

                for(Doctor d : typeDoctors) {

                        for(Appointment a : d.getAppointments()) {

                            org.joda.time.LocalTime requestStartTime = org.joda.time.LocalTime.fromDateFields(t);
                            org.joda.time.LocalTime requestEndTime = org.joda.time.LocalTime.fromDateFields(endtimeTime);

                            org.joda.time.LocalTime appointmentStartTime = org.joda.time.LocalTime.fromDateFields(a.getStartTime());
                            org.joda.time.LocalTime appointmentEndTime = org.joda.time.LocalTime.fromDateFields(a.getEndTime());

                            LocalDate appointmentDate = LocalDate.fromDateFields(a.getStart());

                            if(appointmentDate.isEqual(requestDate)) {
                                if(appointmentStartTime.isEqual(requestStartTime)) {

                                    typeDoctors.remove(d);

                                } else if(appointmentStartTime.isBefore(requestStartTime) && appointmentEndTime.isBefore(requestEndTime)) {

                                    continue;

                                } else if(appointmentStartTime.isAfter(requestStartTime) && appointmentEndTime.isAfter(requestEndTime)) {

                                    continue;

                                } else if(appointmentStartTime.isBefore(requestStartTime) && appointmentStartTime.isBefore(requestEndTime)) {

                                    typeDoctors.remove(d);

                                }
                            }

                        }

                }

                int randomDoctor = (int)(Math.random() * typeDoctors.size());
                apreq.setDoctor(typeDoctors.get(randomDoctor));
            }

            apreq = appointmentRequestRepository.save(apreq);

            return true;

        }

        return  false;

    }

    public boolean sendRequest(String roomId, String examdate, String examtime, String endtime, AppointmentRequestDTO appointmentRequestDTO, String id) {

        Long requestId = Long.parseLong(id);
        Optional<AppointmentRequest> appointmentRequest = appointmentRequestRepository.findById(requestId);
        Patient patient = appointmentRequest.get().getPatient();

        if(patient != null) {
            try {
                emailService.sendAppointmentRequest(patient, examdate, examtime, endtime);
            } catch (Exception e) {
                return false;
            }

            appointmentRequest.get().setAppointmentRequestStatus(AppointmentRequestStatus.WAITING);
            appointmentRequestRepository.save(appointmentRequest.get());
            return true;
        }

        return false;

    }

    public List<AppointmentRequestDTO> findMyExams() {

        List<AppointmentRequest> appointmentRequests = appointmentRequestRepository.findAll();

        List<AppointmentRequestDTO> appointmentRequestDTOS = new ArrayList<AppointmentRequestDTO>();
        for (AppointmentRequest h : appointmentRequests) {
            if(h.getAppointmentRequestStatus() == AppointmentRequestStatus.WAITING)
            {
                appointmentRequestDTOS.add(modelMapper.map(h, AppointmentRequestDTO.class));
            }

        }

        return appointmentRequestDTOS;
    }
}



