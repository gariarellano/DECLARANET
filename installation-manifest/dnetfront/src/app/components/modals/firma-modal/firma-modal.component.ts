import { Component, OnInit } from '@angular/core';
import { NgbModal, NgbModalConfig, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FirmaFUPModalComponent } from '../firma-fup-modal/firma-fup-modal.component'

@Component({
  selector: 'app-firma-modal',
  templateUrl: './firma-modal.component.html',
  styleUrls: ['./firma-modal.component.scss']
})
export class FirmaModalComponent implements OnInit {
  modalRef : any;
  help: boolean = false;

  constructor(private modalService: NgbModal, private activeModal: NgbActiveModal) { }

  ngOnInit() {
  }
  
  close() {
    this.modalService.dismissAll('');   
  }

  click(){
    this.modalRef = this.modalService.open(FirmaFUPModalComponent, {
      centered: true,
      size: 'lg',
      scrollable: true
    });
  }

}
