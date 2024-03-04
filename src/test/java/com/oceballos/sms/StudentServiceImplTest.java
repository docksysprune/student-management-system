package com.oceballos.sms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.oceballos.sms.dto.StudentDto;
import com.oceballos.sms.entity.Student;
import com.oceballos.sms.repository.StudentRepository;
import com.oceballos.sms.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllStudentsTest() {

        when(studentRepository.findAll()).thenReturn(Arrays.asList(
                new Student(1L, "John", "Doe", "john@example.com"),
                new Student(2L, "Jane", "Doe", "jane@example.com")
        ));

        List<StudentDto> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void createStudentTest() {

        StudentDto studentDto = new StudentDto(null, "New", "Student", "new@student.com");
        Student student = new Student(null, "New", "Student", "new@student.com");
        when(studentRepository.save(any(Student.class))).thenReturn(student);


        studentService.createStudent(studentDto);


        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void getStudentByIdTest() {

        Long studentId = 1L;
        Optional<Student> studentOptional = Optional.of(new Student(studentId, "John", "Doe", "john@doe.com"));
        when(studentRepository.findById(studentId)).thenReturn(studentOptional);

        StudentDto result = studentService.getStudentById(studentId);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void deleteStudentTest() {
        // Preparación
        Long studentId = 1L;

        doNothing().when(studentRepository).deleteById(studentId);

        // Acción
        studentService.deleteStudent(studentId);

        // Verificación
        verify(studentRepository, times(1)).deleteById(studentId);
    }
}

