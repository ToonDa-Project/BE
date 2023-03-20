package com.example.toonda.rest.diary.dto;

import com.example.toonda.rest.diary.entity.Diary;
import com.example.toonda.rest.folder.dto.FolderResponseDto;
import com.example.toonda.rest.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DiaryResponseDto {

    private Long diaryId;
    private Long userId;
    private String img;
    private String content;
    private LocalDate date;
    private LocalDateTime createdAt;

    public DiaryResponseDto(User user, Diary diary) {
        this.diaryId = diary.getId();
        this.userId = user.getId();
        this.img = diary.getImg();
        this.content = diary.getContent();
        this.date = diary.getDate();
        this.createdAt = diary.getCreatedAt();
    }

    @Getter
    @NoArgsConstructor
    public static class FolderList {

        private List<FolderResponseDto.Title> folderList = new ArrayList<>();

        public void addFolder(FolderResponseDto.Title responseDto) {
            folderList.add(responseDto);
        }
    }

}
