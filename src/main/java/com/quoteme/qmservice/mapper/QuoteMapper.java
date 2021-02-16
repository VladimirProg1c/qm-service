package com.quoteme.qmservice.mapper;

import com.quoteme.qmservice.domain.Quote;
import com.quoteme.qmservice.domain.Tag;
import com.quoteme.qmservice.dto.QuoteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface QuoteMapper {

    QuoteMapper INSTANCE = Mappers.getMapper(QuoteMapper.class);

    @Mapping(target = "id", ignore = true)
    Quote mapToQuote(QuoteDto quoteDto);

    default Set<Tag> tagsDtoToTags(Set<String> tags) {
        if (tags == null) {
            return Set.of();
        }

        return tags
                .stream()
                .map(tag -> new Tag(tag))
                .collect(Collectors.toSet());
    }

    QuoteDto mapToQuoteDto(Quote quote);

    default Set<String> tagsToTagsDto(Set<Tag> tags) {
        if (tags == null) {
            return Set.of();
        }

        return tags
                .stream()
                .map(tag -> tag.getName())
                .collect(Collectors.toSet());
    }

}
