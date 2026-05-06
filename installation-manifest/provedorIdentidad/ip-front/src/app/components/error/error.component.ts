import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SimpleGlobal } from 'ng2-simple-global';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html'
})
export class ErrorComponent implements OnInit {
  newError: any;
  recref: string;
  constructor(
    private route: ActivatedRoute,
    private sg: SimpleGlobal
  ) { }

  ngOnInit() {
    const params = this.route.snapshot.queryParams;
    let redirect = true;
    if (params.k) {
      this.newError = JSON.parse(decodeURIComponent(params.k));
      if (this.newError.redirect) {
        redirect = this.newError.redirect === '0' ? false : true;
      }
    }
    if (params.r) {
      this.recref = params.r;
      if (this.recref === '1') {
        localStorage.setItem('refeer', document.referrer);
      }
    }
    if (redirect) {
      if (localStorage.getItem('refeer')) {
        setTimeout(() => {
          window.location.replace(localStorage.getItem('refeer'));
        }, 1500);
      } else {
        window.close();
      }
    }
  }
}
