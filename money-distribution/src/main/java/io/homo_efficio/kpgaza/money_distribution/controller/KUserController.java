package io.homo_efficio.kpgaza.money_distribution.controller;

import io.homo_efficio.kpgaza.money_distribution.dto.KUserIn;
import io.homo_efficio.kpgaza.money_distribution.dto.KUserOut;
import io.homo_efficio.kpgaza.money_distribution.service.KUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@RequestMapping("/kusers")
@RestController
@RequiredArgsConstructor
public class KUserController {

    private final KUserService kUserService;

    @PostMapping
    public ResponseEntity<KUserOut> create(@RequestBody KUserIn kUserIn) {
        return ResponseEntity.ok(kUserService.create(kUserIn));
    }
}
