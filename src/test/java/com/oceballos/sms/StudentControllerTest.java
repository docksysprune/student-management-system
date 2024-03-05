package com.oceballos.sms;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.oceballos.sms.controller.StudentController;
import com.oceballos.sms.dto.StudentDto;
import com.oceballos.sms.service.StudentService;

import java.util.Arrays;
import java.util.List;

public class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
        viewResolver.setSuffix(".jsp");
        this.mockMvc = standaloneSetup(studentController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testListStudents() throws Exception {
        List<StudentDto> students = Arrays.asList(new StudentDto(1L, "John", "Doe", "john@example.com"),
                new StudentDto(2L, "Jane", "Doe", "jane@example.com"));
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attribute("students", students));
    }

    @Test
    public void testNewStudent() throws Exception {
        mockMvc.perform(get("/students/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_student"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    public void testSaveStudentSuccess() throws Exception {
        mockMvc.perform(post("/students")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john.doe@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/students"));

        verify(studentService, times(1)).createStudent(any(StudentDto.class));
    }

    @Test
    public void testSaveStudentValidationError() throws Exception {
        mockMvc.perform(post("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_student"));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        Long studentIdToDelete = 1L;

        mockMvc.perform(get("/students/{studentId}/delete", studentIdToDelete))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/students"));

        verify(studentService, times(1)).deleteStudent(studentIdToDelete);
    }
}

