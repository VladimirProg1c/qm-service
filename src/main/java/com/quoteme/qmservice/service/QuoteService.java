package com.quoteme.qmservice.service;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.quoteme.qmservice.domain.Category;
import com.quoteme.qmservice.domain.Quote;
import com.quoteme.qmservice.domain.Tag;
import com.quoteme.qmservice.dto.QuoteDto;
import com.quoteme.qmservice.dto.UserDto;
import com.quoteme.qmservice.mapper.QuoteMapper;
import com.quoteme.qmservice.repository.QuoteRepository;
import com.quoteme.qmservice.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuoteService {

    private QuoteRepository quoteRepository;

    private UserService userService;

    private QuoteMapper quoteMapper;

    @Autowired
    public QuoteService(QuoteRepository quoteRepository, UserService userService, QuoteMapper quoteMapper) {
        this.quoteRepository = quoteRepository;
        this.userService = userService;
        this.quoteMapper = quoteMapper;
    }

    public QuoteDto create(QuoteDto quoteDto, String email) {
        Quote quote = quoteMapper.mapToQuote(quoteDto);
        UserDto user = userService.get(email).get();

        quote.setId(UUID.randomUUID());
        quote.setUserId(user.getId());
        quote.setAuthor(getAuthor(quote, user));
        quote.setCreationDate(DateUtils.getCurrentTime());
        setTags(quote, quoteDto);

        Quote createdQuote = quoteRepository.save(quote);
        return quoteMapper.mapToQuoteDto(createdQuote);
    }

    private void setTags(Quote quote, QuoteDto quoteDto) {
        if (CollectionUtils.isNotEmpty(quoteDto.getTags())) {
            quote.setTags(
                    quoteDto.getTags()
                            .stream()
                            .map(tag -> new Tag(tag))
                            .collect(Collectors.toSet())
            );
        }
    }

    private String getAuthor(Quote quote, UserDto user) {
        if (StringUtils.isNotBlank(quote.getAuthor())) {
            return quote.getAuthor();
        }

        return user.getFirstName() + " " + user.getSecondName();
    }

    public Page<QuoteDto> quotes(UUID userId, Category category, String text,
                                 Pageable pageable, String requestingUserEmail) {
        boolean ownQuotes = requestingUserEmail != null && getUserId(requestingUserEmail).equals(userId);

        if (ownQuotes) {
            return quoteRepository
                    .findAll(userId, category, text, pageable)
                    .map(quoteMapper::mapToQuoteDto);
        }

        return quoteRepository
                .findAllPublic(userId, category, text, pageable)
                .map(quoteMapper::mapToQuoteDto);
    }

    public Optional<QuoteDto> getQuoteDto(UUID id) {
        Optional<Quote> quote = quoteRepository.findByIdAndDeletedFalse(id);
        return quote.map(quoteMapper::mapToQuoteDto);
    }

    public Optional<Quote> getQuote(UUID id) {
        return quoteRepository.findByIdAndDeletedFalse(id);
    }

    @Transactional
    public void deleteQuote(Quote quote, String userEmail) {
        checkUserRightsToModifyQuote(userEmail, quote);
        quoteRepository.delete(quote.getId());
    }

    public QuoteDto update(Quote existentQuote, QuoteDto updatedQuote, String userEmail) {
        checkUserRightsToModifyQuote(userEmail, existentQuote);

        Quote quote = quoteMapper.mapToQuote(updatedQuote);
        setUnmodifiableFields(existentQuote, quote);

        Quote savedQuote = quoteRepository.save(quote);
        return quoteMapper.mapToQuoteDto(savedQuote);
    }

    private void setUnmodifiableFields(Quote existentQuote, Quote quote) {
        quote.setId(existentQuote.getId());
        quote.setUserId(existentQuote.getUserId());
        quote.setCreationDate(existentQuote.getCreationDate());
        quote.setLastModificationDate(DateUtils.getCurrentTime());
    }

    public void checkUserRightsToModifyQuote(String userEmail, Quote quote) {
        UUID userId = getUserId(userEmail);
        if (!userId.equals(quote.getUserId())) {
            throw new AccessDeniedException("User does not have rights to update this resource");
        }
    }

    private UUID getUserId(String userEmail) {
        UserDto userDto = userService.get(userEmail).get();
        return userDto.getId();
    }

}
