package ro.dorobantiu.gradis.services;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.dorobantiu.gradis.DTOs.AuthorDTO;
import ro.dorobantiu.gradis.entities.Author;
import ro.dorobantiu.gradis.helpers.ExcelUtil;
import ro.dorobantiu.gradis.repositories.AuthorRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class AuthorServices {
    private static final Logger log = LoggerFactory.getLogger(AuthorServices.class);
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    DepartmentServices departmentServices;
    @Autowired
    ExcelUtil excelUtil;

    @Autowired
    Mapper mapper;

    public List<AuthorDTO> importAuthors(InputStream excelStream) {
        Collection<Author> authors = getAuthors(excelStream);
        authorRepository.saveAll(authors);
        return authors.stream().map(x -> mapper.toDTO(x)).toList();
    }

    private Collection<Author> getAuthors(InputStream excelStream) {
        try {
            Workbook workbook = new XSSFWorkbook(excelStream);
            Sheet sheet = workbook.getSheet(ExcelUtil.SHEET);
            Iterator<Row> rows = sheet.iterator();

            HashSet<Author> authors = new HashSet<>();
            Row currentRow = rows.next(); // skip header
            while (rows.hasNext()) {
                currentRow = rows.next();
                String authorEmail = excelUtil.getCellData(currentRow, "mail");
                String authorName = excelUtil.getCellData(currentRow, "nume") + " " +
                        excelUtil.getCellData(currentRow, "prenume");
                String departmentName = excelUtil.getCellData(currentRow, "DenumireDepartament");
                authors.add(new Author(authorName, authorEmail, departmentServices.getDepartmentByName(departmentName)));
            }
            return authors;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<AuthorDTO> getAllAuthors() {
        List<AuthorDTO> authorsList = new ArrayList<>();
        authorRepository.findAll().forEach(a -> authorsList.add(mapper.toDTO(a)));
        return authorsList;
    }
}
