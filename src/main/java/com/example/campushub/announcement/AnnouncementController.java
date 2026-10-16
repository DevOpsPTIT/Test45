package com.example.campushub.announcement;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementService service;

    public AnnouncementController(AnnouncementService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Announcement>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Announcement>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(service.findByCategory(category));
    }

    @PostMapping
    public ResponseEntity<Announcement> create(@Valid @RequestBody Announcement announcement) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(announcement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Announcement> update(@PathVariable Long id,
                                               @Valid @RequestBody Announcement announcement) {
        return ResponseEntity.ok(service.update(id, announcement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
