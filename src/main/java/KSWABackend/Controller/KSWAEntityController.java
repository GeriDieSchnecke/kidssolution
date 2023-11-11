package KSWABackend.Controller;

import KSWABackend.Model.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RestController
@RequestMapping("/api/")
@SpringBootApplication
@ComponentScan
@EntityScan
public class KSWAEntityController implements WebMvcConfigurer {
    String url = "jdbc:mysql://localhost:3307/kswa";
    String user = "root";
    String pass = "root";
    Connection con = DriverManager.getConnection(url, user, pass);


    public KSWAEntityController() throws SQLException {
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:63342")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(false)
                .maxAge(3600);
    }

    @GetMapping("/children/{id}")
    public ResponseEntity<?> getChildren(@PathVariable Long id) {
        String childQuery = "SELECT * FROM children WHERE id = ?";
        String subjectsQuery = "SELECT * FROM subjects WHERE child_id = ?";
        KSWAChildren kswaChildren = new KSWAChildren();
        try (PreparedStatement childStatement = con.prepareStatement(childQuery)) {
            childStatement.setLong(1, id);
            ResultSet childResultSet = childStatement.executeQuery();
            while (childResultSet.next()) {
                kswaChildren.setId(childResultSet.getLong("id"));
                kswaChildren.setChprename(childResultSet.getString("chprename"));
                kswaChildren.setChname(childResultSet.getString("chname"));
                kswaChildren.setChbirthday(childResultSet.getString("chbirthday"));
                try (PreparedStatement subjectsStatement = con.prepareStatement(subjectsQuery)) {
                    subjectsStatement.setLong(1, id);
                    ResultSet subjectsResultSet = subjectsStatement.executeQuery();
                    List<String> subjectsList = new ArrayList<>();
                    while (subjectsResultSet.next()) {
                        String subjectName = subjectsResultSet.getString("suname");
                        subjectsList.add(subjectName);
                    }
                    kswaChildren.setChsubjects(subjectsList);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (kswaChildren.getId() != null) {
            return ResponseEntity.ok().body(kswaChildren);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("KSWAChildren mit ID " + id + " wurde nicht gefunden");
        }
    }

    @GetMapping("/children")
    public ResponseEntity<?> getAllChildren() {
        String query = "SELECT * FROM children";

        List<KSWAChildren> childrenList = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                KSWAChildren kswaChildren = new KSWAChildren();
                kswaChildren.setId(resultSet.getLong("id"));
                kswaChildren.setChprename(resultSet.getString("chprename"));
                kswaChildren.setChname(resultSet.getString("chname"));
                kswaChildren.setChbirthday(resultSet.getString("chbirthday"));
                childrenList.add(kswaChildren);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(childrenList);
    }


    @PostMapping("/children")
    public ResponseEntity<?> createChildren(@Valid @RequestBody KSWAChildren kswaChildren) {
        String query = "INSERT INTO children (chprename, chname, chbirthday) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, kswaChildren.getChprename());
            preparedStatement.setString(2, kswaChildren.getChname());
            preparedStatement.setString(3, kswaChildren.getChbirthday());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Das Kind konnte nicht erstellt werden");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    kswaChildren.setId(generatedKeys.getLong(1));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Das Kind konnte nicht erstellt werden");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(kswaChildren);
    }

    @PostMapping("/children/{id}/add-subject")
    public ResponseEntity<?> addSubjectToChildren(@PathVariable Long id, @RequestBody KSWASubject kswaSubject) {
        String query = "INSERT INTO subjects (child_id, suname) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, kswaSubject.getSuname());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Fach erfolgreich zum Kind hinzugefügt");
    }


    @PutMapping("/children/{id}")
    public ResponseEntity<?> updateChildren(@PathVariable Long id, @Valid @RequestBody KSWAChildren updatedChildren) {
        String query = "UPDATE children SET chprename = ?, chname = ?, chbirthday = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, updatedChildren.getChprename());
            preparedStatement.setString(2, updatedChildren.getChname());
            preparedStatement.setString(3, updatedChildren.getChbirthday());
            preparedStatement.setLong(4, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("KSWAChildren mit ID " + id + " wurde nicht gefunden");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(updatedChildren);
    }

    @DeleteMapping("/children/{id}")
    public ResponseEntity<?> deleteChildren(@PathVariable Long id) {
        String query = "DELETE FROM children WHERE id = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("KSWAChildren mit ID " + id + " wurde nicht gefunden");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body("KSWAChildren mit ID " + id + " erfolgreich gelöscht");
    }

    @GetMapping("/subjects")
    public ResponseEntity<?> getAllSubjects() {
        String query = "SELECT * FROM subjects";
        List<KSWASubject> subjectList = new ArrayList<>();
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                KSWASubject kswaSubject = new KSWASubject();
                kswaSubject.setSuname(resultSet.getString("suname"));
                kswaSubject.setSugrade(resultSet.getDouble("sugrade"));
                // Implement logic for retrieving and setting tests for the subject
                subjectList.add(kswaSubject);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(subjectList);
    }

    @PostMapping("/subjects")
    public ResponseEntity<?> createSubject(@Valid @RequestBody KSWASubject kswaSubject) {
        String query = "INSERT INTO subjects (suname, sugrade) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, kswaSubject.getSuname());
            preparedStatement.setDouble(2, kswaSubject.getSugrade());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create subject");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Implement logic for retrieving and setting tests for the subject
                    kswaSubject.setId(generatedKeys.getLong(1));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create subject");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(kswaSubject);
    }

    @PostMapping("/subjects/{id}/add-test")
    public ResponseEntity<?> addTestToSubject(@PathVariable Long id, @RequestBody KSWATest kswaTest) {
        String query = "INSERT INTO tests (subject_id, tegrade, tename, tefactor, tedate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setDouble(2, kswaTest.getTegrade());
            preparedStatement.setString(3, kswaTest.getTename());
            preparedStatement.setDouble(4, kswaTest.getTefactor());
            preparedStatement.setDate(5, new java.sql.Date(kswaTest.getTedate().getTime()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Test successfully added to subject");
    }

    @PutMapping("/subjects/{id}")
    public ResponseEntity<?> updateSubject(@PathVariable Long id, @Valid @RequestBody KSWASubject updatedSubject) {
        String query = "UPDATE subjects SET suname = ?, sugrade = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, updatedSubject.getSuname());
            preparedStatement.setDouble(2, updatedSubject.getSugrade());
            preparedStatement.setLong(3, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("KSWASubject with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(updatedSubject);
    }

    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable Long id) {
        String query = "DELETE FROM subjects WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("KSWASubject with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body("KSWASubject with ID " + id + " successfully deleted");
    }

    @GetMapping("/tests")
    public ResponseEntity<?> getAllTests() {
        String query = "SELECT * FROM tests";
        List<KSWATest> testList = new ArrayList<>();
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                KSWATest kswaTest = new KSWATest();
                kswaTest.setTegrade(resultSet.getDouble("tegrade"));
                kswaTest.setTename(resultSet.getString("tename"));
                kswaTest.setTefactor(resultSet.getDouble("tefactor"));
                kswaTest.setTedate(resultSet.getDate("tedate"));
                testList.add(kswaTest);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(testList);
    }

    @PostMapping("/tests")
    public ResponseEntity<?> createTest(@Valid @RequestBody KSWATest kswaTest) {
        String query = "INSERT INTO tests (tegrade, tename, tefactor, tedate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDouble(1, kswaTest.getTegrade());
            preparedStatement.setString(2, kswaTest.getTename());
            preparedStatement.setDouble(3, kswaTest.getTefactor());
            preparedStatement.setDate(4, new java.sql.Date(kswaTest.getTedate().getTime()));
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create test");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    kswaTest.setId(generatedKeys.getLong(1));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create test");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(kswaTest);
    }

    @PutMapping("/tests/{id}")
    public ResponseEntity<?> updateTest(@PathVariable Long id, @Valid @RequestBody KSWATest updatedTest) {
        String query = "UPDATE tests SET tegrade = ?, tename = ?, tefactor = ?, tedate = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setDouble(1, updatedTest.getTegrade());
            preparedStatement.setString(2, updatedTest.getTename());
            preparedStatement.setDouble(3, updatedTest.getTefactor());
            preparedStatement.setDate(4, new java.sql.Date(updatedTest.getTedate().getTime()));
            preparedStatement.setLong(5, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("KSWATest with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(updatedTest);
    }

    @DeleteMapping("/tests/{id}")
    public ResponseEntity<?> deleteTest(@PathVariable Long id) {
        String query = "DELETE FROM tests WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("KSWATest with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body("KSWATest with ID " + id + " successfully deleted");
    }
}
