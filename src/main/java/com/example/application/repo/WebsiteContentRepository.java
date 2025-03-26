package com.example.application.repo;


import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsiteContentRepository extends JpaRepository<WebsiteContent, Long> {
    WebsiteContent findByUrl(String url);
}
