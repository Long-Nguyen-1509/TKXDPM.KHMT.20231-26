package com.example.Service;

import com.example.Entity.Media;
import com.example.Repository.MediaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class MediaService {
    private MediaRepository mediaRepository = new MediaRepository();

    public Media getMedia(int id) {
        return mediaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Media> getAll() { return mediaRepository.findAll(); }

    public void merge(Media media) { mediaRepository.merge(media); }

    public void delete(Media media) { mediaRepository.delete(media); }

    public boolean checkAvailability(int id, int requiredQuantity) {
        Optional<Media> media = mediaRepository.findById(id);
        return media.filter(value -> requiredQuantity <= value.getQuantity()).isPresent();
    }
}
