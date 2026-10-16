package com.example.campushub.announcement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AnnouncementService {

    private final AnnouncementRepository repository;

    public AnnouncementService(AnnouncementRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Announcement> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Announcement findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AnnouncementNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Announcement> findByCategory(String category) {
        return repository.findByCategory(category);
    }

    public Announcement create(Announcement announcement) {
        return repository.save(announcement);
    }

    public Announcement update(Long id, Announcement updated) {
        Announcement existing = findById(id);
        existing.setTitle(updated.getTitle());
        existing.setContent(updated.getContent());
        existing.setCategory(updated.getCategory());
        return repository.save(existing);
    }

    public void delete(Long id) {
        Announcement existing = findById(id);
        repository.delete(existing);
    }
}
