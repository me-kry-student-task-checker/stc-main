package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.user.client.EmailClient;
import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.controller.dto.StudentDto;
import hu.me.iit.malus.thesis.user.controller.dto.TeacherDto;
import hu.me.iit.malus.thesis.user.model.*;
import hu.me.iit.malus.thesis.user.model.factory.UserFactory;
import hu.me.iit.malus.thesis.user.repository.ActivationTokenRepository;
import hu.me.iit.malus.thesis.user.repository.AdminRepository;
import hu.me.iit.malus.thesis.user.repository.StudentRepository;
import hu.me.iit.malus.thesis.user.repository.TeacherRepository;
import hu.me.iit.malus.thesis.user.service.UserService;
import hu.me.iit.malus.thesis.user.service.exception.DatabaseOperationFailedException;
import hu.me.iit.malus.thesis.user.service.exception.EmailExistsException;
import hu.me.iit.malus.thesis.user.service.exception.UserNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private AdminRepository adminRepository;
    private ActivationTokenRepository tokenRepository;
    private EmailClient emailClient;
    private UserFactory userFactory;

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        this.studentRepository = mock(StudentRepository.class);
        this.teacherRepository = mock(TeacherRepository.class);
        this.adminRepository = mock(AdminRepository.class);
        this.tokenRepository = mock(ActivationTokenRepository.class);
        this.emailClient = mock(EmailClient.class);
        this.userFactory = mock(UserFactory.class);
        this.userService = new UserServiceImpl(studentRepository, teacherRepository, adminRepository,
                tokenRepository, emailClient, userFactory);
    }

    @Test(expected = EmailExistsException.class)
    public void whenEmailIsAlreadyExists_doNotRegisterNewUserAccount() throws EmailExistsException {
        // GIVEN
        String email = "user@example.com";
        String firstName = "First";
        String lastName = "Last";
        String role = UserRole.STUDENT.getRoleString();
        String password = "example";

        RegistrationRequest req = new RegistrationRequest();
        req.setEmail(email);
        req.setFirstName(firstName);
        req.setLastName(lastName);
        req.setRole(role);
        req.setPassword(password);
        req.setPasswordConfirm(password);

        Student student = new Student(email, password, firstName, lastName, List.of());
        when(studentRepository.findByEmail(any())).thenReturn(Optional.of(student));

        //WHEN
        this.userService.registerNewUserAccount(req);

        //THEN
    }

    @Test
    public void whenEmailIsNotExists_registerNewUserAccount() throws EmailExistsException {
        // GIVEN
        String email = "user@example.com";
        String firstName = "First";
        String lastName = "Last";
        String role = UserRole.STUDENT.getRoleString();
        String password = "example";

        RegistrationRequest req = new RegistrationRequest();
        req.setEmail(email);
        req.setFirstName(firstName);
        req.setLastName(lastName);
        req.setRole(role);
        req.setPassword(password);
        req.setPasswordConfirm(password);

        Student student = new Student(email, password, firstName, lastName, List.of());

        when(studentRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userFactory.create(req)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);

        //WHEN
        User user = this.userService.registerNewUserAccount(req);

        //THEN
        verify(studentRepository, times(1)).save(student);
        assertThat(user.getEmail(), equalTo(email));
        assertThat(user.getRole(), equalTo(UserRole.fromString(role)));
        assertThat(user.getFirstName(), equalTo(firstName));
        assertThat(user.getLastName(), equalTo(lastName));
    }

    @Test
    public void createActivationToken() {
        // GIVEN
        User user = new Admin("admin@example.com", "admin", "First", "Last");
        String token = "dummy-text";
        ActivationToken activationToken = ActivationToken.of(token, user);

        when(tokenRepository.save(any())).thenReturn(activationToken);

        // WHEN
        userService.createActivationToken(user, token);

        // THEN
        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    public void whenTokenIsInvalid_thenDoNotActivateUser() {
        // GIVEN
        String token = "dummy-text";
        when(tokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // WHEN
        boolean success = userService.activateUser(token);

        // THEN
        Assertions.assertThat(success).isFalse();
    }

    @Test
    public void whenTokenIsExpired_thenDeletesTokenAndDoNotActivateUser() {
        // GIVEN
        String token = "dummy-text";
        User user = new Admin("admin@example.com", "admin", "First", "Last");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        ActivationToken activationToken = ActivationToken.of(token, user);
        activationToken.setExpiryDate(calendar.getTime());

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(activationToken));

        // WHEN
        boolean success = userService.activateUser(token);

        // THEN
        verify(tokenRepository, times(1)).delete(activationToken);
        Assertions.assertThat(success).isFalse();
    }

    @Test
    public void whenTokenIsValid_thenActivateUser() {
        // GIVEN
        String token = "dummy-text";
        Teacher user = new Teacher("teacher@example.com", "teacher", "First", "Last", List.of());
        ActivationToken activationToken = ActivationToken.of(token, user);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(activationToken));

        // WHEN
        boolean success = userService.activateUser(token);

        // THEN
        verify(teacherRepository, times(1)).save(user);
        verify(tokenRepository, times(1)).delete(activationToken);

        Assertions.assertThat(user.isEnabled()).isTrue();
        Assertions.assertThat(success).isTrue();
    }

    @Test(expected = UserNotFoundException.class)
    public void whenUserIsNotFound_doNotSaveCourseCreation() throws DatabaseOperationFailedException, UserNotFoundException {
        // GIVEN
        String teacherEmail = "teacher@example.com";
        Long courseId = 16L;

        when(teacherRepository.findLockByEmail(teacherEmail)).thenReturn(Optional.empty());

        // WHEN
        userService.saveCourseCreation(teacherEmail, courseId);

        // THEN
    }

    @Test(expected = DatabaseOperationFailedException.class)
    public void whenDatabaseErrorOccures_saveCourseCreationThrowsError() throws DatabaseOperationFailedException, UserNotFoundException {
        // GIVEN
        String teacherEmail = "teacher@example.com";
        String teacherFirstName = "First";
        String teacherLastName = "Last";
        String teacherPassword = "pswd";

        Long courseId = 16L;
        List<Long> createdCourseIds = new ArrayList<>();
        createdCourseIds.add(courseId);

        Teacher teacher = new Teacher(teacherEmail, teacherPassword, teacherFirstName, teacherLastName, createdCourseIds);

        when(teacherRepository.findLockByEmail(teacherEmail)).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(teacher)).thenThrow(DataRetrievalFailureException.class);

        // WHEN
        userService.saveCourseCreation(teacherEmail, courseId);

        // THEN
    }

    @Test
    public void saveCourseCreation() throws DatabaseOperationFailedException, UserNotFoundException {
        // GIVEN
        String teacherEmail = "teacher@example.com";
        String teacherFirstName = "First";
        String teacherLastName = "Last";
        String teacherPassword = "pswd";

        Long courseId = 16L;
        List<Long> createdCourseIds = new ArrayList<>();
        createdCourseIds.add(courseId);

        Teacher teacher = new Teacher(teacherEmail, teacherPassword, teacherFirstName, teacherLastName, createdCourseIds);

        when(teacherRepository.findLockByEmail(teacherEmail)).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        // WHEN
        TeacherDto teacherDto = userService.saveCourseCreation(teacherEmail, courseId);

        // THEN
        verify(teacherRepository, times(1)).findLockByEmail(teacherEmail);
        verify(teacherRepository, times(1)).save(teacher);
        Assertions.assertThat(teacherDto.getEmail()).isEqualTo(teacherEmail);
        Assertions.assertThat(teacherDto.getFirstName()).isEqualTo(teacherFirstName);
        Assertions.assertThat(teacherDto.getLastName()).isEqualTo(teacherLastName);
    }

    @Test(expected = DatabaseOperationFailedException.class)
    public void whenDatabaseErrorOccures_thenAssignStudentsToCourseThrowsException() throws DatabaseOperationFailedException {
        // GIVEN
        Long courseId = 16L;
        List<Long> courseIds = new ArrayList<>();
        courseIds.add(courseId);
        String studentEmail1 = "stud1@example.com";
        String studentEmail2 = "stud2@example.com";
        List<String> studentEmails = new ArrayList<>();
        studentEmails.add(studentEmail1);
        studentEmails.add(studentEmail2);
        Student student = new Student(studentEmail1, "pswd", "First", "Last", courseIds);
        List<Student> studentList = new ArrayList<>();
        studentList.add(student);

        when(studentRepository.findAllLockByEmailIn(studentEmails)).thenReturn(studentList);
        when(studentRepository.save(any())).thenThrow(DataRetrievalFailureException.class);

        // WHEN
        userService.assignStudentsToCourse(courseId, studentEmails);

        // THEN
    }

    @Test
    public void assignStudentsToCourse() throws DatabaseOperationFailedException {
        // GIVEN
        Long courseId = 16L;
        List<Long> courseIds = new ArrayList<>();
        courseIds.add(courseId);
        String studentEmail1 = "stud1@example.com";
        String studentEmail2 = "stud2@example.com";
        List<String> studentEmails = new ArrayList<>();
        studentEmails.add(studentEmail1);
        studentEmails.add(studentEmail2);
        Student student = new Student(studentEmail1, "pswd", "First", "Last", courseIds);
        List<Student> studentList = new ArrayList<>();
        studentList.add(student);

        when(studentRepository.findAllLockByEmailIn(studentEmails)).thenReturn(studentList);

        // WHEN
        userService.assignStudentsToCourse(courseId, studentEmails);

        // THEN
        verify(studentRepository, times(studentList.size())).save(any());
    }

    @Test(expected = UserNotFoundException.class)
    public void whenUserIsNotFound_thenGetStudentByEmailThrowsException() throws DatabaseOperationFailedException, UserNotFoundException {
        // GIVEN
        String studentEmail = "stud@example.com";
        when(studentRepository.findByEmail(any())).thenReturn(Optional.empty());

        // WHEN
        userService.getStudentByEmail(studentEmail);

        // THEN
    }

    @Test(expected = DatabaseOperationFailedException.class)
    public void whenDatabaseErrorOccures_thenGetStudentByEmailThrowsException() throws DatabaseOperationFailedException, UserNotFoundException {
        // GIVEN
        String studentEmail = "stud@example.com";
        when(studentRepository.findByEmail(any())).thenThrow(DataRetrievalFailureException.class);

        // WHEN
        userService.getStudentByEmail(studentEmail);

        // THEN
    }

    @Test
    public void getStudentByEmail() throws DatabaseOperationFailedException, UserNotFoundException {
        // GIVEN
        String studentEmail = "stud@example.com";
        String studentFirstName = "First";
        String studentLastName = "Last";
        String studentPassword = "psw";
        Student student = new Student(studentEmail, studentPassword, studentFirstName, studentLastName, List.of());

        when(studentRepository.findByEmail(any())).thenReturn(Optional.of(student));

        // WHEN
        StudentDto studentDto = userService.getStudentByEmail(studentEmail);

        // THEN
        verify(studentRepository, times(1)).findByEmail(studentEmail);
        Assertions.assertThat(studentDto.getEmail()).isEqualTo(studentEmail);
        Assertions.assertThat(studentDto.getFirstName()).isEqualTo(studentFirstName);
        Assertions.assertThat(studentDto.getLastName()).isEqualTo(studentLastName);
    }

    @Test(expected = DatabaseOperationFailedException.class)
    public void whenDatabaseErrorOccured_thenGetStudentsByAssignedCourseIdThrowsException() throws DatabaseOperationFailedException {
        // GIVEN
        Long courseId = 16L;
        when(studentRepository.findAllAssignedForCourseId(any())).thenThrow(DataRetrievalFailureException.class);

        // WHEN
        userService.getStudentsByAssignedCourseId(courseId);

        // THEN
    }

    @Test
    public void getStudentsByAssignedCourseId() throws DatabaseOperationFailedException {
        // GIVEN
        Long courseId = 16L;
        Student student1 = new Student("stud1@example.com", "psw1", "First1", "Last1", List.of(courseId, 18L, 22L));
        Student student2 = new Student("stud2@example.com", "psw2", "First2", "Last2", List.of(courseId, 14L));
        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);

        when(studentRepository.findAllAssignedForCourseId(any())).thenReturn(studentList);

        // WHEN
        Set<StudentDto> studentDtoSet = userService.getStudentsByAssignedCourseId(courseId);

        // THEN
        verify(studentRepository, times(1)).findAllAssignedForCourseId(courseId);
        Assertions.assertThat(studentDtoSet.size()).isEqualTo(studentList.size());
    }

    @Test(expected = DatabaseOperationFailedException.class)
    public void whenDatabaseErrorOccured_thenGetStudentsByNotAssignedCourseIdThrowsException() throws DatabaseOperationFailedException {
        // GIVEN
        Long courseId = 16L;
        when(studentRepository.findAllNotAssignedForCourseId(any())).thenThrow(DataRetrievalFailureException.class);

        // WHEN
        userService.getStudentsByNotAssignedCourseId(courseId);

        // THEN
    }

    @Test
    public void getStudentsByNotAssignedCourseId() throws DatabaseOperationFailedException {
        // GIVEN
        Long courseId = 16L;
        Student student1 = new Student("stud1@example.com", "psw1", "First1", "Last1", List.of(18L, 22L));
        Student student2 = new Student("stud2@example.com", "psw2", "First2", "Last2", List.of(14L));
        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);

        when(studentRepository.findAllNotAssignedForCourseId(any())).thenReturn(studentList);

        // WHEN
        Set<StudentDto> studentDtoSet = userService.getStudentsByNotAssignedCourseId(courseId);

        // THEN
        verify(studentRepository, times(1)).findAllNotAssignedForCourseId(courseId);
        Assertions.assertThat(studentDtoSet.size()).isEqualTo(studentList.size());
    }
}