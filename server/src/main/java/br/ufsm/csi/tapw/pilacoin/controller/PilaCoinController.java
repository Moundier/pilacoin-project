package br.ufsm.csi.tapw.pilacoin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufsm.csi.tapw.pilacoin.model.PilaCoin;
import br.ufsm.csi.tapw.pilacoin.service.PilaCoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("pila")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class PilaCoinController {
  
  private final PilaCoinService pilaCoinService; 

  @GetMapping("any")
  public ResponseEntity<PilaCoin> findPila() {
    PilaCoin pilaCoin = pilaCoinService.findFirstPilaCoin();
    System.out.println("\n\n\nFIND ANY PILA\n\n\n " + pilaCoin.toString());
    return ResponseEntity.ok(pilaCoin);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deletePila(@PathVariable Long id) {
    pilaCoinService.deletePilaCoinById(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
