package com.pyrenty.akl.service;

import com.pyrenty.akl.domain.Text;
import com.pyrenty.akl.repository.TextRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

@Service
@Transactional
public class TextService {
    @Inject
    private TextRepository textRepository;

    @Transactional(readOnly = true)
    public Optional<Text> get(Long id) {
        return Optional.ofNullable(textRepository.getOne(id));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('CMS') or hasRole('ADMIN')")
    public Page<Text> getAll(Pageable pageable) {
        return textRepository.findAll(pageable);
    }

    @PreAuthorize("hasRole('CMS') or hasRole('ADMIN')")
    public Text update(Long id, Text newText) {
        Text text = textRepository.getOne(id);
        if (text != null) {
            text.setFi(newText.getFi());
            text.setEn(newText.getEn());
            textRepository.save(text);
            return text;
        }

        return null;
    }
}
