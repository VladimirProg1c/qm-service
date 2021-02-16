package com.quoteme.qmservice.controller;

import com.quoteme.qmservice.domain.Category;
import com.quoteme.qmservice.domain.Quote;
import com.quoteme.qmservice.dto.QuoteDto;
import com.quoteme.qmservice.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/quotes")
public class QuoteController {

    private QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping
    public ResponseEntity<QuoteDto> createQuote(@RequestBody @Valid QuoteDto quote, Principal principal) {
        QuoteDto createdQuote = quoteService.create(quote, principal.getName());
        return ResponseEntity.ok(createdQuote);
    }

    @PutMapping("/{quoteId}")
    public ResponseEntity<?> updateQuote(@PathVariable UUID quoteId,
                                         @RequestBody @Valid QuoteDto updatedQuote,
                                         Principal principal) {
        Optional<Quote> existentQuote = quoteService.getQuote(quoteId);

        if (existentQuote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        QuoteDto savedQuote = quoteService.update(existentQuote.get(), updatedQuote, principal.getName());
        return ResponseEntity.ok(savedQuote);
    }

    @GetMapping("/categories")
    public Category[] categories() {
        return Category.values();
    }

    @GetMapping
    public Page<QuoteDto> quotes(@RequestParam(required = false) UUID userId,
                                 @RequestParam(required = false) Category category,
                                 @RequestParam(required = false) String text,
                                 Pageable pageable,
                                 Principal principal) {
        String userEmail = principal != null ? principal.getName() : null;
        return quoteService.quotes(userId, category, text, pageable, userEmail);
    }

    @GetMapping("/{quoteId}")
    public ResponseEntity<QuoteDto> get(@PathVariable UUID quoteId) {
        Optional<QuoteDto> quote = quoteService.getQuoteDto(quoteId);

        if (quote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(quote.get());
    }

    @DeleteMapping("/{quoteId}")
    public ResponseEntity<?> delete(@PathVariable("quoteId") UUID quoteId, Principal principal) {
        Optional<Quote> quote = quoteService.getQuote(quoteId);

        if (quote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        quoteService.deleteQuote(quote.get(), principal.getName());
        return ResponseEntity.noContent().build();
    }
}
