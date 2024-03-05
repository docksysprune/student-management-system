package com.oceballos.sms;

import com.oceballos.sms.entity.Student;
import com.oceballos.sms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void run(String... args) throws Exception {
        if (studentRepository.count() == 0) {
            studentRepository.save(new Student(1L, "Isela", "Miranda", "imiranda@example.com"));
            studentRepository.save(new Student(2L, "Gustavo", "Ceballos", "gceballos6@example.com"));
        }
    }
}
