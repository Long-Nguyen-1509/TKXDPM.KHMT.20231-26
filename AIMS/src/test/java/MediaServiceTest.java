import com.example.Entity.Media;
import com.example.Repository.MediaRepository;
import com.example.Service.MediaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MediaServiceTest {
    private MediaService mediaService;

    @BeforeEach
    public void setUp() {
        this.mediaService = new MediaService();
    }

    @Test
    public void testCheckAvailability_MediaNotPresent() {
        MediaRepository mockRepository = Mockito.mock(MediaRepository.class);
        Mockito.when(mockRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        MediaService mediaService = new MediaService(mockRepository);
        assertFalse(mediaService.checkAvailability(1, 1));
    }

    @Test
    public void testCheckAvailability_RequiredQuantityEqualToMediaQuantity() {
        Media fakeMedia = createFakeMedia(1, 5);
        MediaRepository mockRepository = Mockito.mock(MediaRepository.class);
        Mockito.when(mockRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(fakeMedia));

        MediaService mediaService = new MediaService(mockRepository);
        assertTrue(mediaService.checkAvailability(1, 5));
    }

    @Test
    public void testCheckAvailability_RequiredQuantityLessThanMediaQuantity() {
        Media fakeMedia = createFakeMedia(1, 10);
        MediaRepository mockRepository = Mockito.mock(MediaRepository.class);
        Mockito.when(mockRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(fakeMedia));

        MediaService mediaService = new MediaService(mockRepository);
        assertTrue(mediaService.checkAvailability(1, 5));
    }

    @Test
    public void testCheckAvailability_RequiredQuantityGreaterThanMediaQuantity() {
        Media fakeMedia = createFakeMedia(1, 5);
        MediaRepository mockRepository = Mockito.mock(MediaRepository.class);
        Mockito.when(mockRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(fakeMedia));

        MediaService mediaService = new MediaService(mockRepository);
        assertFalse(mediaService.checkAvailability(1, 10));
    }

    // Helper method to create a fake Media
    private Media createFakeMedia(int id, int quantity) {
        Media fakeMedia = new Media();
        fakeMedia.setId(id);
        fakeMedia.setQuantity(quantity);
        return fakeMedia;
    }
}
