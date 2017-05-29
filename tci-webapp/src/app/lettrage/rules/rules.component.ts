import { Component, OnInit } from '@angular/core';
import {Rule} from '../models/rule';

@Component({
  templateUrl: './rules.component.html'
})
export class RulesComponent implements OnInit {

  private rules: Rule[];

  constructor() {}

  ngOnInit() {
    this.rules = [{
      id: 1,
      name: 'L-PILOTE',
      att1: 'Test1',
      att2: 'Test2'
    },{
      id: 2,
      name: 'L-BILAN01',
      att1: 'Test1',
      att2: 'Test2'
    },{
      id: 2,
      name: 'L-BILAN9',
      att1: 'Test1',
      att2: 'Test2'
    },{
      id: 3,
      name: 'L-MANUEL',
      att1: 'Test1',
      att2: 'Test2'
    }];
  }
}
