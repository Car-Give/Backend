package com.example.cargive.domain.tag.service;

import com.example.cargive.domain.tag.entity.Tag;
import com.example.cargive.domain.tag.entity.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TagCreateService {
    private final TagRepository tagRepository;

    // 사용자로부터 입력 받은 TagNameList를 통해서 새로운 Tag Entity를 생성하는 로직
    public List<Tag> createTagList(List<String> tagList) {
        List<Tag> tagEntityList = tagList.stream()
                .map(Tag::new).toList();

        return tagRepository.saveAll(tagEntityList);
    }
}