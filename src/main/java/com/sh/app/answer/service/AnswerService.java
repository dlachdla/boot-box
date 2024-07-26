package com.sh.app.answer.service;

import com.sh.app.answer.dto.AnswerCreateDto;
import com.sh.app.answer.dto.AnswerDetailDto;
import com.sh.app.answer.entity.Answer;
import com.sh.app.answer.repository.AnswerRepository;
import com.sh.app.ask.dto.AskDetailDto;
import com.sh.app.ask.entity.Ask;
import com.sh.app.ask.repository.AskRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional
@Slf4j
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AskRepository askRepository;

    public List<Answer> findAnswerByAskId(Long askId) {
        return answerRepository.findAnswerByAskId(askId);
    }

    public AnswerDetailDto findById(Long askId) {
        return answerRepository.findByAskId(askId)
                .map((answer) -> convertToAnswer(answer))
                .orElseThrow();
    }

    private AnswerDetailDto convertToAnswer(Answer answer) {
        AnswerDetailDto answerDetailDto = modelMapper.map(answer, AnswerDetailDto.class);
        System.out.println("service = " + answerDetailDto);
        return answerDetailDto;
    }


    public void createAnswer(AnswerCreateDto answerCreateDto) {
        Answer answer = answerRepository.save(convertToAnswerCreateDto(answerCreateDto));
//        log.debug("answer = {}", answer);
    }

    private Answer convertToAnswerCreateDto(AnswerCreateDto answerCreateDto) {
        return  modelMapper.map(answerCreateDto, Answer.class);
    }
}
