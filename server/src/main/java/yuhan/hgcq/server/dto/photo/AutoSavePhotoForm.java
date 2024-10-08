package yuhan.hgcq.server.dto.photo;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AutoSavePhotoForm implements Serializable {
    private Long teamId;
    private List<MultipartFile> files;
    private List<String> creates;
}
