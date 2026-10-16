package com.example.campushub.announcement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByCategory(String category);
    List<Announcement> findAllByOrderByCreatedAtDesc();
}
