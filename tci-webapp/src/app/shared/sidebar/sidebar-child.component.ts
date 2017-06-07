import {Component, Input, OnInit} from '@angular/core';
import {ChildItem} from '../models/sidebar/child-item.model';

@Component({
  selector: 'app-sidebar-child',
  templateUrl: './sidebar-child.component.html'
})
export class SidebarChildComponent implements OnInit {

  @Input() childrenItems: ChildItem[];

  constructor() {}

  ngOnInit() {}
}
