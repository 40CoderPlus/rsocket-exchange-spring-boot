/*
 * (c) Copyright 2023 40CoderPlus. All rights reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortycoderplus.rsocket.exchange.sample.server;

import com.fortycoderplus.rsocket.exchange.sample.message.Answer;
import com.fortycoderplus.rsocket.exchange.sample.message.Person;
import com.fortycoderplus.rsocket.exchange.sample.message.Question;
import com.fortycoderplus.rsocket.exchange.sample.message.Score;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Profile("server")
@Controller
public class AnswerEndpoint {

    @MessageMapping("answer")
    public Mono<Answer> answer(@Payload Question question) {
        logger.info("Received question:{}", question);
        return question.current() < Integer.MAX_VALUE
                ? Mono.just(new Answer(question.current(), question.current() + 1))
                : Mono.error(() -> new IllegalArgumentException("max integer"));
    }

    @MessageMapping("score")
    public Mono<Score> answer(@Payload Person person) {
        logger.info("Received person:{} to ask score", person);
        return Mono.just(new Score(person.name().length() + 60));
    }
}
