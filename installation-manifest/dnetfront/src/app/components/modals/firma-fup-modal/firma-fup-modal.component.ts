import { Component, OnInit, AfterViewInit, ElementRef } from '@angular/core';
import { NgbModal, NgbModalConfig, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

declare function setSignValue(a:any);

@Component({
  selector: 'app-firma-fup-modal',
  templateUrl: './firma-fup-modal.component.html',
  styleUrls: ['./firma-fup-modal.component.scss']
})



export class FirmaFUPModalComponent implements OnInit, AfterViewInit {
  
  
  constructor(private modalService: NgbModal, private activeModal: NgbActiveModal, private elementRef: ElementRef) {

    
   }

  ngOnInit() {
   
  }
  ngAfterViewInit() {
    console.log('carga');
    
    const s = document.createElement('script');
    s.type = 'text/javascript';
    s.id = 'sfpSignScript';
    this.elementRef.nativeElement.append(s);
    
    
    setSignValue({
      cadena:'Prueba declaranet',
      rfc: 'BEGL200886PZ5',
      //representadoPor: rfcDePersonaMoraloRepresentada
      /*En caso de que el usuario tenga un representante legal, poner el RFC de dicha persona en la variable "representadoPor"*/
  }
  );
    
    }

  close() {
    this.modalService.dismissAll('');   
  }

  

  // getTransact(){
  //   var t = getValuesTransaction();
  //   getSignedData(t);
  // }
  
  // receiveSignedData(data){
  //   console.log(data);
  //   // Do something

  //   }

}
