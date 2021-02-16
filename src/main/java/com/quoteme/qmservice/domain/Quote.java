package com.quoteme.qmservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "quote")
public class Quote extends BaseEntity {

    @Id
    @Column(name = "id")
    @Type(type="org.hibernate.type.UUIDCharType")
    UUID id;

    @Column(name = "author")
    String author;

    @Column(name = "user_id")
    @Type(type="org.hibernate.type.UUIDCharType")
    UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    @NotNull(message = "category cannot be empty")
    Category category;

    @Column(name = "quote_text")
    @NotEmpty(message = "text cannot be empty")
    String text;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name="quote_tags",
            joinColumns={@JoinColumn(name="quote_id")},
            inverseJoinColumns={@JoinColumn(name="tag_id")})
    Set<Tag> tags;

    @Column(name = "deleted")
    boolean deleted;

    @Column(name = "private")
    boolean privateQuote;

}
