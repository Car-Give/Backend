package com.example.cargive.domain.tag.service;

import com.example.cargive.domain.tag.entity.Tag;
import com.example.cargive.domain.tag.entity.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagFindService {
    private final TagRepository tagRepository;

    // 사용자로부터 입력 받은 TagIdList를 통해서 Tag Entity List를 반환하는 메서드
    public List<Tag> findTagListById(List<Long> tagIdList) {
        return tagRepository.findAllById(tagIdList);
    }
}
