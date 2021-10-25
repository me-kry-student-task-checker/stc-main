package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.user.client.EmailClient;
import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.controller.dto.StudentDto;
import hu.me.iit.malus.thesis.user.controller.dto.TeacherDto;
import hu.me.iit.malus.thesis.user.controller.dto.UserDto;
import hu.me.iit.malus.thesis.user.model.*;
import hu.me.iit.malus.thesis.user.model.factory.UserFactory;
import hu.me.iit.malus.thesis.user.repository.ActivationTokenRepository;
import hu.me.iit.malus.thesis.user.repository.AdminRepository;
import hu.me.iit.malus.thesis.user.repository.StudentRepository;
import hu.me.iit.malus.thesis.user.repository.TeacherRepository;
import hu.me.iit.malus.thesis.user.service.UserService;
import hu.me.iit.malus.thesis.user.service.converter.Converter;
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

    @Test(expected = UserNotFoundException.class)
    public void whenUserIsNotFound_thenGetTeacherByEmailThrowsException() throws DatabaseOperationFailedException, UserNotFoundException {
        // GIVEN
        String teacherEmail = "teacher@example.com";
        when(teacherRepository.findByEmail(any())).thenReturn(Optional.empty());

        // WHEN
        userService.getTeacherByEmail(teacherEmail);

        // THEN
    }

    @Test(expected = DataRetrievalFailureException.class)
    public void whenDatabaseErrorOccures_thenGetTeacherByEmailThrowsException() throws DatabaseOperationFailedException, UserNotFoundException {
        // GIVEN
        String teacherEmail = "teacher@example.com";
        when(teacherRepository.findByEmail(any())).thenThrow(DataRetrievalFailureException.class);

        // WHEN
        userService.getTeacherByEmail(teacherEmail);

        // THEN
    }

    @Test
    public void getTeacherByEmail() throws UserNotFoundException, DatabaseOperationFailedException {
        // GIVEN
        String teacherEmail = "teacher@example.com";
        String teacherFirstName = "First";
        String teacherLastName = "Last";
        String teacherPassword = "psw";
        Teacher teacher = new Teacher(teacherEmail, teacherPassword, teacherFirstName, teacherLastName, List.of());

        when(teacherRepository.findByEmail(any())).thenReturn(Optional.of(teacher));

        // WHEN
        TeacherDto teacherDto = userService.getTeacherByEmail(teacherEmail);

        // THEN
        verify(teacherRepository, times(1)).findByEmail(teacherEmail);
        Assertions.assertThat(teacherDto.getEmail()).isEqualTo(teacherEmail);
        Assertions.assertThat(teacherDto.getFirstName()).isEqualTo(teacherFirstName);
        Assertions.assertThat(teacherDto.getLastName()).isEqualTo(teacherLastName);
    }

    @Test(expected = UserNotFoundException.class)
    public void whenUserIsNotFound_thenGetTeacherByCreatedCourseIdThrowsException() throws DatabaseOperationFailedException, UserNotFoundException {
        // GIVEN
        Long courseId = 16L;
        when(teacherRepository.findByCreatedCourseId(any())).thenReturn(Optional.empty());

        // WHEN
        userService.getTeacherByCreatedCourseId(courseId);

        // THEN
    }

    @Test(expected = DataRetrievalFailureException.class)
    public void whenDatabaseErrorOccured_thenGetTeacherByCreatedCourseIdThrowsException() throws UserNotFoundException, DatabaseOperationFailedException {
        // GIVEN
        Long courseId = 16L;
        when(teacherRepository.findByCreatedCourseId(any())).thenThrow(DataRetrievalFailureException.class);

        // WHEN
        userService.getTeacherByCreatedCourseId(courseId);

        // THEN
    }

    @Test
    public void getTeacherByCreatedCourseId() throws UserNotFoundException, DatabaseOperationFailedException {
        // GIVEN
        Long courseId = 16L;
        Teacher teacher = new Teacher("teacher@example.com", "psw1", "First1", "Last1", List.of(courseId, 18L, 22L));

        when(teacherRepository.findByCreatedCourseId(any())).thenReturn(Optional.of(teacher));

        // WHEN
        TeacherDto teacherDto = userService.getTeacherByCreatedCourseId(courseId);

        // THEN
        verify(teacherRepository, times(1)).findByCreatedCourseId(courseId);
        Assertions.assertThat(teacherDto.getEmail()).isEqualTo(teacher.getEmail());
        Assertions.assertThat(teacherDto.getFirstName()).isEqualTo(teacher.getFirstName());
        Assertions.assertThat(teacherDto.getLastName()).isEqualTo(teacher.getLastName());
    }

    @Test(expected = UserNotFoundException.class)
    public void whenUserIsNotFound_thenGetRelatedCourseIdsThrowsException() throws UserNotFoundException {
        // GIVEN
        String userEmail = "teacher@example.com";
        when(teacherRepository.findByEmail(any())).thenReturn(Optional.empty());

        // WHEN
        userService.getRelatedCourseIds(userEmail);

        // THEN
    }

    @Test
    public void getRelatedCourseIds() throws UserNotFoundException {
        Set<Long> relatedCourseIds;

        // GIVEN
        String userEmail = "teacher@example.com";
        Teacher teacher = new Teacher(userEmail, "psw1", "First1", "Last1", List.of(18L, 22L));
        Student student = new Student(userEmail, "psw1", "First1", "Last1", List.of(18L));

        when(teacherRepository.findByEmail(any())).thenReturn(Optional.of(teacher));
        relatedCourseIds = userService.getRelatedCourseIds(userEmail);
        verify(teacherRepository, times(1)).findByEmail(userEmail);
        Assertions.assertThat(relatedCourseIds.size()).isEqualTo(teacher.getCreatedCourseIds().size());

        when(studentRepository.findByEmail(any())).thenReturn(Optional.of(student));
        relatedCourseIds = userService.getRelatedCourseIds(userEmail);
        verify(studentRepository, times(2)).findByEmail(userEmail);
        Assertions.assertThat(relatedCourseIds.size()).isEqualTo(student.getAssignedCourseIds().size());
    }

    @Test(expected = UserNotFoundException.class)
    public void whenUserIsNotFound_thenIsRelatedToCourseThrowsException() throws UserNotFoundException {
        // GIVEN
        String userEmail = "teacher@example.com";
        Long courseId = 16L;
        when(teacherRepository.findByEmail(any())).thenReturn(Optional.empty());

        // WHEN
        userService.isRelatedToCourse(userEmail, courseId);

        // THEN
    }

    @Test
    public void isRelatedToCourse() throws UserNotFoundException {
        boolean isRelatedToCourse;

        // GIVEN
        String userEmail = "test@example.com";
        Long courseId = 16L;
        Teacher teacher = new Teacher(userEmail, "psw1", "First1", "Last1", List.of(courseId, 18L, 22L));
        Student student = new Student(userEmail, "psw1", "First1", "Last1", List.of(courseId, 18L));

        when(teacherRepository.findByEmail(any())).thenReturn(Optional.of(teacher));
        isRelatedToCourse = userService.isRelatedToCourse(userEmail, courseId);
        verify(teacherRepository, times(1)).findByEmail(userEmail);
        Assertions.assertThat(isRelatedToCourse).isEqualTo(teacher.getCreatedCourseIds().contains(courseId));

        when(studentRepository.findByEmail(any())).thenReturn(Optional.of(student));
        isRelatedToCourse = userService.isRelatedToCourse(userEmail, courseId);
        verify(studentRepository, times(2)).findByEmail(userEmail);
        Assertions.assertThat(isRelatedToCourse).isEqualTo(student.getAssignedCourseIds().contains(courseId));

        when(studentRepository.findByEmail(any())).thenReturn(Optional.empty());
        isRelatedToCourse = userService.isRelatedToCourse(userEmail, courseId);
        verify(studentRepository, times(3)).findByEmail(userEmail);
        Assertions.assertThat(isRelatedToCourse).isEqualTo(true);
    }

    @Test(expected = UserNotFoundException.class)
    public void whenUserIsNotFound_thenGetAnyUserByEmailThrowsException() throws UserNotFoundException {
        // GIVEN
        String userEmail = "teacher@example.com";
        when(teacherRepository.findByEmail(any())).thenReturn(Optional.empty());

        // WHEN
        userService.getAnyUserByEmail(userEmail);

        // THEN
    }

    @Test
    public void getAnyUserByEmail() throws UserNotFoundException {
        // GIVEN
        String email = "teacher@example.com";
        Teacher teacher = new Teacher(email, "psw1", "First1", "Last1", List.of());

        when(teacherRepository.findByEmail(any())).thenReturn(Optional.of(teacher));

        // WHEN
        Teacher resultTeacher = (Teacher) userService.getAnyUserByEmail(email);

        // THEN
        verify(teacherRepository, times(1)).findByEmail(email);
        Assertions.assertThat(resultTeacher.getEmail()).isEqualTo(teacher.getEmail());
        Assertions.assertThat(resultTeacher.getFirstName()).isEqualTo(teacher.getFirstName());
        Assertions.assertThat(resultTeacher.getLastName()).isEqualTo(teacher.getLastName());
    }

    @Test(expected = UserNotFoundException.class)
    public void whenUserIsNotFound_thenGetAnyUserDtoByEmailThrowsException() throws UserNotFoundException {
        // GIVEN
        String userEmail = "teacher@example.com";
        when(teacherRepository.findByEmail(any())).thenReturn(Optional.empty());

        // WHEN
        userService.getAnyUserDtoByEmail(userEmail);

        // THEN
    }

    @Test
    public void getAnyUserDtoByEmail() throws UserNotFoundException {
        // GIVEN
        String email = "teacher@example.com";

        Teacher teacher = new Teacher(email, "psw1", "First1", "Last1", List.of());
        UserDto teacherDto = Converter.createUserDtoFromUser(teacher);
        when(teacherRepository.findByEmail(any())).thenReturn(Optional.of(teacher));

        // WHEN
        UserDto resultDto = userService.getAnyUserDtoByEmail(email);

        // THEN
        verify(teacherRepository, times(1)).findByEmail(email);
        Assertions.assertThat(resultDto.getEmail()).isEqualTo(teacherDto.getEmail());
        Assertions.assertThat(resultDto.getFirstName()).isEqualTo(teacherDto.getFirstName());
        Assertions.assertThat(resultDto.getLastName()).isEqualTo(teacherDto.getLastName());
    }

    @Test
    public void removeCourseIdFromRelatedLists() {
        // GIVEN
        Long courseId = 16L;

        List<Long> courses1 = new ArrayList<>();
        courses1.add(courseId);
        courses1.add(22L);

        List<Long> courses2 = new ArrayList<>();
        courses2.add(courseId);

        Student student1 = new Student("stud1@example.com", "psw1", "First1", "Last1", courses1);
        Student student2 = new Student("stud2@example.com", "psw2", "First2", "Last2", courses2);
        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);

        List<Long> coursesTeacher = new ArrayList<>();
        coursesTeacher.add(courseId);
        coursesTeacher.add(22L);

        Teacher teacher = new Teacher("teacher1@example.com", "psw1", "First1", "Last1", coursesTeacher);

        when(studentRepository.findAllAssignedForCourseId(any())).thenReturn(studentList);
        when(teacherRepository.findByCreatedCourseId(any())).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        // WHEN
        userService.removeCourseIdFromRelatedLists(courseId);

        // THEN
        verify(studentRepository, times(1)).findAllAssignedForCourseId(courseId);
        verify(teacherRepository, times(1)).findByCreatedCourseId(courseId);
        verify(teacherRepository, times(1)).save(teacher);

        boolean hasCourseIdStudent = studentList
                .stream()
                .anyMatch(student -> student.getAssignedCourseIds().contains(courseId));
        boolean hasCourseIdTeacher = teacher.getCreatedCourseIds().contains(courseId);
        Assertions.assertThat(hasCourseIdStudent).isEqualTo(false);
        Assertions.assertThat(hasCourseIdTeacher).isEqualTo(false);
    }
}