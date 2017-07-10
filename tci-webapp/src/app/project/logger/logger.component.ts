import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-logger',
  templateUrl: './logger.component.html'
})
export class LoggerComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  cardClassDependingOnStatus(): string {
    switch(1) {
      case 1:
        //success
        //warning
        //danger
        return 'card-success';
    }
  }

}
