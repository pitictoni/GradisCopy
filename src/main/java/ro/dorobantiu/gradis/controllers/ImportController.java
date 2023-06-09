package ro.dorobantiu.gradis.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.dorobantiu.gradis.DTOs.AuthorDTO;
import ro.dorobantiu.gradis.DTOs.DepartmentDTO;
import ro.dorobantiu.gradis.DTOs.FacultyDTO;
import ro.dorobantiu.gradis.DTOs.UserDTO;
import ro.dorobantiu.gradis.services.AuthorServices;
import ro.dorobantiu.gradis.services.DepartmentServices;
import ro.dorobantiu.gradis.services.FacultyServices;
import ro.dorobantiu.gradis.services.UserServices;

import java.io.IOException;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/imports")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ImportController {
    private static final Logger log = LoggerFactory.getLogger(TestRestController.class);
    @Autowired
    FacultyServices facultyServices;
    @Autowired
    UserServices userServices;
    @Autowired
    DepartmentServices departmentServices;
    @Autowired
    AuthorServices authorServices;

    @PostMapping(value = "/users", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public List<UserDTO> importUsers(@RequestPart MultipartFile file) throws IOException {
        return userServices.importUsers(file.getInputStream());
    }

    @PostMapping(value = "/authors", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public List<AuthorDTO> importAuthors(@RequestPart MultipartFile file) throws IOException {
        return authorServices.importAuthors(file.getInputStream());
    }

    @PostMapping(value = "/faculties", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Collection<FacultyDTO> importFaculties(@RequestPart MultipartFile file) throws IOException {
        return facultyServices.importFaculties(file.getInputStream());
    }

    @PostMapping(value = "/departments", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public List<DepartmentDTO> importDepartments(@RequestPart MultipartFile file) throws IOException {
        return departmentServices.importDepartments(file.getInputStream());
    }

    @PostMapping(value = "/all", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String importAll(@RequestPart MultipartFile file) throws IOException {
        facultyServices.importFaculties(file.getInputStream());
        departmentServices.importDepartments(file.getInputStream());
        userServices.importUsers(file.getInputStream());
        authorServices.importAuthors(file.getInputStream());
        return "Faculties and Departments imported";
    }

}
