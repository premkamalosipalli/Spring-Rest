package com.spring.springdemo.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
@RequestMapping("/api")
public class StudentRestController {
	
	private List<Student> theStudents;
	
	//define @PostConstructor to Load the Student data Only once
	
	@PostConstruct
	public void loadData() {
		
		theStudents=new ArrayList<>();
		
		theStudents.add(new Student("Jackson", "PostMan"));
		theStudents.add(new Student("Mario", "Rossi"));
		theStudents.add(new Student("Mary", "Smith"));
		
	}
	
	//define endpoint fr "/students" - return list of students
	
	@GetMapping("/students")
	public List<Student> getStudents(){
		
	
		return theStudents;
		
	}
	
	//define endpoint for "/students/{studentId}" - return studentId
	
	@GetMapping("/students/{studentId}")
	public Student getStudent(@PathVariable int studentId) {
	
		//chech the studentId list size
		if(studentId>=theStudents.size() || (studentId<0)) {
			throw new StudentNotFoundException("Student id not found Exception-"+studentId);
		}
		
		
		return theStudents.get(studentId);
	}

	//Add an Exception Handler using @ExceptionHandler
	
	@ExceptionHandler
	public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc){
		
		StudentErrorResponse error=new StudentErrorResponse();
		
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	
	//Add Custom Exception to handle any Exception
	
	@ExceptionHandler
	public ResponseEntity<StudentErrorResponse> handleException(Exception exc){
		
		StudentErrorResponse error=new StudentErrorResponse();
		
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
}
