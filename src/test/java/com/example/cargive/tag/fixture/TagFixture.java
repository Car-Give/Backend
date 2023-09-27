package com.example.cargive.tag.fixture;

import com.example.cargive.domain.tag.entity.Tag;
import com.example.cargive.global.template.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TagFixture {
    TAG_1("태그1", Status.NORMAL),
    TAG_2("태그2", Status.NORMAL),
    TAG_3("태그3", Status.NORMAL)
    ;
    private final String name;
    private final Status status;

    public Tag createEntity() {
        return new Tag(name);
    }
}
