import { Component, OnInit, ElementRef, ViewChild, AfterViewInit, Input } from '@angular/core';
import { NgbModal, NgbModalConfig, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DeclaracionService } from '../../../services/declaracion.service';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-preview',
  templateUrl: './preview.component.html',
  styleUrls: ['./preview.component.scss']
})
export class PreviewComponent implements OnInit, AfterViewInit {
  spinner: boolean;
  base64: string;
  height: '100%';
  zoom = 'page-width';
  constructor(private modalService: NgbModal, private activeModal: NgbActiveModal,
              private declaracionService: DeclaracionService) {
                this.spinner = false;
               }
  src: string;
  @ViewChild('preview') documentElement: ElementRef;
  @Input() public objPreview;
  @Input() public tipoImpresion;
  @Input() public titulo;
  ngOnInit() {


  }
  ngAfterViewInit(){
    console.log(this.documentElement);
    if (this.tipoImpresion === 'declaracion') {
      this.viewDeclaracion();
    } else if (this.tipoImpresion === 'acuse') {
      this.viewAcuse();
    }


    // this.documentElement.nativeElement.setAttribute('src', this.src);
  }

  viewDeclaracion() {    
    if (this.objPreview) {
      this.declaracionService.
      previewDeclaracion(this.objPreview)
        .pipe(first())
        .subscribe(
          data => {
            if (data.status === 200) {
              // this.src = `data:application/pdf;base64, ${data.body}`;
              // this.documentElement.nativeElement.setAttribute('src', this.src);
              this.base64 = data.body;
              this.spinner = true;
            }
          },
          error => {
            this.src = '/error';
            // this.src = `data:application/pdf;base64, ${error.error.text}`;
            this.documentElement.nativeElement.setAttribute('src', this.src);
          }
          );
    }
  }

  viewAcuse() {

    if (this.objPreview) {
      this.declaracionService.
      viewAcuse(this.objPreview)
        .pipe(first())
        .subscribe(
          data => {
            if (data.status === 200) {
              // this.src = `data:application/pdf;base64, ${data.body}`;
              // this.documentElement.nativeElement.setAttribute('src', this.src);
              this.base64 = data.body;
              this.spinner = true;
            }
          },
          error => {
            this.src = '/error';
            // this.src = `data:application/pdf;base64, ${error.error.text}`;
            this.documentElement.nativeElement.setAttribute('src', this.src);
          }
          );
    }
  }

  close() {
    this.activeModal.close();
  }
}
